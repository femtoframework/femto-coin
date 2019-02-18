package org.femtoframework.bean.info.ext;

import org.femtoframework.bean.annotation.Coined;

@Coined(alphabeticOrder = true)
public class AlphabeticProperties {

    private String b;

    private int a;

    private boolean c;

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public boolean isC() {
        return c;
    }

    public void setC(boolean c) {
        this.c = c;
    }
}
