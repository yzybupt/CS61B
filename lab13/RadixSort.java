/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    private static int maxLength = 0;
    private static String[] results;
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        for (String str : asciis) { //初始化，找到最长的字符串长度
            if (str.length() > maxLength) {
                maxLength = str.length();
            }
        }

        String[] a = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            a[i] = asciis[i];
        }

        sortHelperLSD(a, 0);

        return results;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int[] countings = new int[256];
        int[] indexes = new int[256];
        String[] sortedLSD = new String[asciis.length];
        for (int i = 0; i < 256; i++) {
            countings[i] = 0;
        }

        for (String str : asciis) { // 计算完成counting arrary
            countings[(int) charAt(str, index)]++;
        }

        for (int i = 0; i < 256; i++) { // 计算完成index array
            if (i == 0) {
                indexes[i] = 0;
            } else {
                indexes[i] = countings[i - 1] + indexes[i - 1];
            }
        }

        for (String str : asciis) {
            sortedLSD[indexes[(int) charAt(str, index)]] = str;
            indexes[(int) charAt(str, index)]++;
        }

        if (index == maxLength - 1) {
            results = sortedLSD;
            return;
        }

        sortHelperLSD(sortedLSD, index + 1);

    }


    private static char charAt(String str, int index) {
        int convertedIndex = maxLength - 1 - index;
        //System.out.println(convertedIndex);
        if (convertedIndex <= str.length() - 1) {
            return str.charAt(convertedIndex);
        } else {
            return (char) 0; //right-padding, 返回最小的char
        }
    }


    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    /*public static void main(String[] args) {
        String[] a = {"astronomy", "aster", "as", "astrolabe"};
        System.out.println(RadixSort.sort(a)[0]);
        System.out.println(RadixSort.sort(a)[1]);
        System.out.println(RadixSort.sort(a)[2]);
        System.out.println(RadixSort.sort(a)[3]);

        System.out.println(a[0]);
        System.out.println(a[1]);
        System.out.println(a[2]);
        System.out.println(a[3]);



    }*/

}
