package com.vegi.vegilabback.service.impl;

import com.vegi.vegilabback.dto.UpdateUserDto;
import com.vegi.vegilabback.dto.UserDto;
import com.vegi.vegilabback.dto.UserDtoForList;
import com.vegi.vegilabback.exception.UserNotFoundException;
import com.vegi.vegilabback.model.RefreshToken;
import com.vegi.vegilabback.model.User;
import com.vegi.vegilabback.repository.RefreshTokenRepository;
import com.vegi.vegilabback.repository.UserRepository;
import com.vegi.vegilabback.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    PasswordEncoder encoder;

    ModelMapper mapper = new ModelMapper();


    @Override
    public UserDto getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            UserDto userDto = mapper.map(user, UserDto.class);
            return userDto;
        }
        return null;
    }

    @Override
    public List<UserDtoForList> getUsers() {
        List<UserDtoForList> userDtos = new ArrayList<>();
        Iterable<User> users = userRepository.findAll();
        for (var user: users) {
            UserDtoForList userDto = mapper.map(user, UserDtoForList.class);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public void deleteUserById(Long id) {
        List<RefreshToken> refreshTokens = refreshTokenRepository.findAll();
        System.out.println(("TOKEN ------->" + refreshTokens));
        Optional<User> user = userRepository.findById(id);
        if(refreshTokens != null) {
            for (var token : refreshTokens) {
                if (token.getUser().getId().equals(id)) {
                    refreshTokenRepository.delete(token);
                }
            }
        }
        System.out.println("USER -----------> ");
        if (user.isPresent()){
            userRepository.delete(user.get());
        }
    }

    @Override
    public User updateUser(Long id, UserDto userDto) {
        var _user = userRepository.findById(id);
        var user = _user.get();
        if(_user.isPresent()){
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            userRepository.save(user);
            return user;
        }
        return null;
    }


    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("Could not find any customer with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        String encodedPassword = encoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }
}
