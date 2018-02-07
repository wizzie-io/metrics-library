# metrics-library

This library allow you to register custom and JVM metrics on java applications. It uses listeners to send this metrics to different backends, by default the metrics-library register the common [JVM metrics](https://github.com/wizzie-io/metrics-library/blob/master/src/main/java/io/wizzie/metrics/MetricsManager.java#L81) (heap, gc count, gc time ...).

## Properties

| Listener class           | Description                                                       | Default        | Type|
| :-------------           | :-------------                                                    | :------------- | :---- |
| `metric.enable`          | Enable or disable the metric library                              | `true`         | boolean |
| `metric.verbose.mode`    | Enable verbose metrics (1min, 5min, 15min ...)                    | `false`        | boolean |
| `metric.listeners`       | The listeners that metric library should be use to export metrics | `["io.wizzie.metrics.listeners.ConsoleMetricListener"]`               | List |
| `metric.interval`        | The interval to configure when the metric are sent (milliseconds) | `60000`        | Long |
| `application.id`         | Identifier to specific what application is exporting the metrics  |                | String |
| `metric.databag`         | Static data to add to all exported metrics.                       |                | Map[String, Object] |

## Listeners

| Listener class                                       | Description                         |
| :-------------                                       | :-------------                      |
| `io.wizzie.metrics.listeners.ConsoleMetricListener`  | Log the metrics ussing log4j-slf4j  |
| `io.wizzie.metrics.listeners.KafkaMetricListener`    | Send metrics to Kafka topic         |

### KafkaMetricListener

#### Properties

| Listener class           | Description                                       | Default        | Type   |
| :-------------           | :-------------                                    | :------------- | :----  |
| `metric.kafka.topic`     | Topic to send metrics                             | `__metrics` | String |
| `bootstrap.servers`      | Kafka brokers (ip111:9092,ip222:9092)             |                | String |

## Work with library

To work with this library you need to add this maven repository, extension and dependency to your pom.xml:

```
<repository>
    <id>wizzie-release</id>
    <name>Wizzie Release Repository</name>
    <url>s3://wizzie-mvn-repo/release</url>
</repository>
```

```
<extension>
    <groupId>org.springframework.build</groupId>
    <artifactId>aws-maven</artifactId>
    <version>5.0.0.RELEASE</version>
</extension>
```

```
<dependency
    <groupId>io.wizzie.metrics</groupId>
    <artifactId>metrics-library</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>
```


Later you can init the `MetricsManager` class and register your metrics:

```java
  Map<String, Object> config = new HashMap<>();

  config.put(METRIC_ENABLE, true);
  config.put(METRIC_INTERVAL, 2000);
  config.put(APPLICATION_ID, "testing-metric-manager");
  config.put(METRIC_LISTENERS, Collections.singletonList("io.wizzie.metrics.listeners.ConsoleMetricListener"));

  MetricsManager metricsManager = new MetricsManager(config);

  metricsManager.registerMetric("myCounterMetric", new Counter());
  metricsManager.registerMetric("myTimerMetric", new Timer());
```
