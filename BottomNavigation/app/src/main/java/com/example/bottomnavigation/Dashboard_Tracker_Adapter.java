package com.example.bottomnavigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Dashboard_Tracker_Adapter extends RecyclerView.Adapter<Dashboard_Tracker_Adapter.MyViewHolder> {

    Context context;

    ArrayList<Data_Entry_Getter> list;

    private Dashboard_tracker_SelectListener listener;

    public Dashboard_Tracker_Adapter(Context context, ArrayList<Data_Entry_Getter> list, Dashboard_tracker_SelectListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_pill_latest, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Data_Entry_Getter data = list.get(position);
        String pill_data = data.getData();

        holder.sched_txt.setText(data.getSched_name());

        String[] parts = pill_data.split(",");

        for (String part : parts) {
            System.out.println(part);

            holder.data_txt.setText(parts[0]);
            holder.date_txt.setText(parts[2]);
            holder.time_txt.setText(parts[3]);
        }




        holder.med_name_cv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.onItemClicked(list.get(position));
            }
        });

        //holder.data_txt.setText(data.getData());
        //holder.container_txt.setText(data.getContainer());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView data_txt, container_txt, time_txt, date_txt, sched_txt;

        CardView med_name_cv;

        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            data_txt = itemView.findViewById(R.id.med_name_tv);
            //container_txt = itemView.findViewById(R.id.container_tv);
            sched_txt = itemView.findViewById(R.id.sched_name);
            time_txt = itemView.findViewById(R.id.time_intake_tv);
            date_txt = itemView.findViewById(R.id.date_intake_tv);

            med_name_cv = itemView.findViewById(R.id.med_container_cv);
            imageView = itemView.findViewById(R.id.imageView5);

        }

    }

}
