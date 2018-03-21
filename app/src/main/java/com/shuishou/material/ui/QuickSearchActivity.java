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
    public static final String INTENTDATA_ACTION = "ACTION";
    public static final int INTENTDATA_ACTION_CHANGE = 1;
    public static final int INTENTDATA_ACTION_IMPORT = 2;
    private EditText txtSearchCode;
    private ArrayList<View> resultCellList = new ArrayList<>(8);
    private AlertDialog dlg;

    private ChangeAmountListener changeAmountListener = new ChangeAmountListener();
    private ImportlListener importlListener = new ImportlListener();
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
            ImageButton btnChange = (ImageButton) v.findViewById(R.id.btn_change);
            btnChange.setOnClickListener(changeAmountListener);
            ImageButton btnImport = (ImageButton) v.findViewById(R.id.btn_import);
            btnImport.setOnClickListener(importlListener);
        }

        txtSearchCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                refreshResult();
                return true;
            }
        });
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
            ImageButton btnChange = (ImageButton)v.findViewById(R.id.btn_change);
            btnChange.setTag(m);
            ImageButton btnImport = (ImageButton) v.findViewById(R.id.btn_import);
            btnImport.setTag(m);
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

    class ChangeAmountListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getTag() != null && v.getTag().getClass().getName().equals(Material.class.getName())){
                Material m = (Material)v.getTag();
                Intent intent = new Intent();
                intent.putExtra(INTENTDATA_MATERIAL, m);
                intent.putExtra(INTENTDATA_ACTION, INTENTDATA_ACTION_CHANGE);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    class ImportlListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getTag() != null && v.getTag().getClass().getName().equals(Material.class.getName())){
                Material m = (Material)v.getTag();
                Intent intent = new Intent();
                intent.putExtra(INTENTDATA_MATERIAL, m);
                intent.putExtra(INTENTDATA_ACTION, INTENTDATA_ACTION_IMPORT);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
