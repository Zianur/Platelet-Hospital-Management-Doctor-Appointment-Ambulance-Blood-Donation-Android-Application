package com.platelet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

public class HealthTipsActivity extends AppCompatActivity {

    private ListView tipsListView;
    private String[] tipsString;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

        tipsString = getResources().getStringArray(R.array.tipsNameString);

        tipsListView = (ListView) findViewById(R.id.tipslistviewId);

        autoCompleteTextView = findViewById(R.id.tips_search_id);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.place_listview_layout,R.id.placetextid,tipsString);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.autocomplete_layout,R.id.autoCompleteText,tipsString);


        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.setAdapter(adapter);

        tipsListView.setAdapter(adapter1);



        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tips = tipsString[position];

//                Toast.makeText(AppointmentActivity.this, placeName, Toast.LENGTH_SHORT).show();


                Intent tipsIntent = new Intent(HealthTipsActivity.this, TipsDescription.class);
                tipsIntent.putExtra("tips", tips);
                startActivity(tipsIntent);

            }
        });



        tipsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tips = tipsString[position];
                String tipsName = tips.replaceAll("\\p{Punct}", "");
                Toast.makeText(HealthTipsActivity.this, tipsName, Toast.LENGTH_SHORT).show();

//                Toast.makeText(AppointmentActivity.this, placeName, Toast.LENGTH_SHORT).show();

                Intent tipsIntent = new Intent(HealthTipsActivity.this, TipsDescription.class);
                tipsIntent.putExtra("tips", tips);
                startActivity(tipsIntent);



            }
        });

    }
}