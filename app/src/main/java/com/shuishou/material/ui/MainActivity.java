package com.shuishou.material.ui;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.shuishou.material.InstantValue;
import com.shuishou.material.R;
import com.shuishou.material.bean.Material;
import com.shuishou.material.bean.MaterialCategory;
import com.shuishou.material.bean.UserData;
import com.shuishou.material.http.HttpOperator;
import com.shuishou.material.io.IOOperator;
import com.shuishou.material.utils.CommonTool;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;

import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Administrator on 2017-12-06.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MainActivity.class.getSimpleName());
    private String TAG_UPLOADERRORLOG = "uploaderrorlog";
    private String TAG_EXITSYSTEM = "exitsystem";
    private String TAG_LOOKFOR = "lookfor";
    private String TAG_SCAN = "scan";
    private final static int REQUESTCODE_SCAN = 1;
    private final static int REQUESTCODE_QUICKSEARCH = 2;
    public final static String INTENTEXTRA_CATEGORYLIST= "CATEGORYLIST";

    private UserData loginUser;
    private ArrayList<MaterialCategory> categories = new ArrayList<>();
    private ArrayList<Material> materials = new ArrayList<>();
    private HttpOperator httpOperator;
    private RecyclerMaterialItemAdapter materialAdapter;

    private android.support.v7.widget.RecyclerView lvCategory;
    private android.support.v7.widget.RecyclerView lvMaterial;
    private ImageButton btnLookfor;
    private ImageButton btnScan;
    private SaveNewAmountDialog saveNewAmountDialog;
    private ImportAmountDialog importAmountDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginUser = (UserData)getIntent().getExtras().getSerializable(LoginActivity.INTENTEXTRA_LOGINUSER);
        setContentView(R.layout.activity_main);
        lvCategory = (android.support.v7.widget.RecyclerView)findViewById(R.id.category_listview);
        lvMaterial = (android.support.v7.widget.RecyclerView)findViewById(R.id.material_listview);
        btnLookfor = (ImageButton)findViewById(R.id.btnLookfor);
        btnScan = (ImageButton)findViewById(R.id.btnScan);
        TextView tvUploadErrorLog = (TextView)findViewById(R.id.drawermenu_uploaderrorlog);
        TextView tvExit = (TextView)findViewById(R.id.drawermenu_exit);

        tvUploadErrorLog.setTag(TAG_UPLOADERRORLOG);
        tvExit.setTag(TAG_EXITSYSTEM);
        btnLookfor.setTag(TAG_LOOKFOR);
        btnScan.setTag(TAG_SCAN);
        tvUploadErrorLog.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        btnLookfor.setOnClickListener(this);
        btnScan.setOnClickListener(this);


        Logger.setDebug(true);
        Logger.setTag("material:nohttp");


        httpOperator = new HttpOperator(this);

        httpOperator.loadData();

        saveNewAmountDialog = new SaveNewAmountDialog(this);
        importAmountDialog = new ImportAmountDialog(this);
    }

    public void initData(ArrayList<MaterialCategory> mcs){
        this.categories = mcs;
        RecyclerCategoryItemAdapter categoryItemAdapter = new RecyclerCategoryItemAdapter(this, R.layout.category_listitem_layout, categories);
        lvCategory.setLayoutManager(new LinearLayoutManager(this));
        lvCategory.setAdapter(categoryItemAdapter);

        materialAdapter = new RecyclerMaterialItemAdapter(this, R.layout.material_listitem_layout, materials);
        lvMaterial.setLayoutManager(new LinearLayoutManager(this));
        lvMaterial.setAdapter(materialAdapter);
    }


    public void doClickCategory(MaterialCategory mc){
        materials.clear();
        if (mc.getMaterials() != null)
            materials.addAll(mc.getMaterials());
        materialAdapter.notifyDataSetChanged();
    }

    public Handler getProgressDlgHandler(){
        return progressDlgHandler;
    }

    public Handler getToastHandler(){
        return toastHandler;
    }

    public UserData getLoginUser() {
        return loginUser;
    }

    public ArrayList<MaterialCategory> getCategories() {
        return categories;
    }

    public void startProgressDialog(String title, String message){
        progressDlg = ProgressDialog.show(this, title, message);
    }

    public HttpOperator getHttpOperator(){
        return httpOperator;
    }

    public SaveNewAmountDialog getSaveNewAmountDialog() {
        return saveNewAmountDialog;
    }

    public ImportAmountDialog getImportAmountDialog(){
        return importAmountDialog;
    }

    public RecyclerMaterialItemAdapter getMaterialAdapter() {
        return materialAdapter;
    }

    public void notifyMaterialItemChanged(Material m){
        boolean isFound = false;//firstly loop the current material list, if find, notify to update UI; if not, loop all the catagories to update the data
        for(int i = 0; i< materials.size(); i++){
            if (materials.get(i).getId() == m.getId()){
                materials.get(i).setLeftAmount(m.getLeftAmount());
                materialAdapter.notifyItemChanged(i);
                isFound = true;
                break;
            }
        }
        if (!isFound){
            for(MaterialCategory mc : categories){
                if (mc.getMaterials() != null){
                    for(Material mtemp : mc.getMaterials()){
                        if (mtemp.getId() == m.getId()){
                            mtemp.setLeftAmount(m.getLeftAmount());
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (TAG_UPLOADERRORLOG.equals(v.getTag())){
            IOOperator.onUploadErrorLog(this);
        } else if (TAG_LOOKFOR.equals(v.getTag())){
            Intent intent = new Intent(MainActivity.this, QuickSearchActivity.class);
            intent.putExtra(INTENTEXTRA_CATEGORYLIST, categories);
            startActivityForResult(intent, REQUESTCODE_QUICKSEARCH);
        } else if (TAG_EXITSYSTEM.equals(v.getTag())){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm")
                    .setIcon(R.drawable.info)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null);
            builder.create().show();
        } else if (TAG_SCAN.equals(v.getTag())){
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivityForResult(intent, REQUESTCODE_SCAN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUESTCODE_SCAN:
                if (resultCode == RESULT_OK){
                    String code = data.getStringExtra(CameraActivity.INTENTDATA_CODE);
                    Material m = null;
                    for(MaterialCategory mc : categories){
                        for(Material material : mc.getMaterials()){
                            if (material.getName().equals(code)
                                    || code.equals(material.getBarCode())){
                                m = material;
                                break;
                            }
                        }
                    }
                    if (m != null){
                        saveNewAmountDialog.showDialog(m);
                    } else {
                        getToastHandler().sendMessage(CommonTool.buildMessage(MainActivity.TOASTHANDLERWHAT_ERRORMESSAGE,
                                "Cannot find Material by name : " + code));
                    }
                } else if (resultCode == RESULT_CANCELED){}
                break;
            case REQUESTCODE_QUICKSEARCH:
                if (resultCode == RESULT_OK){
                    Material m = (Material)data.getSerializableExtra(QuickSearchActivity.INTENTDATA_MATERIAL);
                    int action = data.getIntExtra(QuickSearchActivity.INTENTDATA_ACTION, 0);
                    if (action == QuickSearchActivity.INTENTDATA_ACTION_CHANGE)
                        saveNewAmountDialog.showDialog(m);
                    else if (action == QuickSearchActivity.INTENTDATA_ACTION_IMPORT)
                        importAmountDialog.showDialog(m);
                    else
                        Toast.makeText(this, "Unrecognized Action!", Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED){}
                break;
            default:
        }
    }

    public static final int PROGRESSDLGHANDLER_MSGWHAT_STARTLOADDATA = 3;
    public static final int PROGRESSDLGHANDLER_MSGWHAT_DOWNFINISH = 2;
    public static final int PROGRESSDLGHANDLER_MSGWHAT_SHOWPROGRESS = 1;
    public static final int PROGRESSDLGHANDLER_MSGWHAT_DISMISSDIALOG = 0;
    private ProgressDialog progressDlg;

    private Handler progressDlgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == PROGRESSDLGHANDLER_MSGWHAT_DISMISSDIALOG) {
                if (progressDlg != null)
                    progressDlg.dismiss();
            } else if (msg.what == PROGRESSDLGHANDLER_MSGWHAT_SHOWPROGRESS){
                if (progressDlg != null){
                    progressDlg.setMessage(msg.obj != null ? msg.obj.toString() : InstantValue.NULLSTRING);
                }
            } else if (msg.what == PROGRESSDLGHANDLER_MSGWHAT_DOWNFINISH){
                if (progressDlg != null){
                    progressDlg.setMessage(msg.obj != null ? msg.obj.toString() : InstantValue.NULLSTRING);
                }
            } else if (msg.what == PROGRESSDLGHANDLER_MSGWHAT_STARTLOADDATA){
                if (progressDlg != null){
                    progressDlg.setMessage(msg.obj != null ? msg.obj.toString() : InstantValue.NULLSTRING);
                }
            }
        }
    };

    public static final int TOASTHANDLERWHAT_ERRORMESSAGE = 0;
    private Handler toastHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TOASTHANDLERWHAT_ERRORMESSAGE){
                Toast.makeText(MainActivity.this,msg.obj != null ? msg.obj.toString() : InstantValue.NULLSTRING, Toast.LENGTH_LONG).show();
            }
        }
    };

}

