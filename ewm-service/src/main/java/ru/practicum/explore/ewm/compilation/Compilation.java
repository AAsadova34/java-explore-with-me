package ru.practicum.explore.ewm.compilation;

import lombok.*;
import lombok.experimental.Accessors;
import ru.practicum.explore.ewm.event.Event;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "COMPILATIONS")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany
    @JoinColumn(name = "compilation_id")
    private List<Event> events;

    @Column
    private Boolean pinned;

    @Column
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compilation)) return false;
        return id != null && id.equals(((Compilation) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
