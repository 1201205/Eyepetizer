package com.hyc.eyepetizer;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyc.eyepetizer.beans.ViewData;
import com.hyc.eyepetizer.utils.WidgetHelper;
import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<ViewData> mDatas;
    //建立枚举 2个item 类型

    public TestAdapter(Context context,List<ViewData> datas){
        mDatas=datas;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case WidgetHelper.ViewType.VEDIO:
                return new VedioViewHolder(mLayoutInflater.inflate(R.layout.item_vedio,parent,false));
            case WidgetHelper.ViewType.textHeader:
                return new TextHeaderViewHolder(mLayoutInflater.inflate(R.layout.item_text,parent,false));
            case WidgetHelper.ViewType.forwardFooter:
                return new ForwardViewHolder(mLayoutInflater.inflate(R.layout.item_vedio,parent,false));
            case WidgetHelper.ViewType.videoCollectionWithCover:
                return new CoverVedioViewHolder(mLayoutInflater.inflate(R.layout.item_vedio_collection_cover,parent,false));
            case WidgetHelper.ViewType.videoCollectionWithBrief:
                return new BreifVedioViewHolder(mLayoutInflater.inflate(R.layout.item_vedio_collection_brief,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VedioViewHolder) {
            bindView((VedioViewHolder) holder,mDatas.get(position));
        } else if (holder instanceof CoverVedioViewHolder) {
            bindView((CoverVedioViewHolder) holder,mDatas.get(position));
        } else if (holder instanceof BreifVedioViewHolder) {
            bindView((BreifVedioViewHolder) holder,mDatas.get(position));
        } else if (holder instanceof TextHeaderViewHolder) {
            bindView((TextHeaderViewHolder) holder,mDatas.get(position));
        }
    }

    private void bindView(VedioViewHolder holder,ViewData data){
        holder.img.setImageURI(data.getData().getCover().getDetail());
        holder.title.setText(data.getData().getTitle());
        holder.category.setText(data.getData().getCategory());
    }
    private void bindView(CoverVedioViewHolder holder,ViewData data){
        holder.cover.setImageURI(data.getData().getHeader().getCover());
        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(manager);
        holder.recyclerView.setAdapter(new HorizontalAdapter(data.getData().getItemList(),mLayoutInflater));
    }
    private void bindView(BreifVedioViewHolder holder,ViewData data){
        holder.ico.setImageURI(data.getData().getHeader().getCover());
        holder.count.setText(data.getData().getHeader().getSubTitle());
        holder.name.setText(data.getData().getHeader().getTitle());
        holder.des.setText(data.getData().getHeader().getDescription());
        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(manager);
        holder.recyclerView.setAdapter(new HorizontalAdapter(data.getData().getItemList(),mLayoutInflater));
    }
    private void bindView(TextHeaderViewHolder holder,ViewData data){
        holder.head.setText(data.getData().getText());
    }

    //设置ITEM类型，可以自由发挥，这里设置item position单数显示item1 偶数显示item2
    @Override
    public int getItemViewType(int position) {
        return (WidgetHelper.getViewType(mDatas.get(position).getType()));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    public void addData(List<ViewData> datas) {
        int count = getItemCount();
        mDatas.addAll(datas);
        notifyItemRangeInserted(count, datas.size());
    }

    public static class VedioViewHolder extends RecyclerView.ViewHolder{

        TextView category;
        TextView title;
        SimpleDraweeView img;
        public VedioViewHolder(View itemView) {
            super(itemView);
            category=(TextView)itemView.findViewById(R.id.tv_category);
            title=(TextView)itemView.findViewById(R.id.tv_title);
            img= (SimpleDraweeView) itemView.findViewById(R.id.sdv_img);
        }
    }

    public static class ForwardViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;
        public ForwardViewHolder(View itemView) {
            super(itemView);
//            mTextView=(TextView)itemView.findViewById(R.id.tv_item2_text);
        }
    }
    public static class TextHeaderViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_head)
        TextView head;
        public TextHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
//            mTextView=(TextView)itemView.findViewById(R.id.tv_item2_text);
        }
    }
    public static class CoverVedioViewHolder extends RecyclerView.ViewHolder{

        SimpleDraweeView cover;
        RecyclerView recyclerView;
        public CoverVedioViewHolder(View itemView) {
            super(itemView);
            cover=(SimpleDraweeView)itemView.findViewById(R.id.sdv_cover);
            recyclerView= (RecyclerView) itemView.findViewById(R.id.rv_collection);
        }
    }
    public static class BreifVedioViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_count)
        TextView count;
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_des)
        TextView des;
        @BindView(R.id.sdv_icon)
        SimpleDraweeView ico;
        @BindView(R.id.rv_collection)
        RecyclerView recyclerView;
        public BreifVedioViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
//            count=(TextView)itemView.findViewById(R.id.tv_count);
//            name=(TextView)itemView.findViewById(R.id.tv_name);
//            des=(TextView)itemView.findViewById(R.id.tv_des);
//            ico=(SimpleDraweeView)itemView.findViewById(R.id.sdv_icon);
//            recyclerView= (RecyclerView) itemView.findViewById(R.id.rv_collection);
        }
    }
}
