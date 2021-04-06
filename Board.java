import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.lang.Math;
import java.util.Arrays;
import java.util.LinkedList;


public class Board {
    private final int[][] tiles;
    private final int dimension;
    private int hamming = 0, manhattan = 0;
    private int iBlank, jBlank;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles[0].length;
        this.tiles = deepCopy(tiles);

        // Calculate hamming, manhattan
        int iGoal, jGoal;
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                if (this.tiles[i][j] == 0) { // Catch the blank
                    iBlank = i;
                    jBlank = j;
                    continue;
                }

                if (this.tiles[i][j] != i * dimension + j + 1) { // If not at goal pos
                    jGoal = (this.tiles[i][j] - 1) % dimension;
                    iGoal = (this.tiles[i][j] - jGoal - 1) / dimension;
                    manhattan += Math.abs(i - iGoal) + Math.abs(j - jGoal);
                    hamming++;
                }
            }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", this.tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    /**
     * Private support methods
     */
    private int[][] exchCopy(int[][] tiles, int i1, int j1, int i2, int j2) {
        // Copy
        int[][] copy = deepCopy(tiles);
        // Exchange
        int tmp = copy[i1][j1];
        copy[i1][j1] = copy[i2][j2];
        copy[i2][j2] = tmp;

        return  copy;
    }

    private int[][] deepCopy(int[][] tiles) {
        int dimension = tiles[0].length;
        int[][] copy = new int[dimension][dimension];
        for(int i=0; i < dimension; i++)
            copy[i] = tiles[i].clone();

        return copy;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<>();
        if (jBlank > 0) {
            Board n = new Board(exchCopy(tiles, iBlank, jBlank, iBlank, jBlank - 1));
            neighbors.add(n);
        }
        if (iBlank > 0) {
            Board n = new Board(exchCopy(tiles, iBlank, jBlank, iBlank - 1, jBlank));
            neighbors.add(n);
        }
        if (jBlank < dimension - 1) {
            Board n = new Board(exchCopy(tiles, iBlank, jBlank, iBlank, jBlank + 1));
            neighbors.add(n);
        }
        if (iBlank < dimension - 1) {
            Board n = new Board(exchCopy(tiles, iBlank, jBlank, iBlank + 1, jBlank));
            neighbors.add(n);
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int i1, j1, i2, j2;

        i1 = 0;
        j1 = tiles[0][0] != 0 ? 0 : 1;
        i2 = dimension - 1;
        j2 = tiles[dimension - 1][dimension - 1] != 0 ? dimension - 1 : dimension - 2;

        int[][] copy = exchCopy(tiles, i1, j1, i2, j2);
        return new Board(copy);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = new int[4][4];
        Board board;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i][j] = i * 4 + j;
            }

            StdRandom.shuffle(tiles[i]);
        }

        board = new Board(tiles);
        StdOut.print(board.toString());

        board.neighbors();
        board.neighbors();
        StdOut.println(board.twin());
        board.manhattan();
        board.manhattan();
        board.toString();
        StdOut.println(board.twin());
    }

}