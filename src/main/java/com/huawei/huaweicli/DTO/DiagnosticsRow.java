package com.huawei.huaweicli.DTO;

import java.util.Objects;

import org.jfree.data.xy.XYSeries;

import com.huawei.huaweicli.utils.DiagnosticsHandler;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DiagnosticsRow {

    private final DiagnosticsHandler.Diagnostics diagnostics;

    private final XYSeries series;

    private final StringProperty rowName;

    private final IntegerProperty immediate;

    private final DoubleProperty perMinuteValue;

    private final DoubleProperty perFiveMinutesValue;

    public DiagnosticsRow(DiagnosticsHandler.Diagnostics diagnostics, XYSeries series, IntegerProperty immediate, DoubleProperty perMinuteValue, DoubleProperty perFiveMinutesValue) {
        this.diagnostics = diagnostics;
        this.series = series;
        this.rowName = new SimpleStringProperty(diagnostics.name());
        this.immediate = immediate;
        this.perMinuteValue = perMinuteValue;
        this.perFiveMinutesValue = perFiveMinutesValue;
    }

    public String getRowName() {
        return rowName.get();
    }

    public StringProperty rowNameProperty() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName.set(rowName);
    }

    public int getImmediate() {
        return immediate.get();
    }

    public IntegerProperty immediateProperty() {
        return immediate;
    }

    public void setImmediate(int immediate) {
        this.immediate.set(immediate);
    }

    public double getPerMinuteValue() {
        return perMinuteValue.get();
    }

    public DoubleProperty perMinuteValueProperty() {
        return perMinuteValue;
    }

    public void setPerMinuteValue(double perMinuteValue) {
        this.perMinuteValue.set(perMinuteValue);
    }

    public double getPerFiveMinutesValue() {
        return perFiveMinutesValue.get();
    }

    public DoubleProperty perFiveMinutesValueProperty() {
        return perFiveMinutesValue;
    }

    public void setPerFiveMinutesValue(double perFiveMinutesValue) {
        this.perFiveMinutesValue.set(perFiveMinutesValue);
    }

    public DiagnosticsHandler.Diagnostics getDiagnostics() {
        return diagnostics;
    }

    public XYSeries getSeries() {
        return series;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagnosticsRow that = (DiagnosticsRow) o;
        return rowName.equals(that.rowName) && immediate.equals(that.immediate) && perMinuteValue.equals(that.perMinuteValue) && perFiveMinutesValue.equals(that.perFiveMinutesValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowName, immediate, perMinuteValue, perFiveMinutesValue);
    }
}
