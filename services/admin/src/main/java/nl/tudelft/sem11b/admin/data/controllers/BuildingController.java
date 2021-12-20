package nl.tudelft.sem11b.admin.data.controllers;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.BuildingModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.services.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
     * @param page  Page index (zero-based)
     * @param limit Maximal size of a page
     * @return Page of buildings
     */
    @GetMapping("/buildings")
    public PageData<BuildingModel> listBuildings(
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<Integer> limit) {
        var index = PageIndex.fromQuery(page, limit);

        try {
            return buildings.listBuildings(index);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }
    }

    /**
     * Gets building data.
     *
     * @param id ID of a building
     * @return Building data
     */
    @GetMapping("/buildings/{id}")
    public BuildingModel getBuilding(@PathVariable int id) {
        Optional<BuildingModel> building;
        try {
            building = buildings.getBuilding(id);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }

        if (building.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Building not found!");
        }

        return building.get();
    }

    /**
     * Adds a building to the system.
     * @param model The building to be added
     */
    @PostMapping("/buildings")
    public void addBuilding(@RequestBody BuildingModel model) {
        try {
            buildings.addBuilding(model);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }
    }

}
