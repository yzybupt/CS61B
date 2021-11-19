public class ArrayDeque<T> {
    private int size;
    private int head;
    private int tail;
    private T[] items;

    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        tail = 0;
        head = 0;
    }

    private int correctIndex(int index) {
        if (index < 0) {
            return items.length + index;
        } else if (index > items.length - 1) {
            return index % items.length;
        } else {
            return index;
        }
    }

    private void resize() {
        T[] p = (T[]) new Object[items.length + 8];
        int j = correctIndex(head);
        for (int i = size-1; i >= 0; i--) {
            p[i] = items[j];
            j = correctIndex(j - 1);
        }
        items = p;
        head = size - 1;
        tail = items.length-1;
    }

    private void resize2() {
        /**T[] p = (T[]) new Object[(int) (items.length * 0.5 + 1)];
        int j = correctIndex(head + 1);
        for (int i = 0; i < size; i++) {
            p[i] = items[j];
            j = correctIndex(j + 1);
        }
        items = p;
        head = items.length - 1;
        tail = size;
         */
    }

    public void addFirst(T argument) {
        if (size >= items.length) {
            resize();
        }
        if (size == 0){
            items[head] = argument;
            tail = correctIndex(tail--);
        } else {
            items[correctIndex(head + 1)] = argument;
            head = correctIndex(head + 1);
        }
        size++;
    }

    public void addLast(T argument) {
        if (size >= items.length) {
            resize();
        }
        if (size == 0) {
            items[head] = argument;
            tail = correctIndex(tail - 1);
            size++;
        } else {
            items[tail] = argument;
            tail = correctIndex(tail - 1);
            size++;
        }
    }


    public boolean isEmpty(){
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int j = correctIndex(head);
        for (int i = 0; i < size; i++) {
            System.out.print(items[j] + " ");
            j = correctIndex(j - 1);
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T p = items[correctIndex(head)];
            head = correctIndex(head - 1);
            size--;
            if (items.length >= 16 && size / items.length < 0.30) {
                resize2();
            }
            return p;
        }
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T p = items[correctIndex(tail + 1)];
            tail = correctIndex(tail);
            size--;
            if (items.length >= 16 && size / items.length < 0.30) {
                resize2();
            }
            return p;
        }
    }

    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        } else {
            int j = correctIndex(head + 1);
            for (int i = 0; i < index; i++) {
                j = correctIndex(j + 1);
            }
            T p  = items[j];
            return p;
        }
    }

}
