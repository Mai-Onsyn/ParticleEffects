package mai_onsyn.ParticleEffects;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Arrays;

public class ScatterChartGenerator {
    public static void show(double[] data) {
        Application.launch(ScatterChartApplication.class, Arrays.toString(data));
    }

    public static class ScatterChartApplication extends Application {
        private double[] data;

        @Override
        public void start(Stage stage) {

            String[] strArr = getParameters().getRaw().get(0).replaceAll("\\[|\\]", "").split(", ");
            data = new double[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                data[i] = Double.parseDouble(strArr[i]);
            }

            NumberAxis xAxis = new NumberAxis();
            NumberAxis yAxis = new NumberAxis();
            ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int i = 0; i < data.length; i++) {
                series.getData().add(new XYChart.Data<>(i, data[i]));
            }
            scatterChart.getData().add(series);
            Scene scene = new Scene(scatterChart, 400, 300);
            stage.setScene(scene);
            stage.show();
        }
    }
}
