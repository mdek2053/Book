package nl.tudelft.sem11b.reservation.services;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import nl.tudelft.sem11b.data.Roles;
import nl.tudelft.sem11b.data.exceptions.ApiException;
import nl.tudelft.sem11b.data.exceptions.EntityNotFound;
import nl.tudelft.sem11b.data.exceptions.InvalidData;
import nl.tudelft.sem11b.data.models.GroupModel;
import nl.tudelft.sem11b.data.models.PageData;
import nl.tudelft.sem11b.data.models.PageIndex;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import nl.tudelft.sem11b.data.models.UserModel;
import nl.tudelft.sem11b.reservation.entity.Reservation;
import nl.tudelft.sem11b.reservation.repository.ReservationRepository;
import nl.tudelft.sem11b.services.GroupService;
import nl.tudelft.sem11b.services.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    private final transient ReservationRepository reservations;
    private final transient UserService users;
    private final transient GroupService groups;

    /**
     * Instatinaties UserValidationService class.
     * @param reservations reservations repository
     * @param users user service
     * @param groups groups service
     */
    public UserValidationService(ReservationRepository reservations,
                                 UserService users, GroupService groups) {
        this.reservations = reservations;
        this.users = users;
        this.groups = groups;
    }

    /**
     * Checks if current user has permissions to modify given reservation.
     * @param reservationUserId The id of the user stored in reservation that should be checked.
     * @throws EntityNotFound Thrown when user doesn't have permissions.
     * @throws ApiException Thrown when user doesn't have permissions.
     */
    public void userCanModifyReservation(Long reservationUserId)
            throws ApiException {
        if (!Objects.equals(currentUserId(), reservationUserId) && !currentUserIsAdmin()
                && !verifySecretary(reservationUserId)) {
            throw new ApiException("Reservation",
                    "User not authorized to change given reservation.");
        }

    }

    /**
     * Verifies whether the current user is allowed to make a reservation for the provided user.
     *
     * @param getForUser Id of the user for whom the reservation will be made
     * @return a boolean value whether the current user has the correct rights
     * @throws ApiException Thrown when a remote API encountered an error
     */
    public boolean verifySecretary(Long getForUser) throws ApiException {
        Long userId = currentUserId();
        List<GroupModel> groupList =
                groups.getGroupsOfSecretary(userId);
        for (GroupModel groupModel : groupList) {
            if (groupModel.getGroupMembers().contains(getForUser)) {
                return true;
            }
        }
        return false;
    }

    public Long currentUserId() throws ApiException {
        return users.currentUser().getId();
    }

    public boolean currentUserIsAdmin() throws ApiException {
        return users.currentUser().inRole(Roles.Admin);
    }

    /**
     * Checks if the time of reservation doesn't conflict with any other reservation.
     * @throws InvalidData When data is invalid.
     */
    public void validateUserConflicts(ReservationRequestModel requestModel)
            throws InvalidData, ApiException {
        if (requestModel.getForUser() == null) {
            requestModel.setForUser(currentUserId());
        }
        var sinceTs = requestModel.getSince().toTimestamp();
        var untilTs = requestModel.getUntil().toTimestamp();
        // check if it doesn't conflict with user's other reservations
        if (reservations.hasUserConflict(requestModel.getForUser(), sinceTs, untilTs)) {
            throw new InvalidData("Reservation conflicts with user's existing reservations");
        }
    }

}
