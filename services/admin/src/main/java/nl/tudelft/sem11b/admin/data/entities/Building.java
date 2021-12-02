package nl.tudelft.sem11b.admin.data.entities;

import java.util.Set;
import java.util.stream.Stream;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nl.tudelft.sem11b.data.TimeOfDay;

@Entity
public class Building {
    @Id @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "prefix", nullable = false, unique = true)
    private String prefix;
    @Column(name = "name", nullable = false)
    private String name;
    @Embedded
    // Undeniable proof that JPA is trash. This is required because JPA naming strategy will not
    // affix the embedded column names
    @AttributeOverrides({
        @AttributeOverride(name = "timestamp", column = @Column(name = "opening_timestamp"))
    })
    private TimeOfDay opening;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "timestamp", column = @Column(name = "closing_timestamp"))
    })
    private TimeOfDay closing;

    @OneToMany(mappedBy = "building")
    private Set<Room> rooms;

    public long getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("Prefix may not be empty nor null!");
        }

        this.prefix = prefix.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name may not be empty nor null!");
        }

        this.name = name.trim();
    }

    public TimeOfDay getOpening() {
        return opening;
    }

    public void setOpening(TimeOfDay opening) {
        setHours(opening, closing);
    }

    public TimeOfDay getClosing() {
        return closing;
    }

    public void setClosing(TimeOfDay closing) {
        setHours(opening, closing);
    }

    public void setHours(TimeOfDay opening, TimeOfDay closing) {
        if (opening.compareTo(closing) >= 0) {
            throw new IllegalArgumentException("Opening hours must be before closing hours!");
        }

        this.opening = opening;
        this.closing = closing;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public Stream<Room> getRooms() {
        return rooms.stream();
    }
}
