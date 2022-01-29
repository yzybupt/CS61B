package hw2;

public class PercolationFactory {
   public Percolation make(int N) throws Exception {
        return new Percolation(N);
    }
}
