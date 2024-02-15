package mai_onsyn.ParticleEffects.EffectUtils;

import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Math.Point;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Timeline {
    private final File FUNCTION_PATH;
    private final int COMMANDS_PER_FUNCTION;
    private final int START_INDEX;
    private final String NAME;
    private final String NAMESPACE;
    private final String TARGET;
    private final String SCOREBOARD;
    private final List<List<Particle>> sequence;
    private final List<List<String>> cmdSequence;

    //主要时间线格式
    public Timeline(String name, File path, int count, int startIndex, String namespace, String scoreboard, String target) {
        this.NAME = name;
        this.FUNCTION_PATH = path;
        this.COMMANDS_PER_FUNCTION = count;
        this.START_INDEX = startIndex;
        this.NAMESPACE = namespace;
        this.SCOREBOARD = scoreboard;
        this.TARGET = target;
        this.sequence = new ArrayList<>();
        this.cmdSequence = new ArrayList<>();
    }

    //图形使用格式 用于添加到主时间线
    public Timeline() {
        this.NAME = null;
        this.FUNCTION_PATH = null;
        this.COMMANDS_PER_FUNCTION = 0;
        this.START_INDEX = 0;
        this.NAMESPACE = null;
        this.SCOREBOARD = null;
        this.TARGET = null;
        this.sequence = new ArrayList<>();
        this.cmdSequence = new ArrayList<>();
    }

    public void add(int tick, Particle particle) {
        while (cmdSequence.size() <= tick) this.cmdSequence.add(new ArrayList<>());
        while (sequence.size() <= tick) this.sequence.add(new ArrayList<>());

        this.sequence.get(tick).add(particle);
    }

    public void add(int tick, Effect effect) {
        List<List<Particle>> src = effect.gettimeline().getSequence();

        int count = src.size();
        while (cmdSequence.size() <= tick + count - 1) this.cmdSequence.add(new ArrayList<>());
        while (sequence.size() <= tick + count - 1) this.sequence.add(new ArrayList<>());
        for (int i = 0; i < count; i++) {
            this.sequence.get(tick + i).addAll(src.get(i));
        }
    }

    public void add(int tick, Point point) {
        while (cmdSequence.size() <= tick) this.cmdSequence.add(new ArrayList<>());
        while (sequence.size() <= tick) this.sequence.add(new ArrayList<>());

        this.sequence.get(tick).add(new Particle(null, point, null, 0, 0, 0, null, null));
    }

    public void add(int tick, String command) {
        while (cmdSequence.size() <= tick) this.cmdSequence.add(new ArrayList<>());
        while (sequence.size() <= tick) this.sequence.add(new ArrayList<>());

        this.cmdSequence.get(tick).add(command);
    }

    public void write() {

        if (this.NAME == null) throw new RuntimeException("Not main timeline!");

        final int ticks = sequence.size();//总大小
        List<List<String>> totalCommands = new ArrayList<>(ticks);
        while (totalCommands.size() <= ticks) totalCommands.add(new ArrayList<>());
        for (int i = 0; i < ticks; i++) {
            List<String> subCmd = cmdSequence.get(i);
            List<Particle> subParticle = sequence.get(i);
            totalCommands.get(i).addAll(subCmd);
            for (Particle particle : subParticle) {
                totalCommands.get(i).add(particle.toString());
            }
        }

        try (FileWriter run/*主运行函数*/ = new FileWriter(new File(this.FUNCTION_PATH, "run_" + this.NAME.toLowerCase() + ".mcfunction"))) {

            final File outputMainFolder = new File(this.FUNCTION_PATH, this.NAME.toLowerCase());//主输出路径
            if (!outputMainFolder.exists()) if (!outputMainFolder.mkdir())
                throw new RuntimeException("Failed to create Folder: " + outputMainFolder.getPath());

            for (int tick = 0; tick < ticks; tick++) {
                List<String> group = totalCommands.get(tick);//每tick的粒子
                final int groupSize = group.size();
                if (groupSize == 0) continue;

                final File tickFolder = new File(outputMainFolder, "tick_" + tick);//每tick粒子存储路径
                if (!tickFolder.exists()) if (!tickFolder.mkdir())
                    throw new RuntimeException("Failed to create Folder: " + tickFolder.getPath());

                for (int i = 0; i < groupSize; i += this.COMMANDS_PER_FUNCTION) {
                    int end = Math.min(i + this.COMMANDS_PER_FUNCTION, groupSize);
                    List<String> sublist = group.subList(i, end);

                    try (FileWriter writer/*每tick粒子命令写入*/ = new FileWriter(new File(tickFolder, i + ".mcfunction"))) {

                        for (int j = 0; j < sublist.size(); j++) {
                            writer.write(group.get(i + j));
                            writer.write("\n");
                        }
                    }
                    run.write(String.format("execute if score %s %s matches %d run function %s:%s/tick_%d/%d", this.TARGET, this.SCOREBOARD, tick + this.START_INDEX, this.NAMESPACE, this.NAME.toLowerCase(), tick, i));
                    run.write("\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<List<Particle>> getSequence() {
        return this.sequence;
    }

    public int totalCount() {
        int count = 0;
        for (List<Particle> sub : sequence) {
            count += sub.size();
        }
        return count;
    }

    public static Timeline of(PositionTimeline ptl) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < ptl.getTicks().size(); i++) {
            timeline.add(ptl.getTicks().get(i), ptl.getSequence().get(i));
        }
        return timeline;
    }
}