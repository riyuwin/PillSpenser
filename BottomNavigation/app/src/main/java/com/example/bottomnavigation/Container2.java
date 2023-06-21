package com.example.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Container2 extends Fragment implements Dashboard_tracker_SelectListener  {
    FloatingActionButton AddPillBtn;
    RecyclerView recyclerView;
    FirebaseFirestore firestore;
    Dashboard_Tracker_Adapter myAdapter;
    ArrayList<Data_Entry_Getter> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_container2, container, false);

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

                        if (data.getContainer().equals("Container 2") && data.getStatus().equals("Pending")) {
                            list.add(data);
                        }
                    }
                    myAdapter.notifyDataSetChanged();
                } else {
                    // Handle error
                }
            }
        });


        // Create a List of Strings
        List<String> NumSched = new ArrayList<>();

        AddPillBtn = view.findViewById(R.id.add_con_icon);


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

                            if (container.equals("Container 2") && status.equals("Pending")) {

                                String[] parts = data.split(",");

                                NumSched.add(parts[1].trim());
                            }

                        }
                    }
                } else {
                    // Handle error
                }
            }
        });


        AddPillBtn.setOnClickListener(view1 -> {

            int TotalPillInCon = 0;

            for (String str : NumSched) {
                int number = Integer.parseInt(str);
                TotalPillInCon += number;
            }

            if (TotalPillInCon < 5){
                Intent intent = new Intent(getActivity(), Container_Entry.class);
                intent.putExtra("CONTAINER", "Container 2");
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Sorry, you've reached the maximum number of pill in container.", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }

    @Override
    public void onItemClicked(Data_Entry_Getter data) {
        Intent intent = new Intent(getContext(), DisplayPillSchedule.class);
        intent.putExtra("Data", data.getData());
        intent.putExtra("Container", data.getContainer());
        intent.putExtra("Record_ID", data.getRecord_id());
        intent.putExtra("Sched_name", data.getSched_name());
        intent.putExtra("Status", data.getStatus());
        startActivity(intent);
    }
}