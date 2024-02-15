package mai_onsyn.ParticleEffects.Utils;

import java.util.Objects;

public class Expression {
    private String vx;
    private String vy;
    private String vz;
    private String cr;
    private String cg;
    private String cb;

    public Expression() {
        this.vx = "";
        this.vy = "";
        this.vz = "";
        this.cr = "";
        this.cg = "";
        this.cb = "";
    }

    public Expression(String vx, String vy, String vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.cr = "";
        this.cg = "";
        this.cb = "";
    }

    public Expression(String vx, String vy, String vz, String cr, String cg, String cb) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.cr = cr;
        this.cg = cg;
        this.cb = cb;
    }

    public String vx() {
        return this.vx;
    }

    public String vy() {
        return this.vy;
    }

    public String vz() {
        return this.vz;
    }

    public String cr() {
        return this.cr;
    }

    public String cg() {
        return this.cg;
    }

    public String cb() {
        return this.cb;
    }

    public void addVx(String expression) {
        this.vx += expression;
    }

    public void addVy(String expression) {
        this.vy += expression;
    }

    public void addVz(String expression) {
        this.vz += expression;
    }

    public void addCr(String expression) {
        this.cr += expression;
    }

    public void addCg(String expression) {
        this.cg += expression;
    }

    public void addCb(String expression) {
        this.cb += expression;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!Objects.equals("", this.vx)) builder.append("vx=").append(this.vx).append(';');
        if (!Objects.equals("", this.vy)) builder.append("vy=").append(this.vy).append(';');
        if (!Objects.equals("", this.vz)) builder.append("vz=").append(this.vz).append(';');
        if (!Objects.equals("", this.cr)) builder.append("cr=").append(this.cr).append(';');
        if (!Objects.equals("", this.cg)) builder.append("cg=").append(this.cg).append(';');
        if (!Objects.equals("", this.cb)) builder.append("cb=").append(this.cb).append(';');
        String result = builder.toString();
        if (result.endsWith(";")) result = result.substring(0, result.length() - 1);

        return "\"" + result + "\"";
    }
}
