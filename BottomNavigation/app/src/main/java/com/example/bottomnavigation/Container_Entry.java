package com.example.bottomnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Container_Entry extends AppCompatActivity {
    DatePicker datePicker;
    TimePicker timePicker;
    EditText containerNameEditText;
    EditText pillsToBeTakenEditText;

    EditText Sched_Name;
    Button saveButton;


    DatePicker datePicker2;
    TimePicker timePicker2;
    EditText containerNameEditText2;
    EditText pillsToBeTakenEditText2;
    Button saveButton2;

    TextView ContainerLabel;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_entry);

        Sched_Name = findViewById(R.id.sched_name);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        containerNameEditText = findViewById(R.id.container_name);
        pillsToBeTakenEditText = findViewById(R.id.pills_to_be_taken);
        saveButton = findViewById(R.id.add_container_1);
        ContainerLabel = findViewById(R.id.label_tv);

        Intent Get_Intent = getIntent();
        String container = Get_Intent.getStringExtra("CONTAINER");

        ContainerLabel.setText(container);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToFirebase(container);
            }
        });
    }

    private void saveDataToFirebase(String ContainerName) {

        // Get user inputs
        String containerName = containerNameEditText.getText().toString();
        String sched_name = Sched_Name.getText().toString();
        int pillsToBeTaken = Integer.parseInt(pillsToBeTakenEditText.getText().toString());
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Month starts from 0
        int year = datePicker.getYear();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        if(containerName==null || containerName.isEmpty()) {
            containerNameEditText.setError("Container name is required.");
            return;
        } else if (sched_name==null || sched_name.isEmpty()) {
            Sched_Name.setError("Schedule name is required.");
            return;
        } else if (pillsToBeTaken > 5) {
            pillsToBeTakenEditText.setError("Pill must not greater than 5.");
            return;
        }

        // Format the date and time strings
        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", month, day, year);
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        // Create the data string
        String dataString = containerName + ", " + pillsToBeTaken + ", " + formattedDate + ", " + formattedTime;

        // Save the data string to Firebase Realtime Database
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
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
                        //dataToUpdate.put("PillIntake", pillIntake + 1);
                        //dataToUpdate.put("PillSkip", pillSkip + 1);
                        dataToUpdate.put("PillTotal", pillTotal + pillsToBeTaken);

                        documentRef_Stats.update(dataToUpdate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Update successful

                                        saveToFirestore(dataString, sched_name, ContainerName);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle the error
                                    }
                                });
                    } else {

                    }
                } else {
                    // Handle the error
                }
            }
        });


        ////////////////////////////////////////////////////////





        // Call the Arduino code to rotate the stepper motor
        // Pass the number of pills to be taken (pillsToBeTaken) to the Arduino code
        // You'll need to implement the necessary communication protocol between the Android app and Arduino

        // Optional: Show a success message or perform any other desired actions
    }

    private void saveToFirestore(String dataString, String sched_name, String ContainerName) {

        //---------------------------PARA TO SA PAG SAVE NG DATA SA FIRESTORE ----------------------------------------


        // Assuming you have already initialized Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a reference to the Firestore collection
        CollectionReference usersCollection = db.collection("Pill Record");

        // Create a new document within the collection
        DocumentReference documentRef = usersCollection.document();
        // Generate a unique ID for the document
        String documentId = documentRef.getId();

        // Create a Map to represent the data
        Map<String, Object> userData = new HashMap<>();
        userData.put("data", dataString);
        userData.put("container", ContainerName);
        userData.put("sched_name", sched_name);
        userData.put("status", "Pending");
        userData.put("notif", "Pending");
        userData.put("record_id", documentId); // Add the user ID field

        // Set the data on the document
        documentRef.set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document saved successfully

                        Log.d("Firestore", "Document saved");
                        Toast.makeText(Container_Entry.this, "Data has been saved.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Container_Entry.this, MainActivity.class);
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
