package com.example.fomo.domain.model;

import androidx.annotation.NonNull;
import java.util.Objects;

/**
 * Immutable domain model representing multi-factor outdoor activity suitability scores (0–100).
 */
public final class ActivityScores {

    private final int runningScore;
    private final int cyclingScore;
    private final int commuteScore;
    private final int outdoorDiningScore;
    private final int overallScore;
    private final String summary;

    public ActivityScores(int runningScore, int cyclingScore, int commuteScore,
                          int outdoorDiningScore, int overallScore, @NonNull String summary) {
        this.runningScore = Math.max(0, Math.min(100, runningScore));
        this.cyclingScore = Math.max(0, Math.min(100, cyclingScore));
        this.commuteScore = Math.max(0, Math.min(100, commuteScore));
        this.outdoorDiningScore = Math.max(0, Math.min(100, outdoorDiningScore));
        this.overallScore = Math.max(0, Math.min(100, overallScore));
        this.summary = Objects.requireNonNull(summary, "summary must not be null");
    }

    public int getRunningScore() {
        return runningScore;
    }

    public int getCyclingScore() {
        return cyclingScore;
    }

    public int getCommuteScore() {
        return commuteScore;
    }

    public int getOutdoorDiningScore() {
        return outdoorDiningScore;
    }

    public int getOverallScore() {
        return overallScore;
    }

    @NonNull
    public String getSummary() {
        return summary;
    }

    @NonNull
    public String getPrimaryRecommendation() {
        int max = Math.max(Math.max(runningScore, cyclingScore), Math.max(commuteScore, outdoorDiningScore));
        if (max < 40) {
            return "Indoor activities favored";
        }
        if (max == runningScore) return "Great conditions for running";
        if (max == cyclingScore) return "Great conditions for cycling";
        if (max == outdoorDiningScore) return "Ideal for outdoor dining";
        return "Smooth commute expected";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityScores)) return false;
        ActivityScores that = (ActivityScores) o;
        return runningScore == that.runningScore &&
                cyclingScore == that.cyclingScore &&
                commuteScore == that.commuteScore &&
                outdoorDiningScore == that.outdoorDiningScore &&
                overallScore == that.overallScore &&
                summary.equals(that.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runningScore, cyclingScore, commuteScore, outdoorDiningScore, overallScore, summary);
    }
}

