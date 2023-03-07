package ru.practicum.explore.ewm.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.ewm.comment.dto.CommentDtoOut;
import ru.practicum.explore.ewm.comment.dto.CommentDtoInc;
import ru.practicum.explore.ewm.event.Event;
import ru.practicum.explore.ewm.event.dto.EventShortDto;
import ru.practicum.explore.ewm.user.User;

import static ru.practicum.explore.ewm.user.UserMapper.toUserShortDto;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(User user, Event event, CommentDtoInc commentDto) {
        return new Comment()
                .setText(commentDto.getText())
                .setUser(user)
                .setEvent(event)
                .setRedaction(false);
    }

    public static CommentDtoOut toCommentDto(EventShortDto event, Comment comment) {
        return new CommentDtoOut()
                .setId(comment.getId())
                .setUser(toUserShortDto(comment.getUser()))
                .setEvent(event)
                .setText(comment.getText())
                .setRedaction(comment.getRedaction())
                .setCreated(comment.getCreated());
    }
}
