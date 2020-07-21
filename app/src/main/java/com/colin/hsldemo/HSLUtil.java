package com.colin.hsldemo;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * create by colin
 * 2020/5/26
 */
public class HSLUtil {

    private static int[][] colors = new int[][]{{223, 32, 32, 223, 32, 175, 223, 32, 32, 223, 128, 32, 140, 115, 115, 190, 64, 64, 242, 13, 13, 38, 13, 13, 190, 64, 64, 242, 217, 217},
            {223, 128, 32, 223, 32, 32, 223, 128, 32, 223, 223, 32, 140, 128, 115, 190, 128, 64, 242, 128, 13, 38, 26, 13, 190, 128, 64, 242, 230, 217},
            {223, 223, 32, 223, 128, 32, 223, 223, 32, 32, 223, 32, 140, 140, 115, 190, 190, 64, 242, 242, 13, 38, 38, 13, 190, 190, 64, 242, 242, 217},
            {32, 223, 32, 223, 223, 32, 32, 223, 32, 32, 223, 223, 115, 140, 115, 64, 190, 64, 13, 242, 13, 13, 38, 13, 64, 190, 64, 217, 242, 217},
            {32, 223, 223, 32, 223, 32, 32, 223, 223, 32, 80, 223, 115, 140, 140, 64, 190, 190, 13, 242, 242, 13, 38, 38, 64, 190, 190, 217, 242, 242},
            {32, 80, 223, 32, 223, 223, 32, 80, 223, 128, 32, 223, 115, 120, 140, 64, 96, 190, 13, 70, 242, 13, 19, 38, 64, 96, 190, 217, 223, 242},
            {128, 32, 223, 32, 80, 223, 128, 32, 223, 223, 32, 175, 128, 115, 140, 128, 64, 190, 128, 13, 242, 26, 13, 38, 128, 64, 190, 230, 217, 242},
            {223, 32, 175, 128, 32, 223, 223, 32, 175, 223, 32, 32, 140, 115, 134, 190, 64, 160, 242, 13, 185, 38, 13, 32, 190, 64, 160, 242, 217, 236}};
    public static String[] colorNames = new String[]{"red", "orange", "yellow", "green", "lightGreen", "blue", "purple", "magenta"};
    public static List<HSLColor> result = new ArrayList<>();

    static {
        for (int i = 0; i < colors.length; i++) {
            int[] hsl = colors[i];
            int j = 0;
            int color = Color.rgb(hsl[j++], hsl[j++], hsl[j++]);
            int hs = Color.rgb(hsl[j++], hsl[j++], hsl[j++]);
            int hm = Color.rgb(hsl[j++], hsl[j++], hsl[j++]);
            int he = Color.rgb(hsl[j++], hsl[j++], hsl[j++]);

            int ss = Color.rgb(hsl[j++], hsl[j++], hsl[j++]);
            int sm = Color.rgb(hsl[j++], hsl[j++], hsl[j++]);
            int se = Color.rgb(hsl[j++], hsl[j++], hsl[j++]);

            int ls = Color.rgb(hsl[j++], hsl[j++], hsl[j++]);
            int lm = Color.rgb(hsl[j++], hsl[j++], hsl[j++]);
            int le = Color.rgb(hsl[j++], hsl[j++], hsl[j]);
            HSLColor hslColor = HSLColor.newColor(color, hs, hm, he, ss, sm, se, ls, lm, le);
            hslColor.name = colorNames[i];
            result.add(hslColor);
        }
    }


}
