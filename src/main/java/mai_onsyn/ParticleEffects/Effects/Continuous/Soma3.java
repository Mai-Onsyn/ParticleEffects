package mai_onsyn.ParticleEffects.Effects.Continuous;

import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class Soma3 implements Effect {

    private final Timeline timeline = new Timeline();


    public Soma3(Timeline sequence, Point startPoint, Vector startWay, double lambda, Particle sample) {
        Timeline lists = new Timeline();
        lists.add(0, startPoint);
        lists.add(1, sequence);
        ConnectList cList = new ConnectList(lists);
        cList.setVector(0, 0, startWay);

        for (int i = 0; i < cList.size(); i++) {
            int[] index = cList.getIndex(i);

            Point p1 = cList.getPoint(index[0], index[1]);
            Point p2 = cList.getPoint(index[2], index[3]);

            Vector way = cList.getVector(index[0], index[1]);

            Vector n = Vector.crossProduct(way, Vector.of(p1, p2));
            Surface midSurface = Surface.midOf(p1, p2);
            Line l = new Line(p1, Vector.crossProduct(way, n));
            Point o = cross(l, midSurface);
            //timeline.add(index[0], new Ray(o, n, index[2] - index[0], lambda, new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.CYAN, 0, 1, -1, new Expression(), "null")));
            //timeline.add(index[0], new Ray(p1, new Vector(way.vx(), way.vy(), way.vz()).setLength(5), index[2] - index[0], lambda, new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.GREEN, 0, 1, -1, new Expression(), "null")));
            //timeline.add(index[0], new Ray(p1, Vector.of(p1, p2), index[2] - index[0], lambda, new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.YELLOW, 0, 1, -1, new Expression(), "null")));
            //timeline.add(index[0], midSurface.show());

            double rotateAngle = (Vector.angle(Vector.of(o, p2), way) < PI / 2) ? Vector.angle(Vector.of(o, p1), Vector.of(o, p2)) : 2 * PI - Vector.angle(Vector.of(o, p1), Vector.of(o, p2));
            Vector tangent = Vector.of(o, p1);
            tangent.rotate(n, PI / 2);
            boolean rotateWay = Vector.dotProduct(way, tangent) > 0;

            Vector v1 = new Vector(way.vx(), way.vy(), way.vz());
            v1.rotate(n, rotateWay ? rotateAngle : -rotateAngle);
            cList.setVector(index[2], index[3], v1);
            cList.setCenter(index[2], index[3], o);

            drawRad(o, n, p1, rotateAngle, rotateWay, index[0], index[2] - index[0], lambda, sample);

        }
    }

    //求直线参数方程与平面一般方程的交点
    private Point cross(Line line, Surface surface) {
        double t = - (surface.A * line.dx + surface.B * line.dy + surface.C * line.dz + surface.D) /
                (surface.A * line.kx + surface.B * line.ky + surface.C * line.kz);//交点参数t
        if (abs(t) < 1e-8) return null;//平行或包含情况
        else return new Point(
                line.kx * t + line.dx,
                line.ky * t + line.dy,
                line.kz * t + line.dz
        );
    }

    private void drawRad(Point o, Vector n, Point p, double angle, boolean way, int tick, int duration, double lambda, Particle sample) {
        Vector k = Vector.of(o, p);
        double range = Point.distance(o, p);
        double arcLength = angle * range;
        double count = ceil(arcLength * lambda);
        double step = angle / count;
        this.timeline.add(0, new Particle(
                sample.getName(), Point.of(o, k), sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()
        ));

        for (int i = 1; i < count; i++) {
            k.rotate(n, way ? step : - step);
            this.timeline.add((int) (i / count * duration + tick), new Particle(
                    sample.getName(), Point.of(o, k), sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()
            ));
        }
    }



    @Override
    public Timeline gettimeline() {
        return this.timeline;
    }
}

class Line {
    double kx, ky, kz, dx, dy, dz;

    public Line(Point a, Vector k) {
        kx = k.vx();
        ky = k.vy();
        kz = k.vz();
        dx = a.x();
        dy = a.y();
        dz = a.z();
    }


    public static Line of(Point p1, Point p2) {
        return new Line(p1, Vector.of(p1, p2));
    }
}

class Surface {
    double A, B, C, D;

    public Surface(Point o, Vector v) {
        A = v.vx();
        B = v.vy();
        C = v.vz();

        D = - (A * o.x() + B * o.y() + C * o.z());
    }

    public static Surface midOf(Point p1, Point p2) {
        Vector v = Vector.of(p1, p2);
        Point mid = Point.of(p1, Vector.multiply(v, 0.5));
        return new Surface(mid, v);
    }
    public Timeline show() {
        Random r = new Random();
        Timeline timeline = new Timeline();
        for (int i = 0; i < 4096; i++) {
            double x,y,z;
            x = r.nextDouble(-16, +16);
            y = r.nextDouble(100-16, 100+16);
            z = - (A * x + B * y + D) / C;
            if (z > 16 || z < -16) {
                i--;
                continue;
            }
            timeline.add(0, new Point(x, y, z));
        }
        return timeline;
    }
}

class ConnectList {

    private final List<List<Point>> sequence;
    private final List<List<Vector>> vectors;
    private final List<List<Point>> centers;
    private final List<int[]> index;
    public ConnectList(Timeline timeline) {
        this.sequence = timeline.getAsPointSequence();
        this.vectors = new ArrayList<>(sequence.size());
        this.centers = new ArrayList<>(sequence.size());
        this.index = new ArrayList<>();
        genConnectList();
    }

    public Point getPoint(int tick, int i) {
        return sequence.get(tick).get(i);
    }

    public int[] getIndex(int i) {
        return index.get(i);
    }
    public Point getCenter(int tick, int i) {
        return centers.get(tick).get(i);
    }

    public Vector getVector(int tick, int i) {
        return vectors.get(tick).get(i);
    }

    public void setCenter(int tick, int i, Point o) {
        while (centers.size() <= tick) {
            centers.add(new ArrayList<>());
        }
        while (centers.get(tick).size() <= i) {
            centers.get(tick).add(null);
        }
        centers.get(tick).set(i, o);
    }

    public void setVector(int tick, int i, Vector v) {
        while (vectors.size() <= tick) {
            vectors.add(new ArrayList<>());
        }
        while (vectors.get(tick).size() <= i) {
            vectors.get(tick).add(null);
        }
        vectors.get(tick).set(i, v);
    }

    public int size() {
        return index.size();
    }

    private void genConnectList() {
        List<Integer> notNull = new ArrayList<>();
        for (int i = 0; i < this.sequence.size(); i++) {
            if (this.sequence.get(i).size() != 0) {
                notNull.add(i);
            }
        }

        for (int i = 0; i < notNull.size() - 1; i++) {
            //带连接两点存储为int[4] 第0和1位为第一点的时间索引和时刻索引 第2和3位则是第二点的
            this.index.add(new int[] {notNull.get(i), 0, notNull.get(i + 1), 0});
        }
    }

}

