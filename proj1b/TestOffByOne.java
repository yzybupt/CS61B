import org.junit.Test;

import static org.junit.Assert.*;

public class TestOffByOne {

    //static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testOffbyOne() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('L', 'M'));
        assertTrue(offByOne.equalChars('!', ' '));
        assertFalse(offByOne.equalChars('a', 'a'));
        assertFalse(offByOne.equalChars('c', 'f'));

    }
}
