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

public class AdminAddHospitalActivity extends AppCompatActivity {

    TextView addButton, deleteButton;
    EditText emailEditTex,userName, longitudeEditText, latitudeEditText;
    Spinner locationSpinner, statusSpinner;
    private String placeName, userStatus;
    private ProgressDialog loadingbar;
    private String[] placeNameString;

    String checkEmail;
    private String savecurrentdate;
    private String savecurrentTime, postrandomName;


    private DatabaseReference HospitalInfoRef, UsersRef;
    private String nodeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_hospital);


        HospitalInfoRef = FirebaseDatabase.getInstance().getReference().child("HospitalInfo");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        addButton = findViewById(R.id.addButtonAddHospitalId);
        deleteButton = findViewById(R.id.deleteAddHospitalId);
        emailEditTex = findViewById(R.id.emailAddHospitalId);
        userName = (EditText) findViewById(R.id.nameAddHospitalId);
        longitudeEditText = (EditText) findViewById(R.id.longitudeAddHospitalId);
        latitudeEditText = (EditText) findViewById(R.id.latitudeAddHospitalId);

        loadingbar = new ProgressDialog(this);




        locationSpinner = findViewById(R.id.locationSpinnerAddHospitalId);

        placeNameString = getResources().getStringArray(R.array.placeName);






        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(AdminAddHospitalActivity.this,
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


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEmail();

//                AddHospital();
            }
        });
    }

    private void AddHospital() {




        String email = emailEditTex.getText().toString();
        String username = userName.getText().toString();
        String longitude = longitudeEditText.getText().toString();
        String latitude = latitudeEditText.getText().toString();



        if ("yes".equals(checkEmail))
        {


                loadingbar.setTitle("Creating Profile");
                loadingbar.setMessage("Please wait while we are creating your profile");
                loadingbar.show();
                loadingbar.setCanceledOnTouchOutside(true);

                HashMap donorProfileMap = new HashMap();
                donorProfileMap.put("userName",username);
                donorProfileMap.put("longitude",longitude);
                donorProfileMap.put("latitude",latitude);
                donorProfileMap.put("email",email);
                donorProfileMap.put("location",placeName);


//                Calendar callForDate = Calendar.getInstance();
//                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
//                savecurrentdate = currentDate.format(callForDate.getTime());
//
//                Calendar callForTime = Calendar.getInstance();
//                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
//                savecurrentTime = currentTime.format(callForTime.getTime());
//
//                postrandomName = savecurrentdate + savecurrentTime;


                HospitalInfoRef.child(nodeName).updateChildren(donorProfileMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if(task.isSuccessful())
                        {
//                        EmptyField();
                            Toast.makeText(AdminAddHospitalActivity.this, "Data is Added", Toast.LENGTH_LONG).show();
                            loadingbar.dismiss();

                        }
                        else
                        {
                            String message = task.getException().getMessage();
                            Toast.makeText(AdminAddHospitalActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
                            loadingbar.dismiss();
                        }
                    }
                });


            }

        else
        {
            Toast.makeText(AdminAddHospitalActivity.this, "The email is invalid", Toast.LENGTH_LONG).show();
        }


    }

    private void CheckEmail() {

        String email = emailEditTex.getText().toString();
        String username = userName.getText().toString();
        String longitude = longitudeEditText.getText().toString();
        String latitude = latitudeEditText.getText().toString();



        if(TextUtils.isEmpty(username))
        {
            userName.setError("Please enter name");
            userName.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(longitude))
        {
            longitudeEditText.setError("Please enter info");
            longitudeEditText.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(latitude))
        {
            latitudeEditText.setError("Please enter info");
            latitudeEditText.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(email))
        {
            emailEditTex.setError("Please enter email");
            emailEditTex.requestFocus();
            return;
        }
        else if(placeName.equals("Select Place"))
        {
            Toast.makeText(AdminAddHospitalActivity.this, "Please select location", Toast.LENGTH_LONG).show();
            locationSpinner.setFocusable(true);
            locationSpinner.requestFocus();
        }

        else {


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

                            AddHospital();

                        } else {

                            checkEmail = " ";
                            Toast.makeText(AdminAddHospitalActivity.this, "The email is invalid", Toast.LENGTH_LONG).show();

                        }

//
//                if (dataSnapshot.exists()) {
////                    checkEmail = "yes";
//
//                    nodeName = dataSnapshot.getKey();
//                    checkEmail = "yes";
//
//                    AddHospital();

//                    if (dataSnapshot.hasChild("userName"))//the user is authenticated but does not have a profile
//                    {
//
//                        nodeName = dataSnapshot.getKey();
//                        checkEmail = "yes";
//                    }
                    }
//                else {
//
//                    checkEmail = " ";
//                    Toast.makeText(AdminAddHospitalActivity.this, "The email is invalid", Toast.LENGTH_LONG).show();
//
//                }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(AdminAddHospitalActivity.this, "Error Occured", Toast.LENGTH_LONG).show();

                }
            });
        }

    }
}