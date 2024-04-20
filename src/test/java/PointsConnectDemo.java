import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Continuous.Straight;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Math.MathUtil;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class PointsConnectDemo {

    private static final Timeline timeline = new Timeline("Mutant", new File("E:\\.On_craft\\#Minecrafts\\#HMCL\\.minecraft\\versions\\1.17.1\\saves\\ParticleTest\\datapacks\\pack\\data\\minecraft\\functions"), 65536, 0, "minecraft", "Timeline", "effect");

    public static void main(String[] args) {

        Point[] pl1 = new Point[] {
                new Point(100, 15, 0),
                new Point(103, 14, 4),
                new Point(102, 16, 2),
                new Point(104, 15, 3),
                new Point(100, 14, 2)
        };

        Point[] pl2 = new Point[] {
                new Point(106, 19, 0),
                new Point(103, 20, 5),
                new Point(108, 19, 9),
                new Point(104, 22, 6),
                new Point(102, 21, 4),
                new Point(107, 20, 8),
                new Point(104, 23, 6)
        };

        int[][] connectList = MathUtil.genConnectList(pl1, pl2);

        for (int i = 0; i < connectList.length; i++) {
            System.out.println(Arrays.toString(connectList[i]));
        }
    }
}
