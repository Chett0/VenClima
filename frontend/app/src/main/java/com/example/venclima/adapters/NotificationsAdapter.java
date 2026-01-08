package com.example.venclima.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venclima.R;
import com.example.venclima.models.IslandNotification;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.IslandItem> implements Filterable {

    private List<IslandNotification> islandNotificationList;
    private List<IslandNotification> filteredIslandNotificationList;

    private final OnIslandCheckedListener listener;

    public NotificationsAdapter(OnIslandCheckedListener listener) {
        this.islandNotificationList = new ArrayList<>();
        this.filteredIslandNotificationList = new ArrayList<>();
        this.listener = listener;
    }

    public void setIslandNotificationList(List<IslandNotification> islandNotificationList) {
        this.islandNotificationList = islandNotificationList;
        if(this.filteredIslandNotificationList.isEmpty())
            this.filteredIslandNotificationList = islandNotificationList;
        notifyDataSetChanged();
    }


    public void setFilteredIslandNotificationList(List<IslandNotification> filteredIslandNotificationList) {
        this.filteredIslandNotificationList = filteredIslandNotificationList;
        notifyDataSetChanged();
    }

    public List<IslandNotification> getFilteredIslandNotificationList() {
        return filteredIslandNotificationList;
    }

    public IslandNotification setFilteredIslandNotificationItem(int position, boolean isChecked) {
        IslandNotification notification = this.filteredIslandNotificationList.get(position);
        notification.SetIsNotified(isChecked);
        return notification;
    }


    @Override
    public Filter getFilter() {
        return islandFilter;
    }

    private final Filter islandFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<IslandNotification> newFilteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                newFilteredList.addAll(islandNotificationList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (IslandNotification item : islandNotificationList) {
                    if (item.getIslandName().toLowerCase().contains(filterPattern)) {
                        newFilteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = newFilteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredIslandNotificationList = (List<IslandNotification>) results.values;
            notifyDataSetChanged();
        }

    };

    @NonNull
    @Override
    public NotificationsAdapter.IslandItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.item_notifications, parent, false);

        return new IslandItem(itemView, this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.IslandItem holder, int position) {
        if (filteredIslandNotificationList == null || position < 0 || position >= filteredIslandNotificationList.size()) return;

        IslandNotification notification = filteredIslandNotificationList.get(position);

        holder.setName(notification.getIslandName());
        holder.setChecked(notification.getIsNotified());
    }

    @Override
    public int getItemCount() {
        return filteredIslandNotificationList == null ? 0 : filteredIslandNotificationList.size();
    }


    public static class IslandItem extends RecyclerView.ViewHolder {

        private CheckBox islandCheckBox;

        public IslandItem(View itemView, OnIslandCheckedListener listener) {
            super(itemView);
            islandCheckBox = itemView.findViewById(R.id.item_checkbox);

            islandCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getBindingAdapterPosition();
                NotificationsAdapter notificationsAdapter = (NotificationsAdapter) getBindingAdapter();

                if (position != RecyclerView.NO_POSITION && notificationsAdapter != null) {
                    if (buttonView.isPressed()) {
                        IslandNotification notification = notificationsAdapter.setFilteredIslandNotificationItem(position, isChecked);
                        listener.onIslandChecked(notification.getIslandId(), isChecked);
                    }
                }
            });

        }

        public void setName(String name) {
            islandCheckBox.setText(name);
        }

        public void setChecked(Boolean isChecked) {
            islandCheckBox.setChecked(isChecked);
        }
    }

}


