package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.icu.util.Calendar;
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
import java.util.HashMap;

public class     AdminAddDocActivity extends AppCompatActivity {

    EditText nameEditText, informationEditText, paymentEditText;
    TextView addButton;
    Spinner deptSpinner;
    private String deptName, userStatus;
    private ProgressDialog loadingbar;
    private String[] deptNameString;

    private String savecurrentdate;
    private String savecurrentTime, postrandomName;

    private FirebaseAuth mAuth;
    private DatabaseReference DoctorListRef;
    String CurrentUserid, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_doc);


        mAuth = FirebaseAuth.getInstance();

        CurrentUserid = mAuth.getCurrentUser().getUid();//getting authentication unique id

        //creating a node/child under User with that authentication unique id in database
        DoctorListRef = FirebaseDatabase.getInstance().getReference().child("DoctorList");


        nameEditText = findViewById(R.id.nameAddDoctorId);
        informationEditText = findViewById(R.id.informationAddDoctorId);
        paymentEditText = findViewById(R.id.paymentAddDoctorId);
        addButton = findViewById(R.id.addButtonAddDoctorId);


        loadingbar = new ProgressDialog(this);


        deptSpinner = findViewById(R.id.deptSpinnerAddDoctorId);

        deptNameString = getResources().getStringArray(R.array.dept); //getting the string


        ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(AdminAddDocActivity.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                deptNameString);//putting the string to the layout through adapter


        deptSpinner.setAdapter(deptAdapter);//implementing the adapter to the spinner


        deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                deptName = deptNameString[position];



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddDoctor();

            }
        });


    }

    private void AddDoctor() {

        String username = nameEditText.getText().toString();
        String information = informationEditText.getText().toString();
        String payment = paymentEditText.getText().toString();


        if(TextUtils.isEmpty(username))
        {
            nameEditText.setError("Please enter your name");
            nameEditText.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(information))
        {
            informationEditText.setError("Please enter your phone number");
            informationEditText.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(payment))
        {
            paymentEditText.setError("Please enter a valid email address");
            paymentEditText.requestFocus();
            return;
        }
        else if(deptName.equals("Select Dept"))
        {
            Toast.makeText(AdminAddDocActivity.this, "Please select your bloodgroup", Toast.LENGTH_LONG).show();
            deptSpinner.setFocusable(true);
            deptSpinner.requestFocus();
        }
        else
        {

            loadingbar.setTitle("Creating Profile");
            loadingbar.setMessage("Please wait while we are creating your profile");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);


            HashMap userMap = new HashMap();
            userMap.put("userName",username);
            userMap.put("information",information);
            userMap.put("payment",payment);
            userMap.put("time","6pm - 9pm");

            Calendar callForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                savecurrentdate = currentDate.format(callForDate.getTime());

                Calendar callForTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                savecurrentTime = currentTime.format(callForTime.getTime());

                postrandomName = username+" "+savecurrentdate + savecurrentTime;




            DoctorListRef.child(CurrentUserid).child(deptName).child(postrandomName).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful())
                    {
//                        CreateDonorProfile();
//                        SendUserToMainActivity();
                        Toast.makeText(AdminAddDocActivity.this,"Added Successfully",Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(AdminAddDocActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
                        loadingbar.dismiss();
                    }
                }
            });
        }

    }
}