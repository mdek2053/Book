package nl.tudelft.sem11b.admin.data.entities;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import nl.tudelft.sem11b.data.models.EquipmentModel;


@Entity
public class Equipment {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "equipment")
    private Set<Room> rooms;

    public Equipment(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Equipment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Equipment equipment = (Equipment) o;
        return Objects.equals(id, equipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public EquipmentModel toModel() {
        return new EquipmentModel(this.id, this.name);
    }
}
