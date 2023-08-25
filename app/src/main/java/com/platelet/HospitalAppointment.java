package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HospitalAppointment extends AppCompatActivity implements HospitalAppointmentAdapter.OnRecyclerViewClickListener {

    private ArrayList<HospitalAppointmentModule> hospitalAppointmentModuleArrayList;
    private HospitalAppointmentAdapter hospitalAppointmentAdapter;

    private RecyclerView appointmentRecyclerView;


    private FirebaseAuth mAuth;
    private DatabaseReference HospitalAppointmentRef, NotificationUserRef, UserAppointmentRef, NotificationHospitalRef;
    private String CurrentUserid, notification;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_appointment);

        HospitalAppointmentRef = FirebaseDatabase.getInstance().getReference().child("AppointmentHospital");
        UserAppointmentRef = FirebaseDatabase.getInstance().getReference().child("AppointmentUser");
        NotificationUserRef = FirebaseDatabase.getInstance().getReference().child("UserNotification");
        NotificationHospitalRef = FirebaseDatabase.getInstance().getReference().child("HospitalNotification");

        mAuth = FirebaseAuth.getInstance();
        CurrentUserid = mAuth.getCurrentUser().getUid();
        appointmentRecyclerView = findViewById(R.id.appointmentHospitalRecyclerView);


        NotificationHospitalRef.child(CurrentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    notification = snapshot.getValue().toString();

                    AlertDialog alertDialog = new AlertDialog.Builder(HospitalAppointment.this).create();
                    alertDialog.setTitle("Notification");
                    alertDialog.setMessage(notification );
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            snapshot.getRef().removeValue();
                            alertDialog.cancel();

                        }
                    });

                    alertDialog.show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        appointmentRecyclerView.hasFixedSize();
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        hospitalAppointmentModuleArrayList = new ArrayList<>();

        HospitalAppointmentRef.child(CurrentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    HospitalAppointmentModule hospitalAppointmentModule = new HospitalAppointmentModule();

                    hospitalAppointmentModule.setPatientName(dataSnapshot.child("patientName").getValue().toString());
                    hospitalAppointmentModule.setPatientPhoneNumber(dataSnapshot.child("patientPhoneNumber").getValue().toString());
                    hospitalAppointmentModule.setDoctorName(dataSnapshot.child("doctorName").getValue().toString());
                    hospitalAppointmentModule.setDeptName(dataSnapshot.child("deptName").getValue().toString());
                    hospitalAppointmentModule.setPayment(dataSnapshot.child("payment").getValue().toString());
                    hospitalAppointmentModule.setAppointmentDate(dataSnapshot.child("appointmentDate").getValue().toString());
                    hospitalAppointmentModule.setUserId(dataSnapshot.child("userId").getValue().toString());
                    hospitalAppointmentModule.setEmail(dataSnapshot.child("email").getValue().toString());

                    hospitalAppointmentModule.setNodeKey(dataSnapshot.getKey());//getting the nodeKey




                    hospitalAppointmentModuleArrayList.add(hospitalAppointmentModule);

                }

                hospitalAppointmentAdapter = new HospitalAppointmentAdapter(HospitalAppointment.this, hospitalAppointmentModuleArrayList);
                appointmentRecyclerView.setAdapter(hospitalAppointmentAdapter);
                hospitalAppointmentAdapter.setOnRecyclerViewClickListener(HospitalAppointment.this);
                hospitalAppointmentAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void OnRecyclerViewClick(int position) {

        HospitalAppointmentModule selectedAppointment = hospitalAppointmentModuleArrayList.get(position);

        String userId = selectedAppointment.getUserId();
        String patientName = selectedAppointment.getPatientName();
        String doctorName = selectedAppointment.getDoctorName();

        String appointmentKey = selectedAppointment.getNodeKey();

        AlertDialog alertDialog = new AlertDialog.Builder(HospitalAppointment.this).create();
        alertDialog.setTitle("Cancel Appointment");
        alertDialog.setMessage("You are canceling the appointment with - " + patientName);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.cancel();

                hospitalAppointmentModuleArrayList.clear();

                String notification = "Your appointment has been canceled with doctor " + doctorName;

                NotificationUserRef.child(userId).setValue(notification);

                HospitalAppointmentRef.child(CurrentUserid).child(appointmentKey).removeValue();

                Query query = UserAppointmentRef.child(userId).orderByChild("uniqueKey").equalTo(appointmentKey);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            if (dataSnapshot.exists())
                            {
//                                key = dataSnapshot.getKey();
//                                UserAppointmentRef.child(userId).child(key).removeValue();



                                dataSnapshot.getRef().removeValue(); // for deleting a node

                            }
                            else
                            {

                                Toast.makeText(HospitalAppointment.this, "NOT FOUND!!!!!!!!!!!", Toast.LENGTH_SHORT).show();

                            }



                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





                AlertDialog alertDialog = new AlertDialog.Builder(HospitalAppointment.this).create();
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