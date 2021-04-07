import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.LinkedList;

public class PointSET {
    private SET<Point2D> points = new SET<>();

    public PointSET() {
    }                               // construct an empty set of points

    public boolean isEmpty()                      // is the set empty?
    {
        return points.size() == 0;
    }

    public int size()                         // number of points in the set
    {
        return points.size();
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        if (!points.contains(p)) points.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D p : points) p.draw();
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();

        LinkedList<Point2D> range = new LinkedList<>();
        for (Point2D p : points)
            if (rect.contains(p)) range.add(p);
        return range;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        Point2D nearest = points.max();
        double distance = nearest.distanceTo(p);
        for (Point2D sp : points) {
            if (sp.distanceTo(p) < distance) {
                nearest = sp;
                distance = sp.distanceTo(p);
            }
        }

        return nearest;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        PointSET points = new PointSET();
        points.insert(new Point2D(0.5, 0.6));
        points.insert(new Point2D(0.2, 0.8));
        points.insert(new Point2D(3, 2));
        points.insert(new Point2D(4, 2));
        points.insert(new Point2D(4, 4));

        RectHV rect = new RectHV(2.5, 1.5, 3.5, 2.5);

        for (Point2D p : points.range(rect)) StdOut.println(p);

        StdOut.println("Nearest");
        StdOut.println(points.nearest(new Point2D(2.5, 2)));

        points.draw();
    }
}