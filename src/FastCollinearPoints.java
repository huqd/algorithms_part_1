import java.util.Arrays;

import edu.princeton.cs.algs4.StdOut;

import java.util.Random;

public class FastCollinearPoints {
    private final Point[] points;
    private int numSeg;
    private LineSegment[] segments = new LineSegment[0];

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null) throw new IllegalArgumentException();

        for (int i = 0; i < points.length; i++)
            if (points[i] == null) throw new IllegalArgumentException();

        // Should not mutate arguments
        this.points = points.clone();
        Arrays.sort(this.points);
        for (int i = 0; i < this.points.length - 1; i++)
            if (this.points[i].compareTo(this.points[i + 1]) == 0) throw new IllegalArgumentException();

        this.recognizeSegments();
    }

    private void recognizeSegments() {
        // the line segments
        int length = this.points.length;
        if (length <=3) return;

        LineSegment[] lineSegments = new LineSegment[length * length];
        this.numSeg = 0;

        Point[] sortedPoints = points.clone();

        for (Point p : this.points) {
            Arrays.sort(sortedPoints, p.slopeOrder());
            // Accumulate points
            int start = 1, stop = 1;
            double startSlope = p.slopeTo(sortedPoints[start]);

            for (int j = 1; j < length; j++) {
                if (p.slopeTo(sortedPoints[j]) == startSlope) {
                    if (j >= start + 2 && (j == length - 1 || p.slopeTo(sortedPoints[j]) != p.slopeTo(sortedPoints[j + 1]))) {
                        stop = j;
                        Arrays.sort(sortedPoints, start, stop + 1);
                        if (p.compareTo(sortedPoints[start]) < 0 && sortedPoints[start].compareTo(sortedPoints[stop]) < 0) {
                            lineSegments[numSeg++] = new LineSegment(p, sortedPoints[stop]);
                        }
                    }
                } else {
                    start = stop = j;
                    startSlope = p.slopeTo(sortedPoints[start]);
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
        // the line segments
        return this.segments;
    }

    public static void main(String[] args) {
        Point[] points = new Point[90];
        Random rand = new Random();
        Point p;
        int x, y;
        for (int i = 0; i < 90; i++) {
            x = i / 10;
            y = i - x * 10;
            p = new Point(x, y);
            points[i] = p;
        }


        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        LineSegment[] segments = fastCollinearPoints.segments();
        StdOut.println(segments.length);
    }
}