package ru.practicum.explore.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.exception.NotFoundException;
import ru.practicum.explore.ewm.user.User;
import ru.practicum.explore.ewm.user.UserRepository;
import ru.practicum.explore.ewm.user.dto.UserDto;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

import static ru.practicum.explore.ewm.utility.Logger.logStorageChanges;
import static ru.practicum.explore.ewm.user.UserMapper.toUser;
import static ru.practicum.explore.ewm.user.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements ru.practicum.explore.ewm.user.service.UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public List<UserDto> getUsers(Integer[] ids, int from, int size) {
        List<UserDto> users;
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids != null && ids.length != 0) {
            Set<Integer> setIds = Set.of(ids);
            users = userRepository.findDesiredUsers(setIds, pageable);
        } else {
            users = userRepository.findAllUsers(pageable);
        }
        return users;
    }

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        User user = toUser(userDto);
        User userStorage = userRepository.save(user);
        logStorageChanges("Add user", userStorage.toString());
        return toUserDto(userStorage);
    }

    @Transactional
    @Override
    public void deleteUser(int userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(String.format("User with id=%s was not found.", userId));
        }

        logStorageChanges("Delete", String.format("User with id %s", userId));
    }

    @Transactional
    @Override
    public User getUserById(int userId) {
        checkUserExists(userId);
        return userRepository.getReferenceById(userId);
    }

    private void checkUserExists(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found.", userId));
        }
    }
}
