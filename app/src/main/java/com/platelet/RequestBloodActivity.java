package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class RequestBloodActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Spinner bloodGroupSpinner, locationSpinner;
    private String[] placeNameString, bloodTypeString;
    EditText bagNumberEditText, phoneEditText, addressEditText, descriptionEditText;
    TextView dateTextView, sendRequestTextView;
    String date, placeName, bloodType, savecurrentdate, patientEmail, patientName;


    private DatabaseReference BloodRequestRef, UserRef;
    private FirebaseAuth mAuth;
    private String CurrentUserid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserid = mAuth.getCurrentUser().getUid();

        BloodRequestRef = FirebaseDatabase.getInstance().getReference().child("BloodRequest");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");


        locationSpinner = findViewById(R.id.locationRequestBloodId);
        bloodGroupSpinner = findViewById(R.id.bloodGroupRequestBloodId);
        placeNameString = getResources().getStringArray(R.array.placeName);
        bloodTypeString = getResources().getStringArray(R.array.bloodType);

        dateTextView = findViewById(R.id.dateRBId);
        bagNumberEditText = findViewById(R.id.numberofBloodBagsRId);
        phoneEditText = findViewById(R.id.phoneRBId);
        addressEditText = findViewById(R.id.addressRBId);
        descriptionEditText = findViewById(R.id.descriptionRBId);
        sendRequestTextView = findViewById(R.id.sendRequestRBId);




        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(RequestBloodActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                placeNameString);

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(RequestBloodActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                bloodTypeString);

        locationSpinner.setAdapter(placeAdapter);

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

        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                bloodType = bloodTypeString[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerFragment(); ///this date picking can be done easily
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });



        sendRequestTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetUserDetails();

            }
        });



    }

    private void SendRequest() {


        String bagNumber = bagNumberEditText.getText().toString();
        String phoneNumber = phoneEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        if(TextUtils.isEmpty(bagNumber))
        {
            bagNumberEditText.setError("Please enter your name");
            bagNumberEditText.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(phoneNumber))
        {
            phoneEditText.setError("Please enter your phone number");
            phoneEditText.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(address))
        {
            addressEditText.setError("Please enter address");
            addressEditText.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(description))
        {
            descriptionEditText.setError("Please enter description");
            descriptionEditText.requestFocus();
            return;
        }
        else if(bloodType.equals("Select Blood Type"))
        {
            Toast.makeText(RequestBloodActivity.this, "Please select your bloodgroup", Toast.LENGTH_LONG).show();
            bloodGroupSpinner.setFocusable(true);
            bloodGroupSpinner.requestFocus();
        }

        else if(placeName.equals("Select Place"))
        {
            Toast.makeText(RequestBloodActivity.this, "Please select your location", Toast.LENGTH_LONG).show();
            locationSpinner.setFocusable(true);
            locationSpinner.requestFocus();
        }
        else
        {
            HashMap userMap = new HashMap();
            userMap.put("bagNumber",bagNumber);
            userMap.put("phoneNumber",phoneNumber);
            userMap.put("address",address);
            userMap.put("description",description);
            userMap.put("date",date);
            userMap.put("userName",patientName);
            userMap.put("email",patientEmail);
            userMap.put("bloodGroup",bloodType);
            userMap.put("userId",CurrentUserid);


            Calendar callForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            savecurrentdate = currentDate.format(callForDate.getTime());

            Calendar callForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            String savecurrentTime = currentTime.format(callForTime.getTime());

            String postrandomName = savecurrentdate + savecurrentTime;


            BloodRequestRef.child(bloodType).child(placeName).child(postrandomName).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful())
                    {
                        Toast.makeText(RequestBloodActivity.this, "Request send successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(RequestBloodActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
//                        loadingbar.dismiss();
                    }
                }
            });
        }






    }

    private void GetUserDetails() {


        UserRef.child(CurrentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                patientName = snapshot.child("userName").getValue().toString();
                patientEmail = snapshot.child("email").getValue().toString();

                SendRequest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        month = month+1;
        date = dayOfMonth + "/" + month + "/" + year ;




        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        savecurrentdate = simpleDateFormat.format(callForDate.getTime());
        try {
            Date currentDate, appointmentDate;

            //1st have to parse the date into date variable
            currentDate = simpleDateFormat.parse(savecurrentdate);
            appointmentDate = simpleDateFormat.parse(date);

            //secondly have to get the time then do calculation
            long startDate = currentDate.getTime();
            long endDate = appointmentDate.getTime();

            if (startDate<=endDate)
            {

                dateTextView.setText(date);

            }
            else
            {
                Toast.makeText(this, "Please select a valid date...", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}