import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items = (Item[]) new Object[1]; // memory 8 * N + 24
    private int N = 0;
    private Deque<Integer> nullStack = new Deque<>();

    public RandomizedQueue() {
        // TODO Auto-generated constructor stub
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = items[i];
        }
        items = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (N == items.length) resize(items.length * 2); // memory 16*N + 24;
        items[N++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        int idx = StdRandom.uniform(N);
        Item item = items[idx];
        items[idx] = items[--N]; // this trick is brilliant ?!

        if(N > 0 && N == items.length/4) resize(items.length/2);

        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        return items[StdRandom.uniform(N)];
    }

    @Override
    public Iterator<Item> iterator() {
        // TODO Auto-generated method stub
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] items;
        private int pos = 0;

        public RandomizedQueueIterator() {
            RandomizedQueue<Item> rq = RandomizedQueue.this;
            if (!rq.isEmpty()) {
                items = (Item[]) new Object[rq.N];
                for (int i = 0; i < rq.N; i++)
                    if (rq.items[i] != null) items[pos++] = rq.items[i];
                pos = 0;
                StdRandom.shuffle(items);
            }
        }

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return items != null && pos < items.length;
        }

        @Override
        public Item next() {
            // TODO Auto-generated method stub
            if (!hasNext()) throw new NoSuchElementException();
            return items[pos++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
//        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
//        rq.enqueue(6);
        rq.dequeue();
        rq.dequeue();

        rq.size();
        rq.iterator();

        for (Integer i : rq) {
            StdOut.println(i);
        }
    }
}
