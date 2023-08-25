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

public class DeptListActivity extends AppCompatActivity {
    private ListView deptListView;
    private String[] deptNameString;
    private AutoCompleteTextView autoCompleteTextView;
    private String hospitalKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_list);

        deptNameString = getResources().getStringArray(R.array.deptAppointment);

        deptListView = (ListView) findViewById(R.id.deptlistviewId);

        autoCompleteTextView = findViewById(R.id.dept_search_id);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.place_listview_layout,R.id.placetextid,deptNameString);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.autocomplete_layout,R.id.autoCompleteText,deptNameString);


        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.setAdapter(adapter);

        deptListView.setAdapter(adapter1);




        hospitalKey = getIntent().getStringExtra("hospital");
//        Toast.makeText(this, hospitalKey, Toast.LENGTH_SHORT).show();


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String deptName = deptNameString[position];

                Toast.makeText(DeptListActivity.this, deptName, Toast.LENGTH_SHORT).show();

                Intent hospitalListIntent = new Intent(DeptListActivity.this, HospitalListActivity.class);
                startActivity(hospitalListIntent);

            }
        });


        deptListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String deptName = deptNameString[position];

                Toast.makeText(DeptListActivity.this, deptName, Toast.LENGTH_SHORT).show();

                Intent doctorListIntent = new Intent(DeptListActivity.this, DoctorListActivity.class);
                doctorListIntent.putExtra("dept",deptName);
                doctorListIntent.putExtra("hospital",hospitalKey);
                startActivity(doctorListIntent);



            }
        });
    }
}