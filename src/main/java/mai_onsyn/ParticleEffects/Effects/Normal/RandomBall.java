package mai_onsyn.ParticleEffects.Effects.Normal;

import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.PolarVector;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.util.Random;

import static java.lang.Math.*;

public class RandomBall implements Effect {

    public final Timeline timeline = new Timeline();


    private static final Random random = new Random();


    public RandomBall(Point o, double range, double lambda, Particle sample) {
        final int N = (int) ceil(4 * PI * range * range * lambda);

        for (int i = 0; i < N; i++) {
            final double theta = random.nextDouble(0, 2 * PI);
            final double phi = (random.nextBoolean() ? 1 : -1) * (asin(random.nextDouble(0, 1)));
            final Vector v = new PolarVector(theta, phi, range).toVector();
            this.timeline.add(0, new Particle(
                    sample.getName(),
                    new Point(o.x() + v.vx(), o.y() + v.vy(), o.z() + v.vz()),
                    sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()
            ));
        }
    }


    @Override
    public Timeline gettimeline() {
        return this.timeline;
    }
}
