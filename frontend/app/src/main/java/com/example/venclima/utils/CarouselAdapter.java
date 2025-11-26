package com.example.venclima.utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venclima.R;
import com.example.venclima.models.RealTimeTide;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {

    private final List<RealTimeTide> items;

    public CarouselAdapter(List<RealTimeTide> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        RealTimeTide curr = items.get(position);

        holder.stationName.setText(curr.getStationName());
        holder.tideLevel.setText(curr.getTideLevel().toString() + " cm");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stationName;
        TextView tideLevel;

        ViewHolder(View itemView) {
            super(itemView);

            stationName = itemView.findViewById(R.id.stationName);
            tideLevel = itemView.findViewById(R.id.tideLevel);
        }
    }
}

