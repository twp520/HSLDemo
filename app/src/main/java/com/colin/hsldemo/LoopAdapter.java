package com.colin.hsldemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * create by colin
 * 2020/5/26
 */
public class LoopAdapter extends RecyclerView.Adapter<HSLColorViewHolder> {

    private List<HSLColor> mData = new ArrayList<>();
    private HSLColorViewHolder.OnItemClickListener itemClickListener;

    @NonNull
    @Override
    public HSLColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HSLColorViewHolder colorViewHolder = new HSLColorViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hsl_color, parent, false));
        colorViewHolder.setItemClickListener(itemClickListener);
        return colorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HSLColorViewHolder holder, int position) {
        holder.bindData(mData.get(position % mData.size()));
    }

    void setNewData(@NonNull List<HSLColor> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public List<HSLColor> getData() {
        return mData;
    }

    void setItemClickListener(HSLColorViewHolder.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mData.isEmpty())
            return 0;
        return 20000;
    }
}
