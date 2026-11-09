package com.example.fomo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.fomo.domain.model.Hourly;
import com.example.fomo.ui.adapter.HourlyAdapter;

import org.junit.Test;

/**
 * Unit tests verifying DiffUtil callback logic for smooth list animations.
 */
public class HourlyDiffCallbackTest {

    @Test
    public void testAreItemsTheSameMatchesOnHourKey() {
        Hourly item1 = new Hourly("9 PM", 28, "cloudy");
        Hourly item2 = new Hourly("9 PM", 30, "rainy");
        Hourly item3 = new Hourly("10 PM", 28, "cloudy");

        assertTrue(HourlyAdapter.DIFF_CALLBACK.areItemsTheSame(item1, item2));
        assertFalse(HourlyAdapter.DIFF_CALLBACK.areItemsTheSame(item1, item3));
    }

    @Test
    public void testAreContentsTheSameRequiresFullEquality() {
        Hourly item1 = new Hourly("9 PM", 28, "cloudy");
        Hourly item2 = new Hourly("9 PM", 28, "cloudy");
        Hourly itemDifferentTemp = new Hourly("9 PM", 29, "cloudy");

        assertTrue(HourlyAdapter.DIFF_CALLBACK.areContentsTheSame(item1, item2));
        assertFalse(HourlyAdapter.DIFF_CALLBACK.areContentsTheSame(item1, itemDifferentTemp));
    }
}
