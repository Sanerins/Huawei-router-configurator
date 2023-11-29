package com.huawei.huaweicli.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartCanvas;
import org.jfree.data.xy.XYSeriesCollection;

import com.huawei.huaweicli.DTO.DiagnosticsRow;
import com.huawei.huaweicli.utils.ChartGenerator;
import com.huawei.huaweicli.utils.DiagnosticsHandler;

import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class DataController implements Initializable {
    @javafx.fxml.FXML
    private TableView<DiagnosticsRow> dataTable;
    @javafx.fxml.FXML
    private TableColumn<DiagnosticsRow, String> nameCol;
    @javafx.fxml.FXML
    private TableColumn<DiagnosticsRow, Integer> immediateCol;
    @javafx.fxml.FXML
    private TableColumn<DiagnosticsRow, Double> perMinuteCol;
    @javafx.fxml.FXML
    private TableColumn<DiagnosticsRow, Double> perFiveMinCol;
    @javafx.fxml.FXML
    private VBox mainBox;

    private DiagnosticsHandler handler;

    private AnimationTimer timer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("rowName"));
        immediateCol.setCellValueFactory(new PropertyValueFactory<>("immediate"));
        perMinuteCol.setCellValueFactory(new PropertyValueFactory<>("perMinuteValue"));
        perFiveMinCol.setCellValueFactory(new PropertyValueFactory<>("perFiveMinutesValue"));

        XYSeriesCollection dataset = new XYSeriesCollection();
        handler = new DiagnosticsHandler(dataset);
        dataTable.setItems(handler.getRows());

        timer = new AnimationTimer() {
            private long lastTime = System.nanoTime();

            @Override
            public void handle(long l) {
                long currTime = System.nanoTime();
                if (TimeUnit.MILLISECONDS.convert(currTime - lastTime, TimeUnit.NANOSECONDS) < DiagnosticsHandler.REFRESH_TIME_MS) {
                    return;
                }
                handler.iterate();
                lastTime = System.nanoTime();
            }
        };
        timer.start();
        JFreeChart chart = ChartGenerator.createChart(dataset);
        ChartCanvas viewer = new ChartCanvas(chart);
        mainBox.getChildren().add(viewer);
        viewer.widthProperty().bind(mainBox.widthProperty());
        viewer.heightProperty().bind(mainBox.heightProperty().divide(1.5));
    }
}
