import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Continuous.Straight;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Math.MathUtil;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Random;

public class PointsConnectDemo {

    private static final Timeline timeline = new Timeline("Mutant", new File("E:\\.On_craft\\#Minecrafts\\#HMCL\\.minecraft\\versions\\1.17.1\\saves\\ParticleTest\\datapacks\\pack\\data\\minecraft\\functions"), 65536, 0, "minecraft", "Timeline", "effect");

    public static void main(String[] args) {

        Point[] pl1a = new Point[] {
                new Point(100, 15, 0),
                new Point(103, 14, 4),
                new Point(102, 16, 2),
                new Point(104, 15, 3),
                new Point(100, 14, 2)
        };

        Point[] pl2a = new Point[] {
                new Point(106, 19, 0),
                new Point(103, 20, 5),
                new Point(108, 19, 9),
                new Point(104, 22, 6),
                new Point(102, 21, 4),
                new Point(107, 20, 8),
                new Point(104, 23, 6)
        };

        Point[] pl1 = new Point[10];
        Point[] pl2 = new Point[14];
        Random random = new Random(1919810);

        for (int i = 0; i < 10; i++) {
            pl1[i] = new Point(random.nextDouble() * 10 - 5, random.nextDouble() * 10 + 100, random.nextDouble() * 10 - 5);
        }
        for (int i = 0; i < 14; i++) {
            pl2[i] = new Point(random.nextDouble() * 10 - 5, random.nextDouble() * 10 + 110, random.nextDouble() * 10 - 5);
        }

        int[][] connectList = MathUtil.genConnectList(pl1, pl2);

        for (int i = 0; i < connectList.length; i++) {
            Effect line = new Straight(pl1[connectList[i][0]], pl2[connectList[i][1]], 8, new Particle(Particle.END_ROD, new Point(0, 0, 0), Color.WHITE));
            timeline.add(0, line);
        }
        timeline.write();
    }
}
