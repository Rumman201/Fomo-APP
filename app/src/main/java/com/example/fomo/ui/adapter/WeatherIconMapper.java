package com.example.fomo.ui.adapter;

import androidx.annotation.DrawableRes;
import com.example.fomo.R;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Deterministic, type-safe mapper from weather condition strings to compiled drawable resources.
 * Replaces discouraged runtime reflection (Resources.getIdentifier) with O(1) in-memory lookup.
 */
public final class WeatherIconMapper {

    private static final Map<String, Integer> ICON_MAP = new HashMap<>();

    static {
        ICON_MAP.put("cloudy", R.drawable.cloudy);
        ICON_MAP.put("cloudy_sunny", R.drawable.cloudy_sunny);
        ICON_MAP.put("sun", R.drawable.sunny);
        ICON_MAP.put("sunny", R.drawable.sunny);
        ICON_MAP.put("rain", R.drawable.rain);
        ICON_MAP.put("rainy", R.drawable.rainy);
        ICON_MAP.put("wind", R.drawable.wind);
        ICON_MAP.put("windy", R.drawable.windy);
        ICON_MAP.put("storm", R.drawable.storm);
        ICON_MAP.put("snowy", R.drawable.snowy);
        ICON_MAP.put("snow", R.drawable.snowy);
        ICON_MAP.put("humidity", R.drawable.humidity);
    }

    private WeatherIconMapper() {
        // Utility class
    }

    /**
     * Resolves a weather condition key to its corresponding drawable resource ID.
     *
     * @param iconKey Weather condition string (e.g. "cloudy", "sun", "rainy")
     * @return Compiled drawable resource ID or fallback R.drawable.cloudy_sunny
     */
    @DrawableRes
    public static int getDrawableResourceId(String iconKey) {
        if (iconKey == null) {
            return R.drawable.cloudy_sunny;
        }
        Integer resId = ICON_MAP.get(iconKey.trim().toLowerCase(Locale.US));
        return (resId != null) ? resId : R.drawable.cloudy_sunny;
    }
}
