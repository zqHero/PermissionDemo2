package com.hero.zhaoq.permissiondemo2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends CheckPermissionActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private TextView txt_info;

    @Override
    protected void initListener() {
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                直接申请  不做权限检查：执行流程：请求权限---弹出Dialog(要允许App申请权限吗？)---拒绝||允许
//                                  拒绝情况：再次点击:请求权限----弹出Dialog(带有不再询问对话框checkBox)---拒绝||允许
//                                  拒绝情况:再次点击:之前未勾选不再询问的checkBox:   此时效果同上。
//                                  拒绝情况:再次点击:之前勾选了不再询问的checkBox:   此时不再提示对话框，但会回调onRequestPermissionsResult打印6权限被拒绝。
                directRequestPermisssion(Manifest.permission.READ_CONTACTS,REQUEST_CONTACTS_CODE);

//                对权限做检查：
//                checkPermission(Manifest.permission.READ_CONTACTS,REQUEST_CONTACTS_CODE); //联系人
//                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATIN_CODE); //定位
                checkPermission(Manifest.permission.CAMERA, REQUEST_CAMERA_CODE); //定位
            }
        });
    }

    @Override
    protected void initView() {
        txt_info = (TextView) findViewById(R.id.txt_info);
    }

    int clicki = 0;

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        clicki ++;
        switch (requestCode) {
            case REQUEST_CONTACTS_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    Log.i("info", "5,权限被同意" + "==联系人");
                    txt_info.setText("联系人权限被同意:clicki="+clicki);
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    Log.i("info", "6,权限被拒绝" + "==联系人");
                    txt_info.setText("联系人权限被拒绝:clicki="+clicki);
//                    //TODO   解释  为什么需要该权限
                    showMissingPermissionDialog();
                }
                break;
            case REQUEST_LOCATIN_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    Log.i("info", "5,权限被同意" + "==定位");
                    txt_info.setText("定位权限被同意:clicki="+clicki);
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    Log.i("info", "6,权限被拒绝" + "==定位");
//                    //TODO   解释  为什么需要该权限
                    showMissingPermissionDialog();
                    txt_info.setText("定位权限被拒绝:clicki="+clicki);
                }
                break;
            case REQUEST_CAMERA_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    Log.i("info", "5,权限被同意" + "==相机");
                    txt_info.setText("相机权限被同意:clicki="+clicki);
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    Log.i("info", "6,权限被拒绝" + "==相机");
                    txt_info.setText("相机权限被拒绝:clicki="+clicki);
//                    //TODO   解释  为什么需要该权限
                    showMissingPermissionDialog();
                }
                break;
        }
    }

    @Override
    protected void permissionHasGranted() {
        txt_info.setText("权限已经被准许了,你可以做你想做的事情");
    }
}
