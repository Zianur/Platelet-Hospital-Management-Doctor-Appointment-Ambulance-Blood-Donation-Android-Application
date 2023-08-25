package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class AmbulanceAdminActivity extends AppCompatActivity {

    CardView ambulanceProfileCard;
    TextView driverNameTextView, ambulanceLocationTextView, phonenNumberTextView;
    String driverName, ambulanceLocation, phoneNumber;

    private DatabaseReference AmbulanceInfoRef;
    private FirebaseAuth mAuth;
    String CurrentUserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_admin);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserid = mAuth.getCurrentUser().getUid();
        AmbulanceInfoRef = FirebaseDatabase.getInstance().getReference().child("AmbulanceList");
        ambulanceProfileCard = findViewById(R.id.ambulanceProfileCardId);
        driverNameTextView = findViewById(R.id.nameAAId);
        ambulanceLocationTextView = findViewById(R.id.locationAAId);
        phonenNumberTextView = findViewById(R.id.phoneAAId);


        AmbulanceInfoRef.child(CurrentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if(snapshot.exists())
                {

                    Toast.makeText(AmbulanceAdminActivity.this, "DATA EXISTS", Toast.LENGTH_SHORT).show();

                    if(snapshot.hasChild("userName"))
                    {
                        driverName = snapshot.child("userName").getValue().toString();//getting the name from the database
                        //setting the full name
                        driverNameTextView.setText(driverName);
                    }

                    if(snapshot.hasChild("phoneNumber"))
                    {
                        phoneNumber = snapshot.child("phoneNumber").getValue().toString();//getting the name from the database
                        //setting the full name
                        phonenNumberTextView.setText(phoneNumber);
                    }

                    if(snapshot.hasChild("location"))
                    {
                        ambulanceLocation = snapshot.child("location").getValue().toString();//getting the name from the database
                        //setting the full name
                        ambulanceLocationTextView.setText(ambulanceLocation);
                    }


                    else
                    {
                        Toast.makeText(AmbulanceAdminActivity.this, "Profile does not exist", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ambulanceProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder builder = new AlertDialog.Builder(AmbulanceAdminActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_ambulanceprofilecard_layout, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                EditText nameEditText,locationEditText, phoneEditText;

                nameEditText = dialogView.findViewById(R.id.nameAPId);
                locationEditText = dialogView.findViewById(R.id.locationAPId);
                phoneEditText = dialogView.findViewById(R.id.phoneAPId);

                nameEditText.setText(driverName);
                locationEditText.setText(ambulanceLocation);
                phoneEditText.setText(phoneNumber);

                TextView cancelButton = dialogView.findViewById(R.id.cancelProfileButton);
                TextView updateButton = dialogView.findViewById(R.id.updateProfileButton);


                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.cancel();
                        Toast.makeText(AmbulanceAdminActivity.this, "Cancel Button is clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        driverName = nameEditText.getText().toString();
                        ambulanceLocation = locationEditText.getText().toString();
                        phoneNumber = phoneEditText.getText().toString();


                        HashMap donorProfileMap = new HashMap();
                        donorProfileMap.put("userName", driverName);
                        donorProfileMap.put("phoneNumber", phoneNumber);
                        donorProfileMap.put("location", ambulanceLocation);


                        AmbulanceInfoRef.child(CurrentUserid).updateChildren(donorProfileMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                                if (task.isSuccessful()) {

                                    alertDialog.cancel();
                                    Toast.makeText(AmbulanceAdminActivity.this, "New profile has been saved", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(AmbulanceAdminActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                });

                alertDialog.show();

            }
        });



    }
}