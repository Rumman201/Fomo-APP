---
name: accessibility-and-inclusive-ux
description: Android TalkBack screen reader support, content descriptions, heading semantics, high contrast, WCAG 2.2 AA conformance, and touch ergonomics.
---

# Accessibility & Inclusive UX Skill

## Purpose & Scope
This skill provides procedures for verifying and enforcing mobile accessibility on Android, targeting WCAG 2.2 AA conformance and native Android Accessibility Services (TalkBack, Switch Access, Voice Access).

## When to Activate
- Reviewing UI layouts for TalkBack screen reader compatibility.
- Auditing `contentDescription` attributes on `ImageView`, `ImageButton`, and custom views.
- Marking accessibility headings (`accessibilityHeading="true"`) for navigational structure.
- Verifying color contrast ratios across background surfaces and text elements.
- Ensuring touch targets meet the 48dp x 48dp minimum physical size standard.

## Engineering Standards & Invariants
1. **Meaningful Content Descriptions**:
   - Every interactive icon and informative graphic must declare a localized `android:contentDescription` referencing `@string/*`.
   - Purely decorative elements must explicitly declare `android:importantForAccessibility="no"` or `android:contentDescription="@null"`.
2. **Heading Semantics**:
   - Section headers (e.g. "Today", "Weekly Forecast") must declare `android:accessibilityHeading="true"`.
3. **Contrast Ratios**:
   - Body text and primary metrics must achieve at least 4.5:1 contrast against their rendered background. Large text (>=18sp bold or >=24sp regular) must achieve at least 3.0:1.
4. **State Announcements**:
   - State changes (e.g. loading, error messages, refresh completion) must be announced via `announceForAccessibility()` or live regions.

## Step-by-Step Procedure
1. **Accessibility Lint Audit**:
   - Run Android Lint accessibility checks (`ClickableViewAccessibility`, `ContentDescription`).
2. **Semantic Annotation**:
   - Add content descriptions, heading flags, and traversal groupings.
3. **Contrast Verification**:
   - Measure text hex codes against surface gradient values.
