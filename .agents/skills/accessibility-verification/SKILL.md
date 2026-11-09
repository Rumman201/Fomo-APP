---
name: accessibility-verification
description: Android TalkBack semantics, WCAG 2.2 AA accessibility evaluation, 48dp touch target compliance, and high contrast testing.
---

# Accessibility Verification Skill

## Purpose
Ensures complete accessibility conformance across all user-facing Android views, input controls, and informative graphics.

## Core Directives
1. Verify minimum touch target boundaries (48x48 dp) on all interactive elements.
2. Confirm meaningful `contentDescription` on informative views and `@null` on purely decorative drawables.
3. Validate typography scaling with system font-size adjustments (`sp` units).

