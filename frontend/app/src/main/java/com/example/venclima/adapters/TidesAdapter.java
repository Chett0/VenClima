package com.example.venclima.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venclima.R;
import com.example.venclima.models.Station;
import com.example.venclima.models.Tide;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TidesAdapter extends RecyclerView.Adapter<TidesAdapter.ViewHolder> {

    private List<Tide> tides;
    private List<Tide> realTimeTides;
    private List<Station> stations;

    public void setTides(List<Tide> tides) {
        this.tides = tides;
        notifyDataSetChanged();
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
        notifyDataSetChanged();
    }

    public void setRealTimeTides(List<Tide> realTimeTides) {
        this.realTimeTides = realTimeTides;
        notifyDataSetChanged();
    }


    public TidesAdapter() {
        this.tides = new ArrayList<>();
        this.stations = new ArrayList<>();
        this.realTimeTides = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Tide curr = realTimeTides.get(position);
        Optional<Station> station = stations.stream().filter(s -> s.getId().equals(curr.getStationId())).findFirst();

        if(station.isEmpty())
            return;

        holder.stationName.setText(station.get().getName());
        holder.tideLevel.setText(curr.getLevel() + " cm");
    }

    @Override
    public int getItemCount() {
        return realTimeTides.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stationName;
        TextView tideLevel;

        ViewHolder(View itemView) {
            super(itemView);

            stationName = itemView.findViewById(R.id.stationName);
            tideLevel = itemView.findViewById(R.id.tideLevel);
        }
    }
}

