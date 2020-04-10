package com.hm.viewdemo.bean;

/**
 * Created by dumingwei on 2020/4/8
 * <p>
 * Desc: 时长节点
 */
public class SendOptionsBean {
    /**
     * seg : 0
     * segKey : 0.5
     * segValue : 20
     * status : 0
     */

    public int seg;
    public double segKey;
    public int segValue;
    //状态，0-不可领，1-已领，2-可领
    public int status;

    public int getSeg() {
        return seg;
    }

    public void setSeg(int seg) {
        this.seg = seg;
    }

    public double getSegKey() {
        return segKey;
    }

    public void setSegKey(double segKey) {
        this.segKey = segKey;
    }

    public int getSegValue() {
        return segValue;
    }

    public void setSegValue(int segValue) {
        this.segValue = segValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean canGet() {
        return status == 2;
    }

    public boolean haveGot() {
        return status == 1;
    }
}
