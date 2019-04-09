package com.sf.DarkCalculator;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.List;

public class SplashActivity extends BaseActivity {
    /**
     * 升级提示
     */
    private LinearLayout ll_update;

    private InstallUtil mInstallUtil;

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private CustomProgressWithPercentView percent;
    public static final String S_HTML = "S_HTML";
    public static final String S_UPDATE = "S_UPDATE";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ll_update = (LinearLayout)findViewById(R.id.ll_update);
        percent = (CustomProgressWithPercentView)findViewById(R.id.percent);

        init();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int grantedResult = ContextCompat.checkSelfPermission(this, permissions[0]);
//            if (grantedResult == PackageManager.PERMISSION_GRANTED) {
//                //申请权限
//                init();
//            } else {
//                ActivityCompat.requestPermissions(this, permissions, 321);
//            }
//        } else {
//            init();
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean granted = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!granted) {
                        finish();
                    } else {
                        ActivityCompat.requestPermissions(this, permissions, 321);
                    }
                } else {
                    init();
                }
            }
        }

    }

    private void init() {
        try {

            AVQuery<AVObject> avQuery1 = new AVQuery<>("config");
            avQuery1.orderByDescending("createdAt");
            avQuery1.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        LogUtil.e("my", "avQuery1 list:" + list.toString());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(S_HTML, list.toString());
                        editor.commit();

                    } else {
                        e.printStackTrace();
                    }
                }
            });

            //如果是配置网页，则只进入网页
            String config_html = preferences.getString(S_HTML, "");
            if (!TextUtils.isEmpty(config_html)) {
                LogUtil.e("my","config_html:" + config_html);
                JSONArray jsonArray1 = new JSONArray(config_html);
                if (jsonArray1 != null && jsonArray1.length() > 0) {
                    JSONObject jsonObject2 = jsonArray1.optJSONObject(0);
                    JSONObject serverData = jsonObject2.optJSONObject("serverData");
                    String url = serverData.optString("url");
                    String des = serverData.optString("des");
                    String type = serverData.optString("type");
                    Intent intent = new Intent();
                    if("2".equals(type)){
                        //如果是配置网页，则只进入网页
                        intent.setClass(SplashActivity.this, HtmlActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("canback", true);
                    } else if("1".equals(type)){
                        //强制更新
                        download(url);
                        return;
                    } else {
                        //
                        intent.setClass(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                //config

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            //c_config_html 自控

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {

        }
    }

    /**
     * 下载应用
     * @param url
     */
    private void download(String url) {
        ll_update.setVisibility(View.VISIBLE);
        String name = getLastName(url);
        String filePath = getFilesDir().getAbsolutePath() + "/" + name;
        LogUtil.e("my","filePath:" + filePath );
        mInstallUtil = new InstallUtil(this, filePath);
        DownloadUtil.get().download(url, getFilesDir().getAbsolutePath(), name, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(final File file) {
                //下载完成进行相关逻辑操作
                openFile(file);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        percent.setProgress(100);
                        percent.setEnabled(true);
                        percent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openFile(file);
                            }
                        });
                    }
                });

            }

            @Override
            public void onDownloading(final int progress) {
                LogUtil.d("my", "下载百分之" + progress + "%。。。。");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        percent.setProgress(progress);
                    }
                });
            }

            @Override
            public void onDownloadFailed(Exception e) {
                //下载异常进行相关提示操作
            }
        });

    }

    private void openFile(final File file) {
        // TODO Auto-generated method stub
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //卸载应用
                mInstallUtil.normaluninstallSilent(getPackageName());
                LogUtil.e("my","uninstallSilent" );
                if(ll_update != null){
                    ll_update.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mInstallUtil.install();//再次执行安装流程，包含权限判等
                        }
                    },50);
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InstallUtil.UNKNOWN_CODE) {
            mInstallUtil.install();//再次执行安装流程，包含权限判等
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //返回键不可返回
            if(ll_update.getVisibility() == View.VISIBLE){
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private String getLastName(String url){
        String name = "";
        if(!TextUtils.isEmpty(url)){
            int index = url.lastIndexOf("/");
            if(index > 0 && index < url.length()){
                name = url.substring(index + 1);
            }
        }

//        LogUtil.e("my","getLastName:" + name);
        return name;
    }
}
