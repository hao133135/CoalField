package com.qindao.coalfield;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qindao.utils.clickUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText path;
    private Button button,but1,but2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        username = (EditText) findViewById(R.id.activity_login_user);
        password = (EditText) findViewById(R.id.activity_login_password);
        button = (Button) findViewById(R.id.activity_login_btn);
        but1 = (Button) findViewById(R.id.login_but1);
        but2 = (Button) findViewById(R.id.login_but2);
        path = (EditText) findViewById(R.id.loginurl);
        SharedPreferences sharedPreferences=getSharedPreferences("config",0);
        String name=sharedPreferences.getString("name","");
        String pwd=sharedPreferences.getString("password","");
        String url1 = sharedPreferences.getString("url","http://192.168.0.200:8090");
        username.setText(name);
        password.setText(pwd);
        path.setText(url1);
        but1.setOnClickListener(new but1Listener());
        but2.setOnClickListener(new but2Listener());
        button.setOnClickListener(new buttonListener());
       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, from.class);
                startActivity(i);
            }
        });*/

    }

    private class buttonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
           /*new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    String user = username.getText().toString();
                    String pwd = password.getText().toString();
                    String url1 = path.getText().toString();
                    //http://39.108.73.207
                    SharedPreferences sp=getSharedPreferences("config",0);
                    SharedPreferences.Editor editor=sp.edit();
                    //把数据进行保存
                    editor.putString("name",user);
                    editor.putString("password",pwd);
                    editor.putString("url",url1);
                    //提交数据
                    editor.commit();
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url1+"/APP/Validate");
                    httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
                    try {
                        JSONObject param = new JSONObject();
                        //param.put("LoginName",user);
                        //param.put("LoginPwd",pwd);
                        //StringEntity se = new StringEntity(param.toString());
                        //se.setContentType("application/json;charset=utf-8");
                        //httpPost.setEntity(se);
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        String key = EntityUtils.toString(httpResponse.getEntity());
                        //String stats = key.toString();
                        JSONObject j = new JSONObject(key);
                        //String stats = (String) j.get("stats");
                         String stats ="1";
                        if(user.isEmpty()||"".equals(user)||pwd.isEmpty()||"".equals(pwd)){
                            Toast.makeText(LoginActivity.this,"请输入用户名和密码！",Toast.LENGTH_SHORT).show();
                        }else if(stats.equals("1")){
                            Intent i = new Intent(LoginActivity.this, from.class);
                            i.putExtra("user1",user);
                            i.putExtra("path",url1);
                            startActivity(i);
                        }else {
                            Toast.makeText(LoginActivity.this,"用户名密码错误！",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this,"服务器链接失败！",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }finally {
                        httpClient.getConnectionManager().shutdown();
                    }
                    Looper.loop();
                }
            }).start();*/
            if (clickUtils.isFastClick()) {
                // 进行点击事件后的逻辑操作
                String url1 = path.getText().toString();
                String name = username.getText().toString();
                String pwd = password.getText().toString();
                new MyTask().execute(url1 + "/APP/Validate",name,pwd);
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private class but1Listener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            View layout = findViewById(R.id.loginlayout);
            layout.setVisibility(ViewGroup.VISIBLE);
        }
    }

    private class but2Listener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            View layout = findViewById(R.id.loginlayout);
            layout.setVisibility(ViewGroup.GONE);
        }
    }

    public class MyTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(strings[0]);
            String result = null;
            HttpResponse httpResponse = null;

            try {
                JSONObject param = new JSONObject();
                param.put("LoginName",strings[1]);
                param.put("LoginPwd",strings[2]);
                StringEntity se = new StringEntity(param.toString());
                se.setContentType("application/json;charset=utf-8");
                httpPost.setEntity(se);
                httpResponse = httpClient.execute(httpPost);
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                result="error";
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                httpClient.getConnectionManager().shutdown();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {super.onPostExecute(s);
            if(s.equals("error")){
                Toast.makeText(LoginActivity.this,"服务器链接失败！",Toast.LENGTH_SHORT).show();
            }else {
                String user = username.getText().toString();
                String pwd = password.getText().toString();
                String url1 = path.getText().toString();
                //http://39.108.73.207
                SharedPreferences sp = getSharedPreferences("config", 0);
                SharedPreferences.Editor editor = sp.edit();
                //把数据进行保存
                editor.putString("name", user);
                editor.putString("password", pwd);
                editor.putString("url", url1);
                //提交数据
                editor.commit();
                JSONObject j = null;
                try {

                    String stats = s;
                    // String stats ="1";
                    if (user.isEmpty() || "".equals(user) || pwd.isEmpty() || "".equals(pwd)) {
                        Toast.makeText(LoginActivity.this, "请输入用户名和密码！", Toast.LENGTH_SHORT).show();
                    } else if (stats.equals("1")) {
                        Intent i = new Intent(LoginActivity.this, from.class);
                        i.putExtra("user1", user);
                        i.putExtra("path", url1);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名密码错误！", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "数据解析错误！", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();

        }

        return false;

    }
    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    System.exit(0);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
}