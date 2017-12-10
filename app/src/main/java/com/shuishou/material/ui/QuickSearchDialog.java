package com.shuishou.material.ui;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.shuishou.material.InstantValue;
import com.shuishou.material.R;
import com.shuishou.material.bean.Material;
import com.shuishou.material.bean.MaterialCategory;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/21.
 */

public class QuickSearchDialog {
    private EditText txtSearchCode;
    private ArrayList<View> resultCellList = new ArrayList<>(8);
    private AlertDialog dlg;

    private MainActivity mainActivity;
    private ChooseMaterialListener listener = new ChooseMaterialListener();
    private ArrayList<Material> allMaterials;

    public QuickSearchDialog(@NonNull MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initUI();
        initData();
    }

    private void initUI(){
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.quicksearchdialog_layout, null);
        txtSearchCode = (EditText) view.findViewById(R.id.txtSearchCode);

        //预设8个查询结果, 避免创建太多对象
        resultCellList.add(view.findViewById(R.id.quicksearchresultcell1));
        resultCellList.add(view.findViewById(R.id.quicksearchresultcell2));
        resultCellList.add(view.findViewById(R.id.quicksearchresultcell3));
        resultCellList.add(view.findViewById(R.id.quicksearchresultcell4));
        resultCellList.add(view.findViewById(R.id.quicksearchresultcell5));
        resultCellList.add(view.findViewById(R.id.quicksearchresultcell6));
        resultCellList.add(view.findViewById(R.id.quicksearchresultcell7));
        resultCellList.add(view.findViewById(R.id.quicksearchresultcell8));
        for(View v : resultCellList){
            ImageButton chooseButton = (ImageButton)v.findViewById(R.id.chooseBtn);
            chooseButton.setOnClickListener(listener);
        }

        txtSearchCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshResult();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setNegativeButton("Close", null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dlg = null;
            }
        });
        builder.setView(view);
        dlg = builder.create();
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
    }

    public void showDialog(){
        dlg.show();
    }

    private void initData(){
        allMaterials = new ArrayList<>();
        for(MaterialCategory mc : mainActivity.getCategories()){
            if (mc.getMaterials() != null){
                allMaterials.addAll(mc.getMaterials());
            }
        }
    }

    private void refreshResult(){
        if (txtSearchCode.getText() == null || txtSearchCode.getText().length() < 2) {
            hideAllResultCell();
        }
        String code = txtSearchCode.getText().toString();
        ArrayList<Material> results = new ArrayList<>();
        for(Material m : allMaterials){
            if (m.getName().toLowerCase().contains(code.toLowerCase())){
                results.add(m);
            }
        }
        hideAllResultCell();
        if (results.size() == 0 || results.size() > 8){
            return;
        }
        for(int i = 0; i< results.size(); i++){
            Material m = results.get(i);
            View v = resultCellList.get(i);
            v.setVisibility(View.VISIBLE);
            Button btn = (Button)v.findViewById(R.id.btn_change);
            btn.setTag(m);
            btn.setOnClickListener(listener);
//            TextView tvName = (TextView)v.findViewById(R.id.txtName);
//            ImageButton chooseButton = (ImageButton) v.findViewById(R.id.chooseBtn);
//            chooseButton.setTag(m);
//            tvName.setText(m.getName());
        }
    }

    private void hideAllResultCell(){
        for(View v : resultCellList){
            v.setVisibility(View.INVISIBLE);
        }
    }

    class ChooseMaterialListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getTag() != null && v.getTag().getClass().getName().equals(Material.class.getName())){
                Material m = (Material)v.getTag();
            }
        }
    }
}
