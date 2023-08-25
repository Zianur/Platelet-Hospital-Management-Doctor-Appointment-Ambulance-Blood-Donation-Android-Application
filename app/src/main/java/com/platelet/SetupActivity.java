package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Set;

public class SetupActivity extends AppCompatActivity {

    Spinner bloodGroupSpinner, locationSpinner, statusSpinner;
    private String[] placeNameString, bloodTypeString, statusString;
    private EditText  userName, userPhone, donationNumber;
    private String placeName, bloodGroup, userStatus;
    TextView setupButton, GBLoginButton;


    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, BloodProfileRef;
    private ProgressDialog loadingbar;
    String CurrentUserid, email;
    private Boolean emailAddressChecker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();

        CurrentUserid = mAuth.getCurrentUser().getUid();//getting authentication unique id

        //creating a node/child under User with that authentication unique id in database
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        BloodProfileRef = FirebaseDatabase.getInstance().getReference().child("DonorList");



        userName = (EditText) findViewById(R.id.nameSignUpId);
        userPhone = (EditText) findViewById(R.id.phoneSignUpId);
        donationNumber = (EditText) findViewById(R.id.donationNumSignUpId);
        setupButton =  findViewById(R.id.setup_button_Id);
        GBLoginButton =  findViewById(R.id.back_to_login_Id);



        loadingbar = new ProgressDialog(this);




        locationSpinner = findViewById(R.id.locationSignUpId);
        statusSpinner = findViewById(R.id.statusSpinnerSignUp);
        bloodGroupSpinner = findViewById(R.id.bloodTypeSignUpId);

        placeNameString = getResources().getStringArray(R.array.placeName);
        statusString = getResources().getStringArray(R.array.status);
        bloodTypeString = getResources().getStringArray(R.array.bloodType);



        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(SetupActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                placeNameString);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(SetupActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                statusString);

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(SetupActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                bloodTypeString);

        locationSpinner.setAdapter(placeAdapter);
        statusSpinner.setAdapter(statusAdapter);
        bloodGroupSpinner.setAdapter(bloodAdapter);




        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                placeName = placeNameString[position];

                Toast.makeText(SetupActivity.this, placeName, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userStatus = statusString[position];

                Toast.makeText(SetupActivity.this, userStatus, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bloodGroup = bloodTypeString[position];

                Toast.makeText(SetupActivity.this, bloodGroup, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyEmailAddress();


            }
        });

        GBLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               SendUserToLoginActivity();


            }
        });






    }

    private void verifyEmailAddress()
    {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        emailAddressChecker = currentUser.isEmailVerified();

        if (emailAddressChecker)
        {
            SaveAccountSetupInformation();

        }
        else
        {
            sendEmailVerificationmessage();
        }

    }

    private void sendEmailVerificationmessage()
    {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {

            currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(SetupActivity.this, " Your eamil is not verified\nWe have send an email to verify your account\nPlease check your email", Toast.LENGTH_LONG).show();
//                        mAuth.signOut();
                    }
                    else
                    {
                        String error = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
//                        mAuth.signOut();
                    }

                }
            });
        }



    }


    private void SaveAccountSetupInformation()
    {

        String username = userName.getText().toString();
        String phone_number = userPhone.getText().toString();
        String number_of_donation = donationNumber.getText().toString();


//        //getting email from the main activity
//        Intent intent = getIntent();
//        email = intent.getExtras().getString("key");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {

            //geeting user email
            email = currentUser.getEmail();


        }


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
            donationNumber.setError("Please enter a valid email address");
            donationNumber.requestFocus();
            return;
        }
        else if(bloodGroup.equals("Select Blood Type"))
        {
            Toast.makeText(SetupActivity.this, "Please select your bloodgroup", Toast.LENGTH_LONG).show();
            bloodGroupSpinner.setFocusable(true);
            bloodGroupSpinner.requestFocus();
        }

        else if(placeName.equals("Select Place"))
        {
            Toast.makeText(SetupActivity.this, "Please select your location", Toast.LENGTH_LONG).show();
            locationSpinner.setFocusable(true);
            locationSpinner.requestFocus();
        }

        else if(userStatus.equals("Select Status"))
        {
            Toast.makeText(SetupActivity.this, "Please select your status", Toast.LENGTH_LONG).show();
            statusSpinner.setFocusable(true);
            statusSpinner.requestFocus();
        }

        else
        {

            loadingbar.setTitle("Creating Profile");
            loadingbar.setMessage("Please wait while we are creating your profile");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);


            HashMap userMap = new HashMap();
            userMap.put("userName",username);
            userMap.put("phoneNumber",phone_number);
            userMap.put("numberofDonation",number_of_donation);
            userMap.put("location",placeName);
            userMap.put("bloodGroup",bloodGroup);
            userMap.put("status",userStatus);
            userMap.put("email",email);

            UsersRef.child(CurrentUserid).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful())
                    {
//                        CreateDonorProfile();
//                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this,"Your Profile is created successfully",Toast.LENGTH_LONG).show();
//                        loadingbar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
//                        loadingbar.dismiss();
                    }
                }
            });


            HashMap donorProfileMap = new HashMap();
            donorProfileMap.put("userName",username);
            donorProfileMap.put("phoneNumber",phone_number);
            donorProfileMap.put("numberofDonation",number_of_donation);
            donorProfileMap.put("status",userStatus);


            BloodProfileRef.child(bloodGroup).child(placeName).child(CurrentUserid).updateChildren(donorProfileMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this, "Set up completed", Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();

                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }
                }
            });

        }


    }

//    private void CreateDonorProfile()
//    {
//
//
//        String username = userName.getText().toString();
//        String phone_number = userPhone.getText().toString();
//        String number_of_donation = donationNumber.getText().toString();
//
//        HashMap donorProfileMap = new HashMap();
//        donorProfileMap.put("UserName",username);
//        donorProfileMap.put("PhoneNumber",phone_number);
//        donorProfileMap.put("NumberofDonation",number_of_donation);
//        donorProfileMap.put("Location",placeName);
//
//
//        BloodProfileRef.child(CurrentUserid).updateChildren(donorProfileMap).addOnCompleteListener(new OnCompleteListener() {
//            @Override
//            public void onComplete(@NonNull Task task) {
//
//                if(task.isSuccessful())
//                {
//                    Toast.makeText(SetupActivity.this, "Your Profile is created successfully", Toast.LENGTH_LONG).show();
//                }
//                else
//                {
//                    String message = task.getException().getMessage();
//                    Toast.makeText(SetupActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//
//
//
//    }

    private void SendUserToMainActivity()
    {

        Intent mainactivityIntent = new Intent(SetupActivity.this,MainActivity.class);
//        mainactivityIntent.putExtra("key", email );
        mainactivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainactivityIntent);
        finish();
    }


    private void SendUserToLoginActivity()
    {

        mAuth.signOut();
        Intent loginIntent = new Intent(SetupActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

}