package com.platelet;

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

public class SelectAdminActivity extends AppCompatActivity {

    private CardView hospitalCard, doctorCard, ambulanceCard, adminCard;
    TextView ambulanceHospital, ambulancePersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_admin);

        ambulanceCard = findViewById(R.id.selectAmbulanceAdminCardId);
        ambulanceHospital = findViewById(R.id.hospitalAmbulanceAdmin);
        ambulancePersonal = findViewById(R.id.personalAmbulanceAdmin);
        adminCard = findViewById(R.id.selectAdminCard);
        doctorCard = findViewById(R.id.selectDoctorAdminCard);
        hospitalCard = findViewById(R.id.selectHospitalAdminCard);

        ambulanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ambulanceHospital.setVisibility(TextView.VISIBLE);
                ambulancePersonal.setVisibility(TextView.VISIBLE);

                ambulancePersonal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent ambulanceIntent = new Intent(SelectAdminActivity.this,AmbulanceAdminActivity.class);
                        startActivity(ambulanceIntent);
                        Toast.makeText(SelectAdminActivity.this, "Personal is clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                ambulanceHospital.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent ambulanceIntent = new Intent(SelectAdminActivity.this,AmbulanceAdminActivity.class);
                        startActivity(ambulanceIntent);
                        Toast.makeText(SelectAdminActivity.this, "Hospital is clicked", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        adminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(SelectAdminActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_password_layout, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                EditText passwordEditText;

                passwordEditText = dialogView.findViewById(R.id.passwordId);


                TextView cancelButton = dialogView.findViewById(R.id.cancelProfileButton);
                TextView updateButton = dialogView.findViewById(R.id.updateProfileButton);


                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.cancel();
                        Toast.makeText(SelectAdminActivity.this, "Cancel Button is clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String passwordString;

                        passwordString = passwordEditText.getText().toString();

                        if ("1234567".equals(passwordString))
                        {

                            Intent adminIntent = new Intent(SelectAdminActivity.this,AdminActivity.class);
                            startActivity(adminIntent);
                        }
                        else
                        {
                            alertDialog.cancel();
                        }


                    }
                });

                alertDialog.show();

            }
        });


        doctorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent docIntent = new Intent(SelectAdminActivity.this,DoctorAdminActivity.class);
                startActivity(docIntent);


            }
        });

        hospitalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent HosIntent = new Intent(SelectAdminActivity.this,HospitalAdminActivity.class);
                startActivity(HosIntent);


            }
        });



    }
}