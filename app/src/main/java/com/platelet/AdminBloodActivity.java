package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminBloodActivity extends AppCompatActivity {

    Spinner bloodGroupSpinner, locationSpinner, statusSpinner;
    private String[] placeNameString, bloodTypeString, statusString;
    private EditText userName, userPhone, donationNumber;
    private String placeName, bloodGroup, userStatus;
    TextView addDonorButton, deleteDonorButton;


    private DatabaseReference BloodProfileRef;
    private ProgressDialog loadingbar;
    String CurrentUserid, email;
    private String savecurrentdate;
    private String savecurrentTime, postrandomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_blood);


        BloodProfileRef = FirebaseDatabase.getInstance().getReference().child("DonorList");



        userName = (EditText) findViewById(R.id.nameAdminBloodId);
        userPhone = (EditText) findViewById(R.id.phoneAdminBloodId);
        donationNumber = (EditText) findViewById(R.id.donationNumberAdminBloodId);
        addDonorButton =  findViewById(R.id.addDonorAdminBloodId);
        deleteDonorButton =  findViewById(R.id.deleteDonorAdminBloodId);



        loadingbar = new ProgressDialog(this);




        locationSpinner = findViewById(R.id.locationSpinnerAdminBloodId);
        statusSpinner = findViewById(R.id.statusSpinnerAdminBloodId);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinnerAdminBloodId);

        placeNameString = getResources().getStringArray(R.array.placeName);
        statusString = getResources().getStringArray(R.array.status);
        bloodTypeString = getResources().getStringArray(R.array.bloodType);



        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(AdminBloodActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                placeNameString);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(AdminBloodActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                statusString);

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(AdminBloodActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                bloodTypeString);

        locationSpinner.setAdapter(placeAdapter);
        statusSpinner.setAdapter(statusAdapter);
        bloodGroupSpinner.setAdapter(bloodAdapter);




        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                placeName = placeNameString[position];



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userStatus = statusString[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bloodGroup = bloodTypeString[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addDonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddDonor();

            }
        });



    }

    private void AddDonor() {


        String username = userName.getText().toString();
        String phone_number = userPhone.getText().toString();
        String number_of_donation = donationNumber.getText().toString();


        if(TextUtils.isEmpty(username))
        {
            userName.setError("Please enter your name");
            userName.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(phone_number))
        {
            userPhone.setError("Please enter your phone number");
            userPhone.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(number_of_donation))
        {
            donationNumber.setError("Please enter a valid number of donation");
            donationNumber.requestFocus();
            return;
        }
        else if(bloodGroup.equals("Select Blood Type"))
        {
            Toast.makeText(AdminBloodActivity.this, "Please select your bloodgroup", Toast.LENGTH_LONG).show();
            bloodGroupSpinner.setFocusable(true);
            bloodGroupSpinner.requestFocus();
        }

        else if(placeName.equals("Select Place"))
        {
            Toast.makeText(AdminBloodActivity.this, "Please select your location", Toast.LENGTH_LONG).show();
            locationSpinner.setFocusable(true);
            locationSpinner.requestFocus();
        }

        else if(userStatus.equals("Select Status"))
        {
            Toast.makeText(AdminBloodActivity.this, "Please select your status", Toast.LENGTH_LONG).show();
            statusSpinner.setFocusable(true);
            statusSpinner.requestFocus();
        }

        else
        {

            loadingbar.setTitle("Creating Profile");
            loadingbar.setMessage("Please wait while we are creating your profile");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);

            HashMap donorProfileMap = new HashMap();
            donorProfileMap.put("userName",username);
            donorProfileMap.put("phoneNumber",phone_number);
            donorProfileMap.put("numberofDonation",number_of_donation);
            donorProfileMap.put("status",userStatus);

            Calendar callForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            savecurrentdate = currentDate.format(callForDate.getTime());

            Calendar callForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            savecurrentTime = currentTime.format(callForTime.getTime());

            postrandomName = savecurrentdate + savecurrentTime;


            BloodProfileRef.child(bloodGroup).child(placeName).child(postrandomName).updateChildren(donorProfileMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful())
                    {
//                        EmptyField();
                        Toast.makeText(AdminBloodActivity.this, "Data is Added", Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();

                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(AdminBloodActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }
                }
            });

        }
    }

    private void EmptyField() {

        userName.setError(" ");
        userPhone.setText(" ");
        donationNumber.setText(" ");
    }
}