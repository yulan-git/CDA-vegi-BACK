package com.vegi.vegilabback.controller;

import com.vegi.vegilabback.dto.CreateUserDto;
import com.vegi.vegilabback.exception.TokenRefreshException;
import com.vegi.vegilabback.model.RefreshToken;
import com.vegi.vegilabback.model.Role;
import com.vegi.vegilabback.model.User;
import com.vegi.vegilabback.model.enums.RoleEnum;
import com.vegi.vegilabback.payload.request.LoginRequest;
import com.vegi.vegilabback.payload.request.SignupRequest;
import com.vegi.vegilabback.payload.request.TokenRefreshRequest;
import com.vegi.vegilabback.payload.response.JwtResponse;
import com.vegi.vegilabback.payload.response.MessageResponse;
import com.vegi.vegilabback.payload.response.TokenRefreshResponse;
import com.vegi.vegilabback.repository.RoleRepository;
import com.vegi.vegilabback.repository.UserRepository;
import com.vegi.vegilabback.security.jwt.JwtUtils;
import com.vegi.vegilabback.security.services.RefreshTokenService;
import com.vegi.vegilabback.security.services.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    ModelMapper mapper = new ModelMapper();

    @PostMapping("/auth/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return registerProcess(signUpRequest, RoleEnum.USER);
    }

    @PostMapping("/auth/advegim")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignupRequest signUpRequest) {
        return registerProcess(signUpRequest, RoleEnum.ADMIN);
    }

    @PostMapping("/modvegilator")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> registerModerator(@Valid @RequestBody SignupRequest signUpRequest) {
        return registerProcess(signUpRequest, RoleEnum.MODERATOR);
    }

    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

    private ResponseEntity<?> registerProcess(@RequestBody @Valid SignupRequest signUpRequest, RoleEnum roleEnum) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        CreateUserDto userDto = new CreateUserDto(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        User user = mapper.map(userDto, User.class);

        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRole(role);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
