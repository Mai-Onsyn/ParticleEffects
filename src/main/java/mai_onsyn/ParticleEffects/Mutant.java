package mai_onsyn.ParticleEffects;

import mai_onsyn.ParticleEffects.EffectUtils.ExpressionUtil;
import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Continuous.Straight;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Effects.Normal.Cube;
import mai_onsyn.ParticleEffects.Utils.Particle;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;

import java.awt.*;

public class Mutant {

    public static Timeline timeline = new Timeline("Mutant", Static.path, 65536, 0);


    public static void main(String[] args) {

        Vector v = new Vector(10, 20, 11.4514);
        Point orinV = new Point(-38, 8, 40);
        Point orinC = new Point(-33, 20, 46);

        Effect cube = new Cube(orinC, 3, 8, Particle.END_ROD, Color.RED);
        ExpressionUtil.addRotation(cube, orinV, v, false, 0.1);

        Effect axis = new Straight(orinV, Point.of(orinV, v), 8, Particle.END_ROD, Color.CYAN);

        timeline.add(0, cube);
        timeline.add(0, axis);
        timeline.write();
    }
}