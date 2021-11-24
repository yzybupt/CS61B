import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

public class TestArrayDequeGold {

    private String message(int i, int[] numbers, int[] operations) {
        String mess = "";
        for (int j = 0; j <= i; j++) {
            if (operations[j] == 1) {
                mess = mess + "addFirst(" + numbers[j] + ")\n";
            } else if (operations[j] == 2) {
                mess = mess + "addLast(" + numbers[j] + ")\n";
            } else if (operations[j] == 3) {
                mess = mess + "removeFirst()\n";
            } else if (operations[j] == 4) {
                mess = mess + "removeLast()\n";
            }
        }
        return mess;
    }



    @Test
    public void test() {
        int kk = 0;
        label: while (true) {
                ArrayDequeSolution<Integer> list = new ArrayDequeSolution<>();
                StudentArrayDeque<Integer> studentlist = new StudentArrayDeque<>();
                int number_test = StdRandom.uniform(1, 20);
                int [] operations = new int[number_test];
                operations[0] = StdRandom.uniform(1, 3);
                for (int i = 1; i < number_test; i++) {
                    operations[i] = StdRandom.uniform(1, 5);
                }
                int number_add = 1;
                int number_remove = 0;
                for (int i = 1; i < number_test; i++) {
                    if (operations[i] == 1 || operations[i] == 2) {
                        number_add = number_add + 1;
                    } else {
                        number_remove = number_remove + 1;
                    }
                    if (number_remove > number_add) {
                        continue label;
                    }
                }
                int [] numbers = new int[number_test];
                for (int i = 0; i < number_test; i++) {
                    numbers[i] = StdRandom.uniform(1, 100);
                }

                for (int i = 0; i < number_test; i++) {
                    switch (operations[i]) {
                        case 1:
                            list.addFirst(numbers[i]);
                            studentlist.addFirst(numbers[i]);
                            break;
                        case 2:
                            list.addLast(numbers[i]);
                            studentlist.addLast(numbers[i]);
                            break;
                        case 3:
                            Integer actual  = list.removeFirst();
                            Integer expected = studentlist.removeFirst();
                            assertEquals(message(i, numbers, operations), expected, actual);
                            break;
                        case 4:
                            Integer actual1  = list.removeLast();
                            Integer expected1 = studentlist.removeLast();
                            assertEquals(message(i, numbers, operations), expected1, actual1);
                            break;
                        default:
                            break;
                    }
                }
                kk = kk + 1;
        }

    }
}
