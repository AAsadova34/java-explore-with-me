package ru.practicum.explore.ewm.event;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.practicum.explore.ewm.category.Category;
import ru.practicum.explore.ewm.event.enums.EventState;
import ru.practicum.explore.ewm.event.location.Location;
import ru.practicum.explore.ewm.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "EVENTS")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EntityListeners({AuditingEntityListener.class})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String annotation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "lat", referencedColumnName = "lat"),
            @JoinColumn(name = "lon", referencedColumnName = "lon")
    })
    private Location location;

    @Column(columnDefinition = "boolean default false")
    private Boolean paid;

    @Column(name = "participant_limit", columnDefinition = "integer default 0")
    private int participantLimit;

    @Column(name = "request_moderation", columnDefinition = "boolean default true")
    private Boolean requestModeration;

    @Column
    private String title;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @CreatedDate
    private LocalDateTime created;

    @Column
    private LocalDateTime published;

    @Column
    private int views;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        return id != null && id.equals(((Event) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
