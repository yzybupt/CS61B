package hw2;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Assert;
import java.lang.Exception;

public class PercolationStats {
    private double[] results;
    private int T;

    private double experiment(int N, PercolationFactory pf) throws Exception{
        Percolation grid = pf.make(N);
        while (!grid.percolates()) {
            grid.open(StdRandom.uniform(N),StdRandom.uniform(N));
        }
        return (double) grid.numberOfOpenSites() / (N * N);
    }

    public PercolationStats(int N, int T, PercolationFactory pf) throws Exception{ // perform T independent experiments on an N-by-N grid
        if (N <= 0 || T <= 0) {
            throw new Exception("T or N is out of bounds");
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


    public double stddev(){ // sample standard deviation of percolation threshold
        double sum = 0.0;
        for (double d : results) {
            sum = sum + (d - mean()) * (d - mean()) ;
        }
        return (double) sum / (results.length - 1);
    }

    public double confidenceLow() { // low endpoint of 95% confidence interval
        double low;
        low = mean() - 1.96 * Math.pow(stddev(), 0.5) / Math.pow(this.T, 0.5);
        return low;

    }

    public double confidenceHigh() { // high endpoint of 95% confidence interval
        double high;
        high = mean() + 1.96 * Math.pow(stddev(), 0.5) / Math.pow(this.T, 0.5);
        return high;
    }

    public static void main(String[] args) throws Exception{ // use for unit testing (not required)
        PercolationFactory pf = new PercolationFactory();
        PercolationStats ps = new PercolationStats(20,10,pf);
        System.out.println(ps.mean());
        System.out.println(ps.stddev());


    }
}