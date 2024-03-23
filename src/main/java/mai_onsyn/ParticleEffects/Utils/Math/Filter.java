package mai_onsyn.ParticleEffects.Utils.Math;

import mai_onsyn.ParticleEffects.Effects.Effect;
import mai_onsyn.ParticleEffects.Utils.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Filter {

    public static void filterClose(Effect effect) {
        for (int i = 0; i < effect.gettimeline().getSequence().size(); i++) {
            List<Particle> temp = new ArrayList<>();
            for (int j = 0; j < effect.gettimeline().getSequence().get(i).size(); j++) {

                for (int k = 0; k < temp.size(); k++) {
                    if (Objects.equals(effect.gettimeline().getSequence().get(i).get(j), temp.get(k))) {
                        effect.gettimeline().getSequence().get(i).remove(j);
                    }
                }
                temp.add(effect.gettimeline().getSequence().get(i).get(j));
            }
        }
    }

}
