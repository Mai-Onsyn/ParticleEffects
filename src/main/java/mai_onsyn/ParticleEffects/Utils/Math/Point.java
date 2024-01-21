package mai_onsyn.ParticleEffects.Utils.Math;

import static java.lang.Math.*;

public class Point {
    private final double x;
    private final double y;
    private final double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public static Point of(Vector v) {
        return new Point(v.vx(), v.vy(), v.vz());
    }

    public static Point of(Point p, Vector v) {
        return new Point(v.vx() + p.x(), v.vy() + p.y(), v.vz() + p.z());
    }

    public static double distance(Point A, Point B) {
        return sqrt(pow(A.x() - B.x(), 2) + pow(A.y() - B.y(), 2) + pow(A.z() - B.z(), 2));
    }

    public static double distance(Point p, Point vs, Vector v) {
        Vector AP = Vector.of(vs, p);
        return sqrt(Vector.dotProduct(AP, AP) - (Vector.dotProduct(AP, v) / v.length()));
    }

    @Override
    public String toString() {
        return String.format("(%.8f, %.8f, %.8f)", this.x, this.y, this.z);
    }
}