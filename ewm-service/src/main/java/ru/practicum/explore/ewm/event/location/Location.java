package ru.practicum.explore.ewm.event.location;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "LOCATIONS")
@IdClass(LocationId.class)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    private Float lat;

    @Id
    private Float lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(lat, location.lat) && Objects.equals(lon, location.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}
