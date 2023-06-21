package com.example.bottomnavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class History_Summary extends AppCompatActivity {

    TextView SchedNameTv, DateIntakeTv, TimeIntakeTv, PillNameTv, ContainerTv, QtyTv, ActivityLabel;

    ImageButton BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_summary);


        SchedNameTv = findViewById(R.id.sched_name);
        PillNameTv = findViewById(R.id.pill_name_tv);
        ContainerTv = findViewById(R.id.container_tv);
        QtyTv = findViewById(R.id.quantity_tv);
        DateIntakeTv = findViewById(R.id.date_intake_tv);
        TimeIntakeTv = findViewById(R.id.time_intake_tv);
        ActivityLabel = findViewById(R.id.activity_label);

        Intent intent = getIntent();
        String data = intent.getStringExtra("Data");
        String container_name = intent.getStringExtra("Container");
        String record_id = intent.getStringExtra("Record_ID");
        String sched_name = intent.getStringExtra("Sched_name");
        String activity = intent.getStringExtra("Activity");

        SchedNameTv.setText(sched_name);
        ContainerTv.setText(container_name);
        ActivityLabel.setText(activity);

        String[] parts = data.split(","); // Split the string at the comma

        for (String part : parts) {
            PillNameTv.setText(parts[0].trim());
            QtyTv.setText(parts[1].trim());
            DateIntakeTv.setText(parts[2].trim());
            TimeIntakeTv.setText(parts[3].trim());
        }

        BackBtn = findViewById(R.id.back_btn);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}