import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Effects.Normal.UniformBall;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Particle;
import mai_onsyn.ParticleEffects.Utils.Math.Point;

import java.awt.*;
import java.io.File;

public class Test02 {

    private static final Timeline timeline = new Timeline("Mutant", new File("E:\\.On_craft\\#Minecrafts\\#HMCL\\.minecraft\\versions\\1.17.1\\saves\\ParticleTest\\datapacks\\pack\\data\\minecraft\\functions"), 65536, 0);

    public static void main(String[] args) {

        Particle sample = new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.WHITE, 0, 1, -1, new Expression(), "null");
        Effect ball = new UniformBall(new Point(0, 10, 0), 2, 8, sample);

        timeline.add(0, ball);

        timeline.write();
    }
}
