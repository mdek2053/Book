package nl.tudelft.sem11b.admin.data.filters;

import nl.tudelft.sem11b.admin.data.entities.Equipment;
import nl.tudelft.sem11b.admin.data.entities.Room;
import nl.tudelft.sem11b.admin.data.repositories.EquipmentRepository;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EquipmentFilter extends BaseFilter {
    private transient Set<Equipment> requiredEquipment = new HashSet<>();
    private transient EquipmentRepository repo;

    public EquipmentFilter(Set<Long> requiredEquipmentIds, EquipmentRepository repo) throws EntityNotFound {
        this.repo = repo;
        for(Long l : requiredEquipmentIds) {
            Optional<Equipment> optionalEquipment = repo.findById(l);
            if(optionalEquipment.isEmpty()) {
                throw new EntityNotFound("Equipment");
            }
            requiredEquipment.add(optionalEquipment.get());
        }
    }

    @Override
    public boolean handle(Room room) {
        if(!room.getEquipment().containsAll(requiredEquipment)) {
            return false;
        }
        return super.handle(room);
    }
}
