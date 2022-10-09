package com.vegi.vegilabback.service;

import com.vegi.vegilabback.dto.UpdateUserDto;
import com.vegi.vegilabback.dto.UserDto;
import com.vegi.vegilabback.dto.UserDtoForList;
import com.vegi.vegilabback.model.User;

import java.util.List;

public interface UserService {
    UserDto getUser(Long id);
    List<UserDtoForList> getUsers();
    void deleteUserById(Long id);
    User updateUser(Long id, UserDto userDto);
}
