package ru.practicum.explore.ewm.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.practicum.explore.ewm.event.Event;
import ru.practicum.explore.ewm.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMMENTS")
@Getter
@Setter
@ToString
@Accessors(chain = true)
@EntityListeners({AuditingEntityListener.class})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String text;

    @Column
    private Boolean redaction;

    @CreatedDate
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        return id != null && id.equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
