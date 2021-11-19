public class ArrayDeque<T> {
    private int size;
    private int nextFirst;
    private int nextEnd;
    private T[] items;

    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        nextEnd = 1;
        nextFirst = 0;
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
        int j = correctIndex(nextFirst + 1);
        for (int i = 0; i < size; i++) {
            p[i] = items[j];
            j = correctIndex(j + 1);
        }
        items = p;
        nextFirst = items.length - 1;
        nextEnd = size;
    }

    private void resize2() {
        T[] p = (T[]) new Object[(int) (items.length * 0.5 + 1)];
        int j = correctIndex(nextFirst + 1);
        for (int i = 0; i < size; i++) {
            p[i] = items[j];
            j = correctIndex(j + 1);
        }
        items = p;
        nextFirst = items.length - 1;
        nextEnd = size;
    }

    public void addFirst(T argument) {
        if (size >= items.length || nextFirst == nextEnd) {
            resize();
        }
        items[nextFirst] = argument;
        nextFirst = correctIndex(nextFirst - 1);
        size++;
    }

    public void addLast(T argument) {
        if (size >= items.length || nextFirst == nextEnd) {
            resize();
        }
        items[nextEnd] = argument;
        nextEnd = correctIndex(nextEnd + 1);
        size++;

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int j = correctIndex(nextFirst + 1);
        for (int i = 0; i < size; i++) {
            System.out.print(items[j] + " ");
            j = correctIndex(j + 1);
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T p = items[correctIndex(nextEnd - 1)];
            nextEnd = correctIndex(nextEnd - 1);
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
            T p = items[correctIndex(nextFirst + 1)];
            nextFirst = correctIndex(nextFirst + 1);
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
            int j = correctIndex(nextFirst + 1);
            for (int i = 0; i < index; i++) {
                j = correctIndex(j + 1);
            }
            T p  = items[j];
            return p;
        }
    }

}
