import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Continuous.Soma3;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.awt.*;
import java.io.File;

public class Soma3Demo {
    private static final Timeline timeline = new Timeline("Mutant", new File("E:\\.On_craft\\#Minecrafts\\#HMCL\\.minecraft\\versions\\1.17.1\\saves\\ParticleTest\\datapacks\\pack\\data\\minecraft\\functions"), 65536, 0, "minecraft", "Timeline", "effect");

    public static void main(String[] args) {

        Point p = new Point(0, 0, 0);

        Particle particle = new Particle(Particle.END_ROD, p, Color.WHITE, 0, 1, -1, new Expression(), "null");

        Timeline t1 = new Timeline();
        t1.add(0, new Point(45.5, 27.5, -4.5));
        t1.add(5, new Point(71.5, 32.5, -13.5));
        t1.add(10, new Point(100.5, 37.5, -22.5));
        t1.add(10, new Point(101.5, 42.5, -11.5));
        t1.add(10, new Point(97.5, 38.5, -2.5));
        t1.add(15, new Point(124.5, 34.5, -14.5));
        t1.add(15, new Point(123.5, 40.5, -22.5));
        t1.add(20, new Point(156.5, 53.5, -12.5));
        t1.add(20, new Point(154.5, 44.5, -22.5));
        t1.add(20, new Point(152.5, 35.5, -11.5));
        t1.add(20, new Point(147.5, 29.5, -7.5));
        t1.add(25, new Point(189.5, 39.5, -8.5));

        timeline.add(0, new Soma3(t1, new Point(34.5, 23.5, -29.5), new Vector(0, -1, 1.2), 8, particle));

        timeline.write();
    }
}
