package com.example.bottomnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.bottomnavigation.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton add_data_btn;

    ActivityMainBinding binding;

    private int currentDialogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        Initializer();

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    Initializer();
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.addmore:
                    Initializer();
                    replaceFragment(new AddMoreFragment());
                    break;

            }

            return true;

        });


        add_data_btn = findViewById(R.id.add_con_icon);

        add_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the new activity here
                showBottomDialog();
            }
        });

        /*
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });*/

    }

    private void Initializer(){


        //Retrieving statistics in firestore
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Note: Months are zero-based, so add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String formattedMonth = String.format("%02d", month);
        String formattedDay = String.format("%02d", day);
        String formattedHour = String.format("%02d", hour);
        String formattedMinute = String.format("%02d", minute);

        // Create a formatted date and time string
        String currentDate = formattedMonth + "/" + formattedDay + "/" + year;
        String currentTime = formattedHour + ":" + formattedMinute;

        // Get a Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the collection you want to query
        CollectionReference collectionRef = db.collection("Pill Record");

        // Save pill records
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        DocumentReference documentRef_Stats = db1.collection("Statistics").document("MyRecords");

        // Execute the query to retrieve the document
        documentRef_Stats.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Long pillIntake = documentSnapshot.getLong("PillIntake");
                            Long pillSkip = documentSnapshot.getLong("PillSkip");
                            Long pillTotal = documentSnapshot.getLong("PillTotal");

                            // Execute the query to retrieve all documents in the collection
                            collectionRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                // Get the document ID
                                                String documentId = documentSnapshot.getId();
                                                String data = documentSnapshot.getString("data");
                                                String status = documentSnapshot.getString("status");
                                                String container = documentSnapshot.getString("container");
                                                String sched_name = documentSnapshot.getString("sched_name");
                                                String notif = documentSnapshot.getString("notif");

                                                String[] parts = data.split(",");

                                                String pillName = parts[0].trim();
                                                String pillQty = parts[1].trim();
                                                String pillDate = parts[2].trim();

                                                String pillTime = parts[3].trim();

                                                String[] SplitPillTime = pillTime.split(":");
                                                int TargetHour =  Integer.parseInt(SplitPillTime[0].trim()) + 1;
                                                int TargetMinute =  Integer.parseInt(SplitPillTime[1].trim());

                                                String OrigPillTime = Integer.parseInt(SplitPillTime[0].trim()) + ":" + TargetMinute;
                                                String Ahead1PillTime = TargetHour + ":" + TargetMinute;


                                                int LapsedTargetHour =  Integer.parseInt(SplitPillTime[0].trim()) + 1;
                                                int LapsedTargetMinute =  Integer.parseInt(SplitPillTime[1].trim()) + 2;
                                                String LapsedPillTime = LapsedTargetHour + ":" + LapsedTargetMinute;

                                                String targetTimeStr = SplitPillTime[0].trim();



                                                int BeforeTargetHour;
                                                if (Integer.parseInt(SplitPillTime[0].trim()) == 0){
                                                    BeforeTargetHour =  23;
                                                } else{
                                                    BeforeTargetHour =  Integer.parseInt(SplitPillTime[0].trim()) - 1;
                                                }

                                                int BeforeTargetMinute =  Integer.parseInt(SplitPillTime[1].trim());


                                                int BeforePillTime1 =  Integer.parseInt(SplitPillTime[0].trim());
                                                String BeforePillTime = BeforePillTime1 + ":" + BeforeTargetMinute;

                                                if (notif != null && status != null && status.equals("Pending") && notif.equals("Lapsed") && isDateTimePassed(pillDate, Ahead1PillTime, currentDate, currentTime)) {


                                                    // Update the document with the new values
                                                    Map<String, Object> dataToUpdate = new HashMap<>();
                                                    dataToUpdate.put("status", "Skipped"); // Update field1 with the desired value

                                                    // Update the document
                                                    collectionRef.document(documentId)
                                                            .update(dataToUpdate)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    // Update the document with the new values
                                                                    dataToUpdate.put("PillSkip", pillSkip + 1); // Update field1 with the desired value
                                                                    // Update the document
                                                                    documentRef_Stats.update(dataToUpdate);

                                                                    String remarks = "Skipped";
                                                                    String activity = "Skipped";
                                                                    String remarks_desc = "The schedule has already passed, you missed out on taking your medicine on time.";


                                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                    CollectionReference usersCollection = db.collection("Notifications");
                                                                    DocumentReference documentRef = usersCollection.document();

                                                                    SaveHistory(data, container, sched_name,  currentDate, currentTime, remarks);
                                                                    SaveNotification(documentRef, data, container, sched_name,  currentDate, currentTime, remarks, activity, documentId);

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Handle the error
                                                                }
                                                            });

                                                } else if (notif != null && status != null && status.equals("Pending") && notif.equals("Pending") && isTargetTimeToday(pillDate, LapsedPillTime, currentDate, currentTime)){

                                                    String remarks = "Lapsed";
                                                    String activity = "Lapsed";
                                                    String remarks_desc = "The schedule has already passed, you missed out on taking your medicine on time.";


                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    CollectionReference usersCollection = db.collection("Notifications");
                                                    DocumentReference documentRef = usersCollection.document();

                                                    SaveNotification(documentRef, data, container, sched_name,  currentDate, currentTime, remarks, activity, documentId);

                                                }else if (notif != null && status != null && status.equals("Pending") && notif.equals("Pending") && pillDate.equals(currentDate) && pillTime.equals(currentTime)){

                                                    String remarks = "Due";
                                                    String activity = "Pending";
                                                    String remarks_desc = "The schedule that you've set is now on queue, take your medicine now";

                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    CollectionReference usersCollection = db.collection("Notifications");
                                                    DocumentReference documentRef = usersCollection.document();

                                                    SaveNotification(documentRef, data, container, sched_name,  currentDate, currentTime, remarks, activity, documentId);

                                                } else if (notif != null && status != null && status.equals("Pending") && notif.equals("Pending") && isTargetTimeToday(pillDate, BeforePillTime, currentDate, currentTime)){

                                                    String remarks = "Upcoming";
                                                    String activity = "Pending";
                                                    String remarks_desc = "The schedule has already passed, you missed out on taking your medicine on time.";

                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    CollectionReference usersCollection = db.collection("Notifications");
                                                    DocumentReference documentRef = usersCollection.document();

                                                    SaveNotification(documentRef, data, container, sched_name,  currentDate, currentTime, remarks, activity, documentId);


                                                }



                                                /*else if (status != null && status.equals("Pending") && isDateTimePassed(pillDate, pillTime, currentDate, currentTime)){

                                                    String remarks = "Lapsed";
                                                    String remarks_desc = "The schedule has already passed, you missed out on taking your medicine on time.";
                                                    SaveNotification(data, container, sched_name,  currentDate, currentTime, remarks, documentId);

                                                }*/
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Error retrieving documents
                                            Log.e("Firestore", "Error getting documents: ", e);
                                        }
                                    });


                        } else {
                            // The document does not exist, insert default data
                            // Update the document with initial values
                            Map<String, Object> dataToUpdate = new HashMap<>();
                            dataToUpdate.put("PillIntake", 0);
                            dataToUpdate.put("PillSkip", 0);
                            dataToUpdate.put("PillTotal", 0);

                            documentRef_Stats.set(dataToUpdate)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Default data insertion successful
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle the error
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting document: ", e);
                    }
                });
        ////////////////////////////////////////////////////////

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Helper method to check if a given date and time has already passed
    private boolean isDateTimePassed(String targetDate, String targetTime, String currentDate, String currentTime) {
        String[] targetDateParts = targetDate.split("/");
        String[] targetTimeParts = targetTime.split(":");
        String[] currentDateParts = currentDate.split("/");
        String[] currentTimeParts = currentTime.split(":");

        int targetYear = Integer.parseInt(targetDateParts[2]);
        int targetMonth = Integer.parseInt(targetDateParts[0]);
        int targetDay = Integer.parseInt(targetDateParts[1]);
        int targetHour = Integer.parseInt(targetTimeParts[0]);
        int targetMinute = Integer.parseInt(targetTimeParts[1]);
        int currentYear = Integer.parseInt(currentDateParts[2]);
        int currentMonth = Integer.parseInt(currentDateParts[0]);
        int currentDay = Integer.parseInt(currentDateParts[1]);
        int currentHour = Integer.parseInt(currentTimeParts[0]);
        int currentMinute = Integer.parseInt(currentTimeParts[1]);

        if (targetYear < currentYear ||
                (targetYear == currentYear && targetMonth < currentMonth) ||
                (targetYear == currentYear && targetMonth == currentMonth && targetDay < currentDay) ||
                (targetYear == currentYear && targetMonth == currentMonth && targetDay == currentDay && targetHour < currentHour) ||
                (targetYear == currentYear && targetMonth == currentMonth && targetDay == currentDay && targetHour == currentHour && targetMinute <= currentMinute)) {

            return true; // The target date and time have already passed
        } else {
            return false; // The target date and time are in the future
        }
    }

    private boolean isTargetTimeToday(String targetDate, String targetTime, String currentDate, String currentTime) {
        String[] targetDateParts = targetDate.split("/");
        String[] targetTimeParts = targetTime.split(":");
        String[] currentDateParts = currentDate.split("/");
        String[] currentTimeParts = currentTime.split(":");

        int targetYear = Integer.parseInt(targetDateParts[2]);
        int targetMonth = Integer.parseInt(targetDateParts[0]);
        int targetDay = Integer.parseInt(targetDateParts[1]);
        int targetHour = Integer.parseInt(targetTimeParts[0]);
        int targetMinute = Integer.parseInt(targetTimeParts[1]);
        int currentYear = Integer.parseInt(currentDateParts[2]);
        int currentMonth = Integer.parseInt(currentDateParts[0]);
        int currentDay = Integer.parseInt(currentDateParts[1]);
        int currentHour = Integer.parseInt(currentTimeParts[0]);
        int currentMinute = Integer.parseInt(currentTimeParts[1]);

        // Compare target and current date/time values
        if (targetYear == currentYear && targetMonth == currentMonth && targetDay == currentDay &&
                targetHour == currentHour && targetMinute == currentMinute) {
            return true; // The target time is equal to the current time

            // Calculate the time difference between target and current time
        } else {

            Calendar targetCalendar = Calendar.getInstance();
            targetCalendar.set(targetYear, targetMonth - 1, targetDay, targetHour, targetMinute);
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.set(currentYear, currentMonth - 1, currentDay, currentHour, currentMinute);

            long timeDifferenceMillis = targetCalendar.getTimeInMillis() - currentCalendar.getTimeInMillis();

            // Check if the time difference is within the desired range
            return timeDifferenceMillis > 0 && timeDifferenceMillis <= (60 * 60 * 1000);
            // 1 hour in milliseconds

        }
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        Button button1 = dialog.findViewById(R.id.button1);
        Button button2 = dialog.findViewById(R.id.button2);

        //AddPillBtn = findViewById(R.id.add_con_icon);

        List<String> Con1NumSched = new ArrayList<>();
        List<String> Con2NumSched = new ArrayList<>();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = firestore.collection("Pill Record");

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            // Access document fields here
                            String status = document.getString("status");
                            String container = document.getString("container");
                            String data = document.getString("data");


                            if (container.equals("Container 1") && status.equals("Pending")) {
                                String[] parts = data.split(",");

                                Con1NumSched.add(parts[1].trim());
                            }

                            if (container.equals("Container 2") && status.equals("Pending")) {
                                String[] parts = data.split(",");

                                Con2NumSched.add(parts[1].trim());
                            }

                        }
                    }
                } else {
                    // Handle error
                }
            }
        });

        button1.setOnClickListener(view1 -> {

            int TotalPillInCon = 0;

            for (String str : Con1NumSched) {
                int number = Integer.parseInt(str);
                TotalPillInCon += number;
            }

            if (TotalPillInCon < 5){
                Intent intent = new Intent(MainActivity.this, Container_Entry.class);
                intent.putExtra("CONTAINER", "Container 1");
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, you've reached the maximum number of pills in the container.", Toast.LENGTH_LONG).show();
            }
        });

        button2.setOnClickListener(view1 -> {

            int TotalPillInCon = 0;

            for (String str : Con2NumSched) {
                int number = Integer.parseInt(str);
                TotalPillInCon += number;
            }

            if (TotalPillInCon < 5){
                Intent intent = new Intent(MainActivity.this, Container_Entry.class);
                intent.putExtra("CONTAINER", "Container 2");
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Sorry, you've reached the maximum number of pills in the container.", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void SaveHistory(String data, String Container, String sched_name, String Date, String Time, String remarks) {

        //------------For firestore -------------------------

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("History");

        DocumentReference documentRef = usersCollection.document();
        String documentId = documentRef.getId();

        Map<String, Object> userData = new HashMap<>();
        userData.put("data", data); // Add the user ID field
        userData.put("container", Container);
        userData.put("activity", remarks);
        userData.put("sched_name", sched_name);
        userData.put("date", Date);
        userData.put("time", Time);

        // Set the data on the document
        documentRef.set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document saved successfully


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

    public void SaveNotification(DocumentReference documentRef, String data, String Container, String sched_name, String Date, String Time, String Remarks, String activity, String DocumentID){

//------------For firestore -------------------------

        String remarks_desc_passed = "The schedule has already passed, you missed out on taking your medicine on time.";
        String remarks_desc_due = "The schedule that you've set is now on queue, take your medicine now";
        String remarks_desc_lapsed = "The schedule is lapsed, take your medicine now";
        String remarks_desc_upcoming = "You have an upcoming schedule, please don't forget.";

        documentRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                        } else {

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("data", data); // Add the user ID field
                            userData.put("container", Container);
                            userData.put("activity", activity);
                            userData.put("sched_name", sched_name);
                            userData.put("current_date", Date);
                            userData.put("current_time", Time);
                            userData.put("remarks", Remarks);
                            userData.put("documentId", DocumentID);

                            // Set the data on the document
                            documentRef.set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Document saved successfully
                                            if (Remarks.equals("Skipped")){
                                                createNotif(remarks_desc_passed);
                                                UpdateNotifStatus(DocumentID, activity);
                                            } else if (Remarks.equals("Lapsed")){
                                                createNotif(remarks_desc_lapsed);
                                                UpdateNotifStatus(DocumentID, activity);
                                            }  else if (Remarks.equals("Due")){
                                                createNotif(remarks_desc_due);
                                                UpdateNotifStatus(DocumentID, activity);
                                            }  else if (Remarks.equals("Upcoming")){
                                                createNotif(remarks_desc_upcoming);
                                                UpdateNotifStatus(DocumentID, activity);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Error occurred while saving document
                                            Log.e("Firestore", "Error saving document", e);
                                        }
                                    });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while retrieving the document
                        Log.e("Firestore", "Error retrieving document", e);
                    }
                });

    }


    private void UpdateNotifStatus(String documentID, String notif){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("Pill Record").document(documentID);

        documentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    // Document exists, proceed with update
                    Map<String, Object> updatedData = new HashMap<>();
                    updatedData.put("notif", notif);

                    documentRef.set(updatedData, SetOptions.merge())
                            .addOnSuccessListener(aVoid -> {
                                // Update successful
                            })
                            .addOnFailureListener(e -> {
                                // Handle any errors
                            });
                } else {
                    // Document does not exist, handle the case if needed
                }
            } else {
                // Error occurred while fetching document
                Exception e = task.getException();
                // Handle the error
            }
        });

    }

    private void createNotif(String NotifDescription){
        String id = "PillSpenser";
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = manager.getNotificationChannel(id);
            if (channel == null){
                channel = new NotificationChannel(id, "PillSpenser", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Your health is our priority.");
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100,1000,200,340});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel);
            }
        }

        Intent notificationIntent = new Intent(this, HomeFragment.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0 , notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.drawable.medlogo_mod)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.medlogo_mod))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.medlogo_mod))
                        .bigLargeIcon(null))
                .setContentText(NotifDescription)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100,1000,200,340})
                .setAutoCancel(false)
                .setTicker("Notification");
        builder.setContentIntent(contentIntent);
        NotificationManagerCompat m = NotificationManagerCompat.from(getApplicationContext());
        m.notify(1,builder.build());


    }


}
