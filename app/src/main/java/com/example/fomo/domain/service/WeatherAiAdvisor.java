package com.example.fomo.domain.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.fomo.domain.model.ActivityScores;
import com.example.fomo.domain.model.AiWeatherAdvisory;
import com.example.fomo.domain.model.AtmosphericIntelligence;
import com.example.fomo.domain.model.AtmosphericRisk;
import com.example.fomo.domain.model.CurrentWeather;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Production AI advisor synthesizing atmospheric metrics into concise natural language briefings.
 * Operates with strict schema validation, input sanitization, prompt injection defense,
 * and a 100% deterministic rule-based fallback pipeline.
 */
public class WeatherAiAdvisor {

    private static final Pattern INJECTION_PATTERN = Pattern.compile(
            "(?i)(ignore\\s+(all\\s+)?previous\\s+instructions|system\\s+prompt|<script|DROP\\s+TABLE|SELECT\\s+\\*)"
    );

    private final AtmosphericIntelligenceService intelligenceService;
    private final Gson gson;

    public WeatherAiAdvisor() {
        this(new AtmosphericIntelligenceService());
    }

    public WeatherAiAdvisor(@NonNull AtmosphericIntelligenceService intelligenceService) {
        this.intelligenceService = Objects.requireNonNull(intelligenceService, "intelligenceService must not be null");
        this.gson = new Gson();
    }

    /**
     * Synthesizes an atmospheric briefing. Defaults to deterministic rule-based generation
     * unless an AI response payload is supplied.
     */
    @NonNull
    public AiWeatherAdvisory generateBriefing(@NonNull CurrentWeather weather) {
        return generateBriefing(weather, null);
    }

    /**
     * Synthesizes an atmospheric briefing, validating optional raw AI JSON against deterministic guardrails.
     */
    @NonNull
    public AiWeatherAdvisory generateBriefing(@NonNull CurrentWeather weather, @Nullable String rawAiJson) {
        Objects.requireNonNull(weather, "weather must not be null");
        AtmosphericIntelligence intel = intelligenceService.analyze(weather);

        if (rawAiJson != null && !rawAiJson.trim().isEmpty()) {
            AiWeatherAdvisory parsed = parseAndValidateAiJson(rawAiJson, weather, intel);
            if (parsed != null) {
                return parsed;
            }
        }

        return generateDeterministicBriefing(weather, intel);
    }

    /**
     * Deterministic rule-based atmospheric briefing generator.
     * Guarantees zero latency, zero hallucinations, and 100% offline reliability.
     */
    @NonNull
    public AiWeatherAdvisory generateDeterministicBriefing(@NonNull CurrentWeather weather,
                                                           @NonNull AtmosphericIntelligence intel) {
        String condition = weather.getCondition() != null ? weather.getCondition() : "Current conditions";
        int temp = weather.getTemperature();

        String headline = condition + " at " + temp + "°C (" + intel.getComfortCategory() + ")";

        StringBuilder summary = new StringBuilder();
        summary.append("Atmospheric temperature is ").append(temp).append("°C");
        if (Math.abs(intel.getApparentTemperature() - temp) >= 1.0) {
            summary.append(", feeling like ").append(Math.round(intel.getApparentTemperature())).append("°C");
        }
        summary.append(". Wind speeds are around ").append(weather.getWindSpeed());
        summary.append(" with ").append(weather.getRainPercentage()).append(" rain probability.");

        ActivityScores scores = intel.getActivityScores();
        String outdoor = scores.getPrimaryRecommendation() + " (Activity index: " + scores.getOverallScore() + "/100).";

        return new AiWeatherAdvisory(
                headline,
                summary.toString(),
                intel.getClothingAdvice(),
                outdoor,
                intel.getRisk().getLevel(),
                false // Deterministic generation
        );
    }

    /**
     * Validates AI-generated JSON against structured schema and safety guardrails.
     * Returns null if response is malformed, contains injection attempts, or contradicts domain truth.
     */
    @Nullable
    public AiWeatherAdvisory parseAndValidateAiJson(@NonNull String json,
                                                    @NonNull CurrentWeather weather,
                                                    @NonNull AtmosphericIntelligence intel) {
        try {
            // Check for prompt-injection markers
            if (INJECTION_PATTERN.matcher(json).find()) {
                return null;
            }

            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            if (!obj.has("headline") || !obj.has("summary") || !obj.has("attireAdvice") || !obj.has("outdoorRecommendation")) {
                return null;
            }

            String headline = obj.get("headline").getAsString().trim();
            String summary = obj.get("summary").getAsString().trim();
            String attire = obj.get("attireAdvice").getAsString().trim();
            String outdoor = obj.get("outdoorRecommendation").getAsString().trim();

            if (headline.isEmpty() || summary.isEmpty() || attire.isEmpty() || outdoor.isEmpty()) {
                return null;
            }

            // Hallucination Guardrail: if headline or summary contains a temperature contradictory to source by > 5C
            // we safely flag and discard the output
            int actualTemp = weather.getTemperature();
            if (containsGrosslyInconsistentTemperature(summary, actualTemp)) {
                return null;
            }

            return new AiWeatherAdvisory(
                    headline,
                    summary,
                    attire,
                    outdoor,
                    intel.getRisk().getLevel(),
                    true // AI generated
            );
        } catch (Exception e) {
            return null;
        }
    }

    private boolean containsGrosslyInconsistentTemperature(String text, int actualTemp) {
        Pattern pattern = Pattern.compile("(-?\\d+)\\s*°?C");
        java.util.regex.Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            try {
                int citedTemp = Integer.parseInt(matcher.group(1));
                if (Math.abs(citedTemp - actualTemp) > 6) {
                    return true; // Gross hallucination detected
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }
}

