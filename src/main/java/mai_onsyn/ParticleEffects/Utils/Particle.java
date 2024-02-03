package mai_onsyn.ParticleEffects.Utils;

import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Math.Vector;

import java.awt.*;
import java.util.Objects;
import java.util.UUID;

public class Particle {

    public static final String EXPLOSION_EMITTER = "explosion_emitter";
    public static final String EXPLOSION = "explosion";
    public static final String BLUE_SLIME = "instant_effect";
    public static final String GREEN_SLIME = "firework";
    public static final String CROSSHAIRS = "visuality:soul";
    public static final String EYE_BIG = "sweep_attack";
    public static final String EYE_MEDIUM = "barrier";
    public static final String SMOKE = "visuality:sparkle";
    public static final String SOLAR = "visuality:bone";
    public static final String VORTEX = "visuality:feather";
    public static final String NEBULA = "visuality:emerald";
    public static final String STARDUST = "visuality:wither_bone";
    public static final String END_ROD = "end_rod";
    public static final String GRAPHIC_SMALL = "snowflake";
    public static final String GRAPHIC_BIG = "squid_ink";
    public static final String  DIRECTIONAL = "arrow";


    private final String NAME;
    private final mai_onsyn.ParticleEffects.Utils.Math.Point POSITION;
    private final Color COLOR;
    private final int COUNT;
    private final int LIFE;
    private final Expression EXPRESSION;
    private String GROUP;
    private final double RANGE;

    public Particle(String name, Point position, Color color, double range, int count, int life, Expression expression, String group) {
        this.NAME = name;
        this.POSITION = position;
        this.COLOR = color;
        this.COUNT = count;
        this.LIFE = life;
        this.EXPRESSION = expression;
        this.GROUP = group;
        this.RANGE = range;
    }

    public Particle(String name, Point position, Color color) {
        this.NAME = name;
        this.POSITION = position;
        this.COLOR = color;
        this.COUNT = 1;
        this.LIFE = 0;
        this.EXPRESSION = new Expression("", "", "");
        this.GROUP = "null";
        this.RANGE = 0;
    }

    public Particle(String name, Point position, Vector v) {
        this.NAME = name;
        this.POSITION = position;
        this.COLOR = null;
        this.COUNT = 0;
        this.LIFE = 0;
        this.EXPRESSION = new Expression(String.valueOf(v.vx()), String.valueOf(v.vy()), String.valueOf(v.vz()));
        this.GROUP = null;
        this.RANGE = 0;
    }

    @Override
    public String toString() {
        if (Objects.equals(this.NAME, "arrow")) {
            String uuid = UUID.randomUUID().toString();
            return String.format("summon arrow %.4f %.4f %.4f {NoGravity:1b,Tags:[\"%s\"]}\ndata modify entity @e[tag=%s,limit=1] Motion set value [%.5fd,%.5fd,%.5fd]",
                    this.POSITION.x(), this.POSITION.y(), this.POSITION.z(),
                    uuid, uuid,
                    Double.valueOf(this.EXPRESSION.vx()), Double.valueOf(this.EXPRESSION.vy()), Double.valueOf(this.EXPRESSION.vz())
            );
        }

        if (this.RANGE == 0) {
            return String.format("particleex normal %s %.4f %.4f %.4f %.4f %.4f %.4f %.4f 0 0 0 0 0 0 %d %d %s 1 %s",
                    this.NAME,
                    this.POSITION.x(), this.POSITION.y(), this.POSITION.z(),
                    this.COLOR.getRed() / 256.0, this.COLOR.getGreen() / 256.0, this.COLOR.getBlue() / 256.0, this.COLOR.getAlpha() / 256.0,
                    this.COUNT,
                    this.LIFE,
                    this.EXPRESSION.toString(),
                    this.GROUP
            );
        }


        return String.format("particleex normal %s %.4f %.4f %.4f %.4f %.4f %.4f %.4f 0 0 0 %.4f %.4f %.4f %d %d %s 1 %s",
                this.NAME,
                this.POSITION.x(), this.POSITION.y(), this.POSITION.z(),
                this.COLOR.getRed() / 256.0, this.COLOR.getGreen() / 256.0, this.COLOR.getBlue() / 256.0, this.COLOR.getAlpha() / 256.0,
                this.RANGE, this.RANGE, this.RANGE,
                this.COUNT,
                this.LIFE,
                this.EXPRESSION.toString(),
                this.GROUP
        );
    }

    public Point getPosition() {
        return POSITION;
    }

    public Expression getExpression() {
        return EXPRESSION;
    }

    public String getName() {
        return NAME;
    }

    public Color getColor() {
        return COLOR;
    }

    public int getCount() {
        return COUNT;
    }

    public int getLife() {
        return LIFE;
    }

    public String getGroup() {
        return GROUP;
    }

    public double getRange() {
        return RANGE;
    }

    public Particle setGroup(String group) {
        this.GROUP = group;
        return this;
    }
}
