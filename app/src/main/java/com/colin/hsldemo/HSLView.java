package com.colin.hsldemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

/**
 * create by colin
 * 2020/5/26
 * <p>
 * 调整HSL的控件
 */
public class HSLView extends LinearLayout implements HSLLayoutManager1.SelectMidListener, SeekBar.OnSeekBarChangeListener, HSLColorViewHolder.OnItemClickListener {

    private LoopAdapter mColorAdapter;
    private HSLLayoutManager1 layoutManager;
    private GradientSeekBar mSeekHue, mSeekSat, mSeekLig;
    private TextView mTextHue, mTextSat, mTextLig;
    private ProgressMappingListener mappingListener;
    private HSLColor mCurrentColor;
    private String hue, sat, light;

    public HSLView(Context context) {
        this(context, null);
    }

    public HSLView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        RecyclerView mColorRecyclerView = new RecyclerView(context);
        ImageView cursor = new ImageView(context);
        cursor.setImageResource(R.drawable.ic_hsl_cursor);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        addView(cursor);
        mColorAdapter = new LoopAdapter();
        layoutManager = new HSLLayoutManager1(context);
        layoutManager.setSelectMidListener(this);
        mColorAdapter.setItemClickListener(this);
        mColorRecyclerView.setLayoutManager(layoutManager);
        mColorRecyclerView.setAdapter(mColorAdapter);
        LayoutParams rlp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        rlp.setMargins(30, 0, 0, 0);
        addView(mColorRecyclerView, rlp);

        setClipChildren(false);
        setClipToPadding(false);

        View seekView = LayoutInflater.from(context).inflate(R.layout.view_hsl_seek, this, false);
        mSeekHue = seekView.findViewById(R.id.view_hsl_seek_hue);
        mSeekSat = seekView.findViewById(R.id.view_hsl_seek_sat);
        mSeekLig = seekView.findViewById(R.id.view_hsl_seek_lig);

        mTextHue = seekView.findViewById(R.id.view_hsl_tv_hue);
        mTextSat = seekView.findViewById(R.id.view_hsl_tv_sat);
        mTextLig = seekView.findViewById(R.id.view_hsl_tv_lig);

        mSeekHue.setOnSeekBarChangeListener(this);
        mSeekSat.setOnSeekBarChangeListener(this);
        mSeekLig.setOnSeekBarChangeListener(this);

        hue = context.getString(R.string.adjust_hsl_hue);
        sat = context.getString(R.string.adjust_hsl_sat);
        light = context.getString(R.string.adjust_hsl_light);
        //设置默认为0
        setProgress(100, 0, mSeekHue, mTextHue, hue);
        setProgress(100, 0, mSeekSat, mTextSat, sat);
        setProgress(100, 0, mSeekLig, mTextLig, light);
        addView(seekView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    public void setColorData(@NonNull List<HSLColor> colorData) {
        mColorAdapter.setNewData(colorData);
        layoutManager.setRealSize(colorData.size());
    }


    /**
     * @param position 当停下的时候选中的中间的 position。
     *                 这个position会比data的size大。因此真正使用的时候要模除data.size
     */
    @Override
    public void onSelected(int position) {
        //通过position 取得 data，设置给进度条。
        HSLColor hslColor = mColorAdapter.getData().get(position % mColorAdapter.getData().size());
        mCurrentColor = hslColor;
        mSeekHue.setGradientModel(hslColor.hueGradient);
        mSeekSat.setGradientModel(hslColor.satGradient);
        mSeekLig.setGradientModel(hslColor.ligGradient);

        if (mappingListener != null) {
            int hueProgress = mappingListener.onHSLMappingRevert(hslColor.hueGradient.progress);
            int satProgress = mappingListener.onHSLMappingRevert(hslColor.satGradient.progress);
            int ligProgress = mappingListener.onHSLMappingRevert(hslColor.ligGradient.progress);
            setProgress(hueProgress, hslColor.hueGradient.progress, mSeekHue, mTextHue, hue);
            setProgress(satProgress, hslColor.satGradient.progress, mSeekSat, mTextSat, sat);
            setProgress(ligProgress, hslColor.ligGradient.progress, mSeekLig, mTextLig, light);
        }
    }

    public void clearValue() {
        for (HSLColor hslColor : mColorAdapter.getData()) {
            hslColor.hueGradient.progress = 0;
            hslColor.satGradient.progress = 0;
            hslColor.ligGradient.progress = 0;
        }
    }

    private void setProgress(int seekProgress, int mappingProgress,
                             @NonNull SeekBar seekBar, @NonNull TextView textView, String text) {
        seekBar.setProgress(seekProgress);
        setProgressText(textView, mappingProgress, text);
    }

    private void setProgressText(@NonNull TextView textView, int mappingProgress, String text) {
        String progressText = mappingProgress > 0 ? "+" + mappingProgress : String.valueOf(mappingProgress);
        textView.setText(String.format("%s %s", text, progressText));
    }

    public void setMappingListener(ProgressMappingListener mappingListener) {
        this.mappingListener = mappingListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mappingListener != null) {
            int mappingProgress = mappingListener.onHSLMapping(progress);
            if (seekBar == mSeekHue) {
                mappingListener.onHSLHueProgressChanged(mCurrentColor.name, mappingProgress, fromUser);
                setProgressText(mTextHue, mappingProgress, hue);
                mCurrentColor.hueGradient.progress = mappingProgress;
            } else if (seekBar == mSeekSat) {
                mappingListener.onHSLSatProgressChanged(mCurrentColor.name, mappingProgress, fromUser);
                setProgressText(mTextSat, mappingProgress, sat);
                mCurrentColor.satGradient.progress = mappingProgress;
            } else {
                mappingListener.onHSLLigProgressChanged(mCurrentColor.name, mappingProgress, fromUser);
                setProgressText(mTextLig, mappingProgress, light);
                mCurrentColor.ligGradient.progress = mappingProgress;
            }
        }
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void itemClick(int position) {
        layoutManager.smoothScrollToPositionMid(position);
    }

    public interface ProgressMappingListener {

        void onHSLHueProgressChanged(String name, int progress, boolean fromUser);

        void onHSLSatProgressChanged(String name, int progress, boolean fromUser);

        void onHSLLigProgressChanged(String name, int progress, boolean fromUser);

        /**
         * 将 seekBar 的进度映射为用户看到的数字。 -100 ~ 100
         *
         * @param seekBarProgress seekBar的进度 0~200
         * @return 用户看到的数字
         */
        int onHSLMapping(int seekBarProgress);

        /**
         * 将用户看到的数字，映射为seekBar的进度。
         *
         * @param hslProgress 用户看到的数字 -100 ~ 100
         * @return seekBar的进度 0~200
         */
        int onHSLMappingRevert(int hslProgress);
    }
}
