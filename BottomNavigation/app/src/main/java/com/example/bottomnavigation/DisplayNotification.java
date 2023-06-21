package com.example.bottomnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DisplayNotification extends AppCompatActivity implements Notification_SelectListener  {
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    NotificationAdapter myAdapter;
    ArrayList<Notification_Retriever> list;

    ImageButton BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notification);

        recyclerView = findViewById(R.id.notification_recyclerview);
        firestore = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new NotificationAdapter(this, list, this);
        recyclerView.setAdapter(myAdapter);

        firestore.collection("History")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Notification_Retriever data = document.toObject(Notification_Retriever.class);
                                list.add(data);
                            }
                            myAdapter.notifyDataSetChanged();
                        } else {
                            // Handle error
                        }
                    }
                });

        BackBtn = findViewById(R.id.back_btn);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onItemClicked(Notification_Retriever data) {

        Intent intent = new Intent(DisplayNotification.this, History_Summary.class);
        intent.putExtra("Data", data.getData());
        intent.putExtra("Container", data.getContainer());
        intent.putExtra("Record_ID", data.getDocumentId());
        intent.putExtra("Sched_name", data.getSched_name());
        intent.putExtra("Activity", data.getActivity());
        intent.putExtra("Current_time", data.getCurrent_time());
        intent.putExtra("Current_date", data.getCurrent_date());
        intent.putExtra("Remarks", data.getRemarks());
        startActivity(intent);
    }
}