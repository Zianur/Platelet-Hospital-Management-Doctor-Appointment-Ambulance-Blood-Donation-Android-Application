package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProfileBloodActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        UserAppointmentAdapter.OnRecyclerViewClickListener{


    LinearLayout linearLayout;

    TextView lastDonateEditText, remainingDaysEditText, appointmentButtnon;
    CardView profileCard;
    Spinner bloodGroupSpinner, locationSpinner, statusSpinner;
    private String[] placeNameString, bloodString, statusString;
    TextView userName, userLocation, userPhone, userBloodgroup, userStatus, donationNumber;

    private ArrayList<UserAppointmentModule> userAppointmentModuleArrayList;
    private UserAppointmentAdapter userAppointmentAdapter;

    private DatabaseReference UsersRef, UserAppointmentRef, NotificationHospitalRef, HospitalAppointmentRef;
    private FirebaseAuth mAuth;
    String CurrentUserid, fullname, phoneNumber, numberofDonation, location, bloodGroup, bloodGroupC,
            statusC, status, placeName, dateLD, key;
    private String savecurrentdate;


    private RecyclerView appointmentRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_blood);

        linearLayout = findViewById(R.id.LLNG);

        lastDonateEditText = findViewById(R.id.lastDonationId);
        remainingDaysEditText = findViewById(R.id.remainingDaysId);
        profileCard = findViewById(R.id.profileCardId);
        userBloodgroup = findViewById(R.id.bloodgroup_profile);
        userName = findViewById(R.id.name_profile);
        userLocation = findViewById(R.id.location_profile);
        userPhone = findViewById(R.id.phone_profile);
        userStatus = findViewById(R.id.status_profile);
        donationNumber = findViewById(R.id.donation_number_profile);
        appointmentButtnon = findViewById(R.id.appointmentButtonId);

        placeNameString = getResources().getStringArray(R.array.placeName);
        bloodString = getResources().getStringArray(R.array.bloodType);
        statusString = getResources().getStringArray(R.array.status);
        appointmentRecyclerView = findViewById(R.id.userAppointmentRecyclerView);





        mAuth = FirebaseAuth.getInstance();
        CurrentUserid = mAuth.getCurrentUser().getUid();


        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserAppointmentRef = FirebaseDatabase.getInstance().getReference().child("AppointmentUser");
        HospitalAppointmentRef = FirebaseDatabase.getInstance().getReference().child("AppointmentHospital");
        NotificationHospitalRef = FirebaseDatabase.getInstance().getReference().child("HospitalNotification");





        lastDonateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment datePicker = new DatePickerFragment(); ///this date picking can be done easily
                datePicker.show(getSupportFragmentManager(),"date picker");

            }
        });


        appointmentRecyclerView.hasFixedSize();
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAppointmentModuleArrayList = new ArrayList<>();

        appointmentButtnon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout.setVisibility(View.GONE);

                userAppointmentModuleArrayList.clear();
                GetAppointments();
            }
        });



        UsersRef.child(CurrentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if(snapshot.exists())
                {

                    if(snapshot.hasChild("userName"))
                    {
                         fullname = snapshot.child("userName").getValue().toString();//getting the name from the database
                        //setting the full name
                        userName.setText(fullname);
                    }

                    if(snapshot.hasChild("phoneNumber"))
                    {
                         phoneNumber = snapshot.child("phoneNumber").getValue().toString();//getting the name from the database
                        //setting the full name
                        userPhone.setText(phoneNumber);
                    }

                    if(snapshot.hasChild("numberofDonation"))
                    {
                         numberofDonation = snapshot.child("numberofDonation").getValue().toString();//getting the name from the database
                        //setting the full name
                        donationNumber.setText(numberofDonation);
                    }

                    if(snapshot.hasChild("location"))
                    {
                         location = snapshot.child("location").getValue().toString();//getting the name from the database
                        //setting the full name
                        userLocation.setText(location);
                    }

                    if(snapshot.hasChild("bloodGroup"))
                    {
                         bloodGroup = snapshot.child("bloodGroup").getValue().toString();//getting the name from the database
                        //setting the full name
                        userBloodgroup.setText(bloodGroup);
                    }
                    if(snapshot.hasChild("status"))
                    {
                         status = snapshot.child("status").getValue().toString();//getting the name from the database
                        //setting the full name
                        userStatus.setText(status);
                    }
                    if (snapshot.hasChild("lastDonation"))
                    {
                        dateLD = snapshot.child("lastDonation").getValue().toString();
                        String date = "Last donated " + dateLD;
                        lastDonateEditText.setText(date);

                        FindRemainingDays();
                    }
                    else
                    {
                        Toast.makeText(ProfileBloodActivity.this, "Profile does not exist", Toast.LENGTH_LONG).show();
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileBloodActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.customview_profileblood, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                EditText nameEditText, phoneNumberEditText, donationNumberEditText;

                nameEditText = dialogView.findViewById(R.id.nameBPCId);
                phoneNumberEditText = dialogView.findViewById(R.id.phoneBPCId);
                donationNumberEditText = dialogView.findViewById(R.id.donationNumberBPId);

                bloodGroupSpinner = dialogView.findViewById(R.id.bloodGroupCustomViewId);
                locationSpinner = dialogView.findViewById(R.id.locationCustomViewId);
                statusSpinner = dialogView.findViewById(R.id.statusBPId);

                nameEditText.setText(fullname);
                phoneNumberEditText.setText(phoneNumber);
                donationNumberEditText.setText(numberofDonation);





                ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(ProfileBloodActivity.this,
                        R.layout.spinner_view,R.id.spinnerLayoutId,
                        placeNameString);

                locationSpinner.setAdapter(placeAdapter);

                ArrayAdapter<String> BGAdapter = new ArrayAdapter<String>(ProfileBloodActivity.this,
                        R.layout.spinner_view,R.id.spinnerLayoutId,
                        bloodString);

                bloodGroupSpinner.setAdapter(BGAdapter);

                ArrayAdapter<String> SAdapter = new ArrayAdapter<String>(ProfileBloodActivity.this,
                        R.layout.spinner_view,R.id.spinnerLayoutId,
                        statusString);

                statusSpinner.setAdapter(SAdapter);




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

                        bloodGroupC = bloodString[position];


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        statusC = statusString[position];


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



                TextView cancelButton = dialogView.findViewById(R.id.cancelProfileButton);
                TextView updateButton = dialogView.findViewById(R.id.updateProfileButton);




                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.cancel();
                        Toast.makeText(ProfileBloodActivity.this, "Cancel Button is clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        fullname = nameEditText.getText().toString();
                        phoneNumber = phoneNumberEditText.getText().toString();
                        numberofDonation = donationNumberEditText.getText().toString();


                        HashMap donorProfileMap = new HashMap();
                        donorProfileMap.put("userName", fullname);
                        donorProfileMap.put("phoneNumber", phoneNumber);
                        donorProfileMap.put("numberofDonation", numberofDonation);
                        donorProfileMap.put("location", placeName);
                        donorProfileMap.put("bloodGroup", bloodGroupC);
                        donorProfileMap.put("status", statusC);


                        if(placeName.equals("Select Place"))
                        {
                            Toast.makeText(ProfileBloodActivity.this, "Please select a place", Toast.LENGTH_SHORT).show();
                        }
                        if(bloodGroupC.equals("Select Blood Type"))
                        {
                            Toast.makeText(ProfileBloodActivity.this, "Please select your blood group", Toast.LENGTH_SHORT).show();
                        }
                        if(statusC.equals("Select Status"))
                        {
                            Toast.makeText(ProfileBloodActivity.this, "Please select your status", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            UsersRef.child(CurrentUserid).updateChildren(donorProfileMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if (task.isSuccessful()) {

                                        alertDialog.cancel();
                                        Toast.makeText(ProfileBloodActivity.this, "New profile has been saved", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(ProfileBloodActivity.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }




                    }
                });





                alertDialog.show();
            }
        });

    }

    private void GetAppointments() {

        UserAppointmentRef.child(CurrentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    UserAppointmentModule userAppointmentModule = new UserAppointmentModule();

                    userAppointmentModule.setHospitalName(dataSnapshot.child("hospitalName").getValue().toString());
                    userAppointmentModule.setDoctorName(dataSnapshot.child("doctorName").getValue().toString());
                    userAppointmentModule.setDeptName(dataSnapshot.child("deptName").getValue().toString());
                    userAppointmentModule.setPayment(dataSnapshot.child("payment").getValue().toString());
                    userAppointmentModule.setAppointmentDate(dataSnapshot.child("appointmentDate").getValue().toString());
                    userAppointmentModule.setHospitalId(dataSnapshot.child("hospitalId").getValue().toString());
                    userAppointmentModule.setUniqueKey(dataSnapshot.child("uniqueKey").getValue().toString());

                    userAppointmentModule.setNodeKey(dataSnapshot.getKey());


                    userAppointmentModuleArrayList.add(userAppointmentModule);

                }

                userAppointmentAdapter = new UserAppointmentAdapter(ProfileBloodActivity.this, userAppointmentModuleArrayList);
                appointmentRecyclerView.setAdapter(userAppointmentAdapter);
                userAppointmentAdapter.notifyDataSetChanged();
                userAppointmentAdapter.setOnRecyclerViewClickListener(ProfileBloodActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void FindRemainingDays() {



        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        savecurrentdate = simpleDateFormat.format(callForDate.getTime());




        try {
            Date currentDate, appointmentDate;

            //1st have to parse the date into date variable
            currentDate = simpleDateFormat.parse(savecurrentdate);
            appointmentDate = simpleDateFormat.parse(dateLD);

            //secondly have to get the time then do calculation
            long startDate = currentDate.getTime();
            long endDate = appointmentDate.getTime();

            if (endDate<=startDate)
            {

                //this part is for the profile activity and it works absolutely fine

                long dateDiff = (startDate- endDate)/(1000*60*60*24);

                dateDiff = 56 - dateDiff;

                if (dateDiff>0)
                {
                    remainingDaysEditText.setText( String.valueOf(dateDiff) + " days remaining");
                }
                else
                {
                    remainingDaysEditText.setText("You can donate now");

                }




            }
            else
            {
                Toast.makeText(this, "Please select a valid date...", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        month = month+1;

        dateLD = dayOfMonth + "/" + month + "/" + year ;


        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        savecurrentdate = simpleDateFormat.format(callForDate.getTime());


        try {
            Date currentDate, appointmentDate;

            //1st have to parse the date into date variable
            currentDate = simpleDateFormat.parse(savecurrentdate);
            appointmentDate = simpleDateFormat.parse(dateLD);

            //secondly have to get the time then do calculation
            long startDate = currentDate.getTime();
            long endDate = appointmentDate.getTime();

            if (endDate<=startDate)
            {
                String date = "Last donated " + dateLD;
                lastDonateEditText.setText(date);

                UpdateDate();
            }
            else
            {
                AlertDialog alertDialog = new AlertDialog.Builder(ProfileBloodActivity.this).create();
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

    }

    private void UpdateDate() {

        UsersRef.child(CurrentUserid).child("lastDonation").setValue(dateLD);
    }

    @Override
    public void OnRecyclerViewClick(int position) {


        UserAppointmentModule selectedAppointment = userAppointmentModuleArrayList.get(position);

        String hospitalIdId = selectedAppointment.getHospitalId();
        String hospitalName = selectedAppointment.getHospitalName();
        String doctorName = selectedAppointment.getDoctorName();
        String date = selectedAppointment.getAppointmentDate();
        key = selectedAppointment.getUniqueKey();

        String appointmentKey = selectedAppointment.getNodeKey();

        AlertDialog alertDialog = new AlertDialog.Builder(ProfileBloodActivity.this).create();
        alertDialog.setTitle("Cancel Appointment");
        alertDialog.setMessage("You are canceling the appointment with Doctor " + doctorName + " of " + hospitalName);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                alertDialog.cancel();


                userAppointmentModuleArrayList.clear();

                String notification = "Appointment with " + doctorName + "on" + date + "has been canceled!";

                NotificationHospitalRef.child(hospitalIdId).setValue(notification);

                UserAppointmentRef.child(CurrentUserid).child(appointmentKey).removeValue();

                HospitalAppointmentRef.child(hospitalIdId).child(key).removeValue();




                AlertDialog alertDialog = new AlertDialog.Builder(ProfileBloodActivity.this).create();
                alertDialog.setTitle("Cancel Appointment");
                alertDialog.setMessage("Your appointment has been canceled! " );
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        alertDialog.cancel();

                    }
                });
                alertDialog.show();


            }
        });
        alertDialog.show();

    }
}