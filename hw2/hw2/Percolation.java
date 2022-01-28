package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private int[][] grid;
    private int number;
    private WeightedQuickUnionUF unions;


    private boolean checkBoarder (int x, int y) {
        if (x < 0 || x > this.size - 1) {
            return false;
        } else if (y < 0 || y > this.size -1) {
            return false;
        }
        return true;
    }

    private void connects (int x, int y) {
        for (int i = 0; i < 4; i++) {
            int row = x + (int) Math.pow(-1, i);
            int col = y + (int) Math.pow(-1, i + 1);
            if(checkBoarder(row, col) && this.grid[row][col] == 1) {
                unions.union(xyTo1d(x,y),xyTo1d(row,col));
            }
        }
    }

    private int xyTo1d(int x , int y) {
        return x * this.size + y;
    }

    private void clear(int[][] grid) {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                grid[i][j] = 0;
            }
        }
    }

    public Percolation(int N) { // create N-by-N grid, with all sites initially blocked
        this.unions = new WeightedQuickUnionUF(N * N);
        this.number = 0;
        this.grid = new int[N][N];
        this.size = N;
        clear(this.grid);
    }


    public void open(int row, int col) { // open the site (row, col) if it is not open already
        if(!checkBoarder(row, col)) {
            return;
        }

        if (!isOpen(row,col)) {
            this.grid[row][col] = 1;
            this.number++;
            connects(row,col);
        }
    }

    public boolean isOpen(int row, int col) { // is the site (row, col) open?
        if(!checkBoarder(row, col)) {
            return false;
        }
        return this.grid[row][col] == 1;
    }

    public boolean isFull(int row, int col) { // is the site (row, col) full?
        if(!checkBoarder(row, col)) {
            return false;
        }
        for (int i = 0; i < this.size; i++) {
            if (unions.connected(i, xyTo1d(row, col))) {
                return true;
            }
        }
        return false;
    }

    public int numberOfOpenSites() { // number of open sites
        return this.number;
    }

    public boolean percolates() { // does the system percolate?
        for(int i = 0; i < this.size; i++) {
            for (int j = 0; i < this.size; j++) {
                if (unions.connected(i,xyTo1d(this.size - 1, j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) { // use for unit testing (not required)

    }
}


