package mai_onsyn.ParticleEffects.Effects.Process;

import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;

public class Spiral implements Effect {

    private final Timeline timeline = new Timeline();

    public Spiral(Effect src, double range, double step, int count) {
    }

    @Override
    public Timeline gettimeline() {
        return this.timeline;
    }
}
