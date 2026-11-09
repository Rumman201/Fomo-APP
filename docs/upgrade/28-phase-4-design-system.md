# 28 — Phase 4 Design System & Token Architecture

## 1. Design Token Taxonomy

### Color Tokens (`res/values/colors.xml`)
- `fomo_gradient_start`: `#1A103C` (Deep midnight violet canvas)
- `fomo_gradient_center`: `#2E1B54` (Atmospheric purple mid-tone)
- `fomo_gradient_end`: `#3A1C52` (Twilight magenta canvas end)
- `fomo_surface_card`: `#2A1B54` (Primary elevated card surface)
- `fomo_surface_card_selected`: `#3D2975` (Pressed / focused card surface)
- `fomo_surface_card_stroke`: `#4D6E50A6` (Card stroke divider)
- `fomo_surface_bottom_sheet`: `#211442` (Modal bottom sheet background)
- `fomo_accent_gold`: `#FFB300` (Warm amber primary accent)
- `fomo_accent_electric_violet`: `#7C4DFF` (Brand secondary accent)
- `fomo_text_primary`: `#FFFFFF` (100% white, high contrast)
- `fomo_text_secondary`: `#D1C4E9` (Soft lavender mist, > 4.5:1 contrast)
- `fomo_text_tertiary`: `#9575CD` (Muted caption)
- `fomo_error`: `#FF5252` (Alert coral red)

### Spatial Tokens (`res/values/dimens.xml`)
- `spacing_xs`: 4dp (Micro-padding between label and value)
- `spacing_s`: 8dp (Standard padding inside cards)
- `spacing_m`: 16dp (Screen margin and horizontal padding)
- `spacing_l`: 24dp (Section spacing)
- `spacing_xl`: 32dp (Hero spacing)
- `corner_radius_card`: 24dp (Primary card corner curvature)
- `corner_radius_hourly`: 20dp (Hourly carousel card curvature)
- `corner_radius_bottom_sheet`: 28dp (Extended outlook sheet curvature)
- `min_touch_target`: 48dp (WCAG AA accessibility requirement)

---

## 2. Reusable Component Styles (`res/values/themes.xml`)

- `TextAppearance.Fomo.DisplayTemperature`: `64sp` bold white.
- `TextAppearance.Fomo.ConditionTitle`: `22sp` bold white.
- `TextAppearance.Fomo.ObservationDate`: `16sp` lavender.
- `TextAppearance.Fomo.HighLow`: `15sp` bold lavender.
- `TextAppearance.Fomo.MetricValue`: `14sp` bold white.
- `TextAppearance.Fomo.MetricLabel`: `12sp` lavender.
- `TextAppearance.Fomo.SectionHeader`: `18sp` bold amber gold.
- `Theme.Fomo.BottomSheetDialog`: Styled bottom sheet with rounded top corners.
