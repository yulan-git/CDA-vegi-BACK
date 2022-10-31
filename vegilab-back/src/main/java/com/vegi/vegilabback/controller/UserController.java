package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.dto.PasswordDto;
import com.vegi.vegilabback.dto.UserDto;
import com.vegi.vegilabback.dto.UserDtoForList;
import com.vegi.vegilabback.exception.exceptions.UserNotFoundException;
import com.vegi.vegilabback.model.PasswordResetToken;
import com.vegi.vegilabback.model.User;
import com.vegi.vegilabback.repository.PasswordTokenRepository;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.service.RecipeService;
import com.vegi.vegilabback.service.UserService;
import com.vegi.vegilabback.validation.AuthentificationValidation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RecipeService recipeService;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    private MessageSource messages;
    @Autowired
    private Environment env;
    @Autowired
    PasswordTokenRepository passwordTokenRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${vegilab.app.adminSecretName}")
    private String adminSecretName;

    private static final String NOREPLY_ADDRESS = "noreply@vegilab.com";

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDtoForList>> getUsers(AuthentificationValidation authentificationValidation) {
        List<UserDtoForList> users = new ArrayList<>();
        if(authentificationValidation.getTokenUsername().equals(adminSecretName)){
            users = userService.getUsers();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id, AuthentificationValidation authentificationValidation) {
        var userId = authentificationValidation.getTokenId();
        if(authentificationValidation.getTokenId() == id || authentificationValidation.getTokenUsername().equals(adminSecretName)) {
            UserDto user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity deleteUser(@PathVariable ("id") Long id, AuthentificationValidation authentificationValidation){
        if(authentificationValidation.getTokenUsername().equals(adminSecretName)) {
            recipeService.deleteAllRecipesFromUser(id);
            userService.deleteUserById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDto userDto, AuthentificationValidation authentificationValidation){
        try{
            if (authentificationValidation.getTokenId() == id){
                var updatedUser = userService.updateUser(id, userDto);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/reset_password")
    public ResponseEntity<String> resetPassword(HttpServletRequest request,
                                         @RequestParam("email") String userEmail) {
        System.out.println(request);
        System.out.println(userEmail);
        try{
            User user = userService.findUserByEmail(userEmail);
            if (user == null) {
                throw new UserNotFoundException("Aucun utilisateur retrouvée depuis l'email : " + userEmail);
            }
            String token = UUID.randomUUID().toString();
            System.out.println(token);
            userService.createPasswordResetTokenForUser(user, token);
            System.out.println(request.getLocale());
            System.out.println(user);
            mailSender.send(constructResetTokenEmail(getAppUrl(request),
                    request.getLocale(), token, user));
            return new ResponseEntity<>("Un email a été envoyé", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/save_password")
    public ResponseEntity<String> savePassword(@RequestBody PasswordDto passwordDto) {

        System.out.println(passwordDto.getToken());
        String result = validatePasswordResetToken(passwordDto.getToken());
        if(result==null){
            Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
            if(user.isPresent()) {
                if(!passwordDto.getOldPassword().equals(passwordDto.getNewPassword())){
                    userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
                }
                return new ResponseEntity<>("Mot de passe modifié", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else if (result.equals("invalidToken")){
            return new ResponseEntity<>("Refresh token invalide", HttpStatus.INTERNAL_SERVER_ERROR);
        } else if(result.equals("expired")){
            return new ResponseEntity<>("Le token a expiré", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // -------------------------------- NON API --------------------------------- //

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/api/user/changePassword?token=" + token;
        final String message = "Veuillez cliquer sur ce lien pour changer votre mot de passe";
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(NOREPLY_ADDRESS);
        System.out.println(email);
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        System.out.println("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


    public String validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passToken = passwordTokenRepository.findByToken(token);
        return !isTokenFound(passToken.get()) ? "invalidToken" : isTokenExpired(passToken.get()) ? "expired" : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    /*    @GetMapping("/user/changePassword")
    public ResponseEntity<String> ChangePasswordPage(Locale locale,
                                         @RequestParam("token") String token) {
        String result = validatePasswordResetToken(token);
        if(result != null) {
            String message = messages.getMessage("auth.message." + result, null, locale);
            return new ResponseEntity<>("Mot de passe modifié", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

}
