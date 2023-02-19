package ru.practicum.explore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "STATS")
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String app;

    @Column
    private String uri;

    @Column
    private String ip;

    @Column(name = "DATE_TIME")
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stats)) return false;
        return id != null && id.equals(((Stats) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
