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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DonateBloodActivity extends AppCompatActivity implements DonateBloodAdapter.OnDonateBloodClickListener {


    Spinner bloodGroupSpinner, locationSpinner;
    private String[] placeNameString, bloodGroupString;
    private RecyclerView donateRecyclerView;
    TextView searchButton;

    private ArrayList<DonateBloodModule> donateBloodModuleArrayList;
    private DonateBloodAdapter donateBloodAdapter;


    private DatabaseReference BloodRequestRef;
    private String placeName, bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_blood);

        BloodRequestRef = FirebaseDatabase.getInstance().getReference().child("BloodRequest");

        locationSpinner = findViewById(R.id.locationDonateBloodId);
        bloodGroupSpinner = findViewById(R.id.bloodGroupDonateBloodId);
        donateRecyclerView =  findViewById(R.id.donateBloodRecyclerView);
        searchButton =  findViewById(R.id.searchButtonDonateBloodId);


        placeNameString = getResources().getStringArray(R.array.placeName);
        bloodGroupString = getResources().getStringArray(R.array.bloodType);

        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(DonateBloodActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                placeNameString);


        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<String>(DonateBloodActivity.this,
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


        donateRecyclerView.hasFixedSize();
        donateRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        donateBloodModuleArrayList = new ArrayList<>();



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                donateBloodModuleArrayList.clear();

                SearchBloodRequest();
            }
        });



    }

    private void SearchBloodRequest() {

        BloodRequestRef.child(bloodGroup).child(placeName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    DonateBloodModule donateBloodModule = new DonateBloodModule();

                    donateBloodModule.setUserName(dataSnapshot.child("userName").getValue().toString());
                    donateBloodModule.setBagNumber(dataSnapshot.child("bagNumber").getValue().toString());
                    donateBloodModule.setPhoneNumber(dataSnapshot.child("phoneNumber").getValue().toString());
                    donateBloodModule.setAddress(dataSnapshot.child("address").getValue().toString());
                    donateBloodModule.setDescription(dataSnapshot.child("description").getValue().toString());
                    donateBloodModule.setDate(dataSnapshot.child("date").getValue().toString());
                    donateBloodModule.setEmail(dataSnapshot.child("email").getValue().toString());


                    donateBloodModule.setUserId(dataSnapshot.child("userId").getValue().toString());



                    donateBloodModuleArrayList.add(donateBloodModule);

                }

                donateBloodAdapter = new DonateBloodAdapter(DonateBloodActivity.this, donateBloodModuleArrayList);
                donateRecyclerView.setAdapter(donateBloodAdapter);
                donateBloodAdapter.setOnDonateBloodClickListener(DonateBloodActivity.this);
                donateBloodAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void OnDonateBloodClick(int position) {

        DonateBloodModule donateBloodModule = donateBloodModuleArrayList.get(position);

        String phnNumber = donateBloodModule.getPhoneNumber();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", phnNumber);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, phnNumber, Toast.LENGTH_SHORT).show();




        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+phnNumber));
        startActivity(callIntent);
    }
}