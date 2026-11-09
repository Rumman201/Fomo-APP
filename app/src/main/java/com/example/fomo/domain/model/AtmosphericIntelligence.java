package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * Composite domain model aggregating edge intelligence, thermal comfort,
 * attire recommendations, activity suitability, and risk analysis.
 */
public final class AtmosphericIntelligence {

    private final double apparentTemperature;
    private final String comfortCategory;
    private final String clothingAdvice;
    private final ActivityScores activityScores;
    private final AtmosphericRisk risk;

    public AtmosphericIntelligence(double apparentTemperature,
                                   @NonNull String comfortCategory,
                                   @NonNull String clothingAdvice,
                                   @NonNull ActivityScores activityScores,
                                   @NonNull AtmosphericRisk risk) {
        this.apparentTemperature = apparentTemperature;
        this.comfortCategory = Objects.requireNonNull(comfortCategory, "comfortCategory must not be null");
        this.clothingAdvice = Objects.requireNonNull(clothingAdvice, "clothingAdvice must not be null");
        this.activityScores = Objects.requireNonNull(activityScores, "activityScores must not be null");
        this.risk = Objects.requireNonNull(risk, "risk must not be null");
    }

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    @NonNull
    public String getComfortCategory() {
        return comfortCategory;
    }

    @NonNull
    public String getClothingAdvice() {
        return clothingAdvice;
    }

    @NonNull
    public ActivityScores getActivityScores() {
        return activityScores;
    }

    @NonNull
    public AtmosphericRisk getRisk() {
        return risk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtmosphericIntelligence)) return false;
        AtmosphericIntelligence that = (AtmosphericIntelligence) o;
        return Double.compare(that.apparentTemperature, apparentTemperature) == 0 &&
                comfortCategory.equals(that.comfortCategory) &&
                clothingAdvice.equals(that.clothingAdvice) &&
                activityScores.equals(that.activityScores) &&
                risk.equals(that.risk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apparentTemperature, comfortCategory, clothingAdvice, activityScores, risk);
    }
}

