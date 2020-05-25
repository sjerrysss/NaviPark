package com.example.asus.navipark_01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SharedPreferences prefs;
    private int doneCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnMap = (Button) findViewById(R.id.btnMap);
        Button btnHistory = (Button) findViewById((R.id.btnHistory));

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "onClicked", Toast.LENGTH_SHORT).show();
                goToMap();
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view)  {
//                getDoneCheck();
//                sendDoneCheck();
                goToHistory();
            }
        });
    }

    private void goToMap()  {
        //Toast.makeText(MainActivity.this, "goToMap", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }

    private void goToHistory()  {
        //Toast.makeText(this, "goToHistory", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

//    private void sendDoneCheck()  {
//        prefs = getSharedPreferences("doneCheck", MODE_PRIVATE);
//        prefs.edit().putInt("doneCheckInt", doneCheck).commit();
//        Log.d(TAG, "sendDoneCheck: "+doneCheck);
//    }
//
//    private void getDoneCheck()  {
//        doneCheck = getSharedPreferences("doneCheck", MODE_PRIVATE).getInt("doneCheckInt", 2);
//        Log.d(TAG, "getDoneCheck: "+doneCheck);
//    }
}
