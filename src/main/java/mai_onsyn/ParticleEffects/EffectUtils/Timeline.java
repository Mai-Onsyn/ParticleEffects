package mai_onsyn.ParticleEffects.EffectUtils;

import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static mai_onsyn.ParticleEffects.Static.*;

public class Timeline {
    private final File FUNCTION_PATH;
    private final int COMMANDS_PER_FUNCTION;
    private final int START_INDEX;
    private final String NAME;
    private final List<List<Particle>> sequence;

    //主要时间线格式
    public Timeline(String name, File path, int count, int startIndex) {
        this.NAME = name;
        this.FUNCTION_PATH = path;
        this.COMMANDS_PER_FUNCTION = count;
        this.START_INDEX = startIndex;
        this.sequence = new ArrayList<>();
    }

    //图形使用格式 用于添加到主时间线
    public Timeline() {
        this.NAME = "";
        this.FUNCTION_PATH = null;
        this.COMMANDS_PER_FUNCTION = 0;
        this.START_INDEX = 0;
        this.sequence = new ArrayList<>();
    }

    public void add(int tick, Particle particle) {
        while (sequence.size() <= tick) {
            this.sequence.add(new ArrayList<>());
        }

        this.sequence.get(tick).add(particle);
    }

    public void add(int tick, Effect effect) {
        List<List<Particle>> src = effect.gettimeline().getSequence();

        int count = src.size();
        for (int i = 0; i < count; i++) {
            while (sequence.size() <= tick + i) {
                this.sequence.add(new ArrayList<>());
            }
            this.sequence.get(tick + i).addAll(src.get(i));
        }
    }

    public void write() {
        final int ticks = sequence.size();//总大小

        try (FileWriter run/*主运行函数*/ = new FileWriter(new File(this.FUNCTION_PATH, "run_" + this.NAME.toLowerCase() + ".mcfunction"))) {

            final File outputMainFolder = new File(this.FUNCTION_PATH, this.NAME.toLowerCase());//主输出路径
            if (!outputMainFolder.exists()) if (!outputMainFolder.mkdir())
                throw new RuntimeException("Failed to create Folder: " + outputMainFolder.getPath());

            for (int tick = 0; tick < ticks; tick++) {
                List<Particle> group = sequence.get(tick);//每tick的粒子
                final int groupSize = group.size();
                if (groupSize == 0) continue;

                final File tickFolder = new File(outputMainFolder, "tick_" + tick);//每tick粒子存储路径
                if (!tickFolder.exists()) if (!tickFolder.mkdir())
                    throw new RuntimeException("Failed to create Folder: " + tickFolder.getPath());

                for (int i = 0; i < groupSize; i += this.COMMANDS_PER_FUNCTION) {

                    try (FileWriter writer/*每tick粒子命令写入*/ = new FileWriter(new File(tickFolder, i + ".mcfunction"))) {
                        for (int j = 0; (groupSize <= this.COMMANDS_PER_FUNCTION) ? (j < groupSize) : (j < this.COMMANDS_PER_FUNCTION)/*拆分function文件*/; j++) {
                            writer.write(group.get(i + j).toString());
                            writer.write("\n");
                        }
                        run.write(String.format("execute if score %s %s matches %d run function %s/tick_%d/%d", TARGET, SCOREBOARD, tick + this.START_INDEX, this.NAME.toLowerCase(), tick, i));
                        run.write("\n");
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<List<Particle>> getSequence() {
        return this.sequence;
    }
}