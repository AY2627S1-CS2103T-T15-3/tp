package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RemarkTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null));
        assertThrows(NullPointerException.class, () -> Remark.isValidRemark(null));
    }

    @Test
    public void empty_representsNoRemark() {
        assertTrue(Remark.EMPTY.isEmpty());
        assertEquals("", Remark.EMPTY.value);
        assertEquals(Remark.EMPTY, new Remark(""));
        assertTrue(Remark.isValidRemark(""));
        assertFalse(new Remark("Practice fractions").isEmpty());
    }

    @Test
    public void constructor_stripsEdges_preservesInternalSpaces() {
        Remark remark = new Remark(" \u2003Practice  fractions\u2003 ");
        assertEquals("Practice  fractions", remark.value);
        assertEquals(remark.value, remark.toString());
        assertTrue(Remark.isValidRemark("  Practice  fractions  "));
    }

    @Test
    public void constructor_blankOrControlText_throwsIllegalArgumentException() {
        String[] invalid = {" ", "\u2003", "\u00a0", "\t", "\n", "\r", "text\ntext", "text\r\ntext",
            "\ttext", "text\t", "text\u0000text", "text\u007f", "text\u0085", "text\u2028text",
            "text\u2029text"};
        for (String text : invalid) {
            assertFalse(Remark.isValidRemark(text));
            assertThrows(IllegalArgumentException.class, () -> new Remark(text));
        }
    }

    @Test
    public void constructor_lengthBoundaries_countsNormalizedCodePoints() {
        for (String character : new String[] {"a", "学", "\ud83d\ude00"}) {
            assertEquals(character.repeat(299), new Remark(character.repeat(299)).value);
            assertEquals(character.repeat(300), new Remark(" " + character.repeat(300) + " ").value);
            assertTrue(Remark.isValidRemark(character.repeat(300)));
            assertFalse(Remark.isValidRemark(character.repeat(301)));
            assertThrows(IllegalArgumentException.class, () -> new Remark(character.repeat(301)));
        }
        assertEquals("学\ud83d\ude00".repeat(150), new Remark("学\ud83d\ude00".repeat(150)).value);
    }

    @Test
    public void equalsAndHashCode_compareNormalizedCaseSensitiveText() {
        Remark remark = new Remark("Practice fractions");
        Remark equivalent = new Remark(" Practice fractions ");
        assertEquals(remark, remark);
        assertEquals(remark, equivalent);
        assertEquals(remark.hashCode(), equivalent.hashCode());
        assertNotEquals(remark, new Remark("practice fractions"));
        assertNotEquals(remark, Remark.EMPTY);
        assertNotEquals(remark, null);
        assertNotEquals(remark, "Practice fractions");
    }
}
