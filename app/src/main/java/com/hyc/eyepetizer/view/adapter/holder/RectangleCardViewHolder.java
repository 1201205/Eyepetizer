package com.hyc.eyepetizer.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.R;

/**
 * Created by ray on 16/9/25.
 */
public class RectangleCardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.sdv_img)
    public SimpleDraweeView sdvImage;


    public RectangleCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
