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
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import android.graphics.Color;
import org.maplibre.android.log.Logger;
import com.github.mikephil.charting.components.XAxis;

public class TidesAdapter extends RecyclerView.Adapter<TidesAdapter.ViewHolder> {

    private List<Station> stations;
    private Map<Integer, List<Tide>> stationTidesMap;

    public void setStations(List<Station> stations) {
        if (stations == null) {
            this.stations = null;
        } else {
            List<Station> filtered = new ArrayList<>();
            for (Station s : stations) {
                if (s == null) continue;
                if (s.getId() != null && s.getId().equals(1029)) continue; //no venezia misericordia
                filtered.add(s);
            }
            this.stations = filtered;
        }
        notifyDataSetChanged();
    }

    public void setStationTides(Map<Integer, List<Tide>> stationTidesMap) {
        this.stationTidesMap = stationTidesMap;
        notifyDataSetChanged();
    }

    public TidesAdapter() {
        this.stations = new ArrayList<>();
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
        // stations list for stations; stationTidesMap for <stations, tides>
        if (stations == null || position < 0 || position >= stations.size()) return;
        Station station = stations.get(position);
        holder.stationName.setText(station.getName());

        Tide curr = null;
        if (stationTidesMap != null) {
            List<Tide> latestList = stationTidesMap.get(station.getId());
            if (latestList != null && !latestList.isEmpty()) {
                curr = latestList.get(latestList.size() - 1); // asc sorting
            }
        }

        String level = curr != null ? curr.getLevel() + " m" : "Non disponibile";
        holder.tideLevel.setText(level);

        // populate chart for each station
        if (stationTidesMap != null) {
            // for punta salute canale giudecca and s.geremia, refer to punta salute canal grande
            Integer displayStationId = station.getId();
            String nameLower = station.getName() != null ? station.getName().toLowerCase() : "";
            if (nameLower.contains("punta salute") || nameLower.contains("canale giudecca") || nameLower.contains("s. geremia")) {
                if (stations != null) {
                    for (Station s : stations) {
                        if (s.getName() != null) {
                            String n = s.getName().toLowerCase();
                            if (n.contains("punta") && n.contains("salute")) {
                                displayStationId = s.getId();
                                break;
                            }
                        }
                    }
                }
            }

            List<Tide> list = stationTidesMap != null ? stationTidesMap.get(displayStationId) : null;
            LineChart chart = holder.stationChart;
            if (list == null || list.isEmpty()) {
                chart.clear();
                chart.setNoDataText("No data");
            } else {
                List<Entry> entries = new ArrayList<>();

                LocalDateTime start = LocalDate.now().atStartOfDay();
                for (Tide t : list) {
                    try {
                        LocalDateTime dt = LocalDateTime.parse(t.getDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        long minutes = Duration.between(start, dt).toMinutes();
                        entries.add(new Entry((float) minutes, (float) t.getLevel()));
                    } catch (Exception ex) {
                        // skip unparsable
                    }
                }

                LineDataSet set = new LineDataSet(entries, "Livello");
                int lineColor = Color.parseColor("#1E88E5");
                int fillColor = Color.parseColor("#BBDEFB"); 

                set.setColor(lineColor);
                set.setDrawCircles(false);
                set.setLineWidth(2f);
                set.setDrawValues(false);
                set.setMode(LineDataSet.Mode.LINEAR);
                set.setDrawFilled(true);
                set.setFillColor(fillColor);
                set.setFillAlpha(180);

                LineData data = new LineData(set);
                chart.setData(data);
                chart.setBackgroundColor(Color.WHITE);
                chart.getDescription().setEnabled(false);
                chart.getLegend().setEnabled(false);
                //styles
                chart.getXAxis().setTextColor(lineColor);
                chart.getAxisLeft().setTextColor(lineColor);
                chart.getAxisRight().setEnabled(false);
                chart.getXAxis().setGridColor(Color.parseColor("#E3F2FD"));
                chart.getAxisLeft().setGridColor(Color.parseColor("#E3F2FD"));
                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chart.getXAxis().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        int mins = (int) value;
                        int hh = (mins / 60) % 24;
                        int mm = mins % 60;
                        return String.format("%02d:%02d", hh, mm);
                    }
                });
                chart.invalidate();
            }
        }
    }

    @Override
    public int getItemCount() {
        return stations == null ? 0 : stations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stationName;
        TextView tideLevel;
        LineChart stationChart;

        ViewHolder(View itemView) {
            super(itemView);

            stationName = itemView.findViewById(R.id.stationName);
            tideLevel = itemView.findViewById(R.id.tideLevel);
            stationChart = itemView.findViewById(R.id.stationChart);
        }
    }
}

