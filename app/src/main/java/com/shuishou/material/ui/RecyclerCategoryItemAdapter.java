package com.shuishou.material.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuishou.material.InstantValue;
import com.shuishou.material.R;
import com.shuishou.material.bean.MaterialCategory;
import com.shuishou.material.io.IOOperator;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/12/25.
 */

public class RecyclerCategoryItemAdapter extends RecyclerView.Adapter<RecyclerCategoryItemAdapter.ViewHolder> {
    private CategoryItemClickListener clickListener = new CategoryItemClickListener();
    private final int resourceId;
    private final ArrayList<MaterialCategory> categories;
    private final MainActivity mainActivity;
    static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tvName;
        final ImageView imgArrow;
        public ViewHolder(View view){
            super(view);
            tvName = (TextView)view.findViewById(R.id.categoryname_textview);
            imgArrow = (ImageView)view.findViewById(R.id.imgChoosedArrow);
        }
    }

    public RecyclerCategoryItemAdapter(MainActivity mainActivity, int resourceId, ArrayList<MaterialCategory> objects){
        categories = objects;
        this.resourceId = resourceId;
        this.mainActivity = mainActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        MaterialCategory mc = categories.get(position);
        holder.tvName.setText(mc.getName());
        holder.tvName.setTag(mc);
        holder.tvName.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryItemClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mainActivity.doClickCategory((MaterialCategory) v.getTag());
        }
    }

}
