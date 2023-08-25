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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddAmbulanceActiviy extends AppCompatActivity {

    TextView addButton, deleteButton;
    EditText emailEditTex, userName, userPhone, areaEditText;
    Spinner locationSpinner, statusSpinner;
    private String placeName, userStatus;
    private ProgressDialog loadingbar;
    private String[] placeNameString;

    String checkEmail;
    private String savecurrentdate;
    private String savecurrentTime, postrandomName;


    private DatabaseReference AmbulanceRef, UsersRef;
    private String nodeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_ambulance_activiy);

        AmbulanceRef = FirebaseDatabase.getInstance().getReference().child("AmbulanceList");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        addButton = findViewById(R.id.addButtonAddAmbulance);
        deleteButton = findViewById(R.id.deleteAmbulanceAdminId);
        emailEditTex = findViewById(R.id.emailAddAmbulance);
        userName = (EditText) findViewById(R.id.nameAddAmbulanceId);
        userPhone = (EditText) findViewById(R.id.phoneAddAmbulanceId);

        loadingbar = new ProgressDialog(this);


        locationSpinner = findViewById(R.id.locationSpinnerAddAmbulanceId);

        placeNameString = getResources().getStringArray(R.array.placeName);


        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(AdminAddAmbulanceActiviy.this,
                R.layout.spinner_view, R.id.spinnerLayoutId,
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


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             CheckEmail();
            }
        });
    }

    private void AddAmbulance() {

//        CheckEmail();

        String email = emailEditTex.getText().toString();
        String username = userName.getText().toString();
        String phone_number = userPhone.getText().toString();


        if ("yes".equals(checkEmail)) {

            loadingbar.setTitle("Creating Profile");
            loadingbar.setMessage("Please wait while we are creating your profile");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);

            HashMap donorProfileMap = new HashMap();
            donorProfileMap.put("userName", username);
            donorProfileMap.put("phoneNumber", phone_number);
            donorProfileMap.put("email", email);
            donorProfileMap.put("location", placeName);


//          Calendar callForDate = Calendar.getInstance();
//          SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
//          savecurrentdate = currentDate.format(callForDate.getTime());
//
//          Calendar callForTime = Calendar.getInstance();
//          SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
//          savecurrentTime = currentTime.format(callForTime.getTime());
//
//          postrandomName = savecurrentdate + savecurrentTime;


            AmbulanceRef.child(nodeName).updateChildren(donorProfileMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()) {
//                        EmptyField();
                        Toast.makeText(AdminAddAmbulanceActiviy.this, "Data is Added", Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();

                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(AdminAddAmbulanceActiviy.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }
                }
            });


        }
        else {
            Toast.makeText(AdminAddAmbulanceActiviy.this, "The email is invalid", Toast.LENGTH_LONG).show();
        }


    }

    private void CheckEmail() {

        String email = emailEditTex.getText().toString();
        String username = userName.getText().toString();
        String phone_number = userPhone.getText().toString();


        if (TextUtils.isEmpty(username)) {
            userName.setError("Please enter name");
            userName.requestFocus();
            return;
        } else if (TextUtils.isEmpty(phone_number)) {
            userPhone.setError("Please enter number");
            userPhone.requestFocus();
            return;
        } else if (TextUtils.isEmpty(email)) {
            emailEditTex.setError("Please enter email");
            emailEditTex.requestFocus();
            return;
        } else if (placeName.equals("Select Place")) {
            Toast.makeText(AdminAddAmbulanceActiviy.this, "Please select location", Toast.LENGTH_LONG).show();
            locationSpinner.setFocusable(true);
            locationSpinner.requestFocus();
        } else {


//            Query query = UsersRef.orderByChild("email").startAt(email).endAt(email);
            Query query = UsersRef.orderByChild("email").equalTo(email);


            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if (snapshot.exists()) {
//                    checkEmail = "yes";

                            nodeName = snapshot.getKey();
                            checkEmail = "yes";

                            AddAmbulance();

                        } else {

                            checkEmail = " ";
                            Toast.makeText(AdminAddAmbulanceActiviy.this, "The email is invalid", Toast.LENGTH_LONG).show();

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(AdminAddAmbulanceActiviy.this, "Error Occured", Toast.LENGTH_LONG).show();

                }
            });
        }
    }
}