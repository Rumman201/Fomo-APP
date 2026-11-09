package com.example.fomo.security.sanitizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.regex.Pattern;

/**
 * Enterprise defensive input validation and sanitization utility.
 * Neutralizes SQL injection fragments, Cross-Site Scripting (XSS) tags and embedded scripts,
 * control characters, prompt injection vectors, and malformed telemetry bounds.
 */
public final class InputSanitizer {

    private static final int MAX_SEARCH_QUERY_LENGTH = 100;
    private static final int MAX_PROMPT_INPUT_LENGTH = 500;

    private static final Pattern SCRIPT_TAG_PATTERN = Pattern.compile("<script[^>]*>[\\s\\S]*?</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern STYLE_TAG_PATTERN = Pattern.compile("<style[^>]*>[\\s\\S]*?</style>", Pattern.CASE_INSENSITIVE);
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SQL_METACHAR_PATTERN = Pattern.compile("([';]|--|/\\*|\\*/)", Pattern.CASE_INSENSITIVE);
    private static final Pattern CONTROL_CHARS_PATTERN = Pattern.compile("[\\p{Cntrl}&&[^\r\n\t]]");
    private static final Pattern MULTI_WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final Pattern STATION_ID_PATTERN = Pattern.compile("^[A-Za-z0-9_-]{3,32}$");

    // Prompt injection heuristic patterns
    private static final Pattern PROMPT_INJECTION_PATTERN = Pattern.compile(
            "(?i)(ignore\\s+(all\\s+)?previous\\s+instructions|system\\s+prompt|disregard\\s+instructions|you\\s+are\\s+now\\s+a)"
    );

    private InputSanitizer() {
        // Prevent instantiation of utility class
    }

    /**
     * Sanitizes city search strings by stripping scripts, HTML, SQL meta-characters, control characters,
     * compressing whitespace, and bounding length to 100 chars.
     */
    @NonNull
    public static String sanitizeSearchQuery(@Nullable String input) {
        if (input == null) {
            return "";
        }

        String cleaned = CONTROL_CHARS_PATTERN.matcher(input).replaceAll("");
        cleaned = SCRIPT_TAG_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = STYLE_TAG_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = HTML_TAG_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = SQL_METACHAR_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = MULTI_WHITESPACE_PATTERN.matcher(cleaned).replaceAll(" ").trim();

        if (cleaned.length() > MAX_SEARCH_QUERY_LENGTH) {
            cleaned = cleaned.substring(0, MAX_SEARCH_QUERY_LENGTH).trim();
        }

        return cleaned;
    }

    /**
     * Validates that geographical coordinates lie strictly within valid Earth bounds.
     */
    public static boolean isValidCoordinate(double latitude, double longitude) {
        if (Double.isNaN(latitude) || Double.isInfinite(latitude) ||
            Double.isNaN(longitude) || Double.isInfinite(longitude)) {
            return false;
        }
        return latitude >= -90.0 && latitude <= 90.0 && longitude >= -180.0 && longitude <= 180.0;
    }

    /**
     * Enforces strict alphanumeric format for PWS (Personal Weather Station) identifiers.
     */
    public static boolean isValidStationId(@Nullable String stationId) {
        if (stationId == null) {
            return false;
        }
        return STATION_ID_PATTERN.matcher(stationId.trim()).matches();
    }

    /**
     * Defensively filters user and external prompt inputs before forwarding to AI logic.
     * Defends against common prompt injection directives and bounds payload size.
     */
    @NonNull
    public static String sanitizePromptInput(@Nullable String prompt) {
        if (prompt == null) {
            return "";
        }

        String cleaned = CONTROL_CHARS_PATTERN.matcher(prompt).replaceAll("");
        cleaned = SCRIPT_TAG_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = STYLE_TAG_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = HTML_TAG_PATTERN.matcher(cleaned).replaceAll("");
        cleaned = PROMPT_INJECTION_PATTERN.matcher(cleaned).replaceAll("[FILTERED]");
        cleaned = MULTI_WHITESPACE_PATTERN.matcher(cleaned).replaceAll(" ").trim();

        if (cleaned.length() > MAX_PROMPT_INPUT_LENGTH) {
            cleaned = cleaned.substring(0, MAX_PROMPT_INPUT_LENGTH).trim();
        }

        return cleaned;
    }
}
