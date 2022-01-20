package nl.tudelft.sem11b.admin.data.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.text.ParseException;

import nl.tudelft.sem11b.admin.data.Closure;
import nl.tudelft.sem11b.data.ApiDateUtils;
import org.junit.jupiter.api.Test;

public class ClosureTest {
    // string constants are used for ApiDate to avoid reference equality
    static final transient String REASON = "stuff happened";
    static final transient String SINCE = "2022-01-01";
    static final transient String UNTIL = "2022-01-02";

    static final transient String NEW_REASON = REASON + "*";
    static final transient String NEW_SINCE = "2023-02-03";
    static final transient String NEW_UNTIL = "2024-05-06";

    @Test
    public void propertyTest() throws ParseException {
        var closure = newSubject();
        assertEquals(REASON, closure.getReason());
        assertEquals(SINCE, closure.getSince().toString());
        assertEquals(UNTIL, closure.getUntil().toString());

        closure.setReason(NEW_REASON);
        assertEquals(NEW_REASON, closure.getReason());
        assertEquals(SINCE, closure.getSince().toString());
        assertEquals(UNTIL, closure.getUntil().toString());

        closure.setSince(ApiDateUtils.parse(NEW_SINCE));
        assertEquals(NEW_REASON, closure.getReason());
        assertEquals(NEW_SINCE, closure.getSince().toString());
        assertEquals(UNTIL, closure.getUntil().toString());

        closure.setUntil(ApiDateUtils.parse(NEW_UNTIL));
        assertEquals(NEW_REASON, closure.getReason());
        assertEquals(NEW_SINCE, closure.getSince().toString());
        assertEquals(NEW_UNTIL, closure.getUntil().toString());
    }

    @Test
    public void modelConversionTest() {
        var closure = newSubject();
        var model = closure.toModel();

        assertEquals(closure.getReason(), model.getReason());
        assertEquals(closure.getSince(), model.getSince());
        assertEquals(closure.getUntil(), model.getUntil());
    }

    @Test
    public void equalityTest() throws ParseException {
        var left = newSubject();
        var right = newSubject();

        assertEquals(left, left);
        assertEquals(right, right);
        assertEquals(left, right);

        assertNotEquals(left, REASON);
        assertNotEquals(left, UNTIL);
        assertNotEquals(left, SINCE);
        assertNotEquals(left, left.toModel());

        left.setReason(NEW_REASON);
        assertNotEquals(left, right);

        right.setReason(NEW_REASON);
        assertEquals(left, right);

        left.setSince(ApiDateUtils.parse(NEW_SINCE));
        assertNotEquals(left, right);

        right.setSince(ApiDateUtils.parse(NEW_SINCE));
        assertEquals(left, right);

        left.setUntil(ApiDateUtils.parse(NEW_SINCE));
        assertNotEquals(left, right);

        right.setUntil(ApiDateUtils.parse(NEW_SINCE));
        assertEquals(left, right);
    }

    @Test
    public void hashCodeTest() throws ParseException {
        var left = newSubject();
        var right = newSubject();

        assertEquals(left.hashCode(), right.hashCode());

        left.setReason(NEW_REASON);
        assertNotEquals(left.hashCode(), right.hashCode());

        right.setReason(NEW_REASON);
        assertEquals(left.hashCode(), right.hashCode());

        left.setSince(ApiDateUtils.parse(NEW_SINCE));
        assertNotEquals(left.hashCode(), right.hashCode());

        right.setSince(ApiDateUtils.parse(NEW_SINCE));
        assertEquals(left.hashCode(), right.hashCode());

        left.setUntil(ApiDateUtils.parse(NEW_SINCE));
        assertNotEquals(left.hashCode(), right.hashCode());

        right.setUntil(ApiDateUtils.parse(NEW_SINCE));
        assertEquals(left.hashCode(), right.hashCode());
    }

    private Closure newSubject() {
        try {
            return new Closure(REASON, ApiDateUtils.parse(SINCE), ApiDateUtils.parse(UNTIL));
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
}
