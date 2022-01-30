package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import org.junit.Assert;

public class Percolation {
    private int size;
    private int[][] grid;
    private int number;
    private WeightedQuickUnionUF unions;
    private WeightedQuickUnionUF unions2;


    private boolean checkBoarder(int x, int y) {
        if (x < 0 || x > this.size - 1) {
            return false;
        } else if (y < 0 || y > this.size - 1) {
            return false;
        }
        return true;
    }

    private void connects(int x, int y) {
        if (checkBoarder(x - 1, y) && this.grid[x - 1][y] == 1) {
            unions.union(xyTo1d(x, y), xyTo1d(x - 1, y));
            unions2.union(xyTo1d(x, y), xyTo1d(x - 1, y));

        }

        if (checkBoarder(x + 1, y) && this.grid[x + 1][y] == 1) {
            unions.union(xyTo1d(x, y), xyTo1d(x + 1, y));
            unions2.union(xyTo1d(x, y), xyTo1d(x + 1, y));
        }

        if (checkBoarder(x, y - 1) && this.grid[x][y - 1] == 1) {
            unions.union(xyTo1d(x, y), xyTo1d(x, y - 1));
            unions2.union(xyTo1d(x, y), xyTo1d(x, y - 1));
        }

        if (checkBoarder(x, y + 1) && this.grid[x][y + 1] == 1) {
            unions.union(xyTo1d(x, y), xyTo1d(x, y + 1));
            unions2.union(xyTo1d(x, y), xyTo1d(x, y + 1));
        }
    }

    private int xyTo1d(int x, int y) {
        return x * this.size + y;
    }

    private void clear() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.grid[i][j] = 0;
            }
        }
    }

    public Percolation(int N) { // create N-by-N grid, with all sites initially blocked
        if (N < 0) {
            throw new IllegalArgumentException("N must > 0");
        }

        this.unions = new WeightedQuickUnionUF(N * N + 2);
        this.unions2 = new WeightedQuickUnionUF(N * N + 1);
        this.number = 0;
        this.grid = new int[N][N];
        this.size = N;
        clear();
    }


    public void open(int row, int col) { // open the site (row, col) if it is not open already
        if (!checkBoarder(row, col)) {
            return;
        }

        if (!isOpen(row, col)) {
            this.grid[row][col] = 1;
            this.number++;
            connects(row, col);
            if (row == 0) {
                unions.union(xyTo1d(row, col), this.size * this.size);
                unions2.union(xyTo1d(row, col), this.size * this.size);
            }
            if (row == this.size - 1) {
                unions.union(xyTo1d(row, col), this.size * this.size + 1);
            }

        }

    }

    public boolean isOpen(int row, int col) { // is the site (row, col) open?
        if (!checkBoarder(row, col)) {
            return false;
        }

        return this.grid[row][col] == 1;
    }

    public boolean isFull(int row, int col) { // is the site (row, col) full?
        if (!checkBoarder(row, col)) {
            return false;
        }

        if (!isOpen(row, col)) {
            return false;
        }

        if (unions2.connected(xyTo1d(row, col), this.size * this.size)) {
            return true;
        }

        return false;
    }

    public int numberOfOpenSites() { // number of open sites
        return this.number;
    }

    public boolean percolates() { // does the system percolate?
        return unions.connected(this.size * this.size, this.size * this.size + 1);
    }

    public static void main(String[] args) { // use for unit testing (not required)

        Percolation pf = new Percolation(5);
        pf.open(0, 3);
        pf.open(1, 3);
        Assert.assertEquals(false, pf.percolates());
        Assert.assertEquals(true, pf.isFull(0, 4));
    }
}


