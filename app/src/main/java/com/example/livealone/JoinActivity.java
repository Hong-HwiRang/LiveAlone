package com.example.livealone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JoinActivity extends AppCompatActivity {

    EditText et_id, et_pw, et_pw_chk;
    String sId, sPw, sPw_chk;
    String finalresult=""; // php로부터 받아오는 문자열
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        et_id = (EditText) findViewById(R.id.inputID);
        et_pw = (EditText) findViewById(R.id.inputPW);
        et_pw_chk = (EditText) findViewById(R.id.inputPWCHECK);
    }

    /* onClick에서 정의한 이름과 똑같은 이름으로 생성 */
    public void mOnClick(View view)
    {
    /* 버튼을 눌렀을 때 동작하는 소스 */
        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();
        sPw_chk = et_pw_chk.getText().toString();

        if(sPw.equals(sPw_chk))
        {
        /* 패스워드 확인이 정상적으로 됨 */
            registDB rdb = new registDB();
            rdb.execute();
            Toast.makeText(getApplicationContext(),finalresult,Toast.LENGTH_SHORT).show();

            // 회원가입하고자 하는 id가 데이터베이스 상에 없어서 가입완료 되었을 때
            if(finalresult.contains("insert success")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // 전환할 화면의 엑티비티 클래스명을 입력
                startActivity(intent); // 새로운 엑티비티를 띄움
                finish(); // 현재 엑티비티를 없애고 다른 화면을 나타내기 위함
            }

            // 회원가입하고자 하는 id가 데이터베이스 상에 있어서 가입이 불가할 때
            else{

            }
        }

        else
        {
        /* 패스워드 확인이 불일치 함 */
            Toast.makeText(getApplicationContext(),"패스워드 확인 불일치"+sId+sPw,Toast.LENGTH_SHORT).show();
        }
    }

    public class registDB extends AsyncTask<Void, Integer, Void> {

        @Override
        // 처리하고하자 하는 작업을 한다
        protected Void doInBackground(Void... unused) {

/* 인풋 파라메터값 생성 */
            String param = "user_id=" + sId + "&user_password=" + sPw + "";
            String data = ""; // php로부터 반환받는 문자열
            try {
/* 서버연결 */
                URL url = new URL("http://hrang4983.cafe24.com/user_signup/signup_user_information.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

 /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

 /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                finalresult = data;
                Log.e("RECV DATA",finalresult);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }



}