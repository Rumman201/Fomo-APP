package com.example.fomo.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fomo.R;
import com.example.fomo.databinding.ActivityMainBinding;
import com.example.fomo.domain.model.AiWeatherAdvisory;
import com.example.fomo.domain.model.AutomationAlert;
import com.example.fomo.domain.model.City;
import com.example.fomo.domain.model.CurrentWeather;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.ui.adapter.HourlyAdapter;
import com.example.fomo.ui.adapter.WeatherIconMapper;
import com.example.fomo.ui.dialog.CitySearchBottomSheetDialog;
import com.example.fomo.ui.dialog.WeeklyForecastBottomSheetDialog;
import com.example.fomo.ui.viewmodel.WeatherUiState;
import com.example.fomo.ui.viewmodel.WeatherViewModel;
import java.util.List;

/**
 * Main Activity managing weather intelligence presentation, multi-city navigation,
 * AI atmospheric briefings, pull-to-refresh, state transition choreography,
 * and extended outlook interactions.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WeatherViewModel viewModel;
    private HourlyAdapter hourlyAdapter;
    private WeatherForecast lastForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        initViewModel();
    }

    private void initViews() {
        hourlyAdapter = new HourlyAdapter();
        binding.view1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.view1.setHasFixedSize(true);
        binding.view1.setItemViewCacheSize(24);
        binding.view1.setAdapter(hourlyAdapter);

        // Pull-to-refresh setup
        binding.swipeRefreshLayout.setColorSchemeResources(
                R.color.fomo_accent_electric_violet,
                R.color.fomo_accent_gold
        );
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.fomo_surface_card);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> viewModel.loadWeather());

        // Error retry button
        binding.statusRetryButton.setOnClickListener(v -> viewModel.loadWeather());

        // City Search / Selection interactions
        binding.citySelectorButton.setOnClickListener(v -> openCitySearchDialog());
        binding.citySearchButton.setOnClickListener(v -> openCitySearchDialog());

        // Dismiss alert banner
        binding.dismissAlertButton.setOnClickListener(v -> binding.weatherAlertBannerContainer.setVisibility(View.GONE));

        // Extended 7-day outlook interaction
        binding.next7DaysButton.setOnClickListener(v -> openWeeklyForecastDialog());
    }

    private void initViewModel() {
        WeatherViewModel.Factory factory = new WeatherViewModel.Factory(this);
        viewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);
        viewModel.getUiState().observe(this, this::renderUiState);
    }

    private void renderUiState(WeatherUiState state) {
        if (state == null) return;

        if (state.isLoading()) {
            binding.loadingIndicator.setVisibility(View.VISIBLE);
        } else {
            binding.loadingIndicator.setVisibility(View.GONE);
            binding.swipeRefreshLayout.setRefreshing(false);
        }

        // Active city label
        City city = state.getSelectedCity() != null ? state.getSelectedCity() : viewModel.getCurrentCity();
        if (city != null) {
            binding.currentCityText.setText(city.getDisplayName());
        }

        if (state.isSuccess() && state.getData() != null) {
            lastForecast = state.getData();
            binding.statusBannerContainer.setVisibility(View.GONE);
            bindWeatherData(lastForecast);
            bindAtmosphericIntelligence(state.getAiAdvisory());
            bindAutomationAlerts(state.getActiveAlerts());
            animateContentEntrance();
        } else if (state.isError()) {
            binding.statusBannerContainer.setVisibility(View.VISIBLE);
            String errorMsg = state.getErrorMessage() != null ? state.getErrorMessage() : getString(R.string.error_loading_weather);
            binding.statusBannerText.setText(errorMsg);
        }
    }

    private void bindWeatherData(@NonNull WeatherForecast forecast) {
        CurrentWeather current = forecast.getCurrentWeather();

        binding.conditionTitle.setText(current.getCondition());
        binding.temperatureText.setText(getString(R.string.temp_celsius_format, current.getTemperature()));
        binding.dateTimeText.setText(current.getDateTime());
        binding.highLowText.setText(current.getHighLow());

        binding.rainPercentageText.setText(current.getRainPercentage());
        binding.windSpeedText.setText(current.getWindSpeed());
        binding.humidityPercentageText.setText(current.getHumidityPercentage());

        int iconRes = WeatherIconMapper.getDrawableResourceId(current.getIconKey());
        binding.conditionIcon.setImageResource(iconRes);

        hourlyAdapter.submitList(forecast.getHourlyForecasts());
    }

    private void bindAtmosphericIntelligence(AiWeatherAdvisory advisory) {
        if (advisory != null) {
            binding.atmosphericIntelligenceCard.setVisibility(View.VISIBLE);
            binding.aiHeadlineText.setText(advisory.getHeadline());
            binding.aiSummaryText.setText(advisory.getSummary());
            binding.aiAttireText.setText(advisory.getAttireAdvice());
            binding.aiOutdoorActivityText.setText(advisory.getOutdoorRecommendation());
        } else {
            binding.atmosphericIntelligenceCard.setVisibility(View.GONE);
        }
    }

    private void bindAutomationAlerts(List<AutomationAlert> alerts) {
        if (alerts != null && !alerts.isEmpty()) {
            AutomationAlert primaryAlert = alerts.get(0);
            binding.weatherAlertBannerContainer.setVisibility(View.VISIBLE);
            binding.weatherAlertTitle.setText(primaryAlert.getTitle());
            binding.weatherAlertMessage.setText(primaryAlert.getMessage());
        } else {
            binding.weatherAlertBannerContainer.setVisibility(View.GONE);
        }
    }

    private void animateContentEntrance() {
        if (!isAnimationEnabled()) {
            binding.weatherContentLayout.setAlpha(1.0f);
            binding.weatherContentLayout.setTranslationY(0f);
            return;
        }

        binding.weatherContentLayout.setAlpha(0.6f);
        binding.weatherContentLayout.setTranslationY(30f);

        binding.weatherContentLayout.animate()
                .alpha(1.0f)
                .translationY(0f)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        binding.weatherContentLayout.setAlpha(1.0f);
                        binding.weatherContentLayout.setTranslationY(0f);
                    }
                })
                .start();
    }

    private void openCitySearchDialog() {
        CitySearchBottomSheetDialog dialog = CitySearchBottomSheetDialog.newInstance(city -> {
            binding.currentCityText.setText(city.getDisplayName());
        });
        dialog.show(getSupportFragmentManager(), CitySearchBottomSheetDialog.TAG);
    }

    private void openWeeklyForecastDialog() {
        WeeklyForecastBottomSheetDialog dialog = WeeklyForecastBottomSheetDialog.newInstance(lastForecast);
        dialog.show(getSupportFragmentManager(), WeeklyForecastBottomSheetDialog.TAG);
    }

    /**
     * Checks if system animator duration scale is greater than zero to respect reduced-motion settings.
     */
    private boolean isAnimationEnabled() {
        try {
            float durationScale = Settings.Global.getFloat(
                    getContentResolver(),
                    Settings.Global.ANIMATOR_DURATION_SCALE,
                    1.0f
            );
            return durationScale > 0f;
        } catch (Exception e) {
            return true;
        }
    }
}
