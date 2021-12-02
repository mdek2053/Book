package nl.tudelft.sem11b.admin.data.entities;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.tudelft.sem11b.admin.data.Closure;

@Entity
@Table(indexes = {@Index(columnList = "suffix, building_id", unique = true)})
public class Room {
    @Id @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "suffix", nullable = false)
    private String suffix;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "capacity", nullable = false)
    private int capacity;
    @Embedded
    private Closure closure;

    @JoinColumn(name = "building_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Building building;

    public long getId() {
        return id;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setPrefix(String suffix) {
        if (suffix == null || suffix.trim().isEmpty()) {
            throw new IllegalArgumentException("Suffix may not be empty nor null!");
        }

        this.suffix = suffix.trim();
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

    public Building getBuilding() {
        return building;
    }
}
