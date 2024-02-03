package mai_onsyn.ParticleEffects.EffectUtils;

import mai_onsyn.ParticleEffects.Effects.Continuous.CatmullRom;
import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Static;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static mai_onsyn.ParticleEffects.Static.*;

public class PositionTimeline {

    private final List<Point> sequence;

    private final List<Integer> ticks;

    private final String name;

    private final String player;

    private final File path;

    private List<Point> fullTimeLine;

    private int tps;

    public PositionTimeline(String name, String player, File path, int tps) {
        this.sequence = new ArrayList<>();
        this.ticks = new ArrayList<>();
        this.fullTimeLine = new ArrayList<>();
        this.tps = tps;
        this.name = name;
        this.player = player;
        this.path = path;
    }

    public void add(int tick, double x, double y, double z) {
        sequence.add(new Point(x, y, z));
        ticks.add(tick);
    }

    public void setTps(int tps) {
        this.tps = tps;
    }

    public List<Point> getSequence() {
        return sequence;
    }

    public List<Integer> getTicks() {
        return ticks;
    }

    public void write() {
        this.genFullTimeline();
        final int ticks = fullTimeLine.size();//总大小

        try (FileWriter run/*主运行函数*/ = new FileWriter(new File(this.path, "run_" + this.name.toLowerCase() + ".mcfunction"))) {

            final File outputMainFolder = new File(this.path, this.name.toLowerCase());//主输出路径
            if (!outputMainFolder.exists()) if (!outputMainFolder.mkdir())
                throw new RuntimeException("Failed to create Folder: " + outputMainFolder.getPath());

            for (int i = 0; i < ticks; i += 80) {
                int end = Math.min(i + 80, ticks);
                List<Point> sublist = fullTimeLine.subList(i, end);

                try (FileWriter fi = new FileWriter(new File(outputMainFolder, i + "_" + end + ".mcfunction"))) {

                    for (int j = 0; j < sublist.size(); j++) {
                        fi.write(String.format("execute if score %s %s matches %d run tp %s %.2f %.2f %.2f", TARGET, SCOREBOARD, i + j, this.player, sublist.get(j).x(), sublist.get(j).y(), sublist.get(j).z()));
                        fi.write("\n");
                    }
                }
                run.write(String.format("execute if score %s %s matches %d..%d run function %s:%s/%s", TARGET, SCOREBOARD, i, end, NAMESPACE, this.name.toLowerCase(), i + "_" + end));
                run.write("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Point getIndex(int tick) {
        return this.fullTimeLine.get(tick);
    }

    private void genFullTimeline() {
        List<Point> timeline = new ArrayList<>(ticks.getLast());

        Effect catmullRom = new CatmullRom(Timeline.of(this), sequence.getFirst(), sequence.getLast(), 64, new Particle(null, null, new Color(0)));
        List<List<Particle>> lists = catmullRom.gettimeline().getSequence();

        for (int i = 0; i < lists.size(); i++) {
            timeline.add(lists.get(i).get(0).getPosition());
        }
        timeline.add(sequence.getLast());

        this.fullTimeLine = timeline;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.genFullTimeline();
        builder.append("{");
        for (int i = 0; i < this.fullTimeLine.size(); i++) {
            builder.append("   ");
            builder.append("At ");
            builder.append(i);
            builder.append(": ");
            builder.append(this.fullTimeLine.get(i));
            builder.append("\n");
        }
        builder.append("}");
        return builder.toString();
    }
}
