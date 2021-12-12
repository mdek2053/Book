package nl.tudelft.sem11b.data.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception thrown when some entity couldn't be found.
 */
public class EntityNotFound extends ServiceException {
    private final String entityName;

    /**
     * Instantiates the {@link EntityNotFound} class.
     *
     * @param entityName Name of entity
     */
    public EntityNotFound(String entityName) {
        super("Entity " + entityName + " not found!");
        this.entityName = entityName;
    }

    /**
     * Gets the name of entity that couldn't be found.
     *
     * @return Name of entity
     */
    public String getEntityName() {
        return entityName;
    }

    @Override
    public ResponseStatusException toResponseException() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, entityName + " not found!");
    }
}
