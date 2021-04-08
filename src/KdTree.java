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

    private final int PARTITION_X = 1, PARTITION_Y = -1;
    private Node root;
    private RectHV rootRect = new RectHV(0, 0, 1, 1);

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
        root = put(root, PARTITION_X, p, rootRect);
    }

    private Node put(Node x, int partition, Point2D p, RectHV rect) {
        if (x == null) return new Node(p, rect, 1);

        // Select subtree by comparing x or y keys
        int cmp;
        double xmin = x.rect.xmin(), ymin = x.rect.ymin(), xmax = x.rect.xmax(), ymax = x.rect.ymax();
        if (partition == PARTITION_X) {
            cmp = Double.compare(p.x(), x.p.x());
            if (cmp < 0) x.lb = put(x.lb, PARTITION_Y, p, new RectHV(xmin, ymin, x.p.x(), ymax));
            else x.rt = put(x.rt, PARTITION_Y, p, new RectHV(x.p.x(), ymin, xmax, ymax));
        } else {
            cmp = Double.compare(p.y(), x.p.y());
            if (cmp < 0) x.lb = put(x.lb, PARTITION_X, p, new RectHV(xmin, ymin, xmax, x.p.y()));
            else x.rt = put(x.rt, PARTITION_X, p, new RectHV(xmin, x.p.y(), xmax, ymax));
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
        if (key == PARTITION_X) {
            cmp = Double.compare(p.x(), x.p.x());
            if (cmp < 0) return get(x.lb, PARTITION_Y, p);
            else return get(x.rt, PARTITION_Y, p);
        } else {
            cmp = Double.compare(p.y(), x.p.y());
            if (cmp < 0) return get(x.lb, PARTITION_X, p);
            else return get(x.rt, PARTITION_X, p);
        }

    }

    private RectHV get(Point2D p) {
        return get(root, PARTITION_X, p);
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

    private Point2D nearest(Node x, Point2D p, double currentDis) {
        if (x == null) return null;

        double disToRect = x.rect.distanceTo(p);
        if (disToRect > currentDis) return null;

        Point2D left, right;
        double leftDis = Double.POSITIVE_INFINITY, rightDis = Double.POSITIVE_INFINITY;

        // Nearest point to be x
        Point2D nearest = x.p;
        double nearestDis = nearest.distanceTo(p);

        /**
         * TODO: search the same side with p first
         */
        // Nearest point of left subtree
        currentDis = Double.min(currentDis, nearestDis);
        left = nearest(x.lb, p, currentDis);
        if(left != null) leftDis = left.distanceTo(p);

        // Nearest point of right subtree
        currentDis = Double.min(currentDis, leftDis);
        right = nearest(x.rt, p, currentDis);
        if (right != null) rightDis = right.distanceTo(p);

        // Decide nearest point of the tree among x, left, right
        if (leftDis < nearestDis) {
            nearestDis = leftDis;
            nearest = left;
        }

        if (rightDis < nearestDis) {
            nearestDis = rightDis;
            nearest = right;
        }

        return nearest;
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