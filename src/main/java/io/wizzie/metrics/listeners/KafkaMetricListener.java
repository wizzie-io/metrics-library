package io.wizzie.metrics.listeners;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Map;

import static io.wizzie.metrics.MetricsConstant.METRIC_DATABAG;

public class KafkaMetricListener implements MetricListener {
    public static final String METRIC_KAFKA_TOPIC = "metric.kafka.topic";
    KafkaProducer<String, Map<String, Object>> kafkaProducer;
    Map<String, Object> metricDataBag;
    String topic;
    String appId;

    @Override
    public void init(Map<String, Object> config) {
        appId = (String) config.getOrDefault("application.id", Thread.currentThread().getName());
        topic = (String) config.getOrDefault(METRIC_KAFKA_TOPIC, "__metrics");
        metricDataBag = (Map<String, Object>) config.get(METRIC_DATABAG);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "io.wizzie.metrics.listeners.JsonSerializer");
        kafkaProducer = new KafkaProducer<>(config);
    }

    @Override
    public void updateMetric(String metricName, Object metricValue) {
        Map<String, Object> metric = new HashMap<>();
        metric.put("timestamp", System.currentTimeMillis() / 1000L);
        metric.put("monitor", metricName);
        metric.put("value", metricValue);
        metric.put("app_id", appId);

        if(metricDataBag != null) metric.putAll(metricDataBag);

        if (metricValue != null)
            kafkaProducer.send(new ProducerRecord<>(topic, appId, metric));
    }

    @Override
    public void close() {
        kafkaProducer.flush();
        kafkaProducer.close();
    }

    @Override
    public String name() {
        return "kafka";
    }
}
