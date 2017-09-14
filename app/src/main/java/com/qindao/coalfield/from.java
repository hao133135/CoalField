package com.qindao.coalfield;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qindao.model.Coalbytruckbean;
import com.qindao.model.Coalfieldzonebean;
import com.qindao.utils.clickUtils;
import com.qindao.utils.myadapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/8/21.
 */

public class from extends AppCompatActivity {

    private List<String > slist = new ArrayList<>();
    private List<String > sslist = new ArrayList<>();
    private List<Coalbytruckbean> llist = new ArrayList<Coalbytruckbean>();
    private Spinner mySpinner;
    private ArrayAdapter<String> adapter;
    private Button buttonCheck;
    private Button buttonClose;
    private ListView mylistview;
    private TextView myTextView;
    private Map map = new HashMap();
    private JSONArray jsonreal,jsonzone;
    private String username,path;
    private List<String> myslist;
    private List<Coalbytruckbean> mylistView;
    private String fieldname;
    private boolean spinnerboolean=false,spinnerboolean1=false;
    private String coalfieldlist,coalzone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.from);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         /*获取Intent中的Bundle对象*/
        Bundle bundle = this.getIntent().getExtras();
        /*获取Bundle中的数据，注意类型和key*/
        username = bundle.getString("user1");
        path = bundle.getString("path");
        mySpinner = (Spinner) findViewById(R.id.spinner);
        buttonCheck = (Button) findViewById(R.id.Check);
        buttonClose = (Button) findViewById(R.id.close);
        mylistview = (ListView) findViewById(R.id.listview);
        buttonClose.setOnClickListener(new closeListener());
        buttonCheck.setOnClickListener(new clickListener());

        new SpinnerTask().execute();
        mySpinnerListener();
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(from.this, "请选择验收车辆！", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }).start();

        }
    }

    private class closeListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (clickUtils.isFastClick()) {
                // 进行点击事件后的逻辑操作
                Intent i = new Intent(from.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    private void spinnerClick() {
        //第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项

        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, slist);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        mySpinner.setAdapter(adapter);

    }

    private void listViewClick() {
        final myadapter<Coalfieldzonebean>  myArrayAdapter = new myadapter<Coalfieldzonebean>
                (this,llist,R.layout.items);
        View item = adapter.getView(0, null, mylistview);
        ViewGroup.LayoutParams params = item.getLayoutParams();
        params.height = 6;
        item.setLayoutParams(params);
        mylistview.setAdapter(myArrayAdapter);
        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                if (clickUtils.isFastClick()) {
                    // 进行点击事件后的逻辑操作
                    myArrayAdapter.setSelectedItem(position);
                    myArrayAdapter.notifyDataSetChanged();
                    try {
                        final JSONObject jsonObject = (JSONObject) jsonzone.get(0);
                        for (int i = 0; i < parent.getCount(); i++) {

                            if (position == i) {

                                buttonCheck.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(from.this, acceptance.class);
                                        i.putExtra("vehicleno", llist.get(position).getVehicleno());
                                        i.putExtra("coalfieldid", llist.get(position).getCoalfieldid());
                                        i.putExtra("username", username);
                                        i.putExtra("path", path);
                                        try {
                                            i.putExtra("coalbytruckid", (String) jsonObject.get("COALBYTRUCKID"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        startActivity(i);

                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    //清除处理
    private void cleanlist(){
        int size=llist.size();
        if(size>0){
            llist.removeAll(llist);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(from.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

   /* private void mylistviewListener() {
            mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                    if (clickUtils.isFastClick()) {
                        // 进行点击事件后的逻辑操作
                        try {
                            final JSONObject jsonObject = (JSONObject) jsonzone.get(0);
                            for (int i = 0; i < parent.getCount(); i++) {

                                if (position == i) {

                                    buttonCheck.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(from.this, acceptance.class);
                                            i.putExtra("vehicleno", llist.get(position).getVehicleno());
                                            i.putExtra("coalfieldid", llist.get(position).getCoalfieldid());
                                            i.putExtra("username", username);
                                            i.putExtra("path", path);
                                            try {
                                                i.putExtra("coalbytruckid", (String) jsonObject.get("COALBYTRUCKID"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            startActivity(i);

                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }*/
   private void mySpinnerListener() {
       //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
       mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
           public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                /* 将所选mySpinner 的值带入myTextView 中*/
               fieldname = adapter.getItem(arg2).toString();
                /* 将mySpinner 显示*/
               arg0.setVisibility(View.VISIBLE);
               cleanlist();
               new listViewTask().execute();

           }
           public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub
               myTextView.setText("NONE");
               arg0.setVisibility(View.VISIBLE);
           }
       });
        /*下拉菜单弹出的内容选项触屏事件处理*/
       mySpinner.setOnTouchListener(new Spinner.OnTouchListener(){
           public boolean onTouch(View v, MotionEvent event) {
               // TODO Auto-generated method stub
               return false;
           }
       });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
       mySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){
           public void onFocusChange(View v, boolean hasFocus) {
               // TODO Auto-generated method stub
           }
       });
   }
    class SpinnerTask extends AsyncTask<Object, Void, List<String>>
    {
        @Override
        protected List<String> doInBackground(Object... params) {
            HttpClient httpClient = new DefaultHttpClient();

           // String key = "[{FIELDNAME:A,COALFIELDID:1},{FIELDNAME:B,COALFIELDID:2},{FIELDNAME:临时,COALFIELDID:3}]";
            try {
                HttpGet httpGet = new HttpGet(path+"/App/GetCoalField");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                String key = EntityUtils.toString(httpResponse.getEntity());
                JSONArray jsonArray = new JSONArray(key);
                jsonreal=jsonArray;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String name = (String) jsonObject.get("FIELDNAME");
                    Integer id = (Integer) jsonObject.get("COALFIELDID");
                    String iid = String.valueOf(id);
                    sslist.add(name);
                    sslist.add(iid);
                }
                for (int i = 0; i < sslist.size(); ) {
                    String s1 = null;
                    String s2 = null;
                    for (int j = i; j < i + 2; j++) {
                        if (j % 2 == 0) {
                            s1 = sslist.get(j);
                            slist.add(s1);
                        } else {
                            s2 = sslist.get(j);
                        }

                    }
                    map.put(s1, s2);
                    i += 2;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                httpClient.getConnectionManager().shutdown();
            }
            return slist;
        }
        @Override
        protected void onPostExecute(List<String> result) {
            // TODO Auto-generated method stub
            spinnerClick();
        }
    }
    class listViewTask extends AsyncTask<Object, Void, List<Coalbytruckbean>>
    {
        @Override
        protected List<Coalbytruckbean> doInBackground(Object... params) {
            HttpClient httpClient = new DefaultHttpClient();
            try {
                //fieldname = mySpinner.getSelectedItem().toString();
                HttpPost httpPost = new HttpPost(path+"/App/GetCoalByTruck");
                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
                JSONObject param = new JSONObject();
                if (fieldname != null) {
                    param.put("coalId", map.get(fieldname));
                }else {
                    return null;
                }
                StringEntity se = new StringEntity(param.toString());
                se.setContentType("application/json;charset=utf-8");
                httpPost.setEntity(se);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                String key = EntityUtils.toString(httpResponse.getEntity());
               // String key = "[{vehicleno:\"123\",zonename:南区},{vehicleno:\"111\",zonename:北区},{vehicleno:临时,zonename:北区}]";
                JSONArray jsonArray = new JSONArray(key);
                jsonzone=jsonArray;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Coalbytruckbean cya = new Coalbytruckbean();
                    cya.setVehicleno((String) jsonObject.get("vehicleno"));
                    cya.setCoalfieldname((String) jsonObject.get("zonename"));
                    cya.setCoalfieldid(fieldname + "-" + jsonObject.get("zonename"));
                    llist.add(cya);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                httpClient.getConnectionManager().shutdown();
            }
            return llist;
        }
        @Override
        protected void onPostExecute(List<Coalbytruckbean> result) {
            // TODO Auto-generated method stub
            if(result!=null){
                listViewClick();
            }else {
                return;
            }

        }
    }
}
