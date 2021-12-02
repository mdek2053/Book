package nl.tudelft.sem11b.admin.data.controllers;

import java.util.Optional;

import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.services.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST API proxy for {@link BuildingService}.
 */
@RestController
public class BuildingController {
    private final BuildingService buildings;

    /**
     * Instantiates the {@link BuildingController} class.
     *
     * @param buildings The building handling service
     */
    public BuildingController(BuildingService buildings) {
        this.buildings = buildings;
    }

    /**
     * Lists all buildings in the system.
     *
     * @param page Page index (zero-based)
     * @param limit Maximal size of a page
     * @return Page of buildings
     */
    @GetMapping("/buildings")
    public PageData<BuildingModel> listBuildings(
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<Integer> limit) {
        var index = PageIndex.fromQuery(page, limit);

        return buildings.listBuildings(index);
    }

    /**
     * Gets building data.
     *
     * @param id ID of a building
     * @return Building data
     */
    @GetMapping("/buildings/{id}")
    public BuildingModel getBuilding(@PathVariable int id) {
        var building = buildings.getBuilding(id);
        if (building.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Building not found!");
        }

        return building.get();
    }
}
