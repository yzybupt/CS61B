
package synthesizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {

        rb = (T[]) new Object[capacity];
        this.capacity = capacity;
        this.fillCount = 0;
        this.first = 0;
        this.last = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {

        if (this.fillCount() == this.capacity()) {
            throw new RuntimeException("Ring Buffer Overflow");
        } else {
            rb[this.last] = x;
            this.fillCount++;
            this.last = (this.last + 1) % this.capacity;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {

        if (this.fillCount() == 0) {
            throw new RuntimeException("Ring Buffer Underflow");
        } else {
            this.fillCount--;
            T temp = rb[this.first];
            this.first = (this.first + 1) % this.capacity;
            return temp;
        }
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {

        if (this.isEmpty()) {
            throw new RuntimeException("None in the Array");
        }
        return rb[this.first];
    }

    @Override
    public Iterator<T> iterator() {
        return new Iteration();
    }

    private class Iteration implements Iterator<T> {
        int pointer = -1;
        List<T> list = new ArrayList<T>();
        Iteration() {
            while (!isEmpty()) {
                list.add(dequeue());
            }
            for (int i = 0; i < capacity; i++) {
                enqueue(list.get(i));
            }
            if (!isEmpty()) {
                pointer = 0;
            }
        }


        public boolean hasNext() {
            if (pointer == -1 || pointer == capacity) {
                return false;
            }
            return true;


        }
        public T next() {
            T temp = list.get(pointer);
            pointer++;
            return temp;
        }


    }




}
