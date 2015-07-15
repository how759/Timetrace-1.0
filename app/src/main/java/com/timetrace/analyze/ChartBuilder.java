package com.timetrace.analyze;

import android.graphics.Color;

public class ChartBuilder {
    public static int[] colors = new int[] { Color.rgb(0xa4, 0xc4, 0x00), Color.rgb(0x00, 0xaa, 0x00),
            Color.rgb(0x83, 0xcd, 0xe6),Color.rgb(0x00, 0xa1, 0xe9),
            Color.rgb(0xb0, 0xe0, 0xe6),Color.rgb(0xf0, 0xa3, 0x0a),
            Color.rgb(0x6a, 0x00, 0xff), Color.rgb(0xaa, 0x00, 0xff),
            Color.rgb(0x60, 0xa9, 0x17), Color.rgb(0x1b, 0xa1, 0xe2),
            Color.rgb(0xa2, 0x00, 0x25), Color.rgb(0xe5, 0x14, 0x00),
            Color.rgb(0x00, 0xab, 0xa9), Color.rgb(0x00, 0x50, 0xef),
            Color.rgb(0xe3, 0xc8, 0x00), Color.rgb(0x82, 0x5a, 0x2c),
            Color.rgb(0x6d, 0x87, 0x64), Color.CYAN};

    //设置tag
    public static String[] apptag ={"life_style","social","information","entertainmeng","work"};

    public static String[] acttag = {"unknown","eat","sleep","work_study","motion","relax","trifle","communication",
                                       "game","information","social_network"};
}
