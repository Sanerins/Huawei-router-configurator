package com.huawei.huaweicli.DTO;

public class DiagnosticsSnapshot {

    public DiagnosticsSnapshot(Integer rsrp, Integer rsrq, Integer rssi, Integer rscp) {
        this.rsrp = rsrp;
        this.rsrq = rsrq;
        this.rssi = rssi;
        this.rscp = rscp;
    }

    public Integer rsrp;
    public Integer rsrq;
    public Integer rssi;
    public Integer rscp;

}
