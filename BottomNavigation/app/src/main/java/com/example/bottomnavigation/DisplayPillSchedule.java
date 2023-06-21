package com.example.bottomnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DisplayPillSchedule extends AppCompatActivity {


    EditText PillNameTv, ContainerTv, QtyTv;

    TextView SchedNameTv, DateIntakeTv, TimeIntakeTv;

    Button ChangeSched;

    ImageButton BackBtn;

    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pill_schedule);


        SchedNameTv = findViewById(R.id.sched_name);
        PillNameTv = findViewById(R.id.pill_name_tv);
        ContainerTv = findViewById(R.id.container_tv);
        QtyTv = findViewById(R.id.quantity_tv);
        DateIntakeTv = findViewById(R.id.date_intake_tv);
        TimeIntakeTv = findViewById(R.id.time_intake_tv);

        ChangeSched = findViewById(R.id.change_sched_btn);
        BackBtn = findViewById(R.id.back_btn);

        Intent intent = getIntent();
        String data = intent.getStringExtra("Data");
        String container_name = intent.getStringExtra("Container");
        String record_id = intent.getStringExtra("Record_ID");
        String sched_name = intent.getStringExtra("Sched_name");
        String status = intent.getStringExtra("Status");


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DateIntakeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(DisplayPillSchedule.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = month +"/" + day + "/" + year;
                DateIntakeTv.setText(date);
            }
        };

        TimeIntakeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(DisplayPillSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        TimeIntakeTv.setText(selectedTime);
                    }
                }, 0, 0, false);

                timePickerDialog.show();
            }
        });



        SchedNameTv.setText(sched_name);
        ContainerTv.setText(container_name);

        String[] parts = data.split(","); // Split the string at the comma

        for (String part : parts) {
            PillNameTv.setText(parts[0].trim());
            QtyTv.setText(parts[1].trim());
            DateIntakeTv.setText(parts[2].trim());
            TimeIntakeTv.setText(parts[3].trim());
        }


        ChangeSched.setOnClickListener(view -> {
            String PillName = PillNameTv.getText().toString();
            String ContainerName = ContainerTv.getText().toString();
            String Quantity = QtyTv.getText().toString();
            String Date = DateIntakeTv.getText().toString();
            String Time = TimeIntakeTv.getText().toString();

            UpdateSchedule(PillName, ContainerName, Quantity, Date, Time, record_id);
        });
    }

    private void UpdateSchedule(String PillName, String ContainerName, String Quantity,
                                String Date, String Time, String Record_ID){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Pill Record");
        DocumentReference documentRef = collectionRef.document(Record_ID);

        String UpdateData = PillName + ", " + Quantity + ", " + Date + ", " + Time;

        Map<String, Object> updates = new HashMap<>();
        updates.put("data", UpdateData);
        updates.put("container", ContainerName);

        documentRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DisplayPillSchedule.this, "", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DisplayPillSchedule.this, ContainerScheduleList.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle error
                    }
                });





    }
}