package com.example.livealone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    SessionCallback callback;

    EditText et_id, et_pw;
    String sId, sPw;
    String finalresult=""; // php로부터 받아오는 문자열
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("로그인하기");

        et_id = (EditText) findViewById(R.id.inputID);
        et_pw = (EditText) findViewById(R.id.inputPW);

         callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
    }

    public void mOnClick(View v){

        sId = et_id.getText().toString();
        sPw = et_pw.getText().toString();

        switch(v.getId()){
            case R.id.login:

                loginDB ldb = new loginDB();
                ldb.execute();

                // 만약에 데이터베이스 상에 id가 존재한다면 로그인이 되도록 하라
                if(finalresult.contains(sId)) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    // 전환할 화면의 엑티비티 클래스명을 입력
                    startActivity(intent); // 새로운 엑티비티를 띄움
                    finish(); // 현재 엑티비티를 없애고 다른 화면을 나타내기 위함
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"id 또는 password가 일치하지 않습니다",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.join:
                Intent intent2 = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent2);
                finish();
                break;

            default:
                Toast.makeText(getApplicationContext(),"버튼을 눌러요",Toast.LENGTH_SHORT).show();
        }
    }

    public class loginDB extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... unused) {

/* 인풋 파라메터값 생성 */
            String param = "user_id=" + sId + "&user_password=" + sPw + "";
            String data = ""; // php로부터 반환받는 문자열
            try {
/* 서버연결 */
                URL url = new URL("http://hrang4983.cafe24.com/user_login/log.php");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                    Log.e("UserProfile", userProfile.toString());
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            // 세션 연결이 실패했을때
            // 어쩔때 실패되는지는 테스트를 안해보았음 ㅜㅜ
            Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_SHORT).show();
        }
    }




}
