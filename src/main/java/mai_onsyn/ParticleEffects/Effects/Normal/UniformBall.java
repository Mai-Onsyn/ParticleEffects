package mai_onsyn.ParticleEffects.Effects.Normal;

import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Particle;

import static java.lang.Math.*;

public class UniformBall implements Effect {

    private final Timeline timeline = new Timeline();


    public UniformBall(Point o, double range, double lambda, Particle sample) {
        final int N = (int) ceil(4 * PI * range * range * lambda);
        final double phi = (sqrt(5) - 1) / 2;

        for (int n = 1; n < N; n++) {

            double yn = ((2 * n - 1.0) / N - 1);
            double xn = sqrt(1 - pow(yn , 2)) * cos(2 * PI * n * phi);
            double zn = sqrt(1 - pow(yn , 2)) * sin(2 * PI * n * phi);

            this.timeline.add(0, new Particle(
                    sample.getName(),
                    new Point(
                            range * xn + o.x(),
                            range * yn + o.y(),
                            range * zn + o.z()
                    ),
                    sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()));
        }
    }


    @Override
    public Timeline gettimeline() {
        return this.timeline;
    }
}
