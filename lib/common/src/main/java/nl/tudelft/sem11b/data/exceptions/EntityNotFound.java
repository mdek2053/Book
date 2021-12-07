package nl.tudelft.sem11b.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EntityNotFound extends ServiceException {
    private final String entityName;

    public EntityNotFound(String entityName) {
        super("Entity " + entityName + " not found!");
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public ResponseStatusException toResponseException() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entityName + " not found!");
    }
}
