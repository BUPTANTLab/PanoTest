package com.antlab.panotest.util;


import java.text.DecimalFormat;

public class SlideWin {
    private float[] m_data;
    private int m_size;
    private int m_pos;
    private static final DecimalFormat dFor = new DecimalFormat("0.0");

    public SlideWin(int size) {
        m_size = size;
        m_data = new float[size];
        m_pos = 0;
    }

    public void add(float d) {
        m_data[m_pos] = d;
        m_pos++;
        m_pos %= m_size;
    }

    public String average() {
        float sum = 0;
        for (float i : m_data) {
            sum += i;
        }
        return angleFormat(sum / m_size);
    }

    private String angleFormat(float angle) {
        angle *= 180 / Math.PI;
        return dFor.format(angle);
    }
}
