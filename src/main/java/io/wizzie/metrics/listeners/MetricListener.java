package io.wizzie.metrics.listeners;

import java.util.Map;

/**
 * Simple metric listener
 */
public interface MetricListener {

    /**
     * Initialize MetricListener object
     * @param config Configuration for MetricListener
     */
    void init(Map<String, Object> config);

    /**
     * Allow update metric and send it
     * @param metricName The name of  metric
     * @param metricValue The value of metric
     */
    void updateMetric(String metricName, Object metricValue);

    /**
     * Close metric listener
     */
    void close();

    /**
     * Allow to get name of MetricListener object
     * @return Name of MetricListener object
     */
    String name();
}
