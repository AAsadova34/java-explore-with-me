package ru.practicum.explore.ewm.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment save(Comment comment);

    @Query("SELECT CAST(COUNT(com.id) AS integer) " +
            "FROM Comment as com " +
            "WHERE com.user.id = :userId AND (com.created BETWEEN :start AND :end) AND com.text = :text")
    int countNumberComments(int userId, LocalDateTime start, LocalDateTime end, String text);

    boolean existsById(int commentId);

    Comment getReferenceById(int commentId);

    void deleteById(int commentId);

    List<Comment> findCommentsByUserIdOrderByCreatedDesc(int userId, Pageable pageable);

    List<Comment> findCommentsByEventIdOrderByCreatedDesc(int eventId, Pageable pageable);
}
