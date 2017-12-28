package com.shuishou.material.ui;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shuishou.material.R;
import com.shuishou.material.bean.Material;
import com.shuishou.material.bean.MaterialCategory;

import java.util.ArrayList;

public class QuickSearchActivity extends AppCompatActivity {
    public static final String INTENTDATA_MATERIAL = "MATERIAL";
    private EditText txtSearchCode;
    private ArrayList<View> resultCellList = new ArrayList<>(8);
    private AlertDialog dlg;

    private QuickSearchActivity.ChooseMaterialListener listener = new QuickSearchActivity.ChooseMaterialListener();
    private ArrayList<Material> allMaterials;
    private ArrayList<MaterialCategory> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quicksearch_activity_layout);
        categories = (ArrayList<MaterialCategory>) getIntent().getExtras().getSerializable(MainActivity.INTENTEXTRA_CATEGORYLIST);
        txtSearchCode = (EditText) findViewById(R.id.txtSearchCode);

        //预设8个查询结果, 避免创建太多对象
        resultCellList.add(findViewById(R.id.quicksearchresultcell1));
        resultCellList.add(findViewById(R.id.quicksearchresultcell2));
        resultCellList.add(findViewById(R.id.quicksearchresultcell3));
        resultCellList.add(findViewById(R.id.quicksearchresultcell4));
        resultCellList.add(findViewById(R.id.quicksearchresultcell5));
        resultCellList.add(findViewById(R.id.quicksearchresultcell6));
        resultCellList.add(findViewById(R.id.quicksearchresultcell7));
        resultCellList.add(findViewById(R.id.quicksearchresultcell8));
        for(View v : resultCellList){
            Button btn = (Button)v.findViewById(R.id.btn_change);
            btn.setOnClickListener(listener);
        }

        txtSearchCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                refreshResult();
                return true;
            }
        });
//        txtSearchCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                refreshResult();
//            }
//        });
        initData();
    }

    private void initData(){
        allMaterials = new ArrayList<>();
        for(MaterialCategory mc : categories){
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
            TextView txtName = (TextView)v.findViewById(R.id.txt_materialname);
            TextView txtLeftAmouont = (TextView)v.findViewById(R.id.txt_leftamount);
            TextView txtUnit = (TextView)v.findViewById(R.id.txt_unit);
            txtName.setText(m.getName());
            txtLeftAmouont.setText(String.valueOf(m.getLeftAmount()));
            txtUnit.setText(m.getUnit());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED, null);
        finish();
    }

    class ChooseMaterialListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getTag() != null && v.getTag().getClass().getName().equals(Material.class.getName())){
                Material m = (Material)v.getTag();
                Intent intent = new Intent();
                intent.putExtra(INTENTDATA_MATERIAL, m);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
