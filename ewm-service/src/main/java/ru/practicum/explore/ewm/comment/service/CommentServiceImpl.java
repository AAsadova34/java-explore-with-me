package ru.practicum.explore.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.comment.Comment;
import ru.practicum.explore.ewm.comment.CommentRepository;
import ru.practicum.explore.ewm.comment.dto.CommentDtoOut;
import ru.practicum.explore.ewm.comment.dto.CommentDtoInc;
import ru.practicum.explore.ewm.event.Event;
import ru.practicum.explore.ewm.event.dto.EventShortDto;
import ru.practicum.explore.ewm.event.enums.EventState;
import ru.practicum.explore.ewm.event.service.EventService;
import ru.practicum.explore.ewm.exception.ConflictException;
import ru.practicum.explore.ewm.exception.NotFoundException;
import ru.practicum.explore.ewm.user.User;
import ru.practicum.explore.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.explore.ewm.comment.CommentMapper.toComment;
import static ru.practicum.explore.ewm.comment.CommentMapper.toCommentDto;
import static ru.practicum.explore.ewm.utility.Logger.logStorageChanges;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final EventService eventService;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public CommentDtoOut addCommentByUser(int userId, int eventId, CommentDtoInc commentDtoInc) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            String reason = "For the requested operation the conditions are not met.";
            String message = "You can't participate in an unpublished event.";
            throw new ConflictException(reason, message);
        }
        checkPreviousComments(userId, commentDtoInc.getText());

        Comment comment = toComment(user, event, commentDtoInc);
        Comment commentStorage = commentRepository.save(comment);
        logStorageChanges("Add comment", commentStorage.toString());

        EventShortDto eventShortDto = eventService.toListEventShortDto(List.of(event)).get(0);
        return toCommentDto(eventShortDto, commentStorage);
    }

    @Transactional
    @Override
    public CommentDtoOut updateCommentByUser(int userId, int commentId, CommentDtoInc commentDtoInc) {
        Comment oldComment = getCommentById(commentId);

        if (userId != oldComment.getUser().getId()) {
            String reason = "For the requested operation the conditions are not met.";
            String message = "Attempt to edit someone else's comment.";
            throw new ConflictException(reason, message);
        }
        oldComment.setText(commentDtoInc.getText());
        oldComment.setRedaction(true);
        Comment commentStorage = commentRepository.save(oldComment);
        logStorageChanges("Update comment", commentStorage.toString());

        EventShortDto eventShortDto = eventService.toListEventShortDto(List.of(oldComment.getEvent())).get(0);
        return toCommentDto(eventShortDto, commentStorage);
    }

    @Transactional
    @Override
    public void delCommentByUser(int userId, int commentId) {
        Comment comment = getCommentById(commentId);

        if (userId != comment.getUser().getId()) {
            String reason = "For the requested operation the conditions are not met.";
            String message = "Attempt to delete someone else's comment.";
            throw new ConflictException(reason, message);
        }
        commentRepository.deleteById(commentId);
        logStorageChanges("Delete", String.format("Comment with id %s", commentId));
    }

    @Transactional
    @Override
    public List<CommentDtoOut> getCommentsByUser(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> comments = commentRepository.findCommentsByUserIdOrderByCreatedDesc(userId, pageable);
        return toCommentsDtoOut(comments);
    }

    @Transactional
    @Override
    public void delCommentByAdmin(int commentId) {
        try {
            commentRepository.deleteById(commentId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Comment with id=%s was not found.", commentId));
        }
        logStorageChanges("Delete", String.format("Comment with id %s", commentId));
    }

    @Transactional
    @Override
    public List<CommentDtoOut> getCommentsByPublic(int eventId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> comments = commentRepository.findCommentsByEventIdOrderByCreatedDesc(eventId, pageable);
        return toCommentsDtoOut(comments);
    }

    private Comment getCommentById(int commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException(String.format("Comment with id=%s was not found.", commentId));
        } else {
            return commentRepository.getReferenceById(commentId);
        }
    }

    private List<CommentDtoOut> toCommentsDtoOut(List<Comment> comments) {
        if (!comments.isEmpty()) {
            List<Event> events = comments.stream()
                    .map(Comment::getEvent)
                    .collect(Collectors.toList());
            Map<Integer, EventShortDto> eventShortDto = eventService.toListEventShortDto(events).stream()
                    .collect(Collectors.toMap(EventShortDto::getId, Function.identity(),
                            (existing, replacement) -> existing));

            return comments.stream()
                    .map(c -> toCommentDto(eventShortDto.get(c.getEvent().getId()), c))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private void checkPreviousComments(int userId, String text) {
        int numberHourToCheck = 1;
        int allowedNumberOfRepetitions = 5;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hoursAgo = now.minusHours(numberHourToCheck);
        int numberOfIdenticalCom = commentRepository.countNumberComments(userId, hoursAgo, now, text);

        if (numberOfIdenticalCom >= allowedNumberOfRepetitions) {
            String reason = "For the requested operation the conditions are not met.";
            String message = "Exceeded allowed number of comments with identical content.";
            throw new ConflictException(reason, message);
        }
    }
}
