package mai_onsyn.ParticleEffects.EffectUtils;

import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.ScatterChartGenerator;
import mai_onsyn.ParticleEffects.Utils.*;
import mai_onsyn.ParticleEffects.Utils.Math.*;

import java.util.Arrays;

import static java.lang.Math.*;

public class ExpressionUtil {

    public static void addTranslation(Effect effect, Vector v) {
        effect.gettimeline().getSequence().forEach(sub -> sub.forEach(particle -> {
            addTransition(particle, v);
        }));
    }

    public static void addTransition(Particle particle, Vector v) {
        particle.getExpression().addVx(String.valueOf(v.vx()));
        particle.getExpression().addVy(String.valueOf(v.vy()));
        particle.getExpression().addVz(String.valueOf(v.vz()));
    }

    public static void addRotation(Effect effect, Point o, Vector m, boolean way, double omega) {
        effect.gettimeline().getSequence().forEach(sub -> sub.forEach(particle -> {
            addRotation(particle, o, m, way, omega);
        }));
    }

    public static void addRotation(Particle particle, Point o, Vector m, boolean way, double omega) {
        Vector k = new Vector(m.vx(), m.vy(), m.vz());
        k.setLength(1);
        Point position = particle.getPosition();
        position = Point.of(Vector.of(o, position));
        int eCount = 1024;

        Point[] round = MathUtil.exhaustivePosition(position, k, way, eCount);
        double[] ax = new double[eCount], ay = new double[eCount], az = new double[eCount];
        for (int i = 0; i < eCount-1; i++) {
            ax[i] = round[i+1].x() - round[i].x();
            ay[i] = round[i+1].y() - round[i].y();
            az[i] = round[i+1].z() - round[i].z();
        }
        Complex[] xResult = Fourier.FFT(Complex.of(ax));
        Complex[] yResult = Fourier.FFT(Complex.of(ay));
        Complex[] zResult = Fourier.FFT(Complex.of(az));

        double[] xWave = MathUtil.getMaxWave(xResult);
        double[] yWave = MathUtil.getMaxWave(yResult);
        double[] zWave = MathUtil.getMaxWave(zResult);

        ScatterChartGenerator.show(az);

        double omegaFactor = eCount/PI * omega;

        particle.getExpression().addVx(String.format("-%.10f*cos(%.10f*t+%.10f)", xWave[0]*omegaFactor, xWave[1]*omegaFactor, xWave[2]));
        particle.getExpression().addVy(String.format("-%.10f*cos(%.10f*t+%.10f)", yWave[0]*omegaFactor, yWave[1]*omegaFactor, yWave[2]));
        particle.getExpression().addVz(String.format("-%.10f*cos(%.10f*t+%.10f)", zWave[0]*omegaFactor, zWave[1]*omegaFactor, zWave[2]));
    }
}
