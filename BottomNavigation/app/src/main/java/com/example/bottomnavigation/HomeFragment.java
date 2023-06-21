package com.example.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements Dashboard_tracker_SelectListener  {
    FloatingActionButton addAppointmentBtn;

    ImageButton NotificationBtn;
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    Dashboard_Tracker_Adapter myAdapter;
    ArrayList<Data_Entry_Getter> list;

    TextView TotalPillTv, TotalSkippedTv, TotalIntakeTv;

    TextView NoMedReminder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TotalPillTv = view.findViewById(R.id.total_pill_tv);
        TotalIntakeTv = view.findViewById(R.id.total_pill_intake_tv);
        TotalSkippedTv = view.findViewById(R.id.total_pill_skipped_tv);

        NotificationBtn = view.findViewById(R.id.notification_btn);

        NotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DisplayNotification.class);
                startActivity(intent);
            }
        });

        // Save pill records
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        DocumentReference documentRef_Stats = db1.collection("Statistics").document("MyRecords");

        // Execute the query to retrieve the document
        documentRef_Stats.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Long PillIntake = documentSnapshot.getLong("PillIntake");
                            Long PillSkip = documentSnapshot.getLong("PillSkip");
                            Long PillTotal = documentSnapshot.getLong("PillTotal");

                            TotalPillTv.setText(String.valueOf(PillTotal));
                            TotalIntakeTv.setText(String.valueOf(PillIntake));
                            TotalSkippedTv.setText(String.valueOf(PillSkip));

                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting document: ", e);
                    }
                });


        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Note: Month is zero-based, so add 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a formatted date string (optional)
        String currentDate = String.format("%02d/%02d/%d",  month, day, year);


        recyclerView = view.findViewById(R.id.challengeView);
        firestore = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        list = new ArrayList<>();
        myAdapter = new Dashboard_Tracker_Adapter(getContext(), list, this);
        recyclerView.setAdapter(myAdapter);

        firestore.collection("Pill Record").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Data_Entry_Getter data = document.toObject(Data_Entry_Getter.class);

                        String[] parts = data.getData().split(",");

                        if (parts[2] != null && parts[2].trim().equals(currentDate) && "Pending".equals(data.getStatus())) {
                            list.add(data);

                            NoMedReminder = view.findViewById(R.id.no_medication_reminder);
                            NoMedReminder.setVisibility(View.INVISIBLE);

                        } else if (data.getData() == null){
                            NoMedReminder = view.findViewById(R.id.no_medication_reminder);
                            NoMedReminder.setVisibility(View.VISIBLE);
                        }

                    }
                    myAdapter.notifyDataSetChanged();
                } else {
                }
            }
        });


        return view;
    }

    @Override
    public void onItemClicked(Data_Entry_Getter data) {

        Intent intent = new Intent(getActivity(), Pill_Action.class);
        intent.putExtra("Data", data.getData());
        intent.putExtra("Container", data.getContainer());
        intent.putExtra("Record_ID", data.getRecord_id());
        intent.putExtra("Sched_name", data.getSched_name());
        intent.putExtra("Status", data.getStatus());
        startActivity(intent);

    }
}
