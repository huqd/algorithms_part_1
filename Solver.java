import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private Board initial;
    private boolean solvable;
    private Iterable<Board> solution;
    private int moves;

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode prev;
        private int moves;
        private int priority;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            this.priority = board.manhattan() + moves;
        }

        private Integer priority() {
            return priority;
        }

        @Override
        public int compareTo(SearchNode o) {
            if (this.priority().compareTo(o.priority()) == 0) {
                if (this.board.hamming() < o.board.hamming()) return -1;
                if (this.board.hamming() > o.board.hamming()) return 1;
                return 0;
            }

            return this.priority().compareTo(o.priority());
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        this.initial = initial;
        this.solvable = false;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode dequeued;
        SearchNode twinDequeued;
        while (true) {
            // Initial search
            dequeued = pq.delMin();
            if (dequeued.board.isGoal()) {
                solution = yieldSolution(dequeued);
                moves = dequeued.moves;
                solvable = true;
                break;
            }

            for (Board neighbor : dequeued.board.neighbors()) {
                if (dequeued.prev != null && neighbor.equals(dequeued.prev.board)) continue;
                pq.insert(new SearchNode(neighbor, dequeued.moves + 1, dequeued));
            }

            // Twin search
            twinDequeued = pqTwin.delMin();
            if (twinDequeued.board.isGoal()) {
                break;
            }

            for (Board neighbor : twinDequeued.board.neighbors()) {
                if (twinDequeued.prev != null && neighbor.equals(twinDequeued.prev.board)) continue;
                pqTwin.insert(new SearchNode(neighbor, twinDequeued.moves + 1, twinDequeued));
            }
        }

        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solvable || isSolvable()) return moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solvable || isSolvable()) return solution;
        return null;
    }

    private Iterable<Board> yieldSolution(SearchNode dequeued) {
        Stack<Board> boards = new Stack<>();
        do {
            boards.push(dequeued.board);
            dequeued = dequeued.prev;
        } while (
                dequeued != null
        );

        return boards;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);
        solver.solution();
        solver.moves();

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
