package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AmbulanceActivity extends AppCompatActivity implements AmbulanceAdapter.OnAmbulanceClickListener {


    Spinner  locationSpinner;
    private String[] placeNameString, bloodGroupString;
    private RecyclerView ambulanceRecyclerView;
    TextView searchButton;

    private ArrayList<AmbulanceModule> ambulanceModuleArrayList;
    private AmbulanceAdapter ambulanceAdapter;


    private DatabaseReference AmbulanceRef;
    private String placeName, ambulancePhnNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance);

        AmbulanceRef = FirebaseDatabase.getInstance().getReference().child("AmbulanceList");

        locationSpinner = findViewById(R.id.locationAmbulanceId);
        ambulanceRecyclerView =  findViewById(R.id.ambulanceRecyclerView);
        searchButton =  findViewById(R.id.searchAmbulancelist);


        placeNameString = getResources().getStringArray(R.array.placeName);
        bloodGroupString = getResources().getStringArray(R.array.bloodType);


        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(AmbulanceActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                placeNameString);



        locationSpinner.setAdapter(placeAdapter);


        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                placeName = placeNameString[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        ambulanceRecyclerView.hasFixedSize();
        ambulanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ambulanceModuleArrayList = new ArrayList<>();
//        donorAdapter = new DonorAdapter(this, donorModuleCLassArrayList);
//
//        donorRecylerView.setAdapter(donorAdapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambulanceModuleArrayList.clear();
                SearchAmbulance();
            }
        });


    }


    private void SearchAmbulance() {

        Query query = AmbulanceRef.orderByChild("location").startAt(placeName).endAt(placeName);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    AmbulanceModule ambulanceModule = new AmbulanceModule();


                    ambulanceModule.setUserName(dataSnapshot.child("userName").getValue().toString());
                    ambulanceModule.setPhoneNumber(dataSnapshot.child("phoneNumber").getValue().toString());

                    ambulanceModuleArrayList.add(ambulanceModule);

                }

                ambulanceAdapter = new AmbulanceAdapter(AmbulanceActivity.this, ambulanceModuleArrayList);
                ambulanceRecyclerView.setAdapter(ambulanceAdapter);
                ambulanceAdapter.setOnAmbulanceClickListener(AmbulanceActivity.this);
                ambulanceAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void OnAmbulanceClick(int position) {


        AmbulanceModule ambulanceModule = ambulanceModuleArrayList.get(position);

        String phnNumber = ambulanceModule.getPhoneNumber();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", phnNumber);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, phnNumber, Toast.LENGTH_SHORT).show();



        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+phnNumber));
        startActivity(callIntent);
    }
}