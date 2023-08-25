package com.platelet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

public class AreaActivity extends AppCompatActivity {

    private ListView placeListView;
    private String[] placeNameString;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        placeNameString = getResources().getStringArray(R.array.placeName);

        placeListView = findViewById(R.id.placelistviewId);

        autoCompleteTextView = findViewById(R.id.place_search_id);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.place_listview_layout,R.id.placetextid,placeNameString);



        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.setAdapter(adapter);

        placeListView.setAdapter(adapter);


        placeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String placeName = placeNameString[position];

                Toast.makeText(AreaActivity.this, placeName, Toast.LENGTH_SHORT).show();

                Intent hospitalListIntent = new Intent(AreaActivity.this, HospitalListActivity.class);
                startActivity(hospitalListIntent);



            }
        });

    }
}