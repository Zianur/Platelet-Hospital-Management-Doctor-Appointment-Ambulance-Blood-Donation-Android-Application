package com.platelet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class BloodActivity extends AppCompatActivity {



    private CardView profileCard, requestBloodCard, donateBloodCard, donorListCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);


        profileCard = findViewById(R.id.profileBloodId);
        requestBloodCard = findViewById(R.id.requestDonationId);
        donateBloodCard = findViewById(R.id.donateBloodId);
        donorListCard = findViewById(R.id.donorListId);


        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profileBloodCard = new Intent(BloodActivity.this,ProfileBloodActivity.class);
                startActivity(profileBloodCard);
            }
        });

        requestBloodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent requestBloodActivity = new Intent(BloodActivity.this,RequestBloodActivity.class);
                startActivity(requestBloodActivity);
            }
        });


        donateBloodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent donateBloodCard = new Intent(BloodActivity.this,DonateBloodActivity.class);
                startActivity(donateBloodCard);
            }
        });


        donorListCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent donorListCard = new Intent(BloodActivity.this,DonorListActivity.class);
                startActivity(donorListCard);
            }
        });







    }
}