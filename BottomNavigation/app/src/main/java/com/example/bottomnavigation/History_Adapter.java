package com.example.bottomnavigation;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class History_Adapter extends RecyclerView.Adapter<History_Adapter.MyViewHolder> {

    Context context;

    ArrayList<History_Getter> list;


    private Histroy_SelectListener listener;

    public History_Adapter(Context context, ArrayList<History_Getter> list, Histroy_SelectListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recyclerview_history_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        History_Getter data = list.get(position);
        String pill_data = data.getData();
        String activity = data.getActivity();

        holder.sched_txt.setText(data.getSched_name());
        holder.activity_txt.setText(data.getActivity());

        if (activity.equals("Skipped")){
            holder.activity_txt.setTextColor(Color.parseColor("#FFA500"));
            holder.sched_txt.setTextColor(Color.parseColor("#FFA500"));
            holder.IconStatus.setImageResource(R.drawable.baseline_close_24);
        } else if (activity.equals("Deleted")){
            holder.activity_txt.setTextColor(Color.RED);
            holder.sched_txt.setTextColor(Color.RED);
            holder.IconStatus.setImageResource(R.drawable.delete_icon_history);
        }

        holder.date_txt.setText(data.getDate());
        holder.time_txt.setText(data.getTime());



        holder.history_con.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                listener.onItemClicked(list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView activity_txt, time_txt, date_txt, sched_txt;

        CardView history_con;

        ImageView IconStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sched_txt = itemView.findViewById(R.id.sched_name_tv);
            activity_txt = itemView.findViewById(R.id.activity_tv);
            time_txt = itemView.findViewById(R.id.time_tv);
            date_txt = itemView.findViewById(R.id.date_tv);

            history_con = itemView.findViewById(R.id.history_container_cv);
            IconStatus = itemView.findViewById(R.id.icon_status);

        }

    }

}
