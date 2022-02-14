package hw4.puzzle;

import java.util.ArrayList;
public class Board implements WorldState {
    private int[][] board;
    /** Returns the string representation of the board.
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }


    public Board(int[][] tiles) {
        board = new int[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                board[i][j] = tiles[i][j];
            }
        }

    }

    public int tileAt(int i, int j) {
        if (checkBoarder(i, j)) {
            return board[i][j];
        } else {
            throw new IndexOutOfBoundsException("Out of bound");
        }
    }

    public int size() {
        return board.length;
    }

    public Iterable<WorldState> neighbors() {
        ArrayList<WorldState> neighbours = new ArrayList<>();
        int[] keys = findHole(0);
        if (checkBoarder(keys[0] - 1, keys[1])) {
            neighbours.add(new Board(swap(keys[0] - 1, keys[1], keys[0], keys[1])));
        }

        if (checkBoarder(keys[0] + 1, keys[1])) {
            neighbours.add(new Board(swap(keys[0] + 1, keys[1], keys[0], keys[1])));
        }

        if (checkBoarder(keys[0], keys[1] + 1)) {
            neighbours.add(new Board(swap(keys[0], keys[1] + 1, keys[0], keys[1])));
        }

        if (checkBoarder(keys[0], keys[1] - 1)) {
            neighbours.add(new Board(swap(keys[0], keys[1] - 1, keys[0], keys[1])));
        }

        return neighbours;
    }

    public int hamming() {
        int sum = 0;
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < this.size(); j++) {
                if (this.tileAt(i, j) != 0 && this.tileAt(i, j) != ((j + 1) + this.size() * i)) {
                    sum++;
                }
            }
        }
        return sum;
    }

    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < this.size(); j++) {
                if (this.tileAt(i, j) != 0 && this.tileAt(i, j) != i * this.size() + j + 1) {
                    int[] a = toXY(tileAt(i, j));
                    sum = sum + Math.abs(i - a[0]) + Math.abs(j - a[1]);
                }
            }
        }
        return sum;
    }

    private int[] toXY(int key) {
        key = key - 1;
        int[] a = new int[2];
        a[0] = key  / this.size();
        a[1] = key % this.size();
        return a;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }

        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }

        Board a = (Board) y;
        if (a.size() != this.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < this.size(); j++) {
                if (a.tileAt(i, j) != this.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }



    private int[][] swap(int x1, int y1, int x2, int y2) {
        int[][] newBoard = new int[this.size()][this.size()];
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < this.size(); j++) {
                newBoard[i][j] = this.tileAt(i, j);
            }
        }

        int temp = newBoard[x1][y1];
        newBoard[x1][y1] = newBoard[x2][y2];
        newBoard[x2][y2] = temp;
        return newBoard;
    }

    private boolean checkBoarder(int x, int y) {
        if (x < 0 || x >= this.size() || y < 0 || y >= this.size()) {
            return false;
        }
        return true;
    }

    private int[] findHole(int key) {
        int[] a = new int[2];
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < this.size(); j++) {
                if (this.tileAt(i, j) == key) {
                    a[0] = i;
                    a[1] = j;
                    return a;
                }
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }


}
