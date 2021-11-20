import org.junit.Test;

import static org.junit.Assert.*;

public class TestOffByOne {
    public static OffByOne test = new OffByOne();
    /*
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    Uncomment this class once you've created your CharacterComparator interface and OffByOne class. **/
    @Test
    public void testOffbyOne() {
        assertTrue(test.equalChars('a','b'));
        assertTrue(test.equalChars('L','M'));
        assertTrue(test.equalChars('!',' '));
        assertFalse(test.equalChars('a','a'));
        assertFalse(test.equalChars('c','f'));

    }
}
