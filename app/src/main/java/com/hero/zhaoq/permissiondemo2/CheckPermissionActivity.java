package com.hero.zhaoq.permissiondemo2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Package_name:com.hero.zhaoq.permissiondemo2
 * Author:zhaoqiang
 * Email:zhaoq_hero@163.com
 * Date:2017/04/14   14/42
 * github:https://github.com/229457269  csdn:http://blog.csdn.net/u013233097
 */
public abstract class CheckPermissionActivity extends BaseActivity {

    public final int REQUEST_CONTACTS_CODE = 0x98;
    public final int REQUEST_LOCATIN_CODE = 0x81;
    public final int REQUEST_CAMERA_CODE = 0x51;

    //shouldShowRequestPermissionRationale方法   说明：
//    1，望文生义，是否应该显示请求权限的说明。
//    2，第一次请求权限时，用户拒绝了，调用shouldShowRequestPermissionRationale()后返回true，应该显示一些为什么需要这个权限的说明。
//    3，用户在第一次拒绝某个权限后，下次再次申请时，授权的dialog中将会出现“不再提醒”选项，一旦选中勾选了，那么下次申请将不会提示用户。
//    4，第二次请求权限时，用户拒绝了，并选择了“不再提醒”的选项，调用shouldShowRequestPermissionRationale()后返回false。
//    5，设备的策略禁止当前应用获取这个权限的授权：shouldShowRequestPermissionRationale()返回false 。
//    6，加这个提醒的好处在于，用户拒绝过一次权限后我们再次申请时可以提醒该权限的重要性，
//    免得再次申请时用户勾选“不再提醒”并决绝，导致下次申请权限直接失败。


    //默认权限未被用户同意。 6.0 中效果
    //
    // Mainfest中未注册该权限：  (申请联系人权限)申请该权限则直接App崩溃, (申请定位权限执行流程)1==2==6。未注册有的权限崩溃有的不崩溃。楼主想应该是权限级别的问题，高危权限是应该会崩溃的。
    //1,逻辑：Manifest 中已经注册该权限：第一次点击申请权限：1----2(弹出对话框)----(拒绝-6||同意-5)
    //                                被拒绝后：再次点击：1----3(弹出对话框)---(取消-8||设置-设置界面4)
    protected void checkPermission(String permission, int resultCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {// 没有权限。
            Log.i("info", "1,需要申请权限。");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //TODO 用户未拒绝过 该权限 shouldShowRequestPermissionRationale返回false  用户拒绝过一次则一直返回true
                //TODO   注意小米手机  则一直返回时 false
                Log.i("info", "3,用户已经拒绝过一次该权限，需要提示用户为什么需要该权限。\n" +
                        "此时shouldShowRequestPermissionRationale返回：" + ActivityCompat.shouldShowRequestPermissionRationale(this,
                        permission));
                //TODO  解释为什么  需要该权限的  对话框
                showMissingPermissionDialog();
            } else {
                // 申请授权。
                ActivityCompat.requestPermissions(this, new String[]{permission}, resultCode);
                Log.i("info", "2,用户拒绝过该权限，或者用户从未操作过该权限，开始申请权限。-\n" +
                        "此时shouldShowRequestPermissionRationale返回：" +
                        ActivityCompat.shouldShowRequestPermissionRationale(this, permission));
            }
        } else {
            //TODO 权限 已经被准许  you can do something
            permissionHasGranted();
            Log.i("info", "7,已经被用户授权过了=可以做想做的事情了==打开联系人界面");
        }
    }

    //================权限已经被准许  可以在此做其他 操作=========
    protected abstract void permissionHasGranted();
    //==========================================================

    /**
     * 直接  请求 权限
     * @param permission 权限
     * @param resultCode 结果码
     */
    protected void directRequestPermisssion(String permission,int resultCode){
        ActivityCompat.requestPermissions(this, new String[]{permission}, resultCode);
    }

    /**
     * 提示用户的 dialog
     */
    protected void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少联系人权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。");
        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("info", "8--权限被拒绝,此时不会再回调onRequestPermissionsResult方法");
                    }
                });
        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("info", "4,需要用户手动设置，开启当前app设置界面");
                        startAppSettings();
                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 打开     App设置界面
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
