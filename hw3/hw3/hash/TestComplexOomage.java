package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }



    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();
        List<Integer> lists = new ArrayList<>();
        lists.add(1);
        lists.add(2);
        lists.add(3);
        lists.add(4);
        deadlyList.add(new ComplexOomage(lists));

        lists.add(1);
        lists.add(2);
        lists.add(3);
        lists.add(4);
        deadlyList.add(new ComplexOomage(lists));


        lists.add(1);
        lists.add(2);
        lists.add(3);
        lists.add(4);
        deadlyList.add(new ComplexOomage(lists));

        lists.add(1);
        lists.add(2);
        lists.add(3);
        lists.add(4);
        deadlyList.add(new ComplexOomage(lists));

        lists.add(1);
        lists.add(2);
        lists.add(3);
        lists.add(4);
        deadlyList.add(new ComplexOomage(lists));

        lists.add(1);
        lists.add(2);
        lists.add(3);
        lists.add(4);
        deadlyList.add(new ComplexOomage(lists));

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
