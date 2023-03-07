package ru.practicum.explore.ewm.comment.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.comment.dto.CommentDtoOut;
import ru.practicum.explore.ewm.comment.dto.CommentDtoInc;

import java.util.List;

public interface CommentService {

    @Transactional
    CommentDtoOut addCommentByUser(int userId, int eventId, CommentDtoInc commentDtoInc);

    @Transactional
    CommentDtoOut updateCommentByUser(int userId, int commentId, CommentDtoInc commentDtoInc);

    @Transactional
    void delCommentByUser(int userId, int commentId);

    @Transactional
    List<CommentDtoOut> getCommentsByUser(int userId, int from, int size);

    @Transactional
    void delCommentByAdmin(int commentId);

    @Transactional
    List<CommentDtoOut> getCommentsByPublic(int eventId, int from, int size);
}
