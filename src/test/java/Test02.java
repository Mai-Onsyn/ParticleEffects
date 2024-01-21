import mai_onsyn.ParticleEffects.EffectUtils.ExpressionUtil;
import mai_onsyn.ParticleEffects.Utils.Math.MathUtil;
import mai_onsyn.ParticleEffects.Utils.Particle;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;

import java.awt.*;

public class Test02 {

    public static void main(String[] args) {

        Vector v = new Vector(1, 1, 0);

        Particle particle = new Particle(Particle.END_ROD, new Point(-38, 8, 42), Color.WHITE);
        ExpressionUtil.addRotation(particle, new Point(-38, 8, 40), v, true, 0.1);
        System.out.println(particle);


    }
}
