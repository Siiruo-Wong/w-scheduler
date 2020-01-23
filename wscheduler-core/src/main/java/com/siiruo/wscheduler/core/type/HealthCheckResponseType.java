package com.siiruo.wscheduler.core.type;


/**
 * Created by siiruo wong on 2020/1/22.
 */
public class HealthCheckResponseType extends ResponseType {
    private double cpu;
    private double memUsed;
    private double memMax;
    private double memInit;


    public HealthCheckResponseType() {
    }

    public HealthCheckResponseType(ResultType result) {
        super(result);
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(double memUsed) {
        this.memUsed = memUsed;
    }

    public double getMemMax() {
        return memMax;
    }

    public void setMemMax(double memMax) {
        this.memMax = memMax;
    }

    public double getMemInit() {
        return memInit;
    }

    public void setMemInit(double memInit) {
        this.memInit = memInit;
    }
}
