package nl.tudelft.sem11b.reservation.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

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

    @BeforeEach
    void setUp() throws ParseException {
        since = new Timestamp(dateFormat.parse("23/09/2007").getTime());
        until = new Timestamp(dateFormat.parse("24/09/2007").getTime());
        reservation = new Reservation(id, roomId, userId,
                "Title", since, until, null);
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
    void testHashCode1() {
        assertEquals(Objects.hashCode(reservation), reservation.hashCode());
    }

    @Test
    void testHashCode2() {
        Reservation hash = new Reservation(1L, 1L, 1L, null, null, null, null);
        assertEquals(Objects.hashCode(hash), hash.hashCode());
    }
}