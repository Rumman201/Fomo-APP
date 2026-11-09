package com.example.fomo.domain.service;

import androidx.annotation.NonNull;
import com.example.fomo.core.cache.FastLruCache;
import com.example.fomo.domain.model.ActivityScores;
import com.example.fomo.domain.model.AtmosphericIntelligence;
import com.example.fomo.domain.model.AtmosphericRisk;
import com.example.fomo.domain.model.CurrentWeather;
import java.util.Locale;

/**
 * High-performance edge intelligence service calculating thermal comfort,
 * attire recommendations, multi-factor activity suitability, and atmospheric risks.
 * Executes 100% on-device with zero cloud latency.
 */
public class AtmosphericIntelligenceService {

    private final FastLruCache<Long, Double> apparentTempCache = new FastLruCache<>(128);

    public AtmosphericIntelligence analyze(@NonNull CurrentWeather weather) {
        double temp = weather.getTemperature();
        double humidity = parsePercentage(weather.getHumidityPercentage(), 50.0);
        double windKmh = parseWindSpeed(weather.getWindSpeed(), 10.0);
        int rainPercent = (int) parsePercentage(weather.getRainPercentage(), 0.0);
        String condition = weather.getCondition() != null ? weather.getCondition() : "";

        double apparentTemp = calculateApparentTemperature(temp, humidity, windKmh);
        String comfortCategory = categorizeComfort(apparentTemp);
        String clothingAdvice = deriveAttireRecommendation(temp, apparentTemp, rainPercent, windKmh, condition);
        ActivityScores activityScores = calculateActivityScores(temp, rainPercent, windKmh, condition);
        AtmosphericRisk risk = assessAtmosphericRisk(temp, windKmh, rainPercent, condition);

        return new AtmosphericIntelligence(apparentTemp, comfortCategory, clothingAdvice, activityScores, risk);
    }

    /**
     * Calculates thermal comfort index using NOAA Steadman Heat Index or Wind Chill equations.
     * Results are memoized in an in-memory LRU cache to eliminate redundant transcendental floating-point evaluations.
     */
    public double calculateApparentTemperature(double tempC, double humidityPercent, double windSpeedKmh) {
        long memoKey = toMemoKey(tempC, humidityPercent, windSpeedKmh);
        Double cached = apparentTempCache.get(memoKey);
        if (cached != null) {
            return cached;
        }

        double result;
        if (tempC >= 27.0 && humidityPercent >= 40.0) {
            // NOAA Steadman Heat Index formula
            double tf = tempC * 1.8 + 32.0;
            double r = humidityPercent;
            double hiF = -42.379
                    + 2.04901523 * tf
                    + 10.14333127 * r
                    - 0.22475541 * tf * r
                    - 0.00683783 * tf * tf
                    - 0.05481717 * r * r
                    + 0.00122874 * tf * tf * r
                    + 0.00085282 * tf * r * r
                    - 0.00000199 * tf * tf * r * r;
            result = Math.round(((hiF - 32.0) / 1.8) * 10.0) / 10.0;
        } else if (tempC <= 10.0 && windSpeedKmh >= 4.8) {
            // Environment Canada / NOAA Wind Chill formula
            double wc = 13.12
                    + 0.6215 * tempC
                    - 11.37 * Math.pow(windSpeedKmh, 0.16)
                    + 0.3965 * tempC * Math.pow(windSpeedKmh, 0.16);
            result = Math.round(wc * 10.0) / 10.0;
        } else {
            result = tempC;
        }

        apparentTempCache.put(memoKey, result);
        return result;
    }

    private static long toMemoKey(double t, double h, double w) {
        long tBits = Math.round(t * 10);
        long hBits = Math.round(h * 10);
        long wBits = Math.round(w * 10);
        return (tBits & 0xFFFFFL) << 40 | (hBits & 0xFFFFFL) << 20 | (wBits & 0xFFFFFL);
    }

    public String categorizeComfort(double apparentTempC) {
        if (apparentTempC >= 38.0) return "Extreme Heat Hazard";
        if (apparentTempC >= 30.0) return "Warm & Humid";
        if (apparentTempC >= 20.0) return "Optimal Comfort";
        if (apparentTempC >= 13.0) return "Mild & Fresh";
        if (apparentTempC >= 5.0) return "Cool & Crisp";
        if (apparentTempC >= 0.0) return "Chilly";
        return "Freezing Frost";
    }

    public String deriveAttireRecommendation(double tempC, double apparentTempC,
                                             int rainPercent, double windSpeedKmh,
                                             @NonNull String condition) {
        StringBuilder sb = new StringBuilder();

        // Base thermal layer
        if (tempC < 0.0) {
            sb.append("Heavy winter coat, thermal layers, gloves, and warm hat required.");
        } else if (tempC < 10.0) {
            sb.append("Warm winter jacket, scarf, and layered clothing recommended.");
        } else if (tempC < 17.0) {
            sb.append("Light jacket, fleece, or sweater over everyday wear.");
        } else if (tempC < 25.0) {
            sb.append("Comfortable t-shirt and pants or casual wear.");
        } else {
            sb.append("Light, breathable cotton clothing and UV sun protection.");
        }

        // Precipitation modifier
        String lowerCondition = condition.toLowerCase(Locale.ROOT);
        boolean isWetCondition = lowerCondition.contains("rain")
                || lowerCondition.contains("shower")
                || lowerCondition.contains("drizzle")
                || lowerCondition.contains("storm")
                || rainPercent >= 40;

        if (isWetCondition) {
            sb.append(" Carry an umbrella or waterproof shell.");
        }

        // Wind modifier
        if (windSpeedKmh >= 35.0) {
            sb.append(" Windbreaker suggested for strong gusts.");
        }

        return sb.toString();
    }

    public ActivityScores calculateActivityScores(double tempC, int rainPercent,
                                                  double windKmh, @NonNull String condition) {
        String lowerCond = condition.toLowerCase(Locale.ROOT);
        boolean isStorm = lowerCond.contains("storm") || lowerCond.contains("thunder");

        if (isStorm) {
            return new ActivityScores(15, 10, 35, 5, 15, "Severe storm conditions; avoid outdoor sports.");
        }

        // Running: sweet spot 8-16C
        int running = 95;
        if (tempC > 28.0) running -= (int) ((tempC - 28.0) * 5);
        if (tempC < 5.0) running -= (int) ((5.0 - tempC) * 4);
        if (rainPercent > 30) running -= (rainPercent / 2);
        if (windKmh > 25.0) running -= (int) (windKmh - 25.0);

        // Cycling: sensitive to wind and wet surfaces
        int cycling = 90;
        if (tempC > 30.0) cycling -= (int) ((tempC - 30.0) * 4);
        if (tempC < 8.0) cycling -= (int) ((8.0 - tempC) * 5);
        if (rainPercent > 20) cycling -= (int) (rainPercent * 0.7);
        if (windKmh > 20.0) cycling -= (int) ((windKmh - 20.0) * 1.5);

        // Commute: impacted by rain/freezing
        int commute = 95;
        if (tempC <= 0.0) commute -= 25; // risk of icy roads
        if (rainPercent > 40) commute -= (rainPercent / 3);
        if (windKmh > 45.0) commute -= 20;

        // Outdoor Dining: requires mild temps, no rain, light wind
        int dining = 90;
        if (tempC < 16.0) dining -= (int) ((16.0 - tempC) * 6);
        if (tempC > 28.0) dining -= (int) ((tempC - 28.0) * 4);
        if (rainPercent > 10) dining -= (rainPercent * 2);
        if (windKmh > 18.0) dining -= (int) ((windKmh - 18.0) * 2);

        running = Math.max(0, Math.min(100, running));
        cycling = Math.max(0, Math.min(100, cycling));
        commute = Math.max(0, Math.min(100, commute));
        dining = Math.max(0, Math.min(100, dining));
        int overall = (running + cycling + commute + dining) / 4;

        String summary;
        if (overall >= 80) summary = "Excellent atmospheric conditions for all outdoor activities.";
        else if (overall >= 60) summary = "Favorable conditions with minor weather considerations.";
        else if (overall >= 40) summary = "Suboptimal weather; check gear before heading outside.";
        else summary = "Adverse weather; indoor activities strongly recommended.";

        return new ActivityScores(running, cycling, commute, dining, overall, summary);
    }

    public AtmosphericRisk assessAtmosphericRisk(double tempC, double windKmh,
                                                 int rainPercent, @NonNull String condition) {
        String lowerCond = condition.toLowerCase(Locale.ROOT);
        if (lowerCond.contains("thunder") || lowerCond.contains("storm") || windKmh >= 65.0) {
            return new AtmosphericRisk(
                    AtmosphericRisk.Level.CRITICAL,
                    "Severe Weather Warning",
                    "Thunderstorms or gale-force winds detected. Seek shelter and exercise extreme caution."
            );
        } else if (windKmh >= 45.0 || tempC <= -5.0 || tempC >= 38.0) {
            return new AtmosphericRisk(
                    AtmosphericRisk.Level.WARNING,
                    "Weather Warning",
                    "Elevated hazards from strong winds or extreme temperatures."
            );
        } else if (rainPercent >= 50 || windKmh >= 30.0 || tempC <= 2.0) {
            return new AtmosphericRisk(
                    AtmosphericRisk.Level.ADVISORY,
                    "Weather Advisory",
                    "Noticeable rain or cool gusty conditions expected today."
            );
        }
        return new AtmosphericRisk(
                AtmosphericRisk.Level.NORMAL,
                "Conditions Normal",
                "No severe atmospheric hazards present."
        );
    }

    private double parsePercentage(String text, double fallback) {
        if (text == null) return fallback;
        try {
            String clean = text.replaceAll("[^0-9.]", "").trim();
            if (clean.isEmpty()) return fallback;
            double val = Double.parseDouble(clean);
            return Math.max(0.0, Math.min(100.0, val));
        } catch (Exception e) {
            return fallback;
        }
    }

    private double parseWindSpeed(String text, double fallback) {
        if (text == null) return fallback;
        try {
            String clean = text.replaceAll("[^0-9.]", "").trim();
            if (clean.isEmpty()) return fallback;
            double val = Double.parseDouble(clean);
            return Math.max(0.0, Math.min(300.0, val));
        } catch (Exception e) {
            return fallback;
        }
    }
}

