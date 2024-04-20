import mai_onsyn.ParticleEffects.EffectUtils.ExpressionUtil;
import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Continuous.Ray;
import mai_onsyn.ParticleEffects.Effects.Continuous.Soma3;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Effects.Normal.Cube;
import mai_onsyn.ParticleEffects.Effects.Normal.UniformBall;
import mai_onsyn.ParticleEffects.ScatterChartGenerator;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.Filter;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.*;

public class Test03 {
    private static final Timeline timeline = new Timeline("Mutant", new File("E:\\.On_craft\\#Minecrafts\\#HMCL\\.minecraft\\versions\\1.17.1\\saves\\ParticleTest\\datapacks\\pack\\data\\minecraft\\functions"), 65536, 0, "minecraft", "Timeline", "effect");

    public static void main(String[] args) {

        Point p = new Point(8, 5, -1);

        Particle particle = new Particle(Particle.END_ROD, p, Color.WHITE, 0, 1, -1, new Expression(), "null");

        Timeline t1 = new Timeline();
        t1.add(10, new Point(3, 110, 5));
        t1.add(15, new Point(-3, 108, 0));
        t1.add(20, new Point(7, 115, 2));
        t1.add(25, new Point(10, 104, 8));
        t1.add(30, new Point(0, 102, 0));
        t1.add(35, new Point(8, 108, 2));
        t1.add(40, new Point(12, 120, 4));
        t1.add(45, new Point(8, 118, 10));

        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            //t1.add(i * 5 + 45, new Point(random.nextDouble(-32, 32),100+random.nextDouble(-32, 32), random.nextDouble(-32, 32)));
        }

        timeline.add(0, t1);
        timeline.add(0, new Soma3(t1, new Point(0, 110, 0), new Vector(0, -1, 0), 8, particle));

        timeline.write();
    }
}
