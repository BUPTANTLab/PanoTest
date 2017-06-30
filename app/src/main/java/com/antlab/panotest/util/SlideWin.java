package com.antlab.panotest.util;


import java.text.DecimalFormat;

public class SlideWin {
    private float[] m_data;
    private int m_size;
    private int m_pos;
    private static final DecimalFormat dFor = new DecimalFormat("0.0");

    public SlideWin(int size) {
        init(size);
    }

    private void init(int size) {
        m_size = size;
        m_data = new float[size];
        m_pos = 0;

    }

    public void add(float d) {
        float ave = ave_int();
        if (ave != 0 && Math.abs(d - ave) > Math.PI / 180 * 15) {
            init(m_size);
        }
        m_data[m_pos] = d;
        m_pos++;
        m_pos %= m_size;
    }

    public String average() {
        return angleFormat(ave_int());
    }

    private float ave_int() {
        float sum = 0;
        int tmp_size = m_size;
        for (float i : m_data) {
            if (i != 0)
                sum += i;
            else
                tmp_size--;
        }
        if (tmp_size == 0)
            return 0;
        return sum / tmp_size;
    }

    private String angleFormat(float angle) {
        angle *= 180 / Math.PI;
        return dFor.format(angle);
    }
}
