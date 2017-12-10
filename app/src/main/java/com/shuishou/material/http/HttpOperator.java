package com.shuishou.material.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.material.InstantValue;
import com.shuishou.material.R;
import com.shuishou.material.bean.HttpResult;
import com.shuishou.material.bean.Material;
import com.shuishou.material.bean.MaterialCategory;
import com.shuishou.material.ui.MainActivity;
import com.shuishou.material.utils.CommonTool;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/9.
 */

public class HttpOperator {

    private String logTag = "HttpOperation";


    private MainActivity mainActivity;
    private static final int WHAT_VALUE_QUERYMATERIAL = 1;

    private Gson gson = new Gson();

    private OnResponseListener responseListener =  new OnResponseListener<JSONObject>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            switch (what){
                case WHAT_VALUE_QUERYMATERIAL :
                    doResponseQueryMaterial(response);
                    break;
                default:
            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {
            Log.e("Http failed", "what = "+ what + "\nresponse = "+ response.get());
            MainActivity.LOG.error("Response Listener On Faid. what = "+ what + "\nresponse = "+ response.get());
            String msg = InstantValue.NULLSTRING;
            switch (what){
                case WHAT_VALUE_QUERYMATERIAL :
                    msg = "Failed to load Material data. Please restart app!";
                    break;
            }
            CommonTool.popupWarnDialog(mainActivity, R.drawable.error, "WRONG", msg);
        }

        @Override
        public void onFinish(int what) {
        }
    };

    private RequestQueue requestQueue = NoHttp.newRequestQueue();

    public HttpOperator(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    private void doResponseQueryMaterial(Response<JSONObject> response){
        if (response.getException() != null){
            Log.e(logTag, "doResponseQueryMaterial: " + response.getException().getMessage() );
            MainActivity.LOG.error("doResponseQueryMaterial: " + response.getException().getMessage());
            sendErrorMessageToToast("Http:doResponseQueryMaterial: " + response.getException().getMessage());
            return;
        }
        HttpResult<ArrayList<MaterialCategory>> result = gson.fromJson(response.get().toString(), new TypeToken<HttpResult<ArrayList<MaterialCategory>>>(){}.getType());
        if (result.success){
            ArrayList<MaterialCategory> mcs = result.data;
//            mainActivity.setCategories(mcs);
//            mainActivity.persistData();
            mainActivity.initData(mcs);
        }else {
            Log.e(logTag, "doResponseQueryMaterial: get FALSE for query material");
            MainActivity.LOG.error("doResponseQueryMaterial: get FALSE for query material");
        }
    }

    //load material
    public void loadData(){
        mainActivity.getProgressDlgHandler().sendMessage(CommonTool.buildMessage(MainActivity.PROGRESSDLGHANDLER_MSGWHAT_STARTLOADDATA,
                "start loading material data ..."));
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(InstantValue.URL_TOMCAT + "/material/querymaterialcategory");
        requestQueue.add(WHAT_VALUE_QUERYMATERIAL, request, responseListener);
    }

    private void onFailedLoadMenu(){
        //TODO: require restart app
    }

    private void sendErrorMessageToToast(String sMsg){
        mainActivity.getToastHandler().sendMessage(CommonTool.buildMessage(MainActivity.TOASTHANDLERWHAT_ERRORMESSAGE,sMsg));
    }

    public void uploadErrorLog(File file, String machineCode){
        int key = 0;// the key of filelist;
        UploadErrorLogListener listener = new UploadErrorLogListener(mainActivity);
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(InstantValue.URL_TOMCAT + "/common/uploaderrorlog", RequestMethod.POST);
        FileBinary bin1 = new FileBinary(file);
        request.add("logfile", bin1);
        request.add("machineCode", machineCode);
        listener.addFiletoList(key, file.getAbsolutePath());
        requestQueue.add(key, request, listener);
    }

    public HttpResult<Material> saveAmount(int materialId, double amount){
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(InstantValue.URL_TOMCAT + "/material/updatematerialamount", RequestMethod.POST);
        request.add("userId", "1");//TODO; wait the login dialog
        request.add("id", String.valueOf(materialId));
        request.add("leftAmount", String.valueOf(amount));
        Response<JSONObject> response = NoHttp.startRequestSync(request);

        if (response.getException() != null){
            HttpResult<Material> result = new HttpResult<>();
            result.result = response.getException().getMessage();
            return result;
        }
        if (response.get() == null) {
            Log.e(logTag, "Error occur while change material amount. response.get() is null.");
            MainActivity.LOG.error("Error occur while change material amount. response.get() is null.");
            HttpResult<Material> result = new HttpResult<>();
            result.result = "Error occur while change material amount. response.get() is null";
            return result;
        }
        HttpResult<Material> result = gson.fromJson(response.get().toString(), new TypeToken<HttpResult<Material>>(){}.getType());
        return result;
    }
}
