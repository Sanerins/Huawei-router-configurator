package com.huawei.huaweicli.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.huawei.huaweicli.DTO.DiagnosticsRow;
import com.huawei.huaweicli.DTO.DiagnosticsSnapshot;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DiagnosticsHandler {
    public enum Diagnostics {
        RSRP, RSRQ, RSSI, RSCP
    }

    private final ObservableList<DiagnosticsRow> rows = FXCollections.observableArrayList();

    public static final Integer REFRESH_TIME_MS = 1000;

    public static final Long FOR_5_MINS = TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES) / REFRESH_TIME_MS;
    public static final Long FOR_1_MIN = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES) / REFRESH_TIME_MS;

    private long iteration = 0;
    private List<Integer> rsrpValues = new ArrayList<>();
    private List<Integer> rsrqValues = new ArrayList<>();
    private List<Integer> rssiValues = new ArrayList<>();
    private List<Integer> rscpValues = new ArrayList<>();

    public DiagnosticsHandler(XYSeriesCollection dataset) {
        rows.add(new DiagnosticsRow(Diagnostics.RSRP, new XYSeries("RSRP"), new SimpleIntegerProperty(0), new SimpleDoubleProperty(0), new SimpleDoubleProperty(0)));
        rows.add(new DiagnosticsRow(Diagnostics.RSRQ, new XYSeries("RSRQ"), new SimpleIntegerProperty(0), new SimpleDoubleProperty(0), new SimpleDoubleProperty(0)));
        rows.add(new DiagnosticsRow(Diagnostics.RSSI, new XYSeries("RSSI"), new SimpleIntegerProperty(0), new SimpleDoubleProperty(0), new SimpleDoubleProperty(0)));
        rows.add(new DiagnosticsRow(Diagnostics.RSCP, new XYSeries("RSCP"), new SimpleIntegerProperty(0), new SimpleDoubleProperty(0), new SimpleDoubleProperty(0)));
        for (DiagnosticsRow row : rows) {
            dataset.addSeries(row.getSeries());
        }
        iterate();
    }

    public ObservableList<DiagnosticsRow> getRows() {
        return rows;
    }

    public void iterate() {
        DiagnosticsSnapshot snapshot = RequestHandler.getDataSnapshot();

        rsrpValues.add(snapshot.rsrp);
        rsrqValues.add(snapshot.rsrq);
        rssiValues.add(snapshot.rssi);
        rscpValues.add(snapshot.rscp);

        for (DiagnosticsRow row : rows) {
            row.getSeries().add(Double.valueOf(iteration), getImmediate(row.getDiagnostics()));
            row.setImmediate(getImmediate(row.getDiagnostics()));
            row.setPerMinuteValue(getPerMin(row.getDiagnostics()));
            row.setPerFiveMinutesValue(getPerFiveMins(row.getDiagnostics()));
        }
        iteration++;
    }

    private Integer getImmediate(Diagnostics diagnostics) {
        List<Integer> target = getList(diagnostics);
        return target.get(target.size() - 1);
    }

    private Double getPerMin(Diagnostics diagnostics) {
        List<Integer> target = getList(diagnostics);
        return target.size() - FOR_1_MIN < 0 ?
                target.stream().mapToDouble(a -> a).average().orElseThrow()
                :
                target.stream().skip(target.size() - FOR_1_MIN).mapToDouble(a -> a).average().orElseThrow();
    }

    private Double getPerFiveMins(Diagnostics diagnostics) {
        List<Integer> target = getList(diagnostics);
        if (target.size() - FOR_5_MINS > 0) {
            setList(diagnostics, target.stream().skip(target.size() - FOR_5_MINS).collect(Collectors.toList()));
        }
        return target.stream().mapToDouble(a -> a).average().orElseThrow();
    }

    private void setList(Diagnostics diagnostics, List<Integer> integers) {
        switch (diagnostics) {
            case RSCP -> {
                rscpValues = integers;
            }
            case RSRP -> {
                rsrpValues = integers;
            }
            case RSRQ -> {
                rsrqValues = integers;
            }
            case RSSI -> {
                rssiValues = integers;
            }
            default -> throw new RuntimeException();
        }
    }

    private List<Integer> getList(Diagnostics diagnostics) {
        switch (diagnostics) {
            case RSCP -> {
                return rscpValues;
            }
            case RSRP -> {
                return rsrpValues;
            }
            case RSRQ -> {
                return rsrqValues;
            }
            case RSSI -> {
                return rssiValues;
            }
            default -> throw new RuntimeException();
        }
    }
}
