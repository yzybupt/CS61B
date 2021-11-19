public class LinkedListDeque<Item> {
    public class ItemNode<Item> {
        ItemNode first;
        ItemNode next;
        Item value;
        public ItemNode(Item a) {
            value = a;
            first = null;
            next = null;
        }
    }
    ItemNode front;
    ItemNode end;
    int size;

    public LinkedListDeque ( ) {
        front = new ItemNode(0);
        end = new ItemNode(0);

        front.next = end;
        end.first = front;
        size = 0;

    }

    public void addFirst(Item T) {
        ItemNode p = new ItemNode(T);
        p.next=front.next;
        p.first=front;
        front.next.first=p;
        front.next=p;
        size++;
    }

    public void addLast(Item T) {
        ItemNode p = new ItemNode(T);
        p.next=end;
        p.first=end.first;
        end.first=p;
        end.first.next=p;
        size++;
    }

    public boolean isEmpty() {
        if(size == 0) {
            return true;
        } else {
            return false;
        }

    }

    public int size() {
        return size;
    }

    public void printDeque() {
        if(size == 0) {
            return;
        } else {
            ItemNode p = front;
            for(int i = 0; i < size; i++) {
                System.out.print(p.value+" ");
                p=p.next;
            }
        }

    }

    public Item removeFirst() {
        if(size == 0) {
            return null;
        } else {
            Item p = (Item) front.next.value;
            ItemNode temp = front.next.next;
            front.next = temp;
            temp.first= front;
            size--;
            return p;
        }
    }

    public Item removeLast() {
        if(size == 0) {
            return null;
        } else {
            Item p = (Item) end.first.value;
            ItemNode temp= end.first.first;
            end.first = temp;
            temp.next = end;
            size--;
            return p;
        }
    }

    public Item get(int index) {
        if(size == 0) {
            return null;
        } else if(index < 0 || index >= size) {
            return null;
        } else {
            ItemNode p  = front.next;
            for (int i = 0; i < index; i++) {
                p=p.next;
            }
            return (Item) p.value;
        }
    }

    private Item getRecursive1(int index, ItemNode p) {
        if(index == 0) {
            return (Item) p.value;
        } else {
            return (Item) getRecursive1(index-1,p.next);
        }

    }

    public Item getRecursive(int index) {
        if(size == 0) {
            return null;
        } else if (index < 0 || index >= size) {
            return null;
        }
        return getRecursive1(index,front);
    }

}