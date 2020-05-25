package com.example.asus.navipark_01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryActivity extends AppCompatActivity {
    private static final String TAG = "SummaryActivity";
    private double xLat, xLng;
    private long xLatLong, xLngLong;
    private TextView txtLat, txtLng;
    private ImageButton btnDone;
    private EditText editTxtDescription;
    private String xTime;
    private String txtDescription;
    private SharedPreferences prefs;
    private int xCount = 0;
    private int doneCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        getSupportActionBar().setTitle("Description");

        btnDone = (ImageButton) findViewById(R.id.btnDone) ;
        txtLat = (TextView)findViewById(R.id.txtLat);
        txtLng = (TextView)findViewById(R.id.txtLng);
        editTxtDescription = (EditText) findViewById(R.id.editTxtDescription);
        xCount = setCount();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(xCount>=-1 && xCount<9)  {
                    Log.d(TAG, "onClick: xCount>=0 && xCount<9");

                    xCount = xCount+1;
                    sendCount();
                }
                else if(xCount == 9)  {
                    Log.d(TAG, "onClick: xCount == 9");

                    sortDescription();
                    sortMarkerLatLng();
                    sortDateTime();
                    sendCount();
                }

                getDescription();
                putDescription();
                getMarkerLatLng();
                putMarkerLatLng();
                getDateTime();
                putDateTime();

//                GlobalVariables gv = (GlobalVariables) getApplicationContext();
//                gv.setStr(xTime);

//                getApplicationContext().getSharedPreferences("xCount", MODE_PRIVATE).edit().remove("xCountInt").commit();
//                xCount = setCount();

                Log.d(TAG, "onClick: xCount - "+xCount);

                finishAffinity();

                Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //--------------------------------------------------Count--------------------------------------------------
    private int setCount()  {
        return getSharedPreferences("xCount", MODE_PRIVATE).getInt("xCountInt", -1);
    }

    private void sendCount()  {
        prefs = getSharedPreferences("xCount", MODE_PRIVATE);
        prefs.edit().putInt("xCountInt", xCount).commit();
    }

    //--------------------------------------------------markerLatLng--------------------------------------------------
    private void getMarkerLatLng()  {
        xLatLong = getSharedPreferences("xLatLng", MODE_PRIVATE).getLong("xLat", -1);
        xLngLong = getSharedPreferences("xLatLng", MODE_PRIVATE).getLong("xLng", -1);

        Log.d(TAG, "getMarkerLatLng: ("+xLatLong+", "+xLngLong+")");

//        xLat = Double.longBitsToDouble(xLatLong);
//        xLng = Double.longBitsToDouble(xLngLong);
    }

    //--------------------------------------------------latLng--------------------------------------------------
    private void putMarkerLatLng()  {
        prefs = getSharedPreferences("xLatLng", MODE_PRIVATE);
        prefs.edit().putLong("xLat_"+xCount, xLatLong).putLong("xLng_"+xCount, xLngLong).commit();
    }

    private void sortMarkerLatLng()  {
        prefs = getSharedPreferences("xLatLng", MODE_PRIVATE);

        for(int i = 0; i < xCount; i++)  {
            int j = i+1;
            long lLat = getSharedPreferences("xLatLng", MODE_PRIVATE).getLong("xLat_"+j, -1);
            long lLng = getSharedPreferences("xLatLng", MODE_PRIVATE).getLong("xLng_"+j, -1);

            prefs.edit().putLong("xLat_"+i, lLat).putLong("xLng_"+i, lLng).commit();
        }
    }

    //--------------------------------------------------dateTime--------------------------------------------------
    private void getDateTime()  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date currDate = new Date(System.currentTimeMillis());
        xTime = sdf.format(currDate);
    }

    private void putDateTime()  {
        prefs = getSharedPreferences("xDateTime", MODE_PRIVATE);
        prefs.edit().putString("strDateTime_"+xCount, xTime).commit();
    }

    private void sortDateTime()  {
        prefs = getSharedPreferences("xDateTime", MODE_PRIVATE);

        for(int i = 0; i < xCount; i++)  {
            int j = i+1;
            String str = getSharedPreferences("xDateTime", MODE_PRIVATE).getString("strDateTime_"+j, "Time is null");
            prefs.edit().putString("strDateTime_"+i, str).commit();
        }
    }

    //--------------------------------------------------Description--------------------------------------------------
    private void getDescription()  {
        txtDescription = editTxtDescription.getText().toString();

        Log.d(TAG, "getEditText: "+txtDescription);
    }

    private void putDescription()  {
        prefs = getSharedPreferences("xDescription", MODE_PRIVATE);
        prefs.edit().putString("strDescription_"+xCount, txtDescription).commit();

        String str = getSharedPreferences("xDescription", MODE_PRIVATE).getString("strDescription_"+xCount, "Description is null");

        Log.d(TAG, "putDescription: "+str);
    }

    private void sortDescription()  {
        prefs = getSharedPreferences("xDescription", MODE_PRIVATE);

        for(int i = 0; i < xCount; i++)  {
            int j = i+1;
            String str = getSharedPreferences("xDescription", MODE_PRIVATE).getString("strDescription_"+j, "Description is null");
            prefs.edit().putString("strDescription_"+i, str).commit();
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------
    private void setTextViewText()  {
        txtLat.setText(String.valueOf(xLat));
        txtLng.setText(String.valueOf(xLng)+"          "+xTime);
    }


//    private void putTimeStr()  {
//        prefs = getSharedPreferences()
//    }

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
