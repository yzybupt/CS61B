public class ArrayDeque<T> implements Deque<T> {
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

    private void resize(int a) {
        T[] p = (T[]) new Object[a];
        int j = correctIndex(head);
        for (int i = size - 1; i >= 0; i--) {
            p[i] = items[j];
            j = correctIndex(j - 1);
        }
        items = p;
        head = correctIndex(size - 1);
        tail = correctIndex(items.length - 1);
    }

    @Override
    public void addFirst(T argument) {
        if (size >= items.length) {
            resize(items.length * 2);
        }
        if (size == 0) {
            items[correctIndex(head)] = argument;
            tail = correctIndex(tail - 1);
        } else {
            items[correctIndex(head + 1)] = argument;
            head = correctIndex(head + 1);
        }
        size = size + 1;
    }

    @Override
    public void addLast(T argument) {
        if (size >= items.length) {
            resize(items.length * 2);
        }
        if (size == 0) {
            items[correctIndex(head)] = argument;
        } else {
            items[correctIndex(tail)] = argument;
        }
        tail = correctIndex(tail - 1);
        size = size + 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int j = correctIndex(head);
        for (int i = 0; i < size; i++) {
            System.out.print(items[j] + " ");
            j = correctIndex(j - 1);
        }
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T p = items[correctIndex(head)];
            head = correctIndex(head - 1);
            size = size - 1;
            if (items.length > 16 && size < (items.length / 4)) {
                resize((int) (0.5 * items.length + 1));
            }
            return p;
        }
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T p = items[correctIndex(tail + 1)];
            tail = correctIndex(tail + 1);
            size = size - 1;
            if (items.length > 16 && size < (items.length / 4)) {
                resize((int) (0.5 * items.length + 1));
            }
            return p;
        }
    }

    @Override
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        } else {
            int j = correctIndex(head - index);
            T p  = items[j];
            return p;
        }
    }

}
