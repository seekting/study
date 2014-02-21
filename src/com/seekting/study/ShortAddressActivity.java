
package com.seekting.study;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ShortAddressActivity extends BaseActivity implements OnClickListener {

    public ShortAddressActivity() {
        name = "长地址换短地址";
    }

    Button button1;
    EditText editText1;
    TextView text2;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_address);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        button1 = (Button) findViewById(R.id.button1);
        text2 = (TextView) findViewById(R.id.textView2);
        editText1 = (EditText) findViewById(R.id.textView1);
        button1.setOnClickListener(this);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);

            }

            // {
            //     "long_url": "http:\/\/www.baidu.com",
            //     "format": "json",
            //     "appid": "801058005",
            //     "openid": "85CF60F2F64003FD938E2BBBD53BB0C0",
            //     "openkey": "549D5498AF4CD3FAE55D3DAC86FBBFDD",
            //     "clientip": "220.181.42.195",
            //     "reqtime": 1392799771,
            //     "wbversion": "1",
            //     "pf": "php-sdk2.0beta",
            //     "sig": "R2iZ3\/WDCjMvwpigouKBQxEH4NE="
            // }
            @Override
            protected String doInBackground(Void... params) {
               //  http://open.t.qq.com/api/short_url/shorten?format=xml&long_url=http://www.baidu.com&appid=801452899&openid=B624064BA065E01CB73F835017FE96FA&openkey=03c34b744968d05e2947e2a918bd5435&clientip=xx&reqtime=123456789&sig=xx&wbversion=1
                String web = "http://www.blogjava.net";
                String str = "http://open.t.qq.com/api/short_url/shorten";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("long_url", web);
                    jsonObject.put("format", "json");
                    jsonObject.put("appid", "801452899");
                    jsonObject.put("openkey", "03c34b744968d05e2947e2a918bd5435");
                    jsonObject.put("openid", "lbbrowser");
                    jsonObject.put("clientip", "220.181.42.195");
                    jsonObject.put("reqtime", System.currentTimeMillis());
                    jsonObject.put("wbversion", 1);
                    jsonObject.put("pf", "php-sdk2.0beta");
                    jsonObject.put("sig", "xxx");

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                try {
                    URL url = new URL(str);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    // Read from the connection. Default is true.
                    connection.setDoInput(true);
                    // Set the post method. Default is GET
                    connection.setRequestMethod("GET");

                    connection.connect();

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection
                            .getOutputStream()));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    connection.disconnect();
                    return sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                text2.setText(result);
                progressBar.setVisibility(View.INVISIBLE);
            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
