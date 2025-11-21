package com.example.venclima;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
 

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.VH> {

    public interface OnMonthClickListener {
        void onMonthClicked(int position);
    }

    private final List<String> months;
    private final LayoutInflater inflater;
    private final OnMonthClickListener listener;

    public MonthAdapter(Context context, List<String> months, OnMonthClickListener listener) {
        this.months = months;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_month_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        String month = months.get(position);
        holder.text.setText(month);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onMonthClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView text;
        VH(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_month);
        }
    }
}
