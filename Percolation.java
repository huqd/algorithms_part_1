import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private WeightedQuickUnionUF uf;
	private int n;
	private int openNum;
	private boolean[] open;
	private int virtualTop;
	private int virtualBottom;
	
	enum Direction {LEFT, UP, RIGHT, DOWN};

	// creates n-by-n grid, with all sites initially blocked
	public Percolation(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		this.n = n;
		initSites(n);
		uf = new WeightedQuickUnionUF(n * n + 2); // Add virtual sites
	}

	/*
	 * Support methods
	 */

	private void initSites(int n) {
		open = new boolean[n * n + 2];
		virtualTop = n * n;
		virtualBottom = n * n + 1;

		// Set to be blocked
		for (int i = 0; i < open.length; i++) {
			open[i] = false;
		}

		// Set to be open
		open[virtualTop] = true;
		open[virtualBottom] = true;
	}

	private int siteIdx(int row, int col) {
		if (row < 1 || row > n)
			throw new IllegalArgumentException();
		if (col < 1 || col > n)
			throw new IllegalArgumentException();

		return (row - 1) * n + col - 1;
	}

	private void unionAdjacent(int row, int col, Direction d) {
		try {
			int adjacentRow = row;
			int adjacentCol = col;

			if (d == Direction.LEFT) {
				adjacentCol = col - 1;
			} else if (d == Direction.UP) {
				adjacentRow = row - 1;
			} else if (d == Direction.RIGHT) {
				adjacentCol = col + 1;
			} else {
				adjacentRow = row + 1;
			}

			if (! isOpen(adjacentRow, adjacentCol))
				return;
			if(uf.find(siteIdx(row, col)) == uf.find(siteIdx(adjacentRow, adjacentCol)))
				return;
			uf.union(siteIdx(row, col), siteIdx(adjacentRow, adjacentCol));
		} catch (IllegalArgumentException e) {
			// Invalid adjacent at corners
		}
	}

	/*
	 * APIs
	 */

	// opens the site (row, col) if it is not open already
	public void open(int row, int col) {
		if (isOpen(row, col))
			return;
		
		int site = siteIdx(row, col);
		open[site] = true;
		openNum++;

		if (row == 1) {
			uf.union(site, virtualTop);
		}

		if (row == n) {
			uf.union(site, virtualBottom);
		}
		
		unionAdjacent(row, col, Direction.LEFT);
		unionAdjacent(row, col, Direction.UP);
		unionAdjacent(row, col, Direction.RIGHT);
		unionAdjacent(row, col, Direction.DOWN);
	}

	// is the site (row, col) open?
	public boolean isOpen(int row, int col) {
		return open[siteIdx(row, col)];
	}

	// is the site (row, col) full?
	public boolean isFull(int row, int col) {
		int site = siteIdx(row, col);
		return uf.find(site) == uf.find(virtualTop);
	}

	// returns the number of open sites
	public int numberOfOpenSites() {
		return openNum;
	}

	// does the system percolate?
	public boolean percolates() {
		return uf.find(virtualBottom) == uf.find(virtualTop);
	}

	// test client (optional)
	public static void main(String[] args) {
		Percolation p = new Percolation(20);
		for (int i = 1; i <= 15; i++) {
			p.open(i, 1);
		}

		for (int i = 15; i <= 19; i++) {
			p.open(i, 2);
		}

		p.open(19, 3);
		p.open(19, 4);
		p.open(20, 4);

		StdOut.print(p.isOpen(1, 2));
		StdOut.print(p.isOpen(20, 2));
		StdOut.print(p.percolates());
		StdOut.print(p.numberOfOpenSites());
	}
}
