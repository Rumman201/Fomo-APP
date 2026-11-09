# 27 — Phase 4 Creative Design Direction

## 1. Visual Personality: Atmospheric Twilight Intelligence

The creative vision for Fomo is rooted in calm, premium, reliable meteorological clarity. Rather than using generic flat cards or garish neon accents, Fomo embraces a deep twilight atmospheric palette inspired by evening skies:
- **Primary Canvas**: Midnight violet linear gradient (`#1A103C` to `#3A1C52`).
- **Elevated Surfaces**: Deep indigo card containers (`#2A1B54`) with subtle lavender border strokes (`#4D6E50A6`), providing depth without visual clutter.
- **Solar Accents**: Warm amber/gold (`#FFB300`) highlighting active day headers and interactive controls.
- **Electric Violet Highlights**: Brand accent (`#7C4DFF`) highlighting pull-to-refresh loaders and focus states.

---

## 2. Typography Strategy

Aligned with Material Design 3 typography standards using `sp` scaling:
1. **Hero Temperature**: `64sp` bold display numeral (`TextAppearance.Fomo.DisplayTemperature`) with negative font padding to eliminate vertical dead space.
2. **Atmospheric Condition**: `22sp` bold title (`TextAppearance.Fomo.ConditionTitle`) positioned prominently above the weather icon.
3. **Observation Timestamp & Bounds**: `15–16sp` medium lavender text (`TextAppearance.Fomo.ObservationDate`, `TextAppearance.Fomo.HighLow`).
4. **Metrics & Labels**: `14sp` bold primary values paired with `12sp` secondary descriptive labels.

---

## 3. Rejection of Generic AI Aesthetics

- **No Meaningless Glassmorphism**: Cards use solid elevated backgrounds with crisp border strokes rather than heavy blur filters that degrade rendering performance.
- **No Floating Decorative Shapes**: Every visual asset directly represents meteorological data (condition icon, rain droplet, wind vortex, humidity meter).
- **Purposeful Spatial Composition**: Natural visual scanning flow from Current Temperature -> Daily Bounds -> Three Key Metrics -> Hourly Chronology.
