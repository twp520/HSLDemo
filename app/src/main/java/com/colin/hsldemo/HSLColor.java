package com.colin.hsldemo;


import android.support.annotation.ColorInt;

/**
 * create by colin
 * 2020/5/26
 * <p>
 * 颜色实体
 */
public class HSLColor {

    public String name; //color 的名字。算法用。
    @ColorInt
    public int color; //本身的颜色
    public GradientModel hueGradient; //色相滑动条渐变值
    public GradientModel satGradient; //饱和度滑动条渐变值
    public GradientModel ligGradient; //明度滑动条渐变值

    private HSLColor(int color,
                     GradientModel hueGradient,
                     GradientModel satGradient,
                     GradientModel ligGradient) {
        this.color = color;
        this.hueGradient = hueGradient;
        this.satGradient = satGradient;
        this.ligGradient = ligGradient;
    }

    static HSLColor newColor(int color, int hs, int hm, int he,
                             int ss, int sm, int se, int ls, int lm, int le) {
        GradientModel hue = new GradientModel(hs, hm, he);
        GradientModel sat = new GradientModel(ss, sm, se);
        GradientModel lig = new GradientModel(ls, lm, le);
        return new HSLColor(color, hue, sat, lig);
    }

    public static class GradientModel {

        @ColorInt
        public int start;
        @ColorInt
        public int mid;
        @ColorInt
        public int end;

        public int progress; //每个维度的映射给用户的值。取值范围 -100 ~ 100

        public GradientModel(int start, int mid, int end) {
            this.start = start;
            this.mid = mid;
            this.end = end;
        }
    }
}
