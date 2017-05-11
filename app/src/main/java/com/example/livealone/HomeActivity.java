package com.example.livealone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        list = (Button) findViewById(R.id.list);
        registerForContextMenu(list);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_home);
    }

    public void homeClick(View v)
    {
        Intent intent;
        switch(v.getId())
        {
            case R.id.logout:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                // 전환할 화면의 엑티비티 클래스명을 입력
                startActivity(intent); // 새로운 엑티비티를 띄움
                finish(); // 현재 엑티비티를 없애고 다른 화면을 나타내기 위함
                break;


            //어댑터 뷰 사용해서, 디비로부터 정보 읽어와서 제목별로 클릭가능하게 만들기
            case R.id.buy:
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v == list)
        {
            menu.setHeaderTitle("리스트");
            menu.add(0,1,0,"gs25");
            menu.add(0,2,0,"sevenEleven");
            menu.add(0,3,0,"CU");
            menu.add(0,4,0,"MINISTOP");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId())
        {
            //요부분들을 이제 다 레이아웃으로 넣어주기
            case 1:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(intent); // 새로운 엑티비티를 띄움
                finish(); // 현재 엑티비티를 없애고 다른 화면을 나타내기 위함
                break;

            case 2:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(intent); // 새로운 엑티비티를 띄움
                finish(); // 현재 엑티비티를 없애고 다른 화면을 나타내기 위함
                break;

            case 3:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(intent); // 새로운 엑티비티를 띄움
                finish(); // 현재 엑티비티를 없애고 다른 화면을 나타내기 위함
                break;

            case 4:
        }

        return true;
    }
}
