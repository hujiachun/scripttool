package com.hjc.scriptutil;

/**
 * Created by hujiachun on 15/12/14.
 */
public class MRInfo {

    public String history;
    public String check;
    public int crash;
    public int anr;
    public int fatal;


    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }



    public int getAnr() {
        return anr;
    }

    public void setAnr(int anr) {
        this.anr = anr;
    }

    public int getCrash() {
        return crash;
    }

    public void setCrash(int crash) {
        this.crash = crash;
    }

    public int getFatal() {
        return fatal;
    }

    public void setFatal(int fatal) {
        this.fatal = fatal;
    }
}
