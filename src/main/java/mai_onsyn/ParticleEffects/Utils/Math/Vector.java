package mai_onsyn.ParticleEffects.Utils.Math;

import static java.lang.Math.*;

public class Vector {

    private double vx, vy, vz;

    public Vector(double vx, double vy, double vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public double vx() {
        return vx;
    }

    public double vy() {
        return vy;
    }

    public double vz() {
        return vz;
    }

    public double length () {
        return Math.sqrt(vx * vx + vy * vy + vz * vz);
    }

    public Vector getNormalVector() {
        if (abs(vx) < 0.000000059604644775390625) {
            return new Vector(1, 0, 0);
        }
        if (abs(vy) < 0.000000059604644775390625) {
            return new Vector(0, 1, 0);
        }
        if (abs(vz) < 0.000000059604644775390625) {
            return new Vector(0, 0, 1);
        }
        return new Vector(vz, 0, -vx);
    }

    public PolarVector polarMode() {
        double l = this.length();
        if (abs(this.vx) < 0.000000059604644775390625 && abs(this.vz) < 0.000000059604644775390625) {
            if (this.vy >= 0) {
                return new PolarVector(0, PI / 2, l);
            }
            else {
                return new PolarVector(0, - PI / 2, l);
            }
        }
        double theta, phi;
        Vector projection = new Vector(vx, 0, vz);
        if (vy > 0) phi = angle(this, projection);
        else phi = -angle(this, projection);

        theta = angle(projection, new Vector(1, 0, 0));
        if (vz < 0) theta = PI * 2 - theta;

        return new PolarVector(theta, phi, l);
    }

    public void rotate(Vector m, double theta) {
        final double length = this.length();

        final Vector k = new Vector(m.vx(), m.vy(), m.vz());//复制一份免得污染
        k.setLength(1);//k必须为单位向量

        final double vxT, vyT, vzT;
        final Vector cp = crossProduct(k, this);
        final double dp = dotProduct(k, this);
        final double sinTheta = sin(theta);
        final double cosTheta = cos(theta);
        
        vxT = vx * cosTheta + (1 - cosTheta) * dp * k.vx() + sinTheta * cp.vx();
        vyT = vy * cosTheta + (1 - cosTheta) * dp * k.vy() + sinTheta * cp.vy();
        vzT = vz * cosTheta + (1 - cosTheta) * dp * k.vz() + sinTheta * cp.vz();
        Vector copy = new Vector(vx, vy, vz);

        this.vx = vxT;
        this.vy = vyT;
        this.vz = vzT;

        //保证向量的模长不变
        this.setLength(length);
    }

    public Vector setLength(double length) {
        final double ratio = length / this.length();
        this.vx *= ratio;
        this.vy *= ratio;
        this.vz *= ratio;
        return this;
    }

    public static Vector plus(Vector v1, Vector v2) {
        return new Vector(v1.vx() + v2.vx(), v1.vy() + v2.vy(), v1.vz() + v2.vz());
    }

    public static Vector subtract(Vector v1, Vector v2) {
        return new Vector(v1.vx() - v2.vx(), v1.vy() - v2.vy(), v1.vz() - v2.vz());
    }

    public static double dotProduct(Vector v1, Vector v2) {
        return v1.vx() * v2.vx() + v1.vy() * v2.vy() + v1.vz() * v2.vz();
    }

    public static Vector crossProduct(Vector v1, Vector v2) {
        return new Vector(
                v1.vy() * v2.vz() - v2.vy() * v1.vz(),
                v1.vz() * v2.vx() - v2.vz() * v1.vx(),
                v1.vx() * v2.vy() - v2.vx() * v1.vy()
        );
    }

    public static double angle(Vector v1, Vector v2) {
        double dotProduct = dotProduct(v1, v2);
        double l1 = v1.length();
        double l2 = v2.length();

        if (l1 == 0 || l2 == 0) {
            return 0;
        }

        double cosTheta = dotProduct / (l1 * l2);

        if (cosTheta > 1) {
            cosTheta = 1;
        } else if (cosTheta < -1) {
            cosTheta = -1;
        }

        return acos(cosTheta);
    }

    public static PolarVector angleOnPolar(Vector v1, Vector v2) {
        PolarVector pv1 = v1.polarMode();
        PolarVector pv2 = v2.polarMode();

        return new PolarVector(pv2.getTheta() - pv1.getTheta(), pv2.getPhi() - pv1.getPhi(), 1);
    }

    public static Vector of(Point p) {
        return new Vector(p.x(), p.y(), p.z());
    }

    public static Vector of(Point p1, Point p2) {
        return new Vector(p2.x() - p1.x(), p2.y() - p1.y(), p2.z() - p1.z());
    }

    public String toString() {
        return String.format("(%.5f, %.5f, %.5f)", vx, vy, vz);
    }
}