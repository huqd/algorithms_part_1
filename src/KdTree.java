import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;


import java.util.LinkedList;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int size;

        public Node(Point2D p, RectHV rect, int size) {
            this.p = p;
            this.rect = rect;
            this.size = size;
        }
    }

    private final int KEY_X = 1, KEY_Y = -1;
    private Node root;

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException("calls put() with a null key");

        if (contains(p)) return; // If duplicated
        root = put(root, KEY_X, p, new RectHV(0.0, 0.0, 1.0, 1.0));
    }

    private Node put(Node x, int key, Point2D p, RectHV pRect) {
        if (x == null) return new Node(p, pRect, 1);

        // Select subtree by comparing x or y keys
        int cmp;
        RectHV xRect = x.rect;
        Point2D xP = x.p;

        double xmin, ymin, xmax, ymax;
        xmin = xRect.xmin();
        ymin = xRect.ymin();
        xmax = xRect.xmax();
        ymax = xRect.ymax();

        if (key == KEY_X) {
            cmp = Double.compare(p.x(), x.p.x());
            if (cmp < 0) {
                pRect = new RectHV(xmin, ymin, xP.x(), ymax);
                x.lb = put(x.lb, KEY_Y, p, pRect);
            } else {
                pRect = new RectHV(xP.x(), ymin, xmax, ymax);
                x.rt = put(x.rt, KEY_Y, p, pRect);
            }
        } else {
            cmp = Double.compare(p.y(), x.p.y());
            if (cmp < 0) {
                pRect = new RectHV(xmin, ymin, xmax, xP.y());
                x.lb = put(x.lb, KEY_X, p, pRect);
            } else {
                pRect = new RectHV(xmin, xP.y(), xmax, ymax);
                x.rt = put(x.rt, KEY_X, p, pRect);
            }
        }

        x.size = 1 + size(x.lb) + size(x.rt);
        return x;

    }

    private RectHV get(Node x, int key, Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (x == null) return null;
        if (p.equals(x.p)) return x.rect;

        // Search subtrees
        int cmp;
        if (key == KEY_X) {
            cmp = Double.compare(p.x(), x.p.x());
            if (cmp < 0) return get(x.lb, KEY_Y, p);
            else return get(x.rt, KEY_Y, p);
        } else {
            cmp = Double.compare(p.y(), x.p.y());
            if (cmp < 0) return get(x.lb, KEY_X, p);
            else return get(x.rt, KEY_X, p);
        }

    }

    private RectHV get(Point2D p) {
        return get(root, KEY_X, p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException();
        return get(p) != null;
    }

    public void draw()                         // draw all points to standard draw
    {
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();
        return range(root, rect);
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        return nearest(root, p, Double.POSITIVE_INFINITY);
    }

    private Point2D nearest(Node x, Point2D p, double currentDisMin) {
        if (x == null) return null;

        double disToRect = x.rect.distanceTo(p);
        if (disToRect > currentDisMin) return null;

        Point2D nrL, nrR;
        double nrLD = Double.POSITIVE_INFINITY;
        double nrRD = Double.POSITIVE_INFINITY;

        Point2D nr = x.p;
        double nrD = nr.distanceTo(p);
        if (currentDisMin > nrD) currentDisMin = nrD;

        // Nearest point of left subtree
        nrL = nearest(x.lb, p, currentDisMin);
        if(nrL != null) nrLD = nrL.distanceTo(p);

        // Nearest point of right subtree
        if (currentDisMin > nrLD) currentDisMin = nrLD;
        nrR = nearest(x.rt, p, currentDisMin);
        if (nrR != null) nrRD = nrR.distanceTo(p);

        // Decide nearest point of the tree
        if (nrLD < nrD) {
            nrD = nrLD;
            nr = nrL;
        }

        if (nrRD < nrD) {
            nrD = nrRD;
            nr = nrR;
        }

        return nr;
    }

    private LinkedList<Point2D> range(Node x, RectHV rect) {
        LinkedList<Point2D> points = new LinkedList<>();
        if (x == null) return points;
        if (!x.rect.intersects(rect)) return points;

        LinkedList<Point2D> leftPoints = range(x.lb, rect);
        LinkedList<Point2D> rightPoints = range(x.rt, rect);
        if (rect.contains(x.p)) points.add(x.p);

        points.addAll(leftPoints);
        points.addAll(rightPoints);
        return points;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        KdTree points = new KdTree();
//        points.insert(new Point2D(0.7, 0.2));
//        points.insert(new Point2D(0.5, 0.4));
//        points.insert(new Point2D(0.2, 0.3));
//        points.insert(new Point2D(0.4, 0.7));
//        points.insert(new Point2D(0.9, 0.6));

        points.insert(new Point2D(0.372, 0.497));
        points.insert(new Point2D(0.564, 0.413));
        points.insert(new Point2D(0.226, 0.577));
        points.insert(new Point2D(0.144, 0.179));
        points.insert(new Point2D(0.083, 0.51));
        points.insert(new Point2D(0.32, 0.708));
        points.insert(new Point2D(0.417, 0.362));
        points.insert(new Point2D(0.862, 0.825));
        points.insert(new Point2D(0.785, 0.725));
        points.insert(new Point2D(0.499, 0.208));

//        StdOut.println("Insert/Search");
//        StdOut.println(points.get(points.root, points.KEY_X, new Point2D(0.7, 0.2)).toString());
//
//        StdOut.println("Range");
//        RectHV rect = new RectHV(0, 0.35, 0.8, 1);
//        for (Point2D p : points.range(rect)) StdOut.println(p);

        StdOut.println("Nearest");
        StdOut.println(points.nearest(new Point2D(0.021, 0.819)));
        StdOut.println(points.nearest(new Point2D(0.4, 0.819)));

        StdOut.println(points.nearest(new Point2D(0.021, 0.819)));
        StdOut.println(points.nearest(new Point2D(0.3, 0.819)));

        StdOut.println(points.nearest(new Point2D(0.021, 0.819)));

    }

}
