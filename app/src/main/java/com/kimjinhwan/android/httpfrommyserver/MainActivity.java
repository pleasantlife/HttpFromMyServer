package com.kimjinhwan.android.httpfrommyserver;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTime = (TextView) findViewById(R.id.textTime);
        //pc 에뮬레이터로 실행해야함. 휴대전화로 할 경우 ip 사용 대역이 달라져서 결과값이 제대로 나오지 않음.
        networkTask("http://192.168.10.248:8080/bbb.jsp");


    }


    public void networkTask(String url){
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... params) {
                String result = getData(params[0]);
                return result;
            }


            @Override
            protected void onPostExecute(String r) {
                Log.e("RESULT", "결과"+r);
                textTime.setText(r);
            }

        }.execute(url);

    }



    public String getData(String url) {
        StringBuilder result = new StringBuilder();

        //1. 요청처리
        try {
            URL serverUrl = new URL(url);
            //주소에 해당하는 서버의 socket을 연결
            HttpURLConnection con = (HttpURLConnection) serverUrl.openConnection();
            // OutputStream으로 데이터를 요청
            con.setRequestMethod("GET");    // GET + 요청 url + http 호출 규격 의 데이터가 송신됨.

            //2. 응답처리
            //응답의 유효성 검사
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK/*HTTP_OK = 200*/) {
                //줄 단위로 데이터를 읽기 위해서 버퍼에 담는다. + 속도 향상도 가능.
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String temp ="";
                while ((temp = br.readLine()) != null) {
                    //readLine 명령이 실행되면 줄바꿈 태그가 날아간다!!!!!!
                    //그래서 append(추가) 해 줄 때마다 줄바꿈 태그를 반드시 넣을 것!!!
                    result.append(temp+"\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
