package com.colin.hsldemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * create by colin
 * 2020/5/26
 */
class HSLColorViewHolder extends RecyclerView.ViewHolder {

    private View colorView;
    private OnItemClickListener itemClickListener;

    HSLColorViewHolder(@NonNull View itemView) {
        super(itemView);
        colorView = itemView.findViewById(R.id.item_hsl_view);
    }

    void bindData(HSLColor hslColor) {
        colorView.setBackgroundColor(hslColor.color);
        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null)
                    itemClickListener.itemClick(getAdapterPosition());
            }
        });
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void itemClick(int position);
    }
}
