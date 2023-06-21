package com.example.bottomnavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class FaqsActivity extends AppCompatActivity {

    private LinearLayout btn_hide_show, layout_hide_show;
    private LinearLayout btn_hide_show1, layout_hide_show1;
    private LinearLayout btn_hide_show2, layout_hide_show2;
    private LinearLayout btn_hide_show3, layout_hide_show3;
    private LinearLayout btn_hide_show4, layout_hide_show4;
    private LinearLayout btn_hide_show5, layout_hide_show5;
    private LinearLayout btn_hide_show6, layout_hide_show6;
    private LinearLayout btn_hide_show7, layout_hide_show7;
    private LinearLayout btn_hide_show8, layout_hide_show8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        btn_hide_show = findViewById(R.id.btn_ans);
        layout_hide_show = findViewById(R.id.layout_hide_show);

        btn_hide_show1 = findViewById(R.id.btn_ans1);
        layout_hide_show1 = findViewById(R.id.layout_hide_show1);

        btn_hide_show2 = findViewById(R.id.btn_ans2);
        layout_hide_show2 = findViewById(R.id.layout_hide_show2);

        btn_hide_show3 = findViewById(R.id.btn_ans3);
        layout_hide_show3 = findViewById(R.id.layout_hide_show3);

        btn_hide_show4 = findViewById(R.id.btn_ans4);
        layout_hide_show4 = findViewById(R.id.layout_hide_show4);

        btn_hide_show5 = findViewById(R.id.btn_ans5);
        layout_hide_show5 = findViewById(R.id.layout_hide_show5);

        btn_hide_show6 = findViewById(R.id.btn_ans6);
        layout_hide_show6 = findViewById(R.id.layout_hide_show6);

        btn_hide_show7 = findViewById(R.id.btn_ans7);
        layout_hide_show7 = findViewById(R.id.layout_hide_show7);

        btn_hide_show8 = findViewById(R.id.btn_ans8);
        layout_hide_show8 = findViewById(R.id.layout_hide_show8);

        btn_hide_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isvisible = layout_hide_show.getVisibility();
                if (isvisible == View.VISIBLE) {
                    layout_hide_show.setVisibility(View.GONE);
                } else {
                    layout_hide_show.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_hide_show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isvisible = layout_hide_show1.getVisibility();
                if (isvisible == View.VISIBLE) {
                    layout_hide_show1.setVisibility(View.GONE);
                } else {
                    layout_hide_show1.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_hide_show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isvisible = layout_hide_show2.getVisibility();
                if (isvisible == View.VISIBLE) {
                    layout_hide_show2.setVisibility(View.GONE);
                } else {
                    layout_hide_show2.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_hide_show3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isvisible = layout_hide_show3.getVisibility();
                if (isvisible == View.VISIBLE) {
                    layout_hide_show3.setVisibility(View.GONE);
                } else {
                    layout_hide_show3.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_hide_show4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isvisible = layout_hide_show4.getVisibility();
                if (isvisible == View.VISIBLE) {
                    layout_hide_show4.setVisibility(View.GONE);
                } else {
                    layout_hide_show4.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_hide_show5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isvisible = layout_hide_show5.getVisibility();
                if (isvisible == View.VISIBLE) {
                    layout_hide_show5.setVisibility(View.GONE);
                } else {
                    layout_hide_show5.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_hide_show6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isvisible = layout_hide_show6.getVisibility();
                if (isvisible == View.VISIBLE) {
                    layout_hide_show6.setVisibility(View.GONE);
                } else {
                    layout_hide_show6.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_hide_show7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isvisible = layout_hide_show7.getVisibility();
                if (isvisible == View.VISIBLE) {
                    layout_hide_show7.setVisibility(View.GONE);
                } else {
                    layout_hide_show7.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_hide_show8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isvisible = layout_hide_show8.getVisibility();
                if (isvisible == View.VISIBLE) {
                    layout_hide_show8.setVisibility(View.GONE);
                } else {
                    layout_hide_show8.setVisibility(View.VISIBLE);
                }
            }
        });


    }
}
