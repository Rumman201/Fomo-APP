# 31 — Phase 4 Responsive Scaling & Accessibility Engineering

## 1. Responsive Multi-Density Support

- **Window Size Classes**:
  - Compact Handsets (< 600dp width): Single-column layout with horizontal hourly scroll carousel and bottom sheet modal dialogs.
  - Medium & Expanded (Foldables & Tablets >= 600dp): Preserves centered 600dp max content constraint to prevent stretched text lines.
- **Scroll Container Protection**:
  - Entire screen is contained in `ScrollView` with `android:fillViewport="true"`, preventing content clipping even on small displays (320dp height/width) or extreme accessibility font scale multipliers (200% zoom).
- **8dp Spatial Grid**:
  - All margins and paddings reference `@dimen/spacing_*` tokens based on an 8dp baseline.

---

## 2. Accessibility Engineering (WCAG 2.2 AA & TalkBack)

1. **Touch Ergonomics**:
   - `next7DaysButton`: `minHeight="@dimen/min_touch_target"` (48dp).
   - `statusRetryButton`: `minHeight="@dimen/min_touch_target"` (48dp).
   - Hourly cards: 92dp width x 110dp height with full card click area.
   - 7-day outlook rows: `minHeight="@dimen/min_touch_target"` (48dp).
2. **Semantic Content Descriptions**:
   - `conditionIcon`: `@string/cd_weather_condition` ("Weather condition indicator").
   - `rainIcon`: `@string/cd_rain_icon` ("Rain probability indicator").
   - `windIcon`: `@string/cd_wind_icon` ("Wind speed indicator").
   - `humidityIcon`: `@string/cd_humidity_icon` ("Humidity indicator").
   - `hourlyCard`: `@string/cd_hourly_card` with hour, temperature, and condition.
   - Decorative elements (`dragHandle`): `android:importantForAccessibility="no"` / `contentDescription="@null"`.
3. **Heading Semantics**:
   - `conditionTitle`: `android:accessibilityHeading="true"`.
   - `todayTitle`: `android:accessibilityHeading="true"`.
   - `sheetTitle`: `android:accessibilityHeading="true"`.
4. **Color Contrast**:
   - Body & metric text (`#FFFFFF`, `#D1C4E9`) achieves > 6.5:1 contrast against surface cards (`#2A1B54`), comfortably exceeding WCAG AA 4.5:1 requirement.
