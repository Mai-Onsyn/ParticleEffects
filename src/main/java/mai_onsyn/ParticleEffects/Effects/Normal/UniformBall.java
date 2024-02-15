package mai_onsyn.ParticleEffects.Effects.Normal;

import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.PolarVector;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class UniformBall implements Effect {

    private final Timeline timeline = new Timeline();

    //Normal
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

    //Rotate
    public UniformBall(Point o, double range, double startTheta, double endTheta, double duration, double lambda, Particle sample) {
        final int N = (int) ceil(4 * PI * range * range * lambda);
        final double phi = (sqrt(5) - 1) / 2;
        final List<PolarVector> surfaceList = new ArrayList<>(N);
        final double angle = endTheta - startTheta;
        final double stepAngle = angle / duration;

        for (int n = 1; n < N; n++) {

            double yn = ((2 * n - 1.0) / N - 1);
            double xn = sqrt(1 - pow(yn, 2)) * cos(2 * PI * n * phi);
            double zn = sqrt(1 - pow(yn, 2)) * sin(2 * PI * n * phi);

            surfaceList.add(new Vector(range * xn, range * yn, range * zn).polarMode());
        }

        for (int i = 0; i < duration; i++) {
            double thisAngle = i * stepAngle;
            double nextAngle = thisAngle + stepAngle;
            for (PolarVector polarVector : surfaceList) {
                double theta = polarVector.getTheta();
                while (true) {
                    if (theta >= thisAngle && theta < nextAngle) {
                        this.timeline.add(i, new Particle(
                                sample.getName(),
                                Point.of(o, polarVector.toVector()),
                                sample.getColor(), sample.getRange(), sample.getCount(), sample.getLife(), new Expression(), sample.getGroup()));
                        break;
                    } else if (theta < thisAngle) {
                        theta += 2 * PI;
                    } else if (theta >= nextAngle) break;
                    else
                        System.out.println(String.format("this: %.10f, now: &.10f, next: %.10f", thisAngle, theta, nextAngle));
                }
            }
        }
    }


    @Override
    public Timeline gettimeline() {
        return this.timeline;
    }
}
