package mai_onsyn.ParticleEffects.Utils.Math;

import static java.lang.Math.*;

public class MathUtil {

    public static double[] get_Delta_XYZ_In_3D_Circle(Vector k, double r) {
        final int exhaustiveCount = 1048576;
        final Vector n = k.getNormalVector().polarMode().setLength(r).toVector();

        final double step = 2 * PI / exhaustiveCount;
        double x = 0;
        double y = 0;
        double z = 0;

        for (int e = 0; e < exhaustiveCount; e++) {
            n.rotate(k, step);
            if (n.vx() > x) x = n.vx();
            if (n.vy() > y) y = n.vy();
            if (n.vz() > z) z = n.vz();
        }

        return new double[] {x * 2, y * 2, z * 2};
    }

    public static Point[] exhaustivePosition(Point p, Vector k, boolean way, int exhaustiveCount) {


        Vector m = new Vector(k.vx(), k.vy(), k.vz());
        m.setLength(Vector.dotProduct(k, Vector.of(p)) / k.length());
        Vector n = new Vector(p.x() - m.vx(), p.y() - m.vy(), p.z() - m.vz());
        if (n.length() < 1e-5) n = Vector.of(p);

        final double step = 2 * PI / exhaustiveCount;
        Point[] e = new Point[exhaustiveCount];

        for (int i = 0; i < exhaustiveCount; i++) {
            if (way) n.rotate(k, step);
            else n.rotate(k, -step);
            e[i] = Point.of(n);
        }
        return e;
    }

    public static double[] getMaxWave(Complex[] frequencyDomain) {
        int N = frequencyDomain.length;
        double[] amplitude = Complex.toModArray(frequencyDomain);
        return new double[] {
                2 * max(amplitude) / N,
                2 * PI * (double) maxIndex(amplitude) / N,
                - arg(max(frequencyDomain))
        };
    }





    private static double max(double[] array) {
        double max = 0;
        for (int i = array.length-1; i >= 0; i--) {
            if (array[i] > max) max = array[i];
        }
        return max;
    }

    private static Complex max(Complex[] array) {
        return array[maxIndex(Complex.toModArray(array))];
    }

    private static double arg(Complex complex) {
        return Math.atan2(complex.imag(), complex.real());
    }

    private static int maxIndex(double[] array) {
        int index = 0;
        double max = 0;
        for (int i = 0; i < array.length/2; i++) {
            if (array[i] > max) {
                max = array[i];
                index = i;
            }
        }
        return index;
    }
}


/*

            for (int axis = 0; axis < 3; axis++) {
                Vector spinner = n;
                spinner.rotate(k, maxAngleInterval[axis][0]);
                double step = (maxAngleInterval[axis][1] - maxAngleInterval[axis][0]) / exhaustiveStep;
                double maxD = maxAngleInterval[axis][0];
                for (int i = 0; i < exhaustiveStep; i++) {
                    spinner.rotate(k, step);
                    double[] heft = new double[] {spinner.vx(), spinner.vy(), spinner.vz()};
                    if (heft[axis] > max[axis]) {
                        max[axis] = heft[axis];
                        maxD = i * step + maxAngleInterval[axis][0];
                    }
                }
                maxAngleInterval[axis][0] = maxD - step;
                maxAngleInterval[axis][1] = maxD + step;
            }

            for (int axis = 0; axis < 3; axis++) {
                Vector spinner = n;
                spinner.rotate(k, minAngleInterval[axis][0]);
                double step = (minAngleInterval[axis][1] - minAngleInterval[axis][0]) / exhaustiveStep;
                double minD = minAngleInterval[axis][0];
                for (int i = 0; i < exhaustiveStep; i++) {
                    spinner.rotate(k, step);
                    double[] heft = new double[] {spinner.vx(), spinner.vy(), spinner.vz()};
                    if (heft[axis] < min[axis]) {
                        min[axis] = heft[axis];
                    }
                }

                minAngleInterval[axis][0] = minD - step;
                minAngleInterval[axis][1] = minD + step;
            }

 */
