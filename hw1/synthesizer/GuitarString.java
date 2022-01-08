package synthesizer;
import java.lang.Math.*;
import java.util.HashSet;
import java.util.Set;

//Make sure this class is public
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {


        int length = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<Double>(length);
        for (int i = 0; i < length; i++) {
            buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {

        double number;
        Set<Double> set = new HashSet<Double>();
        while (set.size() < buffer.capacity()) {
            number = Math.random()  - 0.5;
            if (!set.contains(number)) {
                set.add(number);
            }
        }

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.dequeue();
        }

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.enqueue(set.iterator().next());
        }


    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double first = buffer.dequeue();
        double second = buffer.peek();
        buffer.enqueue(0.5d * (first + second) * DECAY);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
