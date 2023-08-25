package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HospitalAdminActivity extends AppCompatActivity {

    CardView  addDocCard, hospitalInfoCard, appointmentCard;
    TextView hospitalNameTextView, hospitalLocationTextView, hospitalLatitudeTextView, hospitalLongitudeTextView;
    String hospitalName, hospitalLocation, hospitalLatitude, hospitalLongitude;


    private DatabaseReference HospitalInfoRef;
    private FirebaseAuth mAuth;
    String CurrentUserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_admin);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserid = mAuth.getCurrentUser().getUid();
        HospitalInfoRef = FirebaseDatabase.getInstance().getReference().child("HospitalInfo");

        addDocCard = findViewById(R.id.addHosDocId);
        hospitalInfoCard = findViewById(R.id.hospitalProfileCardId);
        appointmentCard = findViewById(R.id.appointmentCardHospitalId);
        hospitalNameTextView = findViewById(R.id.nameHospitalAdminId);
        hospitalLocationTextView = findViewById(R.id.locationHospitalAdminId);
        hospitalLatitudeTextView = findViewById(R.id.latitudeHospitalId);
        hospitalLongitudeTextView = findViewById(R.id.longitudeHospitalId);



        appointmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent appointmentIntent = new Intent(HospitalAdminActivity.this,HospitalAppointment.class);
                startActivity(appointmentIntent);
            }
        });


       


        HospitalInfoRef.child(CurrentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if(snapshot.exists())
                {
                    Toast.makeText(HospitalAdminActivity.this, "DATA EXISTS", Toast.LENGTH_SHORT).show();

                    if(snapshot.hasChild("userName"))
                    {
                        hospitalName = snapshot.child("userName").getValue().toString();//getting the name from the database
                        //setting the full name
                        hospitalNameTextView.setText(hospitalName);
                    }

                    if(snapshot.hasChild("longitude"))
                    {
                        hospitalLongitude = snapshot.child("longitude").getValue().toString();//getting the name from the database
                        //setting the full name
                        hospitalLongitudeTextView.setText(hospitalLongitude);
                    }

                    if(snapshot.hasChild("latitude"))
                    {
                       hospitalLatitude = snapshot.child("latitude").getValue().toString();//getting the name from the database
                        //setting the full name
                        hospitalLatitudeTextView.setText(hospitalLatitude);
                    }

                    if(snapshot.hasChild("location"))
                    {
                        hospitalLocation = snapshot.child("location").getValue().toString();//getting the name from the database
                        //setting the full name
                        hospitalLocationTextView.setText(hospitalLocation);
                    }
                    else
                    {
                        Toast.makeText(HospitalAdminActivity.this, "Profile does not exist", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        hospitalInfoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(HospitalAdminActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_hospitalprofileinfo_layout, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                EditText hospitalNameEditText,hospitalLocationEditText, hospitalLatitudeEditText, hospitalLongitudeEditText;

                hospitalNameEditText = dialogView.findViewById(R.id.hospitalNameCLId);
                hospitalLocationEditText = dialogView.findViewById(R.id.hospitalLocationCLId);
                hospitalLatitudeEditText = dialogView.findViewById(R.id.hospitalLatitudeCLId);
                hospitalLongitudeEditText = dialogView.findViewById(R.id.hospitalLongitudeCLId);


                hospitalNameEditText.setText(hospitalName);
                hospitalLocationEditText.setText(hospitalLocation);
                hospitalLatitudeEditText.setText(hospitalLatitude);
                hospitalLongitudeEditText.setText(hospitalLongitude);


                TextView cancelButton = dialogView.findViewById(R.id.cancelProfileButton);
                TextView updateButton = dialogView.findViewById(R.id.updateProfileButton);


                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.cancel();
                        Toast.makeText(HospitalAdminActivity.this, "Cancel Button is clicked", Toast.LENGTH_SHORT).show();
                    }
                });


                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        hospitalName = hospitalNameEditText.getText().toString();
                        hospitalLocation = hospitalLocationEditText.getText().toString();
                        hospitalLongitude = hospitalLongitudeEditText.getText().toString();
                        hospitalLatitude = hospitalLatitudeEditText.getText().toString();


                        HashMap donorProfileMap = new HashMap();
                        donorProfileMap.put("userName", hospitalName);
                        donorProfileMap.put("longitude", hospitalLongitude);
                        donorProfileMap.put("latitude", hospitalLatitude);
                        donorProfileMap.put("location", hospitalLocation);


                        HospitalInfoRef.child(CurrentUserid).updateChildren(donorProfileMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (task.isSuccessful()) {

                                    alertDialog.cancel();
                                    Toast.makeText(HospitalAdminActivity.this, "New profile has been saved", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(HospitalAdminActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }

                });



                alertDialog.show();

                }

        });


        addDocCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent docIntent = new Intent(HospitalAdminActivity.this,AdminAddDocActivity.class);
                startActivity(docIntent);
            }
        });



    }
}