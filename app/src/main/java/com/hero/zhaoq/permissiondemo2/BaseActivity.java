package com.hero.zhaoq.permissiondemo2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Package_name:com.hero.zhaoq.permissiondemo2
 * Author:zhaoqiang
 * Email:zhaoq_hero@163.com
 * Date:2017/04/14   14/42
 * github:https://github.com/229457269  csdn:http://blog.csdn.net/u013233097
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initListener();
    }

    protected abstract void initListener();
    protected abstract void initView();
    protected abstract int getLayoutId();


}
