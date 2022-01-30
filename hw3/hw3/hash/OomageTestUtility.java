package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {

        int[] buckets = new int[M];

        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = 0;
        }

        int i = 0;
        for (Oomage o : oomages) {
            buckets[(o.hashCode() & 0x7FFFFFFF) % M]++;
            i++;
        }

        for (int j : buckets) {
            if (j < i / 50 || j > i / 2.5) {
                return false;
            }
        }

        return true;
    }
}
