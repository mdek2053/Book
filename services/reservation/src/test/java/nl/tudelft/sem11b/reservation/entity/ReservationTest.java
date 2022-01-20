package nl.tudelft.sem11b.reservation.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

import nl.tudelft.sem11b.data.ApiDate;
import nl.tudelft.sem11b.data.ApiDateTime;
import nl.tudelft.sem11b.data.ApiDateTimeUtils;
import nl.tudelft.sem11b.data.ApiTime;
import nl.tudelft.sem11b.data.models.ReservationModel;
import nl.tudelft.sem11b.data.models.ReservationRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationTest {

    long id = 1L;
    long roomId = 2L;
    long userId = 3L;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Timestamp since;
    Timestamp until;
    Reservation reservation;
    ReservationModel reservationModel;
    ReservationRequestModel requestModel;

    @BeforeEach
    void setUp() throws ParseException {
        since = new Timestamp(dateFormat.parse("23/09/2007").getTime());
        until = new Timestamp(dateFormat.parse("24/09/2007").getTime());
        reservation = new Reservation(id, roomId, userId,
                "Title", since, until, null);
        reservationModel = new ReservationModel(id, ApiDateTimeUtils.from(since),
                ApiDateTimeUtils.from(until), "Title");
        requestModel = new ReservationRequestModel(roomId, "Title",
                new ApiDateTime(new ApiDate(2007, 9, 23), new ApiTime(0L, 0L)),
                new ApiDateTime(new ApiDate(2007, 9, 24), new ApiTime(0L, 0L)),
                userId);
    }

    @Test
    void testCreateReservationObject() {
        Reservation r = new Reservation(requestModel.getRoomId(), requestModel.getForUser(),
                requestModel.getTitle(), Timestamp.valueOf(requestModel.getSince().toLocal()),
                Timestamp.valueOf(requestModel.getUntil().toLocal()));
        assertEquals(r, Reservation.createReservation(requestModel));
    }

    @Test
    void testGetId() {
        assertEquals(id, reservation.getId());
    }

    @Test
    void testGetRoomId() {
        assertEquals(roomId, reservation.getRoomId());
    }

    @Test
    void testGetUserId() {
        assertEquals(userId, reservation.getUserId());
    }

    @Test
    void testGetTitle() {
        assertEquals("Title", reservation.getTitle());
    }

    @Test
    void testGetSince() {
        assertEquals(since, reservation.getSince());
    }

    @Test
    void testGetUntil() {
        assertEquals(until, reservation.getUntil());
    }

    @Test
    void testGetCancelReason() {
        reservation.setCancelReason("Room maintenance");
        assertEquals("Room maintenance", reservation.getCancelReason());
    }

    @Test
    void testToModel() {
        assertEquals(reservationModel, reservation.toModel());
    }

    @Test
    void testEqualsTheSame() {
        assertEquals(reservation, reservation);
    }

    @Test
    void testNotInstanceOf() {
        assertFalse(reservation.equals(new Object()));
    }

    @Test
    void testEqualTheSameData() {
        Reservation equal = new Reservation(id, roomId, userId,
                "Title", since, until, null);
        assertEquals(equal, reservation);
    }

    @Test
    void testNotEqualId() {
        Reservation equal = new Reservation(id + 1L, roomId, userId,
                "Title", since, until, null);
        assertNotEquals(equal, reservation);
    }

    @Test
    void testNotEqualRoomId() {
        Reservation equal = new Reservation(id, roomId + 1L, userId,
                "Title", since, until, null);
        assertNotEquals(equal, reservation);
    }

    @Test
    void testNotEqualUserId() {
        Reservation equal = new Reservation(id, roomId, userId + 1L,
                "Title", since, until, null);
        assertNotEquals(equal, reservation);
    }

    @Test
    void testNotEqualTitle() {
        Reservation equal = new Reservation(id, roomId, userId,
                "Different Title", since, until, null);
        assertNotEquals(equal, reservation);
    }

    @Test
    void testNotEqualSince() throws ParseException {
        Timestamp newSince = new Timestamp(dateFormat.parse("22/09/2007").getTime());
        Reservation equal = new Reservation(id, roomId, userId,
                "Title", newSince, until, null);
        assertNotEquals(equal, reservation);
    }

    @Test
    void testNotEqualUntil() throws ParseException {
        Timestamp newUntil = new Timestamp(dateFormat.parse("26/09/2007").getTime());
        Reservation equal = new Reservation(id, roomId, userId,
                "Title", since, newUntil, null);
        assertNotEquals(equal, reservation);
    }

    @Test
    void testNotEqualCancelReason() {
        Reservation reservation1 = new Reservation(id, roomId, userId,
                "Title", since, until, "Good reason");
        Reservation reservation2 = new Reservation(id, roomId, userId,
                "Title", since, until, "Not good reason");
        assertNotEquals(reservation1, reservation2);
    }

    @Test
    void testSetMethods() {
        Reservation equal = new Reservation();
        equal.setId(id);
        equal.setRoomId(roomId);
        equal.setUserId(userId);
        equal.setTitle("Title");
        equal.setSince(since);
        equal.setUntil(until);
        equal.setCancelReason(null);
        assertEquals(equal, reservation);
    }

    @Test
    void testHashCode() {
        int result = Objects.hashCode(reservation);
        assertEquals(result, reservation.hashCode());
    }


    @Test
    void testHashCodeLastFieldsNull() {
        Reservation hash = new Reservation(1L, 1L, 1L, null, null, null, null);
        assertEquals(Objects.hashCode(hash), hash.hashCode());
    }

    @Test
    void testHashCodeNotNullCancelReason() {
        Reservation hash = new Reservation(3L, 2L, 4L, "Title", since, until, "Maintenance");
        assertEquals(Objects.hashCode(hash), hash.hashCode());
    }

    @Test
    void testToString() {
        String s = "Reservation{id=1, room_id=2, user_id=3, title='Title', "
                + "since=" + since + ", until=" + until + ", cancel_reason='null'}";
        assertEquals(s, reservation.toString());
    }

    @Test
    void testSetTimeFromRequest() {
        ApiDateTime tmpSince = new ApiDateTime(new ApiDate(2020, 12, 21), new ApiTime(12L, 0L));
        ApiDateTime tmpUntil = new ApiDateTime(new ApiDate(2020, 12, 22), new ApiTime(9L, 0L));
        requestModel.setSince(tmpSince);
        requestModel.setUntil(tmpUntil);
        reservation.setTimeFromRequest(requestModel);
        assertEquals(tmpSince.toTimestamp(), reservation.getSince());
        assertEquals(tmpUntil.toTimestamp(), reservation.getUntil());
    }

    @Test
    void testFillOutTimeSinceNull() {
        requestModel.setSince(null);
        reservation.fillOutTime(requestModel);
        assertEquals(since, requestModel.getSince().toTimestamp());
    }

    @Test
    void testFillOutTimeUntilNull() {
        requestModel.setUntil(null);
        reservation.fillOutTime(requestModel);
        assertEquals(until, requestModel.getUntil().toTimestamp());
    }

    @Test
    void testFillOutTimeNoChanges() {

        requestModel.setSince(new ApiDateTime(new ApiDate(2020, 12, 21), new ApiTime(12L, 0L)));
        requestModel.setUntil(new ApiDateTime(new ApiDate(2020, 12, 22), new ApiTime(9L, 0L)));
        reservation.fillOutTime(requestModel);
        assertNotEquals(since, requestModel.getSince().toTimestamp());
        assertNotEquals(until, requestModel.getUntil().toTimestamp());
    }
}