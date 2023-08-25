package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HospitalListActivity extends AppCompatActivity implements HospitalListAdapter.OnHospitalClickListener {

    CardView noteHospitalList;

    private ArrayList<HospitalListModule> hospitalListModuleArrayList;
    private HospitalListAdapter hospitalListAdapter;

    private DatabaseReference HospitalRef;

    private RecyclerView hospitalRecyclerView;
    private String placeName, key;
    private String hospitalKey;

    private AutoCompleteTextView autoCompleteTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        HospitalRef = FirebaseDatabase.getInstance().getReference().child("HospitalInfo");

        hospitalRecyclerView =  findViewById(R.id.hospitalListRecyclerView);
        noteHospitalList =  findViewById(R.id.noteHospitalListId);
        autoCompleteTextView = findViewById(R.id.hospital_search_id);


        placeName = getIntent().getStringExtra("place");
        Toast.makeText(this, placeName, Toast.LENGTH_SHORT).show();


        hospitalRecyclerView.hasFixedSize();
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(HospitalListActivity.this));

        hospitalListModuleArrayList = new ArrayList<>();






        ShowHospitalList();




//        CheckExistence();

//        hospitalListAdapter.setOnHospitalClickListener(new HospitalListAdapter.OnHospitalClickListener() {
//            @Override
//            public void OnHospitalClick(int position) {
//
//                HospitalListModule slelectedHospital = hospitalListModuleArrayList.get(position);
//
//                hospitalKey = slelectedHospital.getNodeKey();
//
//                Intent deptIntent = new Intent(HospitalListActivity.this,DeptListActivity.class);
//                deptIntent.putExtra("hospital",hospitalKey);
//                startActivity(deptIntent);
//
//            }
//        });


//
//        if ("yes".equals(key))
//        {
//            ShowHospitalList();
////
//            noteHospitalList.setVisibility(View.VISIBLE);
//        }
//
//        else
//        {
//            ShowHospitalList();
////            noteHospitalList.setVisibility(View.VISIBLE);
//        }






    }

    private void CheckExistence() {


        Query query = HospitalRef.orderByChild("location").equalTo(placeName);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                 if (dataSnapshot.exists())
                 {
                     key = "yes";
                 }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ShowHospitalList() {


        Query query = HospitalRef.orderByChild("location").equalTo(placeName);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    HospitalListModule hospitalListModule = new HospitalListModule();

                    hospitalListModule.setUserName(dataSnapshot.child("userName").getValue().toString());

                    hospitalListModule.setNodeKey(dataSnapshot.getKey());//getting the nodeKey




                    hospitalListModuleArrayList.add(hospitalListModule);


                }

//                hospitalRecyclerView.hasFixedSize();
//                hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(HospitalListActivity.this));

                if(hospitalListModuleArrayList.isEmpty())
                {
                    noteHospitalList.setVisibility(View.VISIBLE);
                }
                else
                {
                    hospitalListAdapter = new HospitalListAdapter(HospitalListActivity.this,hospitalListModuleArrayList);
                    hospitalRecyclerView.setAdapter(hospitalListAdapter);
                    hospitalListAdapter.setOnHospitalClickListener(HospitalListActivity.this);//must need for the onclick
                    hospitalListAdapter.notifyDataSetChanged();
                    

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void OnHospitalClick(int position) {


        HospitalListModule selelectedHospital = hospitalListModuleArrayList.get(position);

        hospitalKey = selelectedHospital.getNodeKey();

        Intent deptIntent = new Intent(HospitalListActivity.this,DeptListActivity.class);
        deptIntent.putExtra("hospital",hospitalKey);
        startActivity(deptIntent);

    }
}