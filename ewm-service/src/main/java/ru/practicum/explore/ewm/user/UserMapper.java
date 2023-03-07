package ru.practicum.explore.ewm.user;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.ewm.user.dto.UserDto;
import ru.practicum.explore.ewm.user.dto.UserShortDto;

@UtilityClass
public class UserMapper {

    public static User toUser(UserDto userDto) {
        User user = new User()
                .setName(userDto.getName())
                .setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
