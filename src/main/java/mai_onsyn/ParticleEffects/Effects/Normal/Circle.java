package mai_onsyn.ParticleEffects.Effects.Normal;

import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.MathUtil;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;

import static java.lang.Math.*;

public class Circle implements Effect {
    private final Timeline timeline = new Timeline();

    //Normal
    public Circle(Point o, Vector v, double r, double lambda, Particle sample) {
        final Point p = Point.of(v.getNormalVector().setLength(r));
        final Point[] circle = MathUtil.exhaustivePosition(p, v, true, (int) (2 * PI * r * lambda));
        for (Point point : circle) {
            this.timeline.add(0, new Particle(sample.getName(), new Point(point.x() + o.x(), point.y() + o.y(), point.z() + o.z()), sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()));
        }
    }

    //Sequentiality
    public Circle(Point o, Vector v, double r, int duration, double lambda, Particle sample) {
        final Point p = Point.of(v.getNormalVector().setLength(r));
        final int count = (int) (2 * PI * r * lambda);
        final Point[] circle = MathUtil.exhaustivePosition(p, v, true, count);
        for (int i = 0; i < count; i++) {
            this.timeline.add((int) ((double) i / count * duration), new Particle(sample.getName(), new Point(circle[i].x() + o.x(), circle[i].y() + o.y(), circle[i].z() + o.z()), sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()));
        }
    }

    //Bloom
    public Circle(Point o, Vector v, double r, double speed, int duration, double lambda, Particle sample) {
        final Point p = Point.of(v.getNormalVector().setLength(r));
        final int count = (int) (2 * PI * r * lambda);
        final Point[] circle = MathUtil.exhaustivePosition(p, v, true, count);
        for (int i = 0; i < count; i++) {
            Particle particle = new Particle(sample.getName(), o, sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup());
            particle.getExpression().addVx(String.format("%.5f", circle[i].x() * speed));
            particle.getExpression().addVy(String.format("%.5f", circle[i].y() * speed));
            particle.getExpression().addVz(String.format("%.5f", circle[i].z() * speed));
            this.timeline.add((int) ((double) i / count * duration), particle);
        }
    }

    @Override
    public Timeline gettimeline() {
        return this.timeline;
    }
}
