package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LevelTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Level(null));
    }

    @Test
    public void constructor_invalidLevel_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Level(""));
        assertThrows(IllegalArgumentException.class, () -> new Level("   "));
        assertThrows(IllegalArgumentException.class, () -> new Level("Secondary-3"));
        assertThrows(IllegalArgumentException.class, () -> new Level("a".repeat(51)));
    }

    @Test
    public void constructor_normalizesWhitespace() {
        assertEquals("Secondary 3", new Level("  Secondary   3 ").value);
    }

    @Test
    public void isValidLevel() {
        assertThrows(NullPointerException.class, () -> Level.isValidLevel(null));
        assertTrue(Level.isValidLevel("Secondary 3"));
        assertTrue(Level.isValidLevel("  JC   1 "));
        assertFalse(Level.isValidLevel(""));
        assertFalse(Level.isValidLevel("Secondary-3"));
    }

    @Test
    public void normalize_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Level.normalize(null));
    }

    @Test
    public void equals() {
        Level level = new Level("Secondary 3");
        Level equivalentLevel = new Level("secondary   3");
        Level differentLevel = new Level("JC 1");

        assertTrue(level.equals(level));
        assertTrue(level.equals(equivalentLevel));
        assertFalse(level.equals(null));
        assertFalse(level.equals(5));
        assertFalse(level.equals(differentLevel));
        assertEquals(level.hashCode(), equivalentLevel.hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals("Secondary 3", new Level("Secondary 3").toString());
    }
}
