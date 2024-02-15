import mai_onsyn.ParticleEffects.ScatterChartGenerator;

import java.util.Arrays;

import static java.lang.Math.*;

public class Test03 {

    public static void main(String[] args) {

        int N = 1024;

        double[] data = new double[N];
        double A = 10, w = 1, phi = 1;

        for (int i = 0; i < N; i++) {
            data[i] = A * sin(2.0 * i / N * PI + phi);
        }


        double A1 = 0;
        int maxIndex = -1;
        for (int i = 0; i < N; i++) {
            if (data[i] > A1) {
                A1 = data[i];
                maxIndex = i;
            }
        }

        double phi1 = 0;
        double l = (maxIndex - N * 0.25 < 0) ? maxIndex + N * 0.75 : maxIndex - N * 0.25;
        phi1 = 2 * PI - l / N * 2 * PI;

        System.out.println(A1);
        System.out.println(phi1);

        ScatterChartGenerator.show(data);
    }
}
