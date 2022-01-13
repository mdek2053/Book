package nl.tudelft.sem11b.admin.data.controllers;

import java.util.Optional;

import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.ServiceException;
import nl.tudelft.sem11b.data.models.FaultModel;
import nl.tudelft.sem11b.data.models.FaultRequestModel;
import nl.tudelft.sem11b.data.models.FaultStudModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.services.FaultService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class FaultController {
    private final transient FaultService faults;

    public FaultController(FaultService faults) {
        this.faults = faults;
    }

    /**
     * Lists all faults in the system.
     *
     * @param page Page index (zero-based)
     * @param limit Maximal size of a page
     * @return Page of faults
     */
    @GetMapping("/faults")
    public PageData<FaultModel> listFaults(
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<Integer> limit) {
        var index = PageIndex.fromQuery(page, limit);

        try {
            return faults.listFaults(index);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }
    }

    /**
     * Lists all faults for a room with the given id.
     *
     * @param id ID of the room to get faults for
     * @param page Page index (zero-based)
     * @param limit Maximal size of a page
     * @return Page of faults
     */
    @GetMapping("/rooms/{id}/faults")
    public PageData<FaultStudModel> listFaultsByRoom(
        @PathVariable int id,
        @RequestParam Optional<Integer> page,
        @RequestParam Optional<Integer> limit) {
        var index = PageIndex.fromQuery(page, limit);

        try {
            return faults.listFaults(index, id);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }
    }

    /**
     * Stores a fault report.
     *
     * @param id    ID of the room that needs maintenance
     * @param fault Request object with the information about the fault.
     */
    @PostMapping("/rooms/{id}/faults")
    public void addFault(@PathVariable int id, @RequestBody FaultRequestModel fault) {
        try {
            faults.addFault(id, fault);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }
    }

    /**
     * Gets fault information.
     *
     * @param id ID of fault to fetch
     * @return Fault information
     */
    @GetMapping("/faults/{id}")
    public FaultModel getFault(@PathVariable int id) {
        Optional<FaultModel> fault;
        try {
            fault = faults.getFault(id);
        } catch (ServiceException e) {
            throw e.toResponseException();
        }

        if (fault.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fault not found!");
        }

        return fault.get();
    }

    /**
     * Removes a fault, i.e. resolved the fault.
     *
     * @param id ID of fault to reopen
     */
    @DeleteMapping("/faults/{id}")
    public void resolveFault(@PathVariable int id) throws EntityNotFound, ApiException {
        faults.resolveFault(id);
    }
}
