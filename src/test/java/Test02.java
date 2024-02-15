import mai_onsyn.ParticleEffects.EffectUtils.ExpressionUtil;
import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Continuous.Ray;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Effects.Normal.Cube;
import mai_onsyn.ParticleEffects.Effects.Normal.RandomBall;
import mai_onsyn.ParticleEffects.Effects.Normal.UniformBall;
import mai_onsyn.ParticleEffects.Static;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;
import mai_onsyn.ParticleEffects.Utils.Math.Point;

import java.awt.*;
import java.io.File;

public class Test02 {

    private static final Timeline timeline = new Timeline("Mutant", new File("E:\\.On_craft\\#Minecrafts\\#HMCL\\.minecraft\\versions\\1.17.1\\saves\\ParticleTest\\datapacks\\pack\\data\\minecraft\\functions"), 65536, 0, "minecraft", "Timeline", "effect");

    public static void main(String[] args) {

        Particle sample = new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.WHITE, 0, 1, 0, new Expression(), "null");
        Point o = new Point(0, 48, 0);
        Vector axis = new Vector(30, 30, 0);

        for (int i = 0; i < 8; i++) {
            Effect ball = new UniformBall(new Point(15+i*2, 63+i*2, 0), 2, 8, new Particle(Particle.END_ROD, new Point(0, 0, 0), new Color(165, 22, 28)));
            ExpressionUtil.addRotation(ball, o, axis, true, 0.2);
            timeline.add(0, ball);
        }

        Particle particle = new Particle(Particle.END_ROD, new Point(1, 0, 1), Color.WHITE);
        //ExpressionUtil.addRotation(particle, o, axis, true, 0.2);


        timeline.add(0, particle);
        timeline.add(0, new Ray(o, axis, 0, 8, new Particle(Particle.END_ROD, new Point(0, 0, 0), new Color(252, 235, 213))));

        timeline.write();
    }
}
