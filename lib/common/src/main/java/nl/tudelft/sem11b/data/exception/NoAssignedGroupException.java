package nl.tudelft.sem11b.data.exception;

/**
 * Provides an exception for when a user is not assigned to any group.
 */
public class NoAssignedGroupException extends Exception {

    public NoAssignedGroupException(String s) {
        super(s);
    }
}
