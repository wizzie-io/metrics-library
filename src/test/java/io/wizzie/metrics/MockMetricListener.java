package io.wizzie.metrics;

import io.wizzie.metrics.listeners.MetricListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.wizzie.metrics.MetricsConstant.METRIC_DATABAG;

public class MockMetricListener implements MetricListener {

    List<Map<String, Object>> content = new ArrayList<>();
    Map<String, Object> metricDataBag;

    @Override
    public void init(Map<String, Object> config) {
        metricDataBag = (Map<String, Object>) config.get(METRIC_DATABAG);
    }

    @Override
    public void updateMetric(String metricName, Object metricValue) {
        Map<String, Object> metric = new HashMap<>();
        metric.put("timestamp", System.currentTimeMillis() / 1000L);
        metric.put("monitor", metricName);
        metric.put("value", metricValue);

        if(metricDataBag != null) metric.putAll(metricDataBag);

        if (metricValue != null)
            content.add(metric);
    }

    @Override
    public void close() {
        //Nothing to do
    }

    @Override
    public String name() {
        return "mock";
    }
}
