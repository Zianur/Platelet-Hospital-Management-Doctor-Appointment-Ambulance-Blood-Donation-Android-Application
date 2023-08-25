  package com.platelet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

  public class MainActivity extends AppCompatActivity {

    private CardView appoinmentCard, ambulanceCard, bloodCard, tmCard, profileCard, logoutCard, healthTipsCard, aboutCard;
    private DatabaseReference UsersRef, UserNotificationRef;
    private FirebaseAuth mAuth;
    String CurrentUserid, email, notification;

      @Override
      public boolean onCreateOptionsMenu(Menu menu) {

          MenuInflater inflater = getMenuInflater();
          inflater.inflate(R.menu.menu_file,menu);
          return true;

      }

      @Override
      public boolean onOptionsItemSelected(@NonNull MenuItem item) {


          final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
          ViewGroup viewGroup = findViewById(android.R.id.content);
          View dialogView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_password_layout, viewGroup, false);
          builder.setView(dialogView);
          final AlertDialog alertDialog = builder.create();

          EditText passwordEditText;

          passwordEditText = dialogView.findViewById(R.id.passwordId);


          TextView cancelButton = dialogView.findViewById(R.id.cancelProfileButton);
          TextView updateButton = dialogView.findViewById(R.id.updateProfileButton);


          cancelButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  alertDialog.cancel();
                  Toast.makeText(MainActivity.this, "Cancel Button is clicked", Toast.LENGTH_SHORT).show();
              }
          });

          updateButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  String passwordString;

                  passwordString = passwordEditText.getText().toString();

                  if ("1234567".equals(passwordString))
                  {
                      alertDialog.cancel();
                      Intent adminIntent = new Intent(MainActivity.this,SelectAdminActivity.class);
                      startActivity(adminIntent);
                  }
                  else
                  {
                      alertDialog.cancel();
                  }


              }
          });


          alertDialog.show();


          return super.onOptionsItemSelected(item);
      }

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appoinmentCard = findViewById(R.id.appointmentCardId);
        ambulanceCard = findViewById(R.id.ambulanceCardId);
        bloodCard = findViewById(R.id.bloodCardId);
        tmCard = findViewById(R.id.telemedicineCardId);
        profileCard = findViewById(R.id.profileandAppointmentCardId);
        healthTipsCard = findViewById(R.id.tipsId);
        aboutCard = findViewById(R.id.aboutAppId);
        logoutCard = findViewById(R.id.logoutId);



        mAuth = FirebaseAuth.getInstance();
        CurrentUserid = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserNotificationRef = FirebaseDatabase.getInstance().getReference().child("UserNotification");




        UserNotificationRef.child(CurrentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {

                    notification = snapshot.getValue().toString();

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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




        appoinmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent AppointmentActivity = new Intent(MainActivity.this,AppointmentActivity.class);
                startActivity(AppointmentActivity);
            }
        });


        ambulanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent AmbulanceActivity = new Intent(MainActivity.this,AmbulanceActivity.class);
                startActivity(AmbulanceActivity);

            }
        });

        bloodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent BloodActivity = new Intent(MainActivity.this,BloodActivity.class);
                startActivity(BloodActivity);


            }
        });


        tmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent TelemedicineActivity = new Intent(MainActivity.this,TelemedicineActivity.class);
//                startActivity(TelemedicineActivity);


            }


        });


          profileCard.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {


                  Intent profileIntent = new Intent(MainActivity.this,ProfileBloodActivity.class);
                  startActivity(profileIntent);



              }
          });


          aboutCard.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Intent BloodActivity = new Intent(MainActivity.this,AboutActivity.class);
                  startActivity(BloodActivity);


              }
          });

          healthTipsCard.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Intent BloodActivity = new Intent(MainActivity.this,HealthTipsActivity.class);
                  startActivity(BloodActivity);


              }
          });

          logoutCard.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  mAuth.signOut();
                  SendUserToLoginActivity();


              }
          });





      }


      @Override
      protected void onStart() {
          super.onStart();

          FirebaseUser currentUser = mAuth.getCurrentUser();

          if(currentUser==null)
          {
              SendUserToLoginActivity();//checking user authentication
          }
          else
          {
              CheckUserExistence();//checking profile is created or not
          }
      }

      private void CheckUserExistence()
      {
//          final String current_User_id = mAuth.getCurrentUser().getUid();
          //if user has created profile then authenticated unique code wil be stored under user node in database

          //getting the email from the main activity
//        Intent intent = getIntent();
//        email = intent.getExtras().getString("key");



//          FirebaseUser currentUser = mAuth.getCurrentUser();
//          if(currentUser!=null)
//          {
//
//              //geeting user email
//              email = currentUser.getEmail();
//
//
//          }

          UsersRef.child(CurrentUserid).addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot)
              {

                  if(dataSnapshot.exists())
                  {
                      if(!dataSnapshot.hasChild("userName"))//the user is authenticated but does not have a profile
                      {
                          SendUserToSetupActivity();//so send to setup profile
                          Toast.makeText(MainActivity.this, "Please Complete your Profile... :)", Toast.LENGTH_LONG).show();
                      }
                  }
                  else
                  {

                      SendUserToSetupActivity();//so send to setup profile

                  }

              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

                  Toast.makeText(MainActivity.this, "Error Occured", Toast.LENGTH_LONG).show();

              }
          });
      }


      private void SendUserToSetupActivity()//method to send user to setupactivity
      {
          Intent setupIntent = new Intent(MainActivity.this,SetupActivity.class);
//        setupIntent.putExtra("key", email );
          setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(setupIntent);
          finish();
      }


      private void SendUserToLoginActivity()
      {
          Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
          loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(loginIntent);
          finish();
      }

  }