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

public class AppointmentActivity extends AppCompatActivity {

    private ListView placeListView;
    private String[] placeNameString;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        placeNameString = getResources().getStringArray(R.array.placeNameH);

        placeListView = (ListView) findViewById(R.id.placelistviewId);

        autoCompleteTextView = findViewById(R.id.place_search_id);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.place_listview_layout,R.id.placetextid,placeNameString);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.autocomplete_layout,R.id.autoCompleteText,placeNameString);


        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.setAdapter(adapter);

        placeListView.setAdapter(adapter1);





        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String placeName = placeNameString[position];

//                Toast.makeText(AppointmentActivity.this, placeName, Toast.LENGTH_SHORT).show();


                Intent deptListIntent = new Intent(AppointmentActivity.this, HospitalListActivity.class);
                deptListIntent.putExtra("place", placeName);
                startActivity(deptListIntent);

            }
        });



        placeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String placeName = placeNameString[position];

//                Toast.makeText(AppointmentActivity.this, placeName, Toast.LENGTH_SHORT).show();

                Intent deptListIntent = new Intent(AppointmentActivity.this, HospitalListActivity.class);
                deptListIntent.putExtra("place", placeName);
                startActivity(deptListIntent);



            }
        });

    }
}