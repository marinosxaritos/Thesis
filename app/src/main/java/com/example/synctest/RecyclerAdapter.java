package com.example.synctest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Contact> arrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<Contact> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return  new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.Name.setText(arrayList.get(position).getName());
        holder.LastName.setText(arrayList.get(position).getLastName());
        holder.Date.setText(arrayList.get(position).getDate());
        holder.Time.setText(arrayList.get(position).getTime());
        holder.Address.setText(arrayList.get(position).getAddress());


        int sync_status = arrayList.get(position).getSync_status();
        if (sync_status == DbContact.SYNC_STATUS_OK) {
            holder.Sync_Status.setImageResource(R.drawable.baseline_add_circle_outline_24);
        }
        else {
            holder.Sync_Status.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView Sync_Status;
        TextView Name;
        TextView LastName;
        TextView Date;
        TextView Time;
        TextView Address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Sync_Status = (ImageView) itemView.findViewById(R.id.imgSync);
            Name = (TextView) itemView.findViewById(R.id.txtName);
            LastName = (TextView) itemView.findViewById(R.id.txtLastName);
            Date = (TextView) itemView.findViewById(R.id.txtDate);
            Time = (TextView) itemView.findViewById(R.id.txtTime);
            Address = (TextView) itemView.findViewById(R.id.txtAddress);
        }
    }
}
