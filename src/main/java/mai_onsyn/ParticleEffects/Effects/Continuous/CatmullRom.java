package mai_onsyn.ParticleEffects.Effects.Continuous;

import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class CatmullRom implements Effect {

    private final Timeline timeline = new Timeline();

    public CatmullRom(Timeline sequence, Point ctrl1, Point ctrl2, double lambda, Particle sample) {
        //转换输入
        List<List<Particle>> lists = sequence.getSequence();
        int size = sequence.totalCount();
        List<Point> points = new ArrayList<>(size);
        List<Integer> ticks = new ArrayList<>(size);
        final int tickSize = lists.size();
        for (int tick = 0; tick < tickSize; tick++) {
            final int particleSize = lists.get(tick).size();
            for (int i = 0; i < particleSize; i++) {
                points.add(lists.get(tick).get(i).getPosition());
                ticks.add(tick);
            }
        }

        //判断输入合法度
        //要连接的点必须大于或等于2个
        if (size > 2) {
            for (int i = 0; i < size - 1; i++) {
                if (i == 0) {//第一个点和第二个点 前一个控制点取ctrl1
                    catmullRomConnect(ctrl1, points.get(i), points.get(i + 1), points.get(i + 2), ticks.get(i), ticks.get(i + 1) - ticks.get(i), lambda, sample);
                    continue;
                }
                if (i == size - 2) {//倒数第二个点和倒数第一个点 后一个控制点取ctrl2
                    catmullRomConnect(points.get(i - 1), points.get(i), points.get(i + 1), ctrl2, ticks.get(i), ticks.get(i + 1) - ticks.get(i), lambda, sample);
                    break;
                }
                //一般情况
                catmullRomConnect(points.get(i - 1), points.get(i), points.get(i + 1), points.get(i + 2), ticks.get(i), ticks.get(i + 1) - ticks.get(i), lambda, sample);
            }
        }
        //当只需要连接2个点时 控制点分别为ctrl1和ctrl2 该情况只有一条曲线
        else if (size == 2) {
            catmullRomConnect(ctrl1, points.get(0), points.get(1), ctrl2, ticks.get(0), ticks.get(1) - ticks.get(0), lambda, sample);
        }
        else {
            throw new NullPointerException("Enter less than 2 points in the timeline");//输入不合法
        }
    }

    private void catmullRomConnect(Point P0, Point P1, Point P2, Point P3, int startTick, int duration, double lambda, Particle sample) {

        //优化lambda值
        //输入的lambda值代表每1方块单位长度绘制lambda个点 但对于这个曲线这样算会有一点误差
        lambda *= Point.distance(P1, P2);//让曲线绘制密度与两点距离成正比
        lambda = 1 / lambda;

        //插值点的个数
        int n = (int) ceil(1 / lambda);

        //曲线扭曲度参数alpha
        double alpha = 0.5;

        //遍历每个插值点
        for (int i = 0; i < n; i++) {
            //计算插值参数t
            double t = i * lambda;

            //套方程式计算插值点的坐标
            this.timeline.add((int) ((double) i / n * duration) + startTick, new Particle(sample.getName(),
                    new Point(
                            (pow(t, 3) * (-alpha * P0.x() + (2 - alpha) * P1.x() + (alpha - 2) * P2.x() + alpha * P3.x()) + pow(t, 2) * (2 * alpha * P0.x() + (alpha - 3) * P1.x() + (3 - 2 * alpha) * P2.x() - alpha * P3.x()) + t * (-alpha * P0.x() + alpha * P2.x()) + P1.x()),
                            (pow(t, 3) * (-alpha * P0.y() + (2 - alpha) * P1.y() + (alpha - 2) * P2.y() + alpha * P3.y()) + pow(t, 2) * (2 * alpha * P0.y() + (alpha - 3) * P1.y() + (3 - 2 * alpha) * P2.y() - alpha * P3.y()) + t * (-alpha * P0.y() + alpha * P2.y()) + P1.y()),
                            (pow(t, 3) * (-alpha * P0.z() + (2 - alpha) * P1.z() + (alpha - 2) * P2.z() + alpha * P3.z()) + pow(t, 2) * (2 * alpha * P0.z() + (alpha - 3) * P1.z() + (3 - 2 * alpha) * P2.z() - alpha * P3.z()) + t * (-alpha * P0.z() + alpha * P2.z()) + P1.z())
                    ),
                    sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup())
            );
        }
    }

    @Override
    public Timeline gettimeline() {
        return this.timeline;
    }
}
