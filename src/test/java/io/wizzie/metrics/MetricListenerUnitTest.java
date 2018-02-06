package io.wizzie.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wizzie.metrics.listeners.MetricListener;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.wizzie.metrics.MetricsConstant.*;
import static org.junit.Assert.*;

public class MetricListenerUnitTest {

    private final Map<String, Object> config = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void initTest() {
        config.put(METRIC_ENABLE, true);
        config.put(METRIC_INTERVAL, 2000);
        config.put(APPLICATION_ID, "testing-metric-manager");
        config.put(METRIC_LISTENERS, Collections.singletonList("io.wizzie.metrics.MockMetricListener"));
    }

    @Test
    public void metricManagerShouldHaveAMetricListener() {
        MetricsManager metricsManager = new MetricsManager(config);

        assertFalse(metricsManager.listeners.isEmpty());

        assertTrue(metricsManager.listeners.get(0) instanceof MockMetricListener);
    }

    @Test
    public void metricManagerShouldSendMetricsToMetricListener() throws InterruptedException, JsonProcessingException {
        MetricsManager metricsManager = new MetricsManager(config);

        assertEquals(1, metricsManager.listeners.size());

        MetricListener metricListener = metricsManager.listeners.get(0);

        assertTrue(metricListener instanceof MockMetricListener);

        MockMetricListener mockMetricListener = (MockMetricListener) metricListener;

        List<Map<String, Object>> expectedList = new ArrayList<>();

        Counter counter = new Counter();
        counter.inc(5);
        mockMetricListener.updateMetric("counter-test", counter.getCount());

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("monitor", "counter-test");
        expectedResponse.put("value", counter.getCount());
        expectedResponse.put("timestamp",  System.currentTimeMillis() / 1000L);

        expectedList.add(expectedResponse);

        Meter meter = new Meter();
        meter.mark();
        meter.mark();
        meter.mark();

        mockMetricListener.updateMetric("meter-test", meter.getCount());

        expectedResponse = new HashMap<>();
        expectedResponse.put("monitor", "meter-test");
        expectedResponse.put("value", meter.getCount());
        expectedResponse.put("timestamp",  System.currentTimeMillis() / 1000L);

        expectedList.add(expectedResponse);

        assertEquals(expectedList, mockMetricListener.content);
    }

    @Test
    public void testDataBagConfig(){
        Map<String, Object> databag = new HashMap<>();
        databag.put("test1", "1");
        databag.put("test2", 2);

        Map<String, Object> newConfig = new HashMap<>(config);
        newConfig.put(METRIC_DATABAG, databag);

        MetricsManager metricsManager = new MetricsManager(newConfig);

        assertEquals(1, metricsManager.listeners.size());

        MetricListener metricListener = metricsManager.listeners.get(0);

        assertTrue(metricListener instanceof MockMetricListener);

        MockMetricListener mockMetricListener = (MockMetricListener) metricListener;

        List<Map<String, Object>> expectedList = new ArrayList<>();

        Counter counter = new Counter();
        counter.inc(5);
        mockMetricListener.updateMetric("counter-test", counter.getCount());

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("monitor", "counter-test");
        expectedResponse.put("value", counter.getCount());
        expectedResponse.put("timestamp",  System.currentTimeMillis() / 1000L);
        expectedResponse.put("test1", "1");
        expectedResponse.put("test2", 2);

        expectedList.add(expectedResponse);

        Meter meter = new Meter();
        meter.mark();
        meter.mark();
        meter.mark();

        mockMetricListener.updateMetric("meter-test", meter.getCount());

        expectedResponse = new HashMap<>();
        expectedResponse.put("monitor", "meter-test");
        expectedResponse.put("value", meter.getCount());
        expectedResponse.put("timestamp",  System.currentTimeMillis() / 1000L);
        expectedResponse.put("test1", "1");
        expectedResponse.put("test2", 2);

        expectedList.add(expectedResponse);

        assertEquals(expectedList, mockMetricListener.content);
    }
}
