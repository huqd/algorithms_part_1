import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        Item item;
        Node next;
        Node prev;

        public Node(Item item) {
            this.item = item;
        }
    }

    private Node first, last;
    private int size = 0;

    public Deque() {

    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node itemNode = new Node(item);
        size++;
        if (isEmpty()) {
            last = first = itemNode;
            return;
        }

        itemNode.next = first;
        first.prev = itemNode;
        first = itemNode;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node itemNode = new Node(item);
        size++;
        if (isEmpty()) {
            first = last = itemNode;
            return;
        }
        last.next = itemNode;
        itemNode.prev = last;
        last = itemNode;
    }

    public Item removeFirst() {
        if (this.isEmpty()) throw new NoSuchElementException();
        size--;
        Node oldFirst = first;
        if (first == last) {
            first = last = null;
        } else {
            first = first.next;
            first.prev = null;
        }

        return oldFirst.item;
    }

    public Item removeLast() {
        if (this.isEmpty())
            throw new NoSuchElementException();
        size--;
        Node oldLast = last;

        if (first == last) {
            first = last = null;
        } else {
            last = last.prev;
            last.next = null;
        }

        return oldLast.item;
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        deque.removeFirst();
        deque.addFirst(3);

        for (Integer i : deque) {
            StdOut.println(i);
        }
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return current != null;
        }

        @Override
        public Item next() {
            // TODO Auto-generated method stub
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
