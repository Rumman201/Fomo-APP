package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * Immutable domain model representing a structured AI atmospheric briefing.
 * Enforces structured schema compliance and transparency regarding generation origin.
 */
public final class AiWeatherAdvisory {

    private final String headline;
    private final String summary;
    private final String attireAdvice;
    private final String outdoorRecommendation;
    private final AtmosphericRisk.Level riskLevel;
    private final boolean isGeneratedByAi;

    public AiWeatherAdvisory(@NonNull String headline,
                             @NonNull String summary,
                             @NonNull String attireAdvice,
                             @NonNull String outdoorRecommendation,
                             @NonNull AtmosphericRisk.Level riskLevel,
                             boolean isGeneratedByAi) {
        this.headline = Objects.requireNonNull(headline, "headline must not be null");
        this.summary = Objects.requireNonNull(summary, "summary must not be null");
        this.attireAdvice = Objects.requireNonNull(attireAdvice, "attireAdvice must not be null");
        this.outdoorRecommendation = Objects.requireNonNull(outdoorRecommendation, "outdoorRecommendation must not be null");
        this.riskLevel = Objects.requireNonNull(riskLevel, "riskLevel must not be null");
        this.isGeneratedByAi = isGeneratedByAi;
    }

    @NonNull
    public String getHeadline() {
        return headline;
    }

    @NonNull
    public String getSummary() {
        return summary;
    }

    @NonNull
    public String getAttireAdvice() {
        return attireAdvice;
    }

    @NonNull
    public String getOutdoorRecommendation() {
        return outdoorRecommendation;
    }

    @NonNull
    public AtmosphericRisk.Level getRiskLevel() {
        return riskLevel;
    }

    public boolean isGeneratedByAi() {
        return isGeneratedByAi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AiWeatherAdvisory)) return false;
        AiWeatherAdvisory that = (AiWeatherAdvisory) o;
        return isGeneratedByAi == that.isGeneratedByAi &&
                headline.equals(that.headline) &&
                summary.equals(that.summary) &&
                attireAdvice.equals(that.attireAdvice) &&
                outdoorRecommendation.equals(that.outdoorRecommendation) &&
                riskLevel == that.riskLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(headline, summary, attireAdvice, outdoorRecommendation, riskLevel, isGeneratedByAi);
    }
}

