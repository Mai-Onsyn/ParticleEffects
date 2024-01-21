package mai_onsyn.ParticleEffects.Effects.Normal;

import mai_onsyn.ParticleEffects.Effects.Continuous.Straight;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Utils.Math.Point;

import java.awt.*;

public class Cube implements Effect {

    private final Timeline timeline = new Timeline();


    public Cube(Point o, double range, double lambda, String particleType, Color color) {
        double halfRange = range / 2;

        Point xPyPzP = new Point(o.x() + halfRange, o.y() + halfRange, o.z() + halfRange);
        Point xSyPzP = new Point(o.x() - halfRange, o.y() + halfRange, o.z() + halfRange);
        Point xSySzP = new Point(o.x() - halfRange, o.y() - halfRange, o.z() + halfRange);
        Point xSySzS = new Point(o.x() - halfRange, o.y() - halfRange, o.z() - halfRange);
        Point xPySzP = new Point(o.x() + halfRange, o.y() - halfRange, o.z() + halfRange);
        Point xPySzS = new Point(o.x() + halfRange, o.y() - halfRange, o.z() - halfRange);
        Point xPyPzS = new Point(o.x() + halfRange, o.y() + halfRange, o.z() - halfRange);
        Point xSyPzS = new Point(o.x() - halfRange, o.y() + halfRange, o.z() - halfRange);

        timeline.add(0, new Straight(xPyPzS, xSyPzS, lambda, particleType, color));
        timeline.add(0, new Straight(xPySzS, xSySzS, lambda, particleType, color));
        timeline.add(0, new Straight(xPyPzP, xSyPzP, lambda, particleType, color));
        timeline.add(0, new Straight(xSySzP, xPySzP, lambda, particleType, color));
        timeline.add(0, new Straight(xPyPzS, xPySzS, lambda, particleType, color));
        timeline.add(0, new Straight(xSyPzS, xSySzS, lambda, particleType, color));
        timeline.add(0, new Straight(xPySzP, xPyPzP, lambda, particleType, color));
        timeline.add(0, new Straight(xSyPzP, xSySzP, lambda, particleType, color));
        timeline.add(0, new Straight(xPyPzS, xPyPzP, lambda, particleType, color));
        timeline.add(0, new Straight(xPySzS, xPySzP, lambda, particleType, color));
        timeline.add(0, new Straight(xSyPzS, xSyPzP, lambda, particleType, color));
        timeline.add(0, new Straight(xSySzS, xSySzP, lambda, particleType, color));
    }


    @Override
    public Timeline gettimeline() {
        return this.timeline;
    }
}
