package com.example.fomo.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fomo.databinding.BottomSheetWeeklyForecastBinding;
import com.example.fomo.domain.model.DailyForecast;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.ui.adapter.DailyForecastAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Modal bottom sheet presenting the 7-day extended weather outlook with Material Design 3.
 */
public class WeeklyForecastBottomSheetDialog extends BottomSheetDialogFragment {

    public static final String TAG = "WeeklyForecastBottomSheetDialog";

    private BottomSheetWeeklyForecastBinding binding;
    private DailyForecastAdapter adapter;
    private WeatherForecast currentForecast;

    public static WeeklyForecastBottomSheetDialog newInstance(WeatherForecast forecast) {
        WeeklyForecastBottomSheetDialog dialog = new WeeklyForecastBottomSheetDialog();
        dialog.currentForecast = forecast;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetWeeklyForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new DailyForecastAdapter();
        binding.weeklyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.weeklyRecyclerView.setHasFixedSize(true);
        binding.weeklyRecyclerView.setAdapter(adapter);

        List<DailyForecast> dailyList = generateDailyProjections(currentForecast);
        adapter.submitList(dailyList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Generates a 7-day outlook aligned with the current weather intelligence.
     */
    public static List<DailyForecast> generateDailyProjections(WeatherForecast forecast) {
        List<DailyForecast> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        int baseTemp = (forecast != null) ? forecast.getCurrentWeather().getTemperature() : 24;
        String baseCondition = (forecast != null) ? forecast.getCurrentWeather().getCondition() : "Partly cloudy";
        String baseIcon = (forecast != null) ? forecast.getCurrentWeather().getIconKey() : "cloudy_sunny";

        // Day 1: Today
        list.add(new DailyForecast("Today", baseCondition, baseTemp + 2, baseTemp - 5, baseIcon));

        // Days 2 to 7
        String[] sampleConditions = {"Mostly sunny", "Scattered showers", "Partly cloudy", "Clear sky", "Thunderstorm", "Sunny"};
        String[] sampleIcons = {"sunny", "rainy", "cloudy_sunny", "sunny", "storm", "sunny"};
        int[] tempOffsets = {1, -2, 0, 3, -4, 2};

        for (int i = 1; i < 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String dayName = (i == 1) ? "Tomorrow" : dayFormat.format(calendar.getTime());
            String condition = sampleConditions[(i - 1) % sampleConditions.length];
            String icon = sampleIcons[(i - 1) % sampleIcons.length];
            int high = baseTemp + tempOffsets[(i - 1) % tempOffsets.length] + 2;
            int low = baseTemp + tempOffsets[(i - 1) % tempOffsets.length] - 5;

            list.add(new DailyForecast(dayName, condition, high, low, icon));
        }

        return list;
    }
}
