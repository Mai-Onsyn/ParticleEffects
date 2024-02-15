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
        Vector m = new Vector(k.vx(), k.vy(), k.vz());//复制一份免得污染
        m.setLength(Vector.dotProduct(k, Vector.of(p)) / k.length());
        Vector n = new Vector(p.x() - m.vx(), p.y() - m.vy(), p.z() - m.vz());//上面提到的MP向量的计算

        if (n.length() < 1e-5) n = Vector.of(p);//注意 若QP向量本来就和k向量垂直 那么MP的模长会变成0

        final double step = 2 * PI / exhaustiveCount;//旋转步进角度 只需要旋转一圈

        Point[] e = new Point[exhaustiveCount];

        for (int i = 0; i < exhaustiveCount; i++) {
            if (way) n.rotate(k, step);//k向量垂直屏幕向外观察的顺时针方向
            else n.rotate(k, -step);//逆时针
            e[i] = Point.of(n);
        }
        return e;
    }

    public static double[] parseWave(double[] data) {
        int N = data.length;
        double A = 0;
        int maxIndex = -1;
        for (int i = 0; i < N; i++) {
            if (data[i] > A) {
                A = data[i];
                maxIndex = i;
            }
        }
        double phi = 2 * PI - ((maxIndex - N * 0.25 < 0) ? maxIndex + N * 0.75 : maxIndex - N * 0.25) / N * 2 * PI;
        return new double[] {A, phi};
    }

}

/*
//获取频域中振幅最大的波的A, w, φ
    public static double[] getMaxWave(Complex[] frequencyDomain) {
        int N = frequencyDomain.length;
        double[] amplitude = Complex.toModArray(frequencyDomain);
        return new double[] {
                2 * max(amplitude) / N,//A
                2 * PI * (double) maxIndex(amplitude) / N,//w
                - arg(max(frequencyDomain))//φ
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
 */