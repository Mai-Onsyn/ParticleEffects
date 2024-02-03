package mai_onsyn.ParticleEffects.Effects.Continuous;

import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;

public class Ray implements Effect {

    private final Timeline timeline = new Timeline();


    public Ray(Point o, Vector v, int duration, double lambda, Particle sample) {
        this.timeline.add(0, new Straight(o, Point.of(o, v), duration, lambda, sample));
    }


    @Override
    public Timeline gettimeline() {
        return this.timeline;
    }
}
