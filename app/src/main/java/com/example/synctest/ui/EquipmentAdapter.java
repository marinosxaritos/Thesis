package com.example.synctest.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.synctest.R;

import java.util.ArrayList;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    View view;
    Context context;
    ArrayList<String>  arrayList;
    EquipmentListener equipmentListener;

    public ArrayList<String> arrayList_0 = new ArrayList<>();

    public EquipmentAdapter(Context context, ArrayList<String> arrayList, EquipmentListener equipmentListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.equipmentListener = equipmentListener;
    }

    public View getView() {
        return view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.equipment_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (arrayList != null && arrayList.size() > 0) {
            holder.checkBox.setText(arrayList.get(position));
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.checkBox.isChecked()) {
                        arrayList_0.add(arrayList.get(position));
                        holder.checkBox.setBackgroundResource(R.drawable.custom_card);
                    } else {
                        arrayList_0.remove(arrayList.get(position));
                        holder.checkBox.setBackgroundResource(R.color.background);
                    }
                    equipmentListener.onEquipmentChange(arrayList_0);
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
