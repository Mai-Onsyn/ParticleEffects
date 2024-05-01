package mai_onsyn.ParticleEffects.Effects.Continuous;

import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.MathUtil;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class Soma3 implements Effect {

    private final Timeline timeline = new Timeline();


    public Soma3(Timeline sequence, Point startPoint, Vector startWay, double lambda, Particle sample) {
        //将设置的起始点插入到点列表中
        Timeline lists = new Timeline();
        lists.add(0, startPoint);
        lists.add(5, sequence);

        //创建数据访问工具并存储第一个向量
        ConnectList cList = new ConnectList(lists);
        cList.setVector(0, 0, startWay);

        for (int i = 0; i < cList.size(); i++) {
            int[] index = cList.getIndex(i);//获取一个连接列表

            Point p1 = cList.getPoint(index[0], index[1]);//列出待连接的两点
            Point p2 = cList.getPoint(index[2], index[3]);

            Vector way = cList.getVector(index[0], index[1]);//获取速度向量

            Vector n = Vector.crossProduct(way, Vector.of(p1, p2));//圆面法向量
            Surface midSurface = Surface.midOf(p1, p2);//待连接两点的中垂面
            Line l = new Line(p1, Vector.crossProduct(way, n));//垂直于速度向量与法向量且过圆心与p1的直线
            Point o = cross(l, midSurface);//直线与平面相交获得圆心
            //timeline.add(index[0], new Ray(o, n, index[2] - index[0], lambda, new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.CYAN, 0, 1, -1, new Expression(), "null")));
            //timeline.add(index[0], new Ray(p1, new Vector(way.vx(), way.vy(), way.vz()).setLength(5), index[2] - index[0], lambda, new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.GREEN, 0, 1, -1, new Expression(), "null")));
            //timeline.add(index[0], new Ray(p1, Vector.of(p1, p2), index[2] - index[0], lambda, new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.YELLOW, 0, 1, -1, new Expression(), "null")));
            //timeline.add(index[0], midSurface.show());
            if (o == null || Point.distance(o, p1) > 32) {
                //圆心不存在或圆弧半径过大的情况 直接用直线连接
                this.timeline.add(index[0], new Straight(p1, p2, index[2]-index[0], lambda, sample));
                //后一圆弧的速度向量设成直线的方向向量 可保证后一圆弧与直线相切 但不能保证前一圆弧与直线相切
                cList.setVector(index[2], index[3], Vector.of(p1, p2));
            }
            else {
                //圆心存在
                double rotateAngle = (Vector.angle(Vector.of(o, p2), way) < PI / 2) ? Vector.angle(Vector.of(o, p1), Vector.of(o, p2)) : 2 * PI - Vector.angle(Vector.of(o, p1), Vector.of(o, p2));//旋转角度的计算

                //切线
                Vector tangent = Vector.of(o, p1);
                tangent.rotate(n, PI / 2);

                //旋转方向 因为夹角只能等于0或π 直接比较数量积的正负即可
                boolean rotateWay = Vector.dotProduct(way, tangent) > 0;

                //下一段圆弧的起始向量
                Vector v1 = new Vector(way.vx(), way.vy(), way.vz());//复制一份免得污染
                v1.rotate(n, rotateWay ? rotateAngle : -rotateAngle);
                cList.setVector(index[2], index[3], v1);

                drawArc(o, n, p1, rotateAngle, rotateWay, index[0], index[2] - index[0], lambda, sample);
            }
        }
    }

    //求直线参数方程与平面一般方程的交点
    private Point cross(Line line, Surface surface) {
        double sub = surface.A * line.kx + surface.B * line.ky + surface.C * line.kz;
        double t = - (surface.A * line.dx + surface.B * line.dy + surface.C * line.dz + surface.D) /
                sub;//交点参数t
        if (abs(sub) < 1e-8) return null;//平行或包含情况
        else return new Point(
                line.kx * t + line.dx,
                line.ky * t + line.dy,
                line.kz * t + line.dz
        );
    }

    //参数：圆心 法向量 起始点 弧度大小 方向 起始游戏刻 持续时间 绘制密度 样本粒子
    private void drawArc(Point o, Vector n, Point p, double angle, boolean way, int tick, int duration, double lambda, Particle sample) {
        Vector k = Vector.of(o, p);//旋转向量
        //lambda的值对应到圆弧长度 根据弧长 绘制点的间隔为:lambda点/方块长度
        double range = Point.distance(o, p);
        double arcLength = angle * range;
        double count = ceil(arcLength * lambda);

        double step = angle / count;//旋转步进角
        this.timeline.add(0, new Particle(
                sample.getName(), p, sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()
        ));//起始点

        for (int i = 1; i < count; i++) {
            k.rotate(n, way ? step : - step);//每转一步记录位置 即可形成圆弧
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

    //由点和方向向量构造直线
    public Line(Point a, Vector k) {
        kx = k.vx();
        ky = k.vy();
        kz = k.vz();
        dx = a.x();
        dy = a.y();
        dz = a.z();
    }

    //由两点构造直线
    public static Line of(Point p1, Point p2) {
        return new Line(p1, Vector.of(p1, p2));
    }
}

class Surface {
    double A, B, C, D;

    //由点法构造平面
    public Surface(Point o, Vector v) {
        A = v.vx();
        B = v.vy();
        C = v.vz();

        D = - (A * o.x() + B * o.y() + C * o.z());
    }

    //由两点构造中垂面
    public static Surface midOf(Point p1, Point p2) {
        Vector v = Vector.of(p1, p2);
        Point mid = Point.of(p1, Vector.multiply(v, 0.5));
        return new Surface(mid, v);
    }
    /*
    //[测试]用离散的点在特定范围内显示平面
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
     */
}

class ConnectList {

    private final List<List<Point>> sequence;//点列表
    private final List<List<Vector>> vectors;//速度向量列表 与点列表一一对应
    private final List<int[]> connectList;//连接列表
    public ConnectList(Timeline timeline) {
        this.sequence = timeline.getAsPointSequence();
        this.vectors = new ArrayList<>(sequence.size());
        this.connectList = new ArrayList<>();
        genConnectList();
    }

    public Point getPoint(int tick, int i) {
        return sequence.get(tick).get(i);
    }

    public int[] getIndex(int i) {
        return connectList.get(i);//获取一个连接列表
    }

    public Vector getVector(int tick, int i) {
        return vectors.get(tick).get(i);
    }

    public void setVector(int tick, int i, Vector v) {
        while (vectors.size() <= tick) {
            vectors.add(new ArrayList<>());//扩容到指定大小
        }
        while (vectors.get(tick).size() <= i) {
            vectors.get(tick).add(null);//设置数据可能不在范围内 需要扩容
        }
        vectors.get(tick).set(i, v);
    }

    public int size() {
        return connectList.size();
    }

    //生成连接列表
    private void genConnectList() {
        //点列表并不是连续的 需要先遍历一遍找到所有有效点的索引 然后通过索引列表遍历
        List<Integer> notNull = new ArrayList<>();
        for (int i = 0; i < this.sequence.size(); i++) {
            if (!this.sequence.get(i).isEmpty()) {
                notNull.add(i);
            }
        }

        for (int i = 0; i < notNull.size() - 1; i++) {
            Point[] pl1 = this.sequence.get(notNull.get(i)).toArray(Point[]::new);//List<Point>转换到Point[]
            Point[] pl2 = this.sequence.get(notNull.get(i + 1)).toArray(Point[]::new);

            int[][] subList = MathUtil.genConnectList(pl1, pl2);//生成两组点的连接列表(int[][2])

            for (int[] index : subList) {
                if (index[1] == -1) continue;//列表中有不连的点 需要跳过
                this.connectList.add(new int[] {notNull.get(i), index[0], notNull.get(i + 1), index[1]});//将时间信息添加到列表中
            }
        }
    }

    /*
    //全程一条线也可以用的懒人方法
    private void genConnectList() {
        List<Integer> notNull = new ArrayList<>();
        for (int i = 0; i < this.sequence.size(); i++) {
            if (this.sequence.get(i).size() != 0) {
                notNull.add(i);
            }
        }

        for (int i = 0; i < notNull.size() - 1; i++) {
            this.index.add(new int[] {notNull.get(i), 0, notNull.get(i + 1), 0});
        }
    }
     */

}

