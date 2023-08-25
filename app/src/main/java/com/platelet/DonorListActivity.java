package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonorListActivity extends AppCompatActivity implements DonorAdapter.OnDonorClickListener {

    Spinner bloodGroupSpinner, locationSpinner;
    private String[] placeNameString, bloodGroupString;
    private RecyclerView donorRecylerView;
    TextView searchButton;

    private ArrayList<DonorModuleCLass> donorModuleCLassArrayList;
    private DonorAdapter donorAdapter;


    private DatabaseReference BloodProfileRef;
    private String placeName, bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        BloodProfileRef = FirebaseDatabase.getInstance().getReference().child("DonorList");

        locationSpinner = findViewById(R.id.locationDonorListBloodId);
        bloodGroupSpinner = findViewById(R.id.bloodGroupDonorListBloodId);
        donorRecylerView =  findViewById(R.id.donorListRecyclerView);
        searchButton =  findViewById(R.id.searchButtonDonorlist);


        placeNameString = getResources().getStringArray(R.array.placeName);
        bloodGroupString = getResources().getStringArray(R.array.bloodType);

//        TextView deleteBP = findViewById(R.id.editDLLId);
//        deleteBP.setVisibility(View.VISIBLE);




        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(DonorListActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                placeNameString);


        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<String>(DonorListActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                bloodGroupString);


        locationSpinner.setAdapter(placeAdapter);
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);


        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                placeName = placeNameString[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bloodGroup = bloodGroupString[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

////        placeListView.setAdapter(placeAdapter1);
////
//
//        placeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                String placeName = placeNameString[position];
//
//                Toast.makeText(DonorListActivity.this, placeName + " is clicked", Toast.LENGTH_SHORT).show();
//
//
//
//
//            }
//        });


        donorRecylerView.hasFixedSize();
        donorRecylerView.setLayoutManager(new LinearLayoutManager(this));

        donorModuleCLassArrayList = new ArrayList<>();
//        donorAdapter = new DonorAdapter(this, donorModuleCLassArrayList);
//
//        donorRecylerView.setAdapter(donorAdapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donorModuleCLassArrayList.clear();
                SearchDonor();
            }
        });



    }

    private void SearchDonor() {


        BloodProfileRef.child(bloodGroup).child(placeName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    DonorModuleCLass donorModuleCLass = new DonorModuleCLass();

                    donorModuleCLass.setUserName(dataSnapshot.child("userName").getValue().toString());
                    donorModuleCLass.setPhoneNumber(dataSnapshot.child("phoneNumber").getValue().toString());
                    donorModuleCLass.setNumberofDonation(dataSnapshot.child("numberofDonation").getValue().toString());
                    donorModuleCLass.setStatus(dataSnapshot.child("status").getValue().toString());

                    donorModuleCLassArrayList.add(donorModuleCLass);

                }

                donorAdapter = new DonorAdapter(DonorListActivity.this, donorModuleCLassArrayList);
                donorRecylerView.setAdapter(donorAdapter);
                donorAdapter.setOnDonorClickListenerClickListener(DonorListActivity.this);
                donorAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void OnDonorClickListener(int position) {


        DonorModuleCLass donorModuleCLass = donorModuleCLassArrayList.get(position);

        String phnNumber = donorModuleCLass.getPhoneNumber();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", phnNumber);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, phnNumber, Toast.LENGTH_SHORT).show();



        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+phnNumber));
        startActivity(callIntent);

    }
}