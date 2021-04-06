import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private double[] fractions;
	private int n;
	private int T;
	
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
    	if (n <= 0 || trials <=0)
    		throw new IllegalArgumentException();
    	
    	this.T = trials;
    	this.n = n; 
    	this.fractions = new double[trials];
    }

    // sample mean of percolation threshold
    public double mean()
    {
    	return StdStats.mean(fractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
    	return StdStats.stddev(fractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo()
    {
    	return mean() - 1.96 *  stddev() / Math.sqrt(T);
    }
    
    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
    	return mean() + 1.96 *  stddev() / Math.sqrt(T);
    }
    
   // test client (see below)
   public static void main(String[] args)
   {
	   int n = Integer.parseInt(args[0]) ;
	   int T = Integer.parseInt(args[1]);
	   
	   PercolationStats prclStats = new PercolationStats(n, T);
	   
	   for (int i = 0; i < prclStats.T; i++) {
		   Percolation prcl = new Percolation(n);
		   while(! prcl.percolates()) {
			   int row = StdRandom.uniform(n);
			   int col = StdRandom.uniform(n);
			   if (! prcl.isOpen(row + 1, col + 1))
				   prcl.open(row + 1, col + 1);
		   }
		   
		   prclStats.fractions[i] = (double) prcl.numberOfOpenSites() / (prclStats.n * prclStats.n);		   
	   }
	   
	   StdOut.printf("mean                      = %f%n", prclStats.mean());
	   StdOut.printf("stddev                    = %f%n", prclStats.stddev());
	   StdOut.printf("95%% confidence interval	  = [%f, %f]%n", prclStats.confidenceLo(), prclStats.confidenceHi());
   }

}
