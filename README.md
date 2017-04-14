#Android Runtime Permission

###1,运行时权限说明：
	 Android运行时权限，是Android6.0新加的功能点。当我们想要把我们的app适配到6.0 以及以上的时候，我们需要对运行时权限做些操作，否则很容易会造成程序崩溃，当我们运行在6.0系统的时候。

###2，常见权限：
```
//    6.0权限的基本知识，以下是需要单独申请的权限，
    // 共分为9组，每组只要有一个权限申请成功了，就默认整组权限都可以使用了。

//    group:android.permission-group.CONTACTS   //第一组 读取 联系人权限
//    permission:android.permission.WRITE_CONTACTS
//    permission:android.permission.GET_ACCOUNTS
//    permission:android.permission.READ_CONTACTS
//
//    group:android.permission-group.PHONE //第二组  拨打电话权限
//    permission:android.permission.READ_CALL_LOG
//    permission:android.permission.READ_PHONE_STATE
//    permission:android.permission.CALL_PHONE
//    permission:android.permission.WRITE_CALL_LOG
//    permission:android.permission.USE_SIP
//    permission:android.permission.PROCESS_OUTGOING_CALLS
//    permission:com.android.voicemail.permission.ADD_VOICEMAIL
//
//    group:android.permission-group.CALENDAR  //第三组 ：允许程序读取用户的日程信息
//    permission:android.permission.READ_CALENDAR
//    permission:android.permission.WRITE_CALENDAR
//
//    group:android.permission-group.CAMERA //第四组 摄像机的 使用 允许访问摄像头进行拍照
//    permission:android.permission.CAMERA
//
//    group:android.permission-group.SENSORS // 第五组  传感器
//    permission:android.permission.BODY_SENSORS
//
//    group:android.permission-group.LOCATION //第六组 允许获得移动网络定位信息改变
//    permission:android.permission.ACCESS_FINE_LOCATION
//    permission:android.permission.ACCESS_COARSE_LOCATION
//
//    group:android.permission-group.STORAGE //第七组  允许程序写入外部存储，如SD卡上写文件
//    permission:android.permission.READ_EXTERNAL_STORAGE
//    permission:android.permission.WRITE_EXTERNAL_STORAGE
//
//    group:android.permission-group.MICROPHONE //第八组  麦风风 权限
//    permission:android.permission.RECORD_AUDIO
//
//    group:android.permission-group.SMS //第九组  读取短信  内容权限
//    permission:android.permission.READ_SMS
//    permission:android.permission.RECEIVE_WAP_PUSH
//    permission:android.permission.RECEIVE_MMS
//    permission:android.permission.RECEIVE_SMS
//    permission:android.permission.SEND_SMS
//    permission:android.permission.READ_CELL_BROADCASTS
```

###3，google 官方的介绍：
	官方运行时demo：https://github.com/bonaparteI/android-RuntimePermissions-master
####1,官方demo中  ：
先看效果图 （以下为我翻译后的界面）：

![这里写图片描述](http://img.blog.csdn.net/20170414161929287?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMzIzMzA5Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)  ![这里写图片描述](http://img.blog.csdn.net/20170414161944240?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMzIzMzA5Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

	google官方的demo 还是比较不错的。但对于权限的处理。我感觉还是不够完善。当我们碰到适配问题时就暴露无疑。
	比如小米对于 shouldShowRequestPermissionRationale方法 的处理。

	若按照google的处理方式：处理显然不够完善。

###4，于是乎    我写了一个  权限请求处理的demo。供大家参考。

	先看效果图：左边是直接请求权限的处理方式，右边是对权限先做检查后做处理的方式。

![这里写图片描述](http://img.blog.csdn.net/20170414163117468?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMzIzMzA5Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)  ![这里写图片描述](http://img.blog.csdn.net/20170414162935387?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMzIzMzA5Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

####5 逻辑说明：

	1，首先我们要知道我们申请权限是需要在  Mainfest.xml 中注册的。默认不注册的权限是不被准许的。直接申请权限存在App崩溃的潜在风险：

```
//                直接申请  不做权限检查：执行流程：请求权限---弹出Dialog(要允许App申请权限吗？)---拒绝||允许
//                                  拒绝情况：再次点击:请求权限----弹出Dialog(带有不再询问对话框checkBox)---拒绝||允许
//                                  拒绝情况:再次点击:之前未勾选不再询问的checkBox:   此时效果同上。
//                                  拒绝情况:再次点击:之前勾选了不再询问的checkBox:   此时不再提示对话框，但会回调onRequestPermissionsResult打印6权限被拒绝。
                directRequestPermisssion(Manifest.permission.READ_CONTACTS,REQUEST_CONTACTS_CODE);


//.....
   /**
     * 直接  请求 权限
     * @param permission 权限
     * @param resultCode 结果码
     */
    protected void directRequestPermisssion(String permission,int resultCode){
        ActivityCompat.requestPermissions(this, new String[]{permission}, resultCode);
    }

```

	2，几个重要方法：

```

/**
     * Determine whether <em>you</em> have been granted a particular permission.
     *
     * @param permission The name of the permission being checked.
     *
     * @return {@link android.content.pm.PackageManager#PERMISSION_GRANTED} if you have the
     * permission, or {@link android.content.pm.PackageManager#PERMISSION_DENIED} if not.
     *
     * @see android.content.pm.PackageManager#checkPermission(String, String)
     */
    public static int checkSelfPermission(@NonNull Context context, @NonNull String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }

        return context.checkPermission(permission, android.os.Process.myPid(), Process.myUid());
    }

//=============================================================
/**
     * Requests permissions to be granted to this application. These permissions
     * must be requested in your manifest, they should not be granted to your app,
     * and they should have protection level {@link android.content.pm.PermissionInfo
     * #PROTECTION_DANGEROUS dangerous}, regardless whether they are declared by
     * the platform or a third-party app.
     * <p>
     * Normal permissions {@link android.content.pm.PermissionInfo#PROTECTION_NORMAL}
     * are granted at install time if requested in the manifest. Signature permissions
     * {@link android.content.pm.PermissionInfo#PROTECTION_SIGNATURE} are granted at
     * install time if requested in the manifest and the signature of your app matches
     * the signature of the app declaring the permissions.
     * </p>
     * <p>
     * If your app does not have the requested permissions the user will be presented
     * with UI for accepting them. After the user has accepted or rejected the
     * requested permissions you will receive a callback reporting whether the
     * permissions were granted or not. Your activity has to implement {@link
     * android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback}
     * and the results of permission requests will be delivered to its {@link
     * android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback#onRequestPermissionsResult(
     * int, String[], int[])} method.
     * </p>
     * <p>
     * Note that requesting a permission does not guarantee it will be granted and
     * your app should be able to run without having this permission.
     * </p>
     * <p>
     * This method may start an activity allowing the user to choose which permissions
     * to grant and which to reject. Hence, you should be prepared that your activity
     * may be paused and resumed. Further, granting some permissions may require
     * a restart of you application. In such a case, the system will recreate the
     * activity stack before delivering the result to your
     * {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])}.
     * </p>
     * <p>
     * When checking whether you have a permission you should use {@link
     * #checkSelfPermission(android.content.Context, String)}.
     * </p>
     * <p>
     * Calling this API for permissions already granted to your app would show UI
     * to the user to decided whether the app can still hold these permissions. This
     * can be useful if the way your app uses the data guarded by the permissions
     * changes significantly.
     * </p>
     * <p>
     * You cannot request a permission if your activity sets {@link
     * android.R.attr#noHistory noHistory} to <code>true</code> in the manifest
     * because in this case the activity would not receive result callbacks including
     * {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])}.
     * </p>
     * <p>
     * The <a href="http://developer.android.com/samples/RuntimePermissions/index.html">
     * RuntimePermissions</a> sample app demonstrates how to use this method to
     * request permissions at run time.
     * </p>
     *
     * @param activity The target activity.
     * @param permissions The requested permissions. Must me non-null and not empty.
     * @param requestCode Application specific request code to match with a result
     *    reported to {@link OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])}.
     *    Should be >= 0.
     *
     * @see OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])
     * @see #checkSelfPermission(android.content.Context, String)
     * @see #shouldShowRequestPermissionRationale(android.app.Activity, String)
     */
    public static void requestPermissions(final @NonNull Activity activity,
            final @NonNull String[] permissions, final @IntRange(from = 0) int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompatApi23.requestPermissions(activity, permissions, requestCode);
        } else if (activity instanceof OnRequestPermissionsResultCallback) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    final int[] grantResults = new int[permissions.length];

                    PackageManager packageManager = activity.getPackageManager();
                    String packageName = activity.getPackageName();

                    final int permissionCount = permissions.length;
                    for (int i = 0; i < permissionCount; i++) {
                        grantResults[i] = packageManager.checkPermission(
                                permissions[i], packageName);
                    }

                    ((OnRequestPermissionsResultCallback) activity).onRequestPermissionsResult(
                            requestCode, permissions, grantResults);
                }
            });
        }
    }

//==========================================================================
 /**
     * Gets whether you should show UI with rationale for requesting a permission.
     * You should do this only if you do not have the permission and the context in
     * which the permission is requested does not clearly communicate to the user
     * what would be the benefit from granting this permission.
     * <p>
     * For example, if you write a camera app, requesting the camera permission
     * would be expected by the user and no rationale for why it is requested is
     * needed. If however, the app needs location for tagging photos then a non-tech
     * savvy user may wonder how location is related to taking photos. In this case
     * you may choose to show UI with rationale of requesting this permission.
     * </p>
     *
     * @param activity The target activity.
     * @param permission A permission your app wants to request.
     * @return Whether you can show permission rationale UI.
     *
     * @see #checkSelfPermission(android.content.Context, String)
     * @see #requestPermissions(android.app.Activity, String[], int)
     */
    public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity,
            @NonNull String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ActivityCompatApi23.shouldShowRequestPermissionRationale(activity, permission);
        }
        return false;
    }
```

	楼主也基本看不懂但大致意思是：
	checkPermission():检查权限。requestPermissions()请求权限。shouldShowRequestPermissionRationale():是否应该请求权限(注意该方法，小米对于该方法一直返回false)。

	经楼主反复测试：
	第一个主要是用于检查权限是否被用户准许过。
	第二个方法主要是用来请求权限。
	第三个怎么解释呢？我直接说返回结果吧：当用户第一次拒绝过之后该方法会一直返回false。其他返回true。(该方法在小米手机中会一直返回false。开发者需注意适配问题。)

	上面说的适配问题怎么解决呢？
	楼主是在权限请求结果中添加了一次判断：当用户拒绝权限后，再次弹出dialog提醒权限的重要性。用户可以选择取消或者打开设置界面进行设置。
	//TODO  解释为什么  需要该权限的  对话框
    showMissingPermissionDialog();


小米手机返回图片截图：

![这里写图片描述](http://img.blog.csdn.net/20170414165042255?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMzIzMzA5Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)![这里写图片描述](http://img.blog.csdn.net/20170414165052662?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMzIzMzA5Nw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

over。

楼主对activity进行了权限请求的封装，需要请求权限的可以直接拿走，修改需要请求的权限即可。。

github：
