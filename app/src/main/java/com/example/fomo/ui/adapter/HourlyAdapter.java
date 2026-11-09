package com.example.fomo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fomo.R;
import com.example.fomo.databinding.ViewholderHourlyBinding;
import com.example.fomo.domain.model.Hourly;

import java.util.Objects;

/**
 * Modern ListAdapter for rendering hourly weather forecast items.
 * Uses DiffUtil for asynchronous item change detection and smooth list animations.
 */
public class HourlyAdapter extends ListAdapter<Hourly, HourlyAdapter.ViewHolder> {

    public static final DiffUtil.ItemCallback<Hourly> DIFF_CALLBACK = new DiffUtil.ItemCallback<Hourly>() {
        @Override
        public boolean areItemsTheSame(@NonNull Hourly oldItem, @NonNull Hourly newItem) {
            return oldItem.getHour().equals(newItem.getHour());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Hourly oldItem, @NonNull Hourly newItem) {
            return oldItem.equals(newItem);
        }
    };

    public HourlyAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderHourlyBinding binding = ViewholderHourlyBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderHourlyBinding binding;

        public ViewHolder(@NonNull ViewholderHourlyBinding binding) {
            super(binding.getRoot());
            this.binding = Objects.requireNonNull(binding);
        }

        public void bind(@NonNull Hourly item) {
            Context context = itemView.getContext();
            binding.hourTxt.setText(item.getHour());
            binding.tempTxt.setText(context.getString(R.string.temp_celsius_format, item.getTemp()));

            int drawableResourceId = WeatherIconMapper.getDrawableResourceId(item.getPicPath());
            Glide.with(context)
                    .load(drawableResourceId)
                    .override(64, 64)
                    .into(binding.pic);
        }
    }
}
