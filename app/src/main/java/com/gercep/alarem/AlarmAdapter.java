package com.gercep.alarem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    protected List<String> mWaktus;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView alarmObj;
        public ViewHolder(View itemView) {
            super(itemView);
            alarmObj = (TextView) itemView.findViewById(R.id.alarmList);
        }
    }

    public AlarmAdapter(List<String> mWaktuAlarm) {
        this.mWaktus = new ArrayList<>();
        this.mWaktus.addAll(mWaktuAlarm);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmAdapter.ViewHolder holder, int position) {
        holder.alarmObj.setText(mWaktus.get(position));
    }

    @Override
    public int getItemCount() {
        return mWaktus.size();
    }

    public String getAlarmLast() {
        return mWaktus.get(mWaktus.size()-1);
    }
}
