public class ArrayDeque<Item> {
    int size;
    int nextFirst;
    int nextEnd;
    Item[] items;

    public ArrayDeque() {
        size = 0;
        items = (Item[]) new Object[8];
        nextEnd = 0;
        nextFirst = 1;
    }

    private int correctIndex(int index) {
        if (index < 0) {
            return (-index) % items.length;
        } else if (index > items.length - 1) {
            return index % items.length;
        } else {
            return index;
        }
    }

    private void resize() {
        Item[] p = (Item[]) new Object[items.length + 8];
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
        Item[] p = (Item[]) new Object[(int) (items.length * 0.5)];
        int j = correctIndex(nextFirst + 1);
        for (int i = 0; i < size; i++) {
            p[i] = items[j];
            j = correctIndex(j + 1);
        }
        items = p;
        nextFirst = items.length - 1;
        nextEnd = size;
    }

    public void addFirst(Item T) {
        if (size >= items.length) {
            resize();
        }
        items[nextFirst] = T;
        nextFirst = correctIndex(nextFirst - 1);
        size++;
    }

    public void addLast(Item T) {
        if (size >= items.length) {
            resize();
        }
        items[nextEnd] = T;
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

    public Item removeFirst() {
        if (size == 0) {
            return null;
        } else {
            Item p = items[correctIndex(nextFirst + 1)];
            nextFirst = correctIndex(nextFirst + 1);
            size--;
            if (items.length >= 16 && size / items.length < 0.30) {
                resize2();
            }
            return p;
        }
    }

    public Item removeLast() {
        if (size == 0) {
            return null;
        } else {
            Item p = items[correctIndex(nextEnd - 1)];
            nextEnd = correctIndex(nextEnd - 1);
            size--;
            if (items.length >= 16 && size / items.length < 0.30) {
                resize2();
            }
            return p;
        }
    }

    public Item get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        } else {
            int j = correctIndex(nextFirst + 1);
            for (int i = 0; i < index; i++) {
                j = correctIndex(j + 1);
            }
            Item p  = items[j];
            return p;
        }
    }

}
