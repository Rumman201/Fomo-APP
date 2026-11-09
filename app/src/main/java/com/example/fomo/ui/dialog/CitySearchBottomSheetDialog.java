package com.example.fomo.ui.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fomo.R;
import com.example.fomo.databinding.BottomSheetCitySearchBinding;
import com.example.fomo.domain.model.City;
import com.example.fomo.domain.repository.LocationRepository;
import com.example.fomo.security.sanitizer.InputSanitizer;
import com.example.fomo.ui.adapter.CityAdapter;
import com.example.fomo.ui.viewmodel.WeatherViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Material 3 modal bottom sheet allowing users to search worldwide cities,
 * view saved favorites, and select an active location.
 */
public class CitySearchBottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = "CitySearchBottomSheetDialog";
    private static final long DEBOUNCE_DELAY_MS = 350;

    public interface CitySelectionListener {
        void onCitySelected(@NonNull City city);
    }

    private BottomSheetCitySearchBinding binding;
    private WeatherViewModel viewModel;
    private CityAdapter adapter;
    private CitySelectionListener selectionListener;

    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    public static CitySearchBottomSheetDialog newInstance(@Nullable CitySelectionListener listener) {
        CitySearchBottomSheetDialog dialog = new CitySearchBottomSheetDialog();
        dialog.selectionListener = listener;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetCitySearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);

        adapter = new CityAdapter(
                city -> {
                    viewModel.selectCity(city);
                    if (selectionListener != null) {
                        selectionListener.onCitySelected(city);
                    }
                    dismiss();
                },
                city -> viewModel.toggleFavorite(city, new LocationRepository.Callback<City>() {
                    @Override
                    public void onSuccess(@NonNull City updated) {
                        // Refresh current list view
                        String currentQuery = binding.searchInputEditText.getText() != null
                                ? binding.searchInputEditText.getText().toString().trim()
                                : "";
                        if (currentQuery.isEmpty()) {
                            loadSavedCities();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable error) {
                    }
                })
        );

        binding.citiesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.citiesRecyclerView.setHasFixedSize(true);
        binding.citiesRecyclerView.setAdapter(adapter);

        setupSearchInput();
        loadSavedCities();
    }

    private void setupSearchInput() {
        binding.searchInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                String query = (s != null) ? s.toString().trim() : "";
                if (query.isEmpty()) {
                    loadSavedCities();
                } else if (query.length() >= 2) {
                    searchRunnable = () -> executeSearch(query);
                    searchHandler.postDelayed(searchRunnable, DEBOUNCE_DELAY_MS);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadSavedCities() {
        binding.searchLoadingIndicator.setVisibility(View.GONE);
        binding.sectionSubtitleText.setText(R.string.saved_cities_section);

        viewModel.getSavedCities(new LocationRepository.Callback<List<City>>() {
            @Override
            public void onSuccess(@NonNull List<City> cities) {
                if (binding == null) return;
                binding.searchLoadingIndicator.setVisibility(View.GONE);
                if (cities.isEmpty()) {
                    binding.emptyStateText.setVisibility(View.VISIBLE);
                    binding.emptyStateText.setText(R.string.no_cities_found);
                    adapter.submitList(new ArrayList<>());
                } else {
                    binding.emptyStateText.setVisibility(View.GONE);
                    adapter.submitList(cities);
                }
            }

            @Override
            public void onError(@NonNull Throwable error) {
                if (binding == null) return;
                binding.searchLoadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    private void executeSearch(@NonNull String query) {
        if (binding == null) return;
        String sanitizedQuery = InputSanitizer.sanitizeSearchQuery(query);
        if (sanitizedQuery.isEmpty()) {
            loadSavedCities();
            return;
        }

        binding.searchLoadingIndicator.setVisibility(View.VISIBLE);
        binding.sectionSubtitleText.setText(R.string.search_results_section);

        viewModel.searchCities(sanitizedQuery, new LocationRepository.Callback<List<City>>() {
            @Override
            public void onSuccess(@NonNull List<City> results) {
                if (binding == null) return;
                binding.searchLoadingIndicator.setVisibility(View.GONE);
                if (results.isEmpty()) {
                    binding.emptyStateText.setVisibility(View.VISIBLE);
                    binding.emptyStateText.setText(R.string.no_cities_found);
                    adapter.submitList(new ArrayList<>());
                } else {
                    binding.emptyStateText.setVisibility(View.GONE);
                    adapter.submitList(results);
                }
            }

            @Override
            public void onError(@NonNull Throwable error) {
                if (binding == null) return;
                binding.searchLoadingIndicator.setVisibility(View.GONE);
                binding.emptyStateText.setVisibility(View.VISIBLE);
                binding.emptyStateText.setText(R.string.no_cities_found);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
        binding = null;
    }
}

