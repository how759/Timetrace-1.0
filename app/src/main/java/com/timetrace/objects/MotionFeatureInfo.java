package com.timetrace.objects;

/**
 * Created by Atomu on 2014/12/2.
 * motionInfo 的统计 feature
 */
public class MotionFeatureInfo {
    /**
     * acc x, y, z 均值
     */
    private float[] accMean;
    /**
     * acc x, y, z 标准差
     */
    private float[] accSD;
    /**
     * acc xy, yz, xz 协方差
     */
    private float[] accCorr;
    /**
     * acc x, y, z 积分
     */
    private float[] accSma;
    /**
     * al first, middle, last
     */
    private float[] al;
    /**
     * acc x 时序
     */
    private float[] coefX;
    /**
     * acc y 时序
     */
    private float[] coefY;
    /**
     * acc z 时序
     */
    private float[] coefZ;

    public float[] getAccMean() {
        return accMean;
    }

    public void setAccMean(float[] accMean) {
        this.accMean = accMean;
    }

    public float[] getAccSD() {
        return accSD;
    }

    public void setAccSD(float[] accSD) {
        this.accSD = accSD;
    }

    public float[] getAccCorr() {
        return accCorr;
    }

    public void setAccCorr(float[] accCorr) {
        this.accCorr = accCorr;
    }

    public float[] getAccSma() {
        return accSma;
    }

    public void setAccSma(float[] accSma) {
        this.accSma = accSma;
    }

    public float[] getAl() {
        return al;
    }

    public void setAl(float[] al) {
        this.al = al;
    }

    public float[] getCoefX() {
        return coefX;
    }

    public void setCoefX(float[] coefX) {
        this.coefX = coefX;
    }

    public float[] getCoefY() {
        return coefY;
    }

    public void setCoefY(float[] coefY) {
        this.coefY = coefY;
    }

    public float[] getCoefZ() {
        return coefZ;
    }

    public void setCoefZ(float[] coefZ) {
        this.coefZ = coefZ;
    }
}
