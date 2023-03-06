package ru.practicum.explore.ewm.event.location;

import java.io.Serializable;
import java.util.Objects;

public class LocationId implements Serializable {

    private Float lat;
    private Float lon;

    public LocationId() {
    }

    public LocationId(Float lat, Float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationId that = (LocationId) o;
        return Objects.equals(lat, that.lat) && Objects.equals(lon, that.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}
