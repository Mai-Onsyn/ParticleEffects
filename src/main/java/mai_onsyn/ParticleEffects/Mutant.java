package mai_onsyn.ParticleEffects;

import mai_onsyn.ParticleEffects.EffectUtils.ExpressionUtil;
import mai_onsyn.ParticleEffects.EffectUtils.PositionTimeline;
import mai_onsyn.ParticleEffects.EffectUtils.Timeline;
import mai_onsyn.ParticleEffects.Effects.Continuous.Straight;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Expression;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.awt.*;

public class Mutant {

    private static final Timeline timeline = new Timeline("Mutant", Static.path, 65536, 0);
    private static final PositionTimeline Steve = new PositionTimeline("Steve_path", "Steve", Static.path, 20);
    private static final PositionTimeline Mutant = new PositionTimeline("Mutant_path", "Mutant", Static.path, 20);


    public static void main(String[] args) {

        steveRoutine();
        mutantRoutine();
        Steve.write();
        Mutant.write();

        effects();
        timeline.write();
    }

    private static void steveRoutine() {
        int s = 20;
        int step = 32;
        Steve.add(0, 8, 256, 0);
        Steve.add(s, 16, 268, 2);
        Steve.add(s+5, 24, 268, 2);
        Steve.add(step+s, 32, 244, 4);
        Steve.add(step+s+5, 40, 244, 4);
        Steve.add(2*step+s, 48, 268, 6);
        Steve.add(2*step+s+5, 56, 268, 6);
        Steve.add(3*step+s, 64, 244, 8);
        Steve.add(3*step+s+5, 72, 244, 8);
        Steve.add(4*step+s, 80, 268, 10);
        Steve.add(4*step+s+5, 88, 268, 10);
        Steve.add(5*step+s, 96, 244, 12);
        Steve.add(5*step+s+5, 104, 244, 12);
        Steve.add(6*step+s, 112, 268, 14);
        Steve.add(6*step+s+5, 120, 268, 14);
        Steve.add(7*step+s, 128, 244, 16);
        Steve.add(7*step+s+5, 136, 244, 16);
        Steve.add(8*step+s, 144, 268, 18);
        Steve.add(8*step+s+5, 152, 268, 18);
        Steve.add(9*step+s, 160, 244, 20);
        Steve.add(9*step+s+5, 168, 244, 20);
        Steve.add(10*step+s, 176, 268, 22);
        Steve.add(10*step+s+5, 184, 268, 22);
        Steve.add(11*step+s, 192, 244, 24);
        Steve.add(11*step+s+5, 200, 244, 24);
    }

    private static void mutantRoutine() {
        int s = 20;
        int step = 32;
        Mutant.add(0, 0, 256, 0);
        Mutant.add(s, 8, 268, 1);
        Mutant.add(s+10, 16, 268, 2);
        Mutant.add(step+s, 24, 244, 3);
        Mutant.add(step+s+10, 32, 244, 5);
        Mutant.add(2*step+s, 40, 268, 8);
        Mutant.add(2*step+s+10, 48, 268, 9);
        Mutant.add(3*step+s, 56, 244, 10);
        Mutant.add(3*step+s+10, 64, 244, 13);
        Mutant.add(4*step+s, 72, 268, 12);
        Mutant.add(4*step+s+10, 80, 268, 10);
        Mutant.add(5*step+s, 88, 244, 8);
        Mutant.add(5*step+s+10, 96, 244, 10);
        Mutant.add(6*step+s, 104, 268, 13);
        Mutant.add(6*step+s+10, 112, 268, 16);
        Mutant.add(7*step+s, 120, 244, 16);
        Mutant.add(7*step+s+10, 128, 244, 20);
        Mutant.add(8*step+s, 136, 268, 21);
        Mutant.add(8*step+s+10, 144, 268, 19);
        Mutant.add(9*step+s, 152, 244, 20);
        Mutant.add(9*step+s+10, 160, 244, 23);
        Mutant.add(10*step+s, 168, 268, 22);
        Mutant.add(10*step+s+10, 176, 268, 22);
        Mutant.add(11*step+s, 184, 244, 24);
        Mutant.add(11*step+s+10, 192, 244, 21);
    }

    private static void effects() {
        //Chapter 1
        {
            int step = 32;
            Particle LightSample = new Particle(Particle.GRAPHIC_BIG, new Point(0, 0, 0), new Color(118, 225, 225, 127), 0, 1, -1, new Expression(), "");
            Particle EyeSample = new Particle(Particle.EYE_MEDIUM, new Point(0, 0, 0), new Color(0xffffffff), 0, 1, 10, new Expression(), "null");
            Particle ExplodeSample = new Particle(Particle.EXPLOSION, new Point(0, 0, 0), new Color(0xffffffff), 0, 1, 10, new Expression(), "null");
            for (int i = 0; i < 12; i++) {
                int tick = 20 + step * i;
                String group = String.valueOf((char) ('a' + i));
                Point mutant = Mutant.getIndex(tick);
                Point steve = Steve.getIndex(tick);
                Vector offset = new Vector(0, (i % 2 == 0) ? 3 : -3, 0);
                Vector target = Vector.plus(Vector.of(mutant, steve), offset).setLength(150);
                Point start = Point.of(mutant, new Vector(-target.vx(), -target.vy(), -target.vz()));
                target.setLength(300);
                Point end = Point.of(start, target);
                timeline.add(tick + 5, new Straight(start, end, 0, 8, LightSample.setGroup(group)));
                timeline.add(tick + 80, "particleex group remove " + group);

                Effect eye1 = new Straight(mutant, end, 20, 1, EyeSample);
                Effect eye2 = new Straight(mutant, end, 20, 1, EyeSample);
                Effect eye3 = new Straight(mutant, end, 20, 1, EyeSample);
                Effect eye4 = new Straight(mutant, end, 20, 1, EyeSample);
                Vector n = target.getNormalVector().setLength(0.4);
                ExpressionUtil.addTranslationResist(eye1, n);
                n.rotate(target, Math.PI / 2);
                ExpressionUtil.addTranslationResist(eye2, n);
                n.rotate(target, Math.PI / 2);
                ExpressionUtil.addTranslationResist(eye3, n);
                n.rotate(target, Math.PI / 2);
                ExpressionUtil.addTranslationResist(eye4, n);
                n.rotate(target, Math.PI / 2);
                n.setLength(3);
                Effect explode1 = new Straight(Point.of(mutant, n), Point.of(end, n), 20, 1, ExplodeSample);
                n.rotate(target, Math.PI / 2);
                Effect explode2 = new Straight(Point.of(mutant, n), Point.of(end, n), 20, 1, ExplodeSample);
                n.rotate(target, Math.PI / 2);
                Effect explode3 = new Straight(Point.of(mutant, n), Point.of(end, n), 20, 1, ExplodeSample);
                n.rotate(target, Math.PI / 2);
                Effect explode4 = new Straight(Point.of(mutant, n), Point.of(end, n), 20, 1, ExplodeSample);
                timeline.add(tick + 5, eye1);
                timeline.add(tick + 5, eye2);
                timeline.add(tick + 5, eye3);
                timeline.add(tick + 5, eye4);
                timeline.add(tick + 15, explode1);
                timeline.add(tick + 15, explode2);
                timeline.add(tick + 15, explode3);
                timeline.add(tick + 15, explode4);
            }
        }
    }
}