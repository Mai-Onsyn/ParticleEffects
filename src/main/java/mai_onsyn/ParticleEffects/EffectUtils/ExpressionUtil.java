package mai_onsyn.ParticleEffects.EffectUtils;

import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.*;
import mai_onsyn.ParticleEffects.Utils.Math.*;

import static java.lang.Math.*;

public class ExpressionUtil {

    public static void addTranslation(Effect effect, Vector v) {
        effect.gettimeline().getSequence().forEach(sub -> sub.forEach(particle -> {
            addTranslation(particle, v);
        }));
    }

    public static void addTranslation(Particle particle, Vector v) {
        particle.getExpression().addVx(String.format("%.10f", v.vx()));
        particle.getExpression().addVy(String.format("%.10f", v.vy()));
        particle.getExpression().addVz(String.format("%.10f", v.vz()));
    }


    public static void addTranslationResist(Effect effect, Vector v) {
        effect.gettimeline().getSequence().forEach(sub -> sub.forEach(particle -> {
            addTranslationResist(particle, v);
        }));
    }

    public static void addTranslationResist(Particle particle, Vector v) {
        particle.getExpression().addVx(String.format("%.10f*E^(-0.1*t)", v.vx()));
        particle.getExpression().addVy(String.format("%.10f*E^(-0.1*t)", v.vy()));
        particle.getExpression().addVz(String.format("%.10f*E^(-0.1*t)", v.vz()));
    }

    public static void addRotation(Effect effect, Point o, Vector m, boolean way, double omega) {
        effect.gettimeline().getSequence().forEach(sub -> sub.forEach(particle -> {
            addRotation(particle, o, m, way, omega);
        }));
    }

    //参数：particle:要旋转的粒子; o:旋转轴向量的起点; m:旋转轴向量; way:旋转方向; omega:旋转角速度
    public static void addRotation(Particle particle, Point o, Vector m, boolean way, double omega) {
        Vector k = new Vector(m.vx(), m.vy(), m.vz());//旋转向量复制一份免得污染
        k.setLength(1);//向量模长设为1 免得后面旋转速度不匹配
        Point position = particle.getPosition();
        position = Point.of(Vector.of(o, position));//向量OP
        int eCount = 1024;//旋转一圈割圆分数

        Point[] round = MathUtil.exhaustivePosition(position, k, way, eCount);//穷举得到圆周上的点
        double[] ax = new double[eCount], ay = new double[eCount], az = new double[eCount];
        for (int i = 0; i < eCount-1; i++) {
            ax[i] = round[i+1].x() - round[i].x();//元数据是位移数组 要转为速度数组
            ay[i] = round[i+1].y() - round[i].y();//这里还要除以时间的 但因为每两个点间的时间都一样 就在后面统一除 减少计算量
            az[i] = round[i+1].z() - round[i].z();
        }
        Complex[] xResult = Fourier.FFT(Complex.of(ax));//傅里叶变换
        Complex[] yResult = Fourier.FFT(Complex.of(ay));
        Complex[] zResult = Fourier.FFT(Complex.of(az));

        double[] xWave = MathUtil.getMaxWave(xResult);//获取变换后最大振幅的波的A, w, φ
        double[] yWave = MathUtil.getMaxWave(yResult);//double[]只是方便返回三个值用的
        double[] zWave = MathUtil.getMaxWave(zResult);

        double omegaFactor = eCount/PI * omega;//一个因数 包括了前面没除的时间

        //将速度表达式添加到粒子的表达式中 关于为什么是cos前面还有个负号 我还是一个一个试的
        particle.getExpression().addVx(String.format("-%.10f*cos(%.10f*t+%.10f)", xWave[0]*omegaFactor, xWave[1]*omegaFactor, xWave[2]));
        particle.getExpression().addVy(String.format("-%.10f*cos(%.10f*t+%.10f)", yWave[0]*omegaFactor, yWave[1]*omegaFactor, yWave[2]));
        particle.getExpression().addVz(String.format("-%.10f*cos(%.10f*t+%.10f)", zWave[0]*omegaFactor, zWave[1]*omegaFactor, zWave[2]));
    }
}
