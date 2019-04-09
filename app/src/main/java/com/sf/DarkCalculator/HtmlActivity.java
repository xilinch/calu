package com.sf.DarkCalculator;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;


public class HtmlActivity extends BaseActivity {

    private H5Fragment h5Fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5);
        h5Fragment = new H5Fragment();
        Bundle bundle = getIntent().getExtras();
        h5Fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.ll_content,h5Fragment).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(h5Fragment != null){
                h5Fragment.goback();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 当前为横屏
            Log.d("my", "ORIENTATION_LANDSCAPE=" + Configuration.ORIENTATION_LANDSCAPE);// 2
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 当前为竖屏
            Log.d("my", "ORIENTATION_PORTRAIT=" + Configuration.ORIENTATION_PORTRAIT);// 1
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(h5Fragment != null && keyCode == KeyEvent.KEYCODE_BACK){
            h5Fragment.onkeyUp(keyCode,event);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
