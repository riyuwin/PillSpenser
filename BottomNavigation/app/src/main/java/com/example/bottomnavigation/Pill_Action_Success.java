package com.example.bottomnavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Pill_Action_Success extends AppCompatActivity {

    TextView MedicineName;

    Button HomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_action_success);

        MedicineName = findViewById(R.id.medName);

        HomeBtn = findViewById(R.id.home_btn);

        Intent Get_Credentials = getIntent();
        String data = Get_Credentials.getStringExtra("DATA");


        String[] parts = data.split(",");

        for (String part : parts) {


            MedicineName.setText(parts[0].trim());

        }

        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pill_Action_Success.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}