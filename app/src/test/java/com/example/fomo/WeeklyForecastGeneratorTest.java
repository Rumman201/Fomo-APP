package com.example.fomo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.fomo.domain.model.CurrentWeather;
import com.example.fomo.domain.model.DailyForecast;
import com.example.fomo.domain.model.Hourly;
import com.example.fomo.domain.model.WeatherForecast;
import com.example.fomo.ui.dialog.WeeklyForecastBottomSheetDialog;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class WeeklyForecastGeneratorTest {

    @Test
    public void testGenerateDailyProjectionsCount() {
        CurrentWeather current = new CurrentWeather("Sunny", 25, "Today | 12:00 PM",
                "H:28  L:18", "0%", "10 Km/h", "50%", "sunny");
        WeatherForecast forecast = new WeatherForecast(current, Collections.singletonList(new Hourly("12 pm", 25, "sunny")));

        List<DailyForecast> dailyList = WeeklyForecastBottomSheetDialog.generateDailyProjections(forecast);

        assertNotNull(dailyList);
        assertEquals(7, dailyList.size());
        assertEquals("Today", dailyList.get(0).getDayName());
        assertEquals("Tomorrow", dailyList.get(1).getDayName());

        for (DailyForecast day : dailyList) {
            assertNotNull(day.getDayName());
            assertNotNull(day.getCondition());
            assertNotNull(day.getIconKey());
            assertTrue(day.getHighTemp() >= day.getLowTemp());
        }
    }

    @Test
    public void testGenerateDailyProjectionsWithNullForecastFallback() {
        List<DailyForecast> dailyList = WeeklyForecastBottomSheetDialog.generateDailyProjections(null);

        assertNotNull(dailyList);
        assertEquals(7, dailyList.size());
        assertEquals("Today", dailyList.get(0).getDayName());
    }
}
