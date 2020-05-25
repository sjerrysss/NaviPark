package com.example.asus.navipark_01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private TextView txtHistory;      //add TextView id txtHistory if needed/original pink TextView showing xCount
    private String xTime;
    private int xCount;
    private int i;
    private SharedPreferences prefs;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mIndex = new ArrayList<>();
    private static final String TAG = "HistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setTitle("History");

        //txtHistory = (TextView) findViewById(R.id.txtHistory);
        xCount = setCount();
        Log.d(TAG, "onCreate: xCount - "+xCount);

        initImageBitmaps();

        //setTextViewText();



        //getDateTime();
        //getDoneCheck();

        //for(i = xCount; i >= 0; i--)  {
//            final Button myButton = new Button(this);
//            myButton.setBackgroundResource(R.drawable.btn_history);
//            myButton.setPadding(0, 0, 0, 4);
//            myButton.setElevation(20);
//            String str = getSharedPreferences("xDateTime", MODE_PRIVATE).getString("strDateTime_"+i, "Time is null")+"   "+i+"\n"+getSharedPreferences("xDescription", MODE_PRIVATE).getString("strDescription_"+i, "Description is null");
//            myButton.setText(str);
//            myButton.setId(i);
//            registerForContextMenu(myButton);
//
//            LinearLayout ll = (LinearLayout)findViewById(R.id.ll);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            ll.addView(myButton, lp);
//
//            myButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(HistoryActivity.this, DirectionsActivity.class).putExtra("index", myButton.getId());
//                    Log.d(TAG, "onClick: "+"["+i+"]");
//                    startActivity(intent);
//                }
//            });
        //}

        //-----------------------------------------Delete SharedPreferences---------------------------------------------------------------------------------------------------------------------------------------------------
//        Button btn = (Button) findViewById(R.id.button2);         add button id "button2" if needed/original reset all history
//        View.OnClickListener imageClickListener;
//
//        imageClickListener = new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(HistoryActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
//
//                for(int i = 0; i <= xCount; i++)  {
//                    getApplicationContext().getSharedPreferences("xDescription", MODE_PRIVATE).edit().remove("strDescription_"+i).commit();
//                    getApplicationContext().getSharedPreferences("xLatLng", MODE_PRIVATE).edit().remove("xLat_"+i).remove("xLng_"+i).commit();
//                    getApplicationContext().getSharedPreferences("xDateTime", MODE_PRIVATE).edit().remove("strDateTime_"+i).commit();
//                }
//
//                getApplicationContext().getSharedPreferences("xCount", MODE_PRIVATE).edit().remove("xCountInt").commit();
//                getApplicationContext().getSharedPreferences("xLatLng", MODE_PRIVATE).edit().remove("xLat").remove("xLng").commit();
//
//            }
//        };
//
//        btn.setOnClickListener(imageClickListener);
//
//        txtHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HistoryActivity.this, DirectionsActivity.class);
//                startActivity(intent);
//            }
//        });
        //-----------------------------------------Delete SharedPreferences---------------------------------------------------------------------------------------------------------------------------------------------------
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int idx = xCount - item.getGroupId();
        switch (item.getItemId())  {
            case 0:
                Toast.makeText(this, "Editted "+idx, Toast.LENGTH_SHORT).show();
                initDialogEdit(idx);

                return true;
            case 1:
                Toast.makeText(this, "Deleted "+idx, Toast.LENGTH_SHORT).show();
                deleteHistory(idx);
                refresh();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void initImageBitmaps()  {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        for(int i = xCount; i >= 0; i--)  {
            mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");

            String str = getSharedPreferences("xDateTime", MODE_PRIVATE).getString("strDateTime_"+i, "Time is null");
            mNames.add(str);

            str = getSharedPreferences("xDescription", MODE_PRIVATE).getString("strDescription_"+i, "Description is null");
            mDescription.add(str);

            str = String.valueOf(i);
            mIndex.add(str);
        }

        initRecyclerView();
    }

    private void initRecyclerView()  {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mImageUrls, mNames, mDescription, mIndex, this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this)  {
            @Override
            public boolean isAutoMeasureEnabled()  {
                return false;
            }
        };
        recyclerView.setLayoutManager(llm);
    }

    private void getDateTime()  {
        xTime = getSharedPreferences("xDateTime", MODE_PRIVATE).getString("strDateTime_"+xCount, "Time is null");
    }

    private void setTextViewText()  {
        txtHistory.setText(xTime+"   "+xCount);
    }

    private int setCount()  {
        return getSharedPreferences("xCount", MODE_PRIVATE).getInt("xCountInt", -1);
    }

    private void sendCount()  {
        prefs = getSharedPreferences("xCount", MODE_PRIVATE);
        prefs.edit().putInt("xCountInt", xCount).commit();
    }

    private void deleteHistory(int idx)  {
        Log.d(TAG, "deleteHistory: deleting "+xCount);

        if(idx == xCount)  {
            Log.d(TAG, "deleteHistory: idx == xCount");

            getApplicationContext().getSharedPreferences("xDescription", MODE_PRIVATE).edit().remove("strDescription_"+idx).commit();
            getApplicationContext().getSharedPreferences("xLatLng", MODE_PRIVATE).edit().remove("xLat_"+idx).remove("xLng_"+idx).commit();
            getApplicationContext().getSharedPreferences("xDateTime", MODE_PRIVATE).edit().remove("strDateTime_"+idx).commit();
        }
        else  {
            Log.d(TAG, "deleteHistory: idx != xCount");

            sortHistory(idx);
        }

        xCount = xCount-1;

        sendCount();
    }

    private void sortHistory(int idx)  {
        Log.d(TAG, "sortHistory: idx - "+idx);

        for(int i = idx; i <= xCount-1; i++)  {
            Log.d(TAG, "sortHistory: [idx : "+idx+"] [xCount : "+xCount+"]");

            int j = i+1;

            prefs = getSharedPreferences("xLatLng", MODE_PRIVATE);
            long lLat = prefs.getLong("xLat_"+j, -1);
            long lLng = prefs.getLong("xLng_"+j, -1);
            prefs.edit().putLong("xLat_"+i, lLat).putLong("xLng_"+i, lLng).commit();

            prefs = getSharedPreferences("xDateTime", MODE_PRIVATE);
            String str = prefs.getString("strDateTime_"+j, "Time is null");
            prefs.edit().putString("strDateTime_"+i, str).commit();

            prefs = getSharedPreferences("xDescription", MODE_PRIVATE);
            str = prefs.getString("strDescription_"+j, "Description is null");
            prefs.edit().putString("strDescription_"+i, str).commit();
        }
    }

    private void refresh() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void initDialogEdit(final int idx)  {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_edit, null);
        final EditText editTxt = (EditText) mView.findViewById(R.id.editTxt);
        Button btnEditDone = (Button) mView.findViewById(R.id.btnEditDone);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnEditDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editTxt.getText().toString();
                editDescriptionPrefs(idx, str);

                Log.d(TAG, "onClick: str - "+str);

                dialog.cancel();
                refresh();
            }
        });


    }

    private void editDescriptionPrefs(int idx, String str)  {
        prefs = getSharedPreferences("xDescription", MODE_PRIVATE);
        getApplicationContext().getSharedPreferences("xDescription", MODE_PRIVATE).edit().remove("strDescription_"+idx).commit();
        prefs.edit().putString("strDescription_"+idx, str).commit();
    }

//    private void getDoneCheck()  {
//        doneCheck = getSharedPreferences("doneCheck", MODE_PRIVATE).getInt("doneCheckInt", 2);
//        Log.d(TAG, "getDoneCheck: "+doneCheck);
//    }
}
