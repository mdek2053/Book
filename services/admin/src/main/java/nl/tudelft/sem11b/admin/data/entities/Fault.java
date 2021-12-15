package nl.tudelft.sem11b.admin.data.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import nl.tudelft.sem11b.data.models.FaultModel;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.FaultStudModel;
import nl.tudelft.sem11b.data.models.RoomStudModel;

/**
 * Represents a room fault report.
 */
@Entity
public class Fault {
    @Id
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "reporter", nullable = false)
    private long reporter;

    @Column(name = "description", nullable = false)
    private String description;

    @JoinColumn(name = "room_id", nullable = false)
    @OneToOne
    private Room room;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReporter() {
        return reporter;
    }

    public void setReporter(long reporter) {
        this.reporter = reporter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Fault() {
    }

    /**
     * Constructs a Fault object.
     * @param reporter    id of the user who reported the fault
     * @param description description of the fault
     * @param room        the room which needs maintenance
     */
    public Fault(long reporter, String description, Room room) {
        this.reporter = reporter;
        this.description = description;
        this.room = room;
    }

    public FaultModel toModel() {
        return new FaultModel(id, reporter, description, room.getId());
    }

    public FaultStudModel toStudModel() {
        return new FaultStudModel(id, reporter, description);
    }

    public FaultRequestModel toRequestModel() {
        return new FaultRequestModel(id, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fault)) {
            return false;
        }

        Fault fault = (Fault) o;

        if (id != fault.id) {
            return false;
        }
        if (reporter != fault.reporter) {
            return false;
        }
        if (!Objects.equals(description, fault.description)) {
            return false;
        }
        return Objects.equals(room, fault.room);
    }
}
