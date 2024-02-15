import mai_onsyn.ParticleEffects.ScatterChartGenerator;
import mai_onsyn.ParticleEffects.Utils.Math.Complex;
import mai_onsyn.ParticleEffects.Utils.Math.Fourier;

public class Test01 {

    public static void main(String[] args) {
        double[] arr = new double[1024];
        for (int i = 0; i < 1024; i++) {
            arr[i] = Math.sin(i/10.0);
        }

        Complex[] complexes = Complex.of(arr);

        int N = complexes.length;
        Complex[] FFTed = Fourier.FFT(complexes);

        double[] result = Complex.toModArray(FFTed);

        double A, phi, omega;

        A = 2 * max(result) / N;
        omega = 2 * Math.PI * (double) maxIndex(result) / N;
        phi = arg(max(FFTed));
        System.out.println(String.format("%.5fsin(%.5ft+%.5f)", A, omega, phi));
        ScatterChartGenerator.show(arr);
    }

    public static double max(double[] array) {
        double max = 0;
        for (int i = array.length-1; i >= 0; i--) {
            if (array[i] > max) max = array[i];
        }
        return max;
    }

    public static Complex max(Complex[] array) {
        Complex max = new Complex(0, 0);
        for (int i = array.length - 1; i <= 0; i--) {
            if (array[i].mod() > max.mod()) max = array[i];
        }
        return max;
    }

    public static double arg(Complex complex) {
        return Math.atan2(complex.imag(), complex.real());
    }

    public static int maxIndex(double[] array) {
        int index = -1;
        double max = 0;
        for (int i = 0; i < array.length/2; i++) {
            if (array[i] > max) {
                max = array[i];
                index = i;
            }
        }
        return index;
    }
}
