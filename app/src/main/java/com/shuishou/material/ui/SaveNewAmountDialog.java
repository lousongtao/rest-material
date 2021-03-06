package com.shuishou.material.ui;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shuishou.material.InstantValue;
import com.shuishou.material.R;
import com.shuishou.material.bean.HttpResult;
import com.shuishou.material.bean.Material;
import com.shuishou.material.bean.MaterialCategory;
import com.shuishou.material.io.IOOperator;
import com.shuishou.material.utils.CommonTool;

/**
 * Created by Administrator on 2017/7/21.
 */

class SaveNewAmountDialog {
    private TextView txtLeftAmount;
    private EditText txtNewAmount;
    private MainActivity mainActivity;

    private AlertDialog dlg;
    private Material material;

    private final static int MESSAGEWHAT_NOTIFYLISTCHANGE=1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dealHandlerMessage(msg);
            super.handleMessage(msg);
        }
    };

    private void dealHandlerMessage(Message msg){
        switch (msg.what){
            case MESSAGEWHAT_NOTIFYLISTCHANGE :
                mainActivity.notifyMaterialItemChanged(material);
                break;
        }
    }

    public SaveNewAmountDialog(@NonNull MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initUI();
    }

    private void initUI(){
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.change_newamount_layout, null);
        txtLeftAmount = (TextView) view.findViewById(R.id.txtLeftAmount);
        txtNewAmount = (EditText) view.findViewById(R.id.txtNewAmount);

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle("Change Amount")
                .setIcon(R.drawable.update)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .setView(view);
        dlg = builder.create();
        dlg.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //add listener for YES button
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doSave();
                    }
                });
            }
        });
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
    }


    private void doSave(){
        final String amount = txtNewAmount.getText().toString();
        if (amount == null || amount.length() == 0){
            Toast.makeText(mainActivity, "Please input amount.", Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(){
            @Override
            public void run() {
                HttpResult<Material> result = mainActivity.getHttpOperator().saveAmount(material.getId(), Integer.parseInt(amount));
                if (result.data instanceof Material){
                    material.setLeftAmount(Double.parseDouble(amount));
                    handler.sendMessage(CommonTool.buildMessage(MESSAGEWHAT_NOTIFYLISTCHANGE));
                } else {
                    MainActivity.LOG.error("get false from server while save material amount. material = " + material.getName()
                            + ", amount = " + amount + ", error message = " + result.data);
                }
            }
        }.start();
        dlg.dismiss();
    }

    public void showDialog(Material m){
        this.material = m;
        txtLeftAmount.setText("Left Amount : " + material.getLeftAmount());
        txtNewAmount.setText(InstantValue.NULLSTRING);
        dlg.setTitle("Change Amount for " + m.getName());
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dlg.show();
    }

    public void dismiss(){
        dlg.dismiss();
    }
}
