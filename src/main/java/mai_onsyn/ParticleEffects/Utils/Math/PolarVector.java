package mai_onsyn.ParticleEffects.Utils.Math;

import static java.lang.Math.*;

public class PolarVector {
    private double theta;
    private double phi;
    private double length;

    public PolarVector(double theta, double phi, double length) {
        this.theta = theta;
        this.phi = phi;
        this.length = length;
    }

    public Point toPoint() {
        return new Point(
                length * cos(theta) * cos(phi),
                length * sin(phi),
                length * sin(theta) * cos(phi)
        );
    }

    public Vector toVector() {
        return new Vector(
                length * cos(theta) * cos(phi),
                length * sin(phi),
                length * sin(theta) * cos(phi)
        );
    }

    public double getTheta() {
        return theta;
    }

    public double getPhi() {
        return phi;
    }

    public double getLength() {
        return length;
    }

    public PolarVector setTheta(double theta) {
        this.theta = theta;
        return this;
    }

    public PolarVector setPhi(double phi) {
        this.phi = phi;
        return this;
    }

    public PolarVector setLength(double length) {
        this.length = length;
        return this;
    }


    @Override
    public String toString() {
        return String.format("(%.5f, %.5f, %.5f)", toDegrees(this.theta), toDegrees(this.phi), this.length);
    }
}