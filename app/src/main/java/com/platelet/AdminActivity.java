package com.platelet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    CardView addHospitalCard, addDocCard, addAmbulanceCard, bloodCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addAmbulanceCard = findViewById(R.id.addAmbulanceAdminCard);
        addDocCard = findViewById(R.id.addDoctorAdminCard);
        addHospitalCard = findViewById(R.id.addHospitalAdminCard);
        bloodCard = findViewById(R.id.bloodAdminCard);

        addDocCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent docIntent = new Intent(AdminActivity.this,AdminAddDocActivity.class);
                startActivity(docIntent);
            }
        });

        addHospitalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent hosIntent = new Intent(AdminActivity.this,AdminAddHospitalActivity.class);
                startActivity(hosIntent);
            }
        });

        addAmbulanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ambIntent = new Intent(AdminActivity.this,AdminAddAmbulanceActiviy.class);
                startActivity(ambIntent);
            }
        });

        bloodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bloodIntent = new Intent(AdminActivity.this,AdminBloodActivity.class);
                startActivity(bloodIntent);
            }
        });
    }
}