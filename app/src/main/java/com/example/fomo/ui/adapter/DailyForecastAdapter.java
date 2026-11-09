package com.example.fomo.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fomo.databinding.ItemDailyForecastBinding;
import com.example.fomo.domain.model.DailyForecast;

/**
 * ListAdapter for rendering the 7-day extended weather outlook with DiffUtil.
 */
public class DailyForecastAdapter extends ListAdapter<DailyForecast, DailyForecastAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<DailyForecast> DIFF_CALLBACK = new DiffUtil.ItemCallback<DailyForecast>() {
        @Override
        public boolean areItemsTheSame(@NonNull DailyForecast oldItem, @NonNull DailyForecast newItem) {
            return oldItem.getDayName().equals(newItem.getDayName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DailyForecast oldItem, @NonNull DailyForecast newItem) {
            return oldItem.equals(newItem);
        }
    };

    public DailyForecastAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDailyForecastBinding binding = ItemDailyForecastBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemDailyForecastBinding binding;

        ViewHolder(ItemDailyForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(DailyForecast item) {
            binding.dayNameText.setText(item.getDayName());
            binding.dayConditionText.setText(item.getCondition());
            binding.dayHighLowText.setText(item.getHighLowFormatted());

            int iconRes = WeatherIconMapper.getDrawableResourceId(item.getIconKey());
            binding.dayConditionIcon.setImageResource(iconRes);
        }
    }
}
