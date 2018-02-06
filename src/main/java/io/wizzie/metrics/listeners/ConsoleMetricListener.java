package io.wizzie.metrics.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static io.wizzie.metrics.MetricsConstant.METRIC_DATABAG;

public class ConsoleMetricListener implements MetricListener {
    private static final Logger log = LoggerFactory.getLogger(ConsoleMetricListener.class);
    ObjectMapper mapper;
    Map<String, Object> metricDataBag;

    @Override
    public void init(Map<String, Object> config) {
        metricDataBag = (Map<String, Object>) config.get(METRIC_DATABAG);
        mapper = new ObjectMapper();
    }

    @Override
    public void updateMetric(String metricName, Object metricValue) {
        Map<String, Object> metric = new HashMap<>();
        metric.put("timestamp", System.currentTimeMillis() / 1000L);
        metric.put("monitor", metricName);
        metric.put("value", metricValue);

        if(metricDataBag != null) metric.putAll(metricDataBag);

        try {
            if (metricValue != null)
                log.info(mapper.writeValueAsString(metric));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        //Nothing to do
    }

    @Override
    public String name() {
        return "console";
    }
}
