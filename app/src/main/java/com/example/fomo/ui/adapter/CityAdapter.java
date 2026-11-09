package com.example.fomo.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fomo.R;
import com.example.fomo.databinding.ItemCityBinding;
import com.example.fomo.domain.model.City;
import java.util.Objects;

/**
 * ListAdapter for rendering searchable and saved cities with DiffUtil and tactile click feedback.
 */
public class CityAdapter extends ListAdapter<City, CityAdapter.CityViewHolder> {

    public interface OnCityClickListener {
        void onCityClick(@NonNull City city);
    }

    public interface OnFavoriteToggleListener {
        void onFavoriteToggle(@NonNull City city);
    }

    private final OnCityClickListener clickListener;
    private final OnFavoriteToggleListener favoriteListener;

    public CityAdapter(@NonNull OnCityClickListener clickListener,
                       @NonNull OnFavoriteToggleListener favoriteListener) {
        super(DIFF_CALLBACK);
        this.clickListener = Objects.requireNonNull(clickListener, "clickListener must not be null");
        this.favoriteListener = Objects.requireNonNull(favoriteListener, "favoriteListener must not be null");
    }

    private static final DiffUtil.ItemCallback<City> DIFF_CALLBACK = new DiffUtil.ItemCallback<City>() {
        @Override
        public boolean areItemsTheSame(@NonNull City oldItem, @NonNull City newItem) {
            if (oldItem.getId() > 0 && newItem.getId() > 0) {
                return oldItem.getId() == newItem.getId();
            }
            return oldItem.getName().equalsIgnoreCase(newItem.getName())
                    && Math.abs(oldItem.getLatitude() - newItem.getLatitude()) < 0.001
                    && Math.abs(oldItem.getLongitude() - newItem.getLongitude()) < 0.001;
        }

        @Override
        public boolean areContentsTheSame(@NonNull City oldItem, @NonNull City newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCityBinding binding = ItemCityBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CityViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class CityViewHolder extends RecyclerView.ViewHolder {
        private final ItemCityBinding binding;

        CityViewHolder(ItemCityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(City city) {
            binding.cityNameText.setText(city.getName());

            String region = city.getCountry();
            if (city.getAdminRegion() != null && !city.getAdminRegion().trim().isEmpty() && !city.getAdminRegion().equalsIgnoreCase(city.getName())) {
                region = city.getAdminRegion() + ", " + city.getCountry();
            }
            binding.cityRegionText.setText(region);

            if (city.isFavorite()) {
                binding.favoriteButton.setImageResource(R.drawable.ic_star_filled);
                binding.favoriteButton.setColorFilter(null);
            } else {
                binding.favoriteButton.setImageResource(R.drawable.ic_star_outline);
            }

            itemView.setOnClickListener(v -> clickListener.onCityClick(city));
            binding.favoriteButton.setOnClickListener(v -> favoriteListener.onFavoriteToggle(city));
        }
    }
}

