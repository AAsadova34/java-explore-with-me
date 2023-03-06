package ru.practicum.explore.ewm.user.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.user.User;
import ru.practicum.explore.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {
    @Transactional
    List<UserDto> getUsers(Integer[] ids, int from, int size);

    @Transactional
    UserDto addUser(UserDto userDto);

    @Transactional
    void deleteUser(int userId);

    @Transactional
    User getUserById(int userId);
}
