import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private Point[] points;
    private int numSeg;
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        if (points == null) throw new IllegalArgumentException();

        for (int i = 0; i < points.length; i++)
            if (points[i] == null) throw new IllegalArgumentException();

        // Should not mutate arguments
        this.points = points.clone();
        Arrays.sort(this.points);
        for (int i = 0; i < this.points.length - 1; i++)
            if (this.points[i].compareTo(this.points[i + 1]) == 0) throw new IllegalArgumentException();

        // Recognize segments
        this.recognizeSegments();
    }

    private void recognizeSegments() {
        // the line segments
        int length = this.points.length;
        Point p, q, r, s;
        LineSegment[] lineSegments = new LineSegment[length * length];

        this.numSeg = 0;

        for (int i = 0; i < length; i++) {
            p = this.points[i];
            for (int j = 0; j < length; j++) {
                if (j == i) continue;
                q = this.points[j];
                for (int k = 0; k < length; k++) {
                    if (k == j || k == i) continue;
                    r = this.points[k];
                    if (p.slopeTo(q) != p.slopeTo(r)) continue;
                    for (int g = 0; g < length; g++) {
                        if (g == k || g == j || g == i) continue;
                        s = this.points[g];
                        if (p.slopeTo(q) == p.slopeTo(s)) {
                            if (i < j && j < k && k < g) { // remember one direction only
                                lineSegments[numSeg++] = new LineSegment(p, s);
                            }
                        }
                    }
                }
            }
        }

        // Filter out nulls
        this.segments = new LineSegment[numSeg];
        for (int i = 0; i < numSeg; i++) {
            this.segments[i] = lineSegments[i];
        }
    }

    public int numberOfSegments() {
        // the number of line segments
        return this.numSeg;
    }

    public LineSegment[] segments() {
        return this.segments;
    }

    public static void main(String[] args) {
        Point p0 = new Point(2, 1);
        Point p1 = new Point(3, 2);
        Point p2 = new Point(3, 3);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 3);
        Point p5 = new Point(5, 4);
        Point p6 = new Point(5, 2);
        Point p7 = new Point(6, 2);

        Point[] points = new Point[8];
        points[0] = p0;
        points[1] = p1;
        points[2] = p2;
        points[3] = p3;
        points[4] = p4;
        points[5] = p5;
        points[6] = p6;
        points[7] = p7;

        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        LineSegment[] ls = bruteCollinearPoints.segments();
        StdOut.println(bruteCollinearPoints.numberOfSegments());
        for(LineSegment i : ls) {
            StdOut.print(i);
        }
    }
}