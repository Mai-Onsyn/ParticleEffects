import mai_onsyn.ParticleEffects.EffectUtils.ExpressionUtil;
import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Continuous.Ray;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Effects.Normal.Cube;
import mai_onsyn.ParticleEffects.Effects.Normal.UniformBall;
import mai_onsyn.ParticleEffects.ScatterChartGenerator;
import mai_onsyn.ParticleEffects.Utils.Math.Filter;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.awt.*;
import java.io.File;
import java.util.Arrays;

import static java.lang.Math.*;

public class Test03 {
    private static final Timeline timeline = new Timeline("Mutant", new File("E:\\.On_craft\\#Minecrafts\\#HMCL\\.minecraft\\versions\\1.17.1\\saves\\ParticleTest\\datapacks\\pack\\data\\minecraft\\functions"), 65536, 0, "minecraft", "Timeline", "effect");

    public static void main(String[] args) {

        Point p = new Point(8, 5, -1);

        Particle particle = new Particle(Particle.END_ROD, p, Color.WHITE);
        Particle particle1 = new Particle(Particle.END_ROD, p, Color.WHITE);
        Vector k = new Vector(0, 1, 2);
        Point o = new Point(0, 16, 0);

        Effect cube = new Cube(o, 1, 4, new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.WHITE));
        Effect ball = new UniformBall(o, 3, 8, new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.WHITE));

        ExpressionUtil.addRotation(cube, o, k, true, 0.2);
        ExpressionUtil.addRotation(ball, o, k, true, 0.2);

        timeline.add(0, ball);
        timeline.add(0, new Ray(o, k, 0, 8, new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.WHITE)));

        ExpressionUtil.addRotation(particle, o, k, true, 0.2);
        ExpressionUtil.addRotation2(particle1, o, k, true, 0.2);
        System.out.println(particle);
        System.out.println(particle1);
        timeline.write();
    }
}
