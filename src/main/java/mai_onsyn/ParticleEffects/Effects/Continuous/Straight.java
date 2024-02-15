package mai_onsyn.ParticleEffects.Effects.Continuous;

import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Particle;
import mai_onsyn.ParticleEffects.Utils.Math.Point;

import java.awt.*;

public class Straight implements Effect {

    private final Timeline timeline = new Timeline();

    public Straight(Point A, Point B, double lambda, Particle sample) {
        final double totalCount = lambda * Point.distance(A, B);

        this.timeline.add(0, new Particle(sample.getName(), sample.getPosition(), sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()));

        final double dx = B.x() - A.x();
        final double dy = B.y() - A.y();
        final double dz = B.z() - A.z();
        for (int i = 0; i < totalCount; i++) {
            this.timeline.add(0, new Particle(sample.getName(), new Point(A.x() + dx * i / totalCount, A.y() + dy * i / totalCount, A.z() + dz * i / totalCount), sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()));
        }
    }

    public Straight(Point A, Point B, int duration, double lambda, Particle sample) {
        final double totalCount = lambda * Point.distance(A, B);

        this.timeline.add(0, new Particle(sample.getName(), sample.getPosition(), sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()));

        final double dx = B.x() - A.x();
        final double dy = B.y() - A.y();
        final double dz = B.z() - A.z();
        for (int i = 0; i < totalCount; i++) {
            this.timeline.add((int) (duration * i / totalCount), new Particle(sample.getName(), new Point(A.x() + dx * i / totalCount, A.y() + dy * i / totalCount, A.z() + dz * i / totalCount), sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()));
        }
    }

    @Override
    public Timeline gettimeline() {
        return this.timeline;
    }
}
