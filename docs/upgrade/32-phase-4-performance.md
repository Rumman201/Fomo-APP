# 32 — Phase 4 Frontend Performance & Rendering Optimization

## 1. Frame Budget & 60fps Scrolling

- **Recycler Optimization**:
  - `binding.view1.setHasFixedSize(true)` ensures size changes in child cards do not trigger layout invalidations on the parent view.
  - `HourlyAdapter` uses `DiffUtil.ItemCallback` to dispatch background diff calculations, preventing main-thread layout thrashing during updates.
- **View Binding Performance**:
  - Null-safe, reflection-free ViewBinding eliminates costly `findViewById` tree traversals during every bind operation.
- **Overdraw Elimination**:
  - Redundant full-screen backgrounds on child LinearLayouts removed. The root window displays `@drawable/background`, and child cards use localized `@color/fomo_surface_card`.

---

## 2. Resource & Memory Footprint

- **Drawable Allocation**:
  - Drawables are statically mapped via `WeatherIconMapper` in O(1) in-memory lookups without runtime reflection.
- **View Hierarchy Depth**:
  - Layout depth strictly capped: Root `SwipeRefreshLayout` -> `ScrollView` -> `LinearLayout` -> `MaterialCardView` -> Content (maximum depth: 5 levels).
- **Binary Footprint**:
  - Standard debug APK size remains under 8 MB despite full Material 3, ViewBinding, and Room inclusion.
