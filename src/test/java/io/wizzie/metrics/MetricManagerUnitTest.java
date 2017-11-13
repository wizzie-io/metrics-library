package io.wizzie.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import io.wizzie.metrics.MetricsManager;
import org.junit.Test;

import java.util.*;

import static io.wizzie.metrics.MetricsConstant.*;
import static org.junit.Assert.*;

public class MetricManagerUnitTest {

    @Test
    public void metricManagerNotRunningIfConfigIsEmpty() {

        Map<String, Object> emptyConfig = new HashMap<>();
        MetricsManager metricsManager = new MetricsManager(emptyConfig);

        assertFalse(metricsManager.running.get());
    }

    @Test
    public void metricManagerShouldLoadConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put(METRIC_ENABLE, true);
        config.put(METRIC_INTERVAL, 2000);
        config.put(APPLICATION_ID, "testing-metric-manager");
        config.put(METRIC_LISTENERS, Collections.singletonList("io.wizzie.metrics.listeners.ConsoleMetricListener"));

        MetricsManager metricsManager = new MetricsManager(config);

        assertTrue((Boolean) metricsManager.config.get("metric.enable"));

        assertEquals(new Long(2000), metricsManager.interval);
        assertEquals("testing-metric-manager", metricsManager.app_id);
    }

    @Test
    public void metricManagerShouldRegisterMetrics() {
        Map<String, Object> config = new HashMap<>();

        config.put(METRIC_ENABLE, true);
        config.put(METRIC_INTERVAL, 2000);
        config.put(APPLICATION_ID, "testing-metric-manager");
        config.put(METRIC_LISTENERS, Collections.singletonList("io.wizzie.metrics.listeners.ConsoleMetricListener"));

        MetricsManager metricsManager = new MetricsManager(config);

        assertEquals(0, metricsManager.registredMetrics.size());

        metricsManager.registerMetric("myCounterMetric", new Counter());
        metricsManager.registerMetric("myTimerMetric", new Timer());

        assertEquals(2, metricsManager.registredMetrics.size());

        Set<String> expectedMetrics = new HashSet<>();
        expectedMetrics.add("myCounterMetric");
        expectedMetrics.add("myTimerMetric");

        assertEquals(expectedMetrics, metricsManager.registredMetrics);
    }

    @Test
    public void metricManagerShouldRemoveMetrics() {
        Map<String, Object> config = new HashMap<>();

        config.put(METRIC_ENABLE, true);
        config.put(METRIC_INTERVAL, 2000);
        config.put(APPLICATION_ID, "testing-metric-manager");
        config.put(METRIC_LISTENERS, Collections.singletonList("io.wizzie.metrics.listeners.ConsoleMetricListener"));

        MetricsManager metricsManager = new MetricsManager(config);

        assertEquals(0, metricsManager.registredMetrics.size());

        metricsManager.registerMetric("myCounterMetric", new Counter());
        metricsManager.registerMetric("myTimerMetric", new Timer());

        assertEquals(2, metricsManager.registredMetrics.size());

        metricsManager.removeMetric("myCounterMetric");

        assertEquals(1, metricsManager.registredMetrics.size());

        Set<String> expectedMetrics = new HashSet<>();
        expectedMetrics.add("myTimerMetric");

        assertEquals(expectedMetrics, metricsManager.registredMetrics);
    }

    @Test
    public void metricManagerShouldCleanMetrics() {
        Map<String, Object> config = new HashMap<>();

        config.put(METRIC_ENABLE, true);
        config.put(METRIC_INTERVAL, 2000);
        config.put(APPLICATION_ID, "testing-metric-manager");
        config.put(METRIC_LISTENERS, Collections.singletonList("io.wizzie.metrics.listeners.ConsoleMetricListener"));

        MetricsManager metricsManager = new MetricsManager(config);

        assertEquals(0, metricsManager.registredMetrics.size());

        metricsManager.registerMetric("myCounterMetric", new Counter());
        metricsManager.registerMetric("myTimerMetric", new Timer());

        assertEquals(2, metricsManager.registredMetrics.size());

        metricsManager.clean();

        assertEquals(0, metricsManager.registredMetrics.size());

        Set<String> expectedMetrics = new HashSet<>();

        assertEquals(expectedMetrics, metricsManager.registredMetrics);
    }


}
