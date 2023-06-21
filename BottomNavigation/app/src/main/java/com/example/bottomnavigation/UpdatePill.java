package com.example.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class UpdatePill extends Fragment {

    TextView SchedNameTv, PillNameTv, ContainerTv, QtyTv, DateIntakeTv, TimeIntakeTv;

    Button ChangeSched;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_pill, container, false);

        SchedNameTv = view.findViewById(R.id.sched_name);
        PillNameTv = view.findViewById(R.id.pill_name_tv);
        ContainerTv = view.findViewById(R.id.container_tv);
        QtyTv = view.findViewById(R.id.quantity_tv);
        DateIntakeTv = view.findViewById(R.id.date_intake_tv);
        TimeIntakeTv = view.findViewById(R.id.time_intake_tv);


        ChangeSched = view.findViewById(R.id.change_sched_btn);

        Intent intent = getActivity().getIntent();
        String data = intent.getStringExtra("Data");
        String container_name = intent.getStringExtra("Container");
        String record_id = intent.getStringExtra("Record_ID");
        String sched_name = intent.getStringExtra("Sched_name");
        String status = intent.getStringExtra("Status");

        SchedNameTv.setText(sched_name);
        ContainerTv.setText(container_name);

        String[] parts = data.split(","); // Split the string at the comma

        for (String part : parts) {
            PillNameTv.setText(parts[0]);
            QtyTv.setText(parts[1]);
            DateIntakeTv.setText(parts[2]);
            TimeIntakeTv.setText(parts[3]);
        }


        return view;

    }
}