package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.dto.UserDto;
import com.vegi.vegilabback.dto.UserDtoForList;
import com.vegi.vegilabback.model.User;
import com.vegi.vegilabback.repository.RecipeRepository;
import com.vegi.vegilabback.service.RecipeService;
import com.vegi.vegilabback.service.UserService;
import com.vegi.vegilabback.validation.AuthentificationValidation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Repository
@Getter
@Setter
@RequestMapping("api")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RecipeService recipeService;
    @Autowired
    RecipeRepository recipeRepository;

    @Value("${vegilab.app.adminSecretName}")
    private String adminSecretName;


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

    @PostMapping("/user/forgot_password")
    @PreAuthorize("hasAuthority('USER')")
    public String processForgotPassword() {
        return null;
    }

    @PostMapping("/user/reset_password")
    @PreAuthorize("hasAuthority('USER')")
    public String processResetPassword() {
        return null;
    }
}
