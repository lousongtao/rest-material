package com.shuishou.material.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.shuishou.material.InstantValue;
import com.shuishou.material.R;
import com.shuishou.material.bean.Material;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/12/25.
 */

public class RecyclerMaterialItemAdapter extends RecyclerView.Adapter<RecyclerMaterialItemAdapter.ViewHolder> {
    private ChangeAmountClickListener clickListener = new ChangeAmountClickListener();
    private final int resourceId;
    private final ArrayList<Material> ms;
    private final MainActivity mainActivity;
    static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView txtName;
        final TextView txtLeftAmount;
        final TextView txtUnit;
        final Button btnChange;
        public ViewHolder(View view){
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_materialname);
            txtLeftAmount = (TextView) view.findViewById(R.id.txt_leftamount);
            txtUnit = (TextView) view.findViewById(R.id.txt_unit);
            btnChange = (Button) view.findViewById(R.id.btn_change);
        }
    }

    public RecyclerMaterialItemAdapter(MainActivity mainActivity, int resourceId, ArrayList<Material> objects){
        ms = objects;
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
        Material m = ms.get(position);
        holder.txtUnit.setText(m.getUnit());
        holder.txtLeftAmount.setText(String.valueOf(m.getLeftAmount()));
        holder.txtName.setText(m.getName());
        holder.btnChange.setTag(m);
        holder.btnChange.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return ms.size();
    }

    class ChangeAmountClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mainActivity.getSaveNewAmountDialog().showDialog((Material)v.getTag());
        }
    }
}
