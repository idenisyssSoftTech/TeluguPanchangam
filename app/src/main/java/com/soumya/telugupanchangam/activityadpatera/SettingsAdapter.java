package com.soumya.telugupanchangam.activityadpatera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soumya.telugupanchangam.R;
import com.soumya.telugupanchangam.models.SettingsModel;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MySettingHolderView> {
    Context context;
    List<SettingsModel> settingsModels;


    public SettingsAdapter(Context context, List<SettingsModel> settingsModels) {
        this.context = context;
        this.settingsModels = settingsModels;
    }

    @NonNull
    @Override
    public SettingsAdapter.MySettingHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_settings,parent,false);
        return new MySettingHolderView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsAdapter.MySettingHolderView holder, int position) {
        holder.settings_title_tv.setText(settingsModels.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return settingsModels.size();
    }

    public class MySettingHolderView extends RecyclerView.ViewHolder {
        TextView settings_title_tv;
        public MySettingHolderView(@NonNull View itemView) {
            super(itemView);
            settings_title_tv = itemView.findViewById(R.id.settings_title_tv);
        }
    }
}
