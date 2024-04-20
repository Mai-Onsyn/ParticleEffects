package mai_onsyn.ParticleEffects.Utils.Math;

import static java.lang.Math.*;

public class MathUtil {

    public static double atan3(double sin, double cos) {
        if (sin >= 0) {
            return acos(cos) + PI;//0~π
        }
        else {
            return PI - acos(cos);//π~2π
        }
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


    public static int[][] genConnectList(Point[] pl1, Point[] pl2) {
        int size1 = pl1.length;//两组点的数量
        int size2 = pl2.length;

        int[][] connectList = genRandomSolution(size1, size2);//连接列表 先生成一个随机解

        //重复遍历
        for (int n = 0; n < connectList.length; n++) {
            //内层遍历两端点
            for (int i = 0; i < connectList.length; i++) {
                for (int j = 0; j < connectList.length; j++) {

                    //相同索引/第一点多少于第二点时的前端点相同情况/两个都是单独不连的点
                    if (i == j || connectList[i][0] == connectList[j][0] || (connectList[i][1] == -1 && connectList[j][1] == -1)) continue;

                    //复杂的多点拆成4点连接
                    Point p1a = pl1[connectList[i][0]];
                    Point p1b = pl1[connectList[j][0]];
                    Point p2a;
                    Point p2b;

                    //有一点不连情况 把其中一条直线前后端点都设为同一点 那么其长度就是0
                    if (connectList[i][1] == -1) p2a = p1a;
                    else p2a = pl2[connectList[i][1]];

                    if (connectList[j][1] == -1) p2b = p1b;
                    else p2b = pl2[connectList[j][1]];

                    double oldValue = Point.distance(p1a, p2a) + Point.distance(p1b, p2b);//交换前的距离总和
                    double newValue = Point.distance(p1b, p2a) + Point.distance(p1a, p2b);//交换后的距离总和

                    //若交换后的距离总和更小 则交换索引的前一点
                    if (newValue < oldValue) {
                        int temp = connectList[i][0];
                        connectList[i][0] = connectList[j][0];
                        connectList[j][0] = temp;
                    }
                }
            }
        }

        return connectList;
    }

    private static int[][] genRandomSolution(int size1, int size2) {
        int[][] connectList = new int[max(size1, size2)][2];

        for (int i = 0; i < max(size1, size2); i++) {
            if (size2 > size1) {
                //第一组少于第二组的情况 若超出第一组索引 重头赋值索引
                int ia = i;
                while (ia >= size1) ia -= size1;
                connectList[i][0] = ia;
                connectList[i][1] = i;
            } else if (size2 <= i) {
                //第一组多于第二组的情况 多于的不连（索引设为-1）
                connectList[i][0] = i;
                connectList[i][1] = -1;
            } else {
                //一般情况 包括第一组多于第二组时索引在第一组范围内时
                connectList[i][0] = i;
                connectList[i][1] = i;
            }
        }

        return connectList;
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