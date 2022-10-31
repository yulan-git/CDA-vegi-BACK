package com.vegi.vegilabback.service;

import com.vegi.vegilabback.dto.UserDto;
import com.vegi.vegilabback.dto.UserDtoForList;
import com.vegi.vegilabback.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto getUser(Long id);
    List<UserDtoForList> getUsers();
    void deleteUserById(Long id);
    User updateUser(Long id, UserDto userDto);
    void createPasswordResetTokenForUser(User user, String token);
    User findUserByEmail(String userEmail);
    Optional<User> getUserByPasswordResetToken(final String token);
    void changeUserPassword(final User user, final String password);
}
