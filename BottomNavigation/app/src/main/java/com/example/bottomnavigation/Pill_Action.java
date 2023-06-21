package com.example.bottomnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Pill_Action extends AppCompatActivity {

    Button DispenseBtn;

    ImageButton BackBtn, DeleteBtn;

    TextView SchedName, PillName, ContainerName, TimeIntake, DateIntake;


    DatabaseReference databaseReference;

    String Container;

    String Date, Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_action);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        DispenseBtn = findViewById(R.id.dispense_btn);
        BackBtn = findViewById(R.id.back_btn);
        DeleteBtn = findViewById(R.id.delete_btn);


        SchedName = findViewById(R.id.sched_name_tv);
        PillName = findViewById(R.id.pill_name_tv);
        ContainerName = findViewById(R.id.container_tv);
        TimeIntake = findViewById(R.id.time_tv);
        DateIntake = findViewById(R.id.date_tv);

        Intent Get_Credentials = getIntent();
        String data = Get_Credentials.getStringExtra("Data");
        String container = Get_Credentials.getStringExtra("Container");
        String record_id = Get_Credentials.getStringExtra("Record_ID");
        String sched_name = Get_Credentials.getStringExtra("Sched_name");
        String status = Get_Credentials.getStringExtra("Status");

        SchedName.setText(sched_name);
        ContainerName.setText(container);


        String[] parts = data.split(",");

        for (String part : parts) {

            PillName.setText(parts[0]);
            DateIntake.setText(parts[2]);
            TimeIntake.setText(parts[3]);
        }

        DispenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DispenseNow(data, container, sched_name, status, record_id);

            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeleteSchedule(data, container, record_id, sched_name, status);

            }
        });

    }

    private void DispenseNow(String data, String container, String sched_name, String status, String record_id){

        /*
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)); // 24-hour format
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String second = String.valueOf(calendar.get(Calendar.SECOND));*/


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int minute = calendar.get(Calendar.MINUTE) + 1;
        int second = calendar.get(Calendar.SECOND);

        String formattedMonth = String.format("%02d", month);
        String formattedDay = String.format("%02d", day);
        String formattedHour = String.format("%02d", hour);
        String formattedMinute = String.format("%02d", minute);

        //Date = formattedMonth + "/" + formattedDay + "/" + year;
        //Time = formattedHour + ":" + formattedMinute;

        //int updatedmin = Integer.parseInt(minute) + 1;

        Date = formattedMonth + "/" + formattedDay + "/" + year;
        Time = formattedHour + ":" + formattedMinute;

        String[] parts = data.split(",");

        String LatestData = parts[0].trim() + ", " + parts[1].trim() + ", " + Date + ", " + Time;

        for (String part : parts) {

            PillName.setText(parts[0]);
            DateIntake.setText(parts[2]);
            TimeIntake.setText(parts[3]);
        }


        if (status.equals("Pending")){
            if (container.equals("Container 1")){
                Container = "Container 1";
                // Save the data string to Firebase Realtime Database
                DatabaseReference dataRef = databaseReference.child("data");
                dataRef.setValue(LatestData);
            } else if (container.equals("Container 2")) {
                Container = "Container 2";
                // Save the data string to Firebase Realtime Database
                DatabaseReference dataRef = databaseReference.child("data2");
                dataRef.setValue(LatestData);
            }

            // Save the data string to Firebase Realtime Database
            FirebaseFirestore db1 = FirebaseFirestore.getInstance();
            DocumentReference docRefUpdate = db1.collection("Pill Record").document(record_id);
            docRefUpdate.update("status", "Dispensed")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update successful
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the error
                        }
                    });

            // Save pill records
            DocumentReference documentRef_Stats = db1.collection("Statistics").document("MyRecords");
            documentRef_Stats.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            // Retrieve specific fields from the document

                            int pillIntake = document.getLong("PillIntake").intValue();
                            int pillSkip = document.getLong("PillSkip").intValue();
                            int pillTotal = document.getLong("PillTotal").intValue();


                            // Update the document with the new values
                            Map<String, Object> dataToUpdate = new HashMap<>();
                            dataToUpdate.put("PillIntake", pillIntake + 1);
                            //dataToUpdate.put("PillSkip", pillSkip + 1);
                            //dataToUpdate.put("PillTotal", pillTotal + 1);

                            documentRef_Stats.update(dataToUpdate)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Update successful
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the error
                                        }
                                    });
                        } else {
                            // Update the document with initial values
                            Map<String, Object> dataToUpdate = new HashMap<>();
                            dataToUpdate.put("PillIntake", 0);
                            dataToUpdate.put("PillSkip", 0);
                            dataToUpdate.put("PillTotal", 0);

                            documentRef_Stats.set(dataToUpdate)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Update successful
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the error
                                        }
                                    });
                        }
                    } else {
                        // Handle the error
                    }
                }
            });
            ////////////////////////////////////////////////////////


            //------------For firestore -------------------------

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference usersCollection = db.collection("History");

            DocumentReference documentRef = usersCollection.document();
            String documentId = documentRef.getId();

            Map<String, Object> userData = new HashMap<>();
            userData.put("data", data); // Add the user ID field
            userData.put("container", Container);
            userData.put("activity", "Dispensed");
            userData.put("sched_name", sched_name);
            userData.put("date", Date);
            userData.put("time", Time);

            // Set the data on the document
            documentRef.set(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Document saved successfully

                            //Toast.makeText(Pill_Action.this, "You've successfully scheduled your pill.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Pill_Action.this, Pill_Action_Success.class);
                            intent.putExtra("DATA", data);
                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error occurred while saving document
                            Log.e("Firestore", "Error saving document", e);
                        }
                    });

            //---------------------------PARA TO SA PAG SAVE NG DATA SA FIRESTORE ----------------------------------------


        } else {
            Toast.makeText(this, "The schedule has already been set.", Toast.LENGTH_SHORT).show();
        }



    }


    private void DeleteSchedule(String data_dets, String container, String record_id, String sched_name, String status){

        // Getting the current date and time

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String formattedMonth = String.format("%02d", month);
        String formattedDay = String.format("%02d", day);
        String formattedHour = String.format("%02d", hour);
        String formattedMinute = String.format("%02d", minute);

        Date = formattedMonth + "/" + formattedDay + "/" + year;
        Time = formattedHour + ":" + formattedMinute;

        //--------------------------------------------------


        String data = "0, 0, 0, 0";

        if (container.equals("Container 1")){
            Container = "Container 1";
        } else if (container.equals("Container 2")) {
            Container = "Container 2";
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("Pill Record");
        DocumentReference documentRef = collection.document(record_id);

        documentRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully deleted
                        Toast.makeText(Pill_Action.this, "Document deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while deleting document
                        Toast.makeText(Pill_Action.this, "Error deleting document", Toast.LENGTH_SHORT).show();

                    }
                });



        //------------For firestore -------------------------

        FirebaseFirestore history_db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = history_db.collection("History");

        DocumentReference history_documentRef = usersCollection.document();
        String history_documentId = history_documentRef.getId();

        Map<String, Object> userData = new HashMap<>();
        userData.put("data", data_dets); // Add the user ID field
        userData.put("container", Container);
        userData.put("activity", "Deleted");
        userData.put("sched_name", sched_name);
        userData.put("date", Date);
        userData.put("time", Time);

        //userData.put("timestamp", FieldValue.serverTimestamp());

        // Set the data on the document
        history_documentRef.set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document saved successfully

                        Toast.makeText(Pill_Action.this, "You've successfully scheduled your pill.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Pill_Action.this, MainActivity.class);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while saving document
                        Log.e("Firestore", "Error saving document", e);
                    }
                });

        //---------------------------PARA TO SA PAG SAVE NG DATA SA FIRESTORE ----------------------------------------

    }
}


