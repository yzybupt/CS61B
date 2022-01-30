package hw2;

import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {
    private double[] results;
    private int T;

    private double experiment(int N, PercolationFactory pf) {
        Percolation grid = pf.make(N);
        while (!grid.percolates()) {
            grid.open(StdRandom.uniform(N), StdRandom.uniform(N));
        }
        return (double) grid.numberOfOpenSites() / (N * N);
    }

    public PercolationStats(int N, int T, PercolationFactory pf)  {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N > 0 && T > 0");
        }

        this.T = T;
        this.results = new double[T];
        for (int i = 0; i < T; i++) {
            results[i] = experiment(N, pf);
        }
    }


    public double mean() {  // sample mean of percolation threshold
        double sum = 0.0;
        for (double d : results) {
            sum = sum + d;
        }
        return (double) sum / results.length;
    }


    public double stddev() { // sample standard deviation of percolation threshold
        double sum = 0.0;
        for (double d : results) {
            sum = sum + (d - mean()) * (d - mean());
        }
        return Math.pow((double) sum / (results.length - 1), 0.5);
    }

    public double confidenceLow() { // low endpoint of 95% confidence interval
        double low;
        low = mean() - 1.96 * stddev() / Math.pow(this.T, 0.5);
        return low;

    }

    public double confidenceHigh() { // high endpoint of 95% confidence interval
        double high;
        high = mean() + 1.96 * stddev() / Math.pow(this.T, 0.5);
        return high;
    }
}
