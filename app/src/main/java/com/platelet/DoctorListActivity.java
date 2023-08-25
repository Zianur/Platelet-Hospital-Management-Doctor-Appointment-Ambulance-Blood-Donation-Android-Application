package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.bluetooth.le.PeriodicAdvertisingParameters;
import android.content.DialogInterface;
import android.graphics.drawable.shapes.OvalShape;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DoctorListActivity extends AppCompatActivity implements DoctorListAdapter.OnRecyclerViewClickListener,
        DatePickerDialog.OnDateSetListener {

    CardView noteHospitalList;

    private ArrayList<DoctorListModule> doctorListModuleArrayList;
    private DoctorListAdapter doctorListAdapter;

    private DatabaseReference DoctorListRef, HospitalRef, UserRef, AppointmentHospitalRef, AppointmentUserRef, NotificationHospitalRef, StateRef;
    private FirebaseAuth mAuth;

    private RecyclerView doctorRecyclerView;

    private String hospitalKey,deptKey, payment, doctorName, date,hospitalName,patientName, patientPhoneNumber,CurrentUserid, stateCount;
    private TextView doctorNameTextView, paymentTextView, dateTextView, deptTextView;
    private String savecurrentdate, userPostion, hospitalPostion, email;
    private TextView hospitalNameTextView, deptNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        DoctorListRef = FirebaseDatabase.getInstance().getReference().child("DoctorList");
        HospitalRef = FirebaseDatabase.getInstance().getReference().child("HospitalInfo");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        AppointmentHospitalRef = FirebaseDatabase.getInstance().getReference().child("AppointmentHospital");
        AppointmentUserRef = FirebaseDatabase.getInstance().getReference().child("AppointmentUser");
        NotificationHospitalRef = FirebaseDatabase.getInstance().getReference().child("HospitalNotification");

        StateRef = FirebaseDatabase.getInstance().getReference().child("HospitalStatistics");

        doctorRecyclerView =  findViewById(R.id.doctorListRecyclerView);
        noteHospitalList =  findViewById(R.id.noteDoctorListId);
        hospitalNameTextView = findViewById(R.id.hospitalNameDoctorlist);
        deptNameTextView = findViewById(R.id.deptNameDoctorlist);


        mAuth = FirebaseAuth.getInstance();
        CurrentUserid = mAuth.getCurrentUser().getUid();





        hospitalKey = getIntent().getStringExtra("hospital");
        deptKey = getIntent().getStringExtra("dept");


        deptNameTextView.setText(deptKey);


        doctorRecyclerView.hasFixedSize();
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(DoctorListActivity.this));

        doctorListModuleArrayList = new ArrayList<>();


        HospitalRef.child(hospitalKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                hospitalName = snapshot.child("userName").getValue().toString();

                hospitalNameTextView.setText(hospitalName);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        GetStateCount();

        GetPosition();

        GetPatientInfo();

        ShowDoctorList();

    }

    private void GetStateCount() {

        StateRef.child(hospitalKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    int count = (int) dataSnapshot.getChildrenCount();
                    stateCount = String.valueOf(count);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ShowDoctorList() {

        DoctorListRef.child(hospitalKey).child(deptKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    DoctorListModule doctorListModule = new DoctorListModule();

                    doctorListModule.setUserName(dataSnapshot.child("userName").getValue().toString());
                    doctorListModule.setInformation(dataSnapshot.child("information").getValue().toString());
                    doctorListModule.setPayment(dataSnapshot.child("payment").getValue().toString());


                    doctorListModuleArrayList.add(doctorListModule);
                }
                if (doctorListModuleArrayList.isEmpty())
                {
                    noteHospitalList.setVisibility(View.VISIBLE);
                }
                else
                {
                    doctorListAdapter = new DoctorListAdapter(DoctorListActivity.this,doctorListModuleArrayList);
                    doctorRecyclerView.setAdapter(doctorListAdapter);
                    doctorListAdapter.setOnRecyclerViewClickListener(DoctorListActivity.this);
                    doctorListAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void OnRecyclerViewClick(int position) {

       ConfirmAppointment(position);
    }

    private void ConfirmAppointment(int position) {

        DoctorListModule selectedDoctor = doctorListModuleArrayList.get(position);

        payment = selectedDoctor.getPayment();
        doctorName = selectedDoctor.getUserName();





        final AlertDialog.Builder builder = new AlertDialog.Builder(DoctorListActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        LayoutInflater layoutInflater = this.getLayoutInflater();//need this layoutinflater to inflate the layout
        View dialogView = layoutInflater.inflate(R.layout.appointment_custom_layout, viewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();


        dateTextView = dialogView.findViewById(R.id.dateACId);
        doctorNameTextView = dialogView.findViewById(R.id.nameACId);
        paymentTextView = dialogView.findViewById(R.id.paymentACId);
        deptTextView = dialogView.findViewById(R.id.departmentACId);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //first calling the datepicker fragment
                DialogFragment datePicker = new DatePickerFragment(); ///this date picking can be done easily
                datePicker.show(getSupportFragmentManager(),"date picker");

                //secondly have to implement the listener in the top then have to get the date in the method


                //there are other ways to implement this datepicker fragment which are easy

            }
        });


        doctorNameTextView.setText(doctorName);
        paymentTextView.setText(payment);
        deptTextView.setText(deptKey);


        TextView cancelButton = dialogView.findViewById(R.id.cancelProfileButton);
        TextView updateButton = dialogView.findViewById(R.id.updateProfileButton);




        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                Toast.makeText(DoctorListActivity.this, "Cancel Button is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                HospitalAppointment();


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

                        HospitalAppointment();

                    }
                    else
                    {

                        AlertDialog alertDialog = new AlertDialog.Builder(DoctorListActivity.this).create();
                        alertDialog.setTitle("Invalid Date");
                        alertDialog.setMessage("Please select a valid date to continue." );
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                alertDialog.cancel();

                            }
                        });

                        alertDialog.show();


                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                alertDialog.cancel();

            }
        });





        alertDialog.show();
    }

    private void HospitalAppointment() {



        HashMap hospitalMap = new HashMap();
        hospitalMap.put("patientName",patientName);
        hospitalMap.put("patientPhoneNumber",patientPhoneNumber);
        hospitalMap.put("doctorName",doctorName);
        hospitalMap.put("deptName",deptKey);
        hospitalMap.put("payment",payment);
        hospitalMap.put("appointmentDate",date);
        hospitalMap.put("userId",CurrentUserid);
        hospitalMap.put("email",email);

//        Calendar callForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
//        savecurrentdate = currentDate.format(callForDate.getTime());
//
//        Calendar callForTime = Calendar.getInstance();
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
//        String savecurrentTime = currentTime.format(callForTime.getTime());
//
//        String postrandomName = savecurrentdate + savecurrentTime;





        AppointmentHospitalRef.child(hospitalKey).child(hospitalPostion).updateChildren(hospitalMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isSuccessful())
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(DoctorListActivity.this).create();
                    alertDialog.setTitle("Appointment Confirmed");
                    alertDialog.setMessage("Before going to the hospital check your appointment. " );
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            alertDialog.cancel();

                        }
                    });

                    alertDialog.show();

                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(DoctorListActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
//
                }

            }
        });



        NotificationHospitalRef.child(hospitalKey).setValue("You have new appointments!!!");
//        StateRef.child(hospitalKey).child(stateCount).setValue("Appointed");


        HashMap userMap = new HashMap();
        userMap.put("hospitalName",hospitalName);
        userMap.put("doctorName",doctorName);
        userMap.put("deptName",deptKey);
        userMap.put("payment",payment);
        userMap.put("appointmentDate",date);
        userMap.put("hospitalId",hospitalKey);
        userMap.put("uniqueKey",hospitalPostion);


        AppointmentUserRef.child(CurrentUserid).child(userPostion).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isSuccessful())
                {
//
                    Toast.makeText(DoctorListActivity.this,"Added Successfully on the USER database",Toast.LENGTH_LONG).show();
//
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(DoctorListActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
//
                }

            }
        });


    }

    private void GetPosition() {

        AppointmentHospitalRef.child(hospitalKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.exists())
                {
                    int lHPosition = (int) snapshot.getChildrenCount();
                    hospitalPostion = String.valueOf(lHPosition +1);
                }
                else
                {
                    hospitalPostion = "1";
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        AppointmentUserRef.child(CurrentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    int lUPosition = (int) snapshot.getChildrenCount();
                    userPostion = String.valueOf(lUPosition + 1);
                }
                else
                {
                    userPostion = "1";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Toast.makeText(this, hospitalPostion + " and  " +userPostion, Toast.LENGTH_SHORT).show();

    }

    private void GetPatientInfo() {

        UserRef.child(CurrentUserid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                patientName = snapshot.child("userName").getValue().toString();
                patientPhoneNumber = snapshot.child("phoneNumber").getValue().toString();
                email = snapshot.child("email").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void ValidateDate() {

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

                //this part is for the profile activity and it works absolutely fine

//                long dateDiff = (endDate- startDate)/(1000*60*60*24);
//
//                if (dateDiff>2)
//                {
//                    dateTextView.setText(String.valueOf(dateDiff));
//                    Toast.makeText(this, "It is a valid date...", Toast.LENGTH_SHORT).show();
//                }


                dateTextView.setText(date);
//                Toast.makeText(this, date, Toast.LENGTH_SHORT).show();

//                HospitalAppointment();




            }
            else
            {
                AlertDialog alertDialog = new AlertDialog.Builder(DoctorListActivity.this).create();
                alertDialog.setTitle("Invalid Date");
                alertDialog.setMessage("Please select a valid date to continue.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        alertDialog.cancel();

                    }
                });

                alertDialog.show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        month = month+1;

        date = dayOfMonth + "/" + month + "/" + year ;


        ValidateDate();

//        dateTextView.setText(date);

    }
}
