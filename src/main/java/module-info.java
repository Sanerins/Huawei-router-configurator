module com.huawei.huaweicli {
    requires com.google.common;
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.web;
    requires com.fasterxml.jackson.core;
    requires java.xml;
    requires org.jfree.jfreechart;
    requires java.desktop;
    requires org.jfree.fxgraphics2d;
    requires org.jfree.chart.fx;


    opens com.huawei.huaweicli to javafx.fxml;
    exports com.huawei.huaweicli;
    exports com.huawei.huaweicli.controller;
    opens com.huawei.huaweicli.controller to javafx.fxml;

    exports com.huawei.huaweicli.DTO;
    exports com.huawei.huaweicli.utils;
    opens com.huawei.huaweicli.DTO to javafx.fxml;
    opens com.huawei.huaweicli.utils to javafx.fxml;
}