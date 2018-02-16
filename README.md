# metrics-library

This library allows you to register custom and JVM metrics on java applications. It uses listeners to send this metrics to different backends, by default the metrics-library register the common [JVM metrics](https://github.com/wizzie-io/metrics-library/blob/master/src/main/java/io/wizzie/metrics/MetricsManager.java#L81) (heap, gc count, gc time ...).

## Properties

| Listener class           | Description                                                       | Default        | Type|
| :-------------           | :-------------                                                    | :------------- | :---- |
| `metric.enable`          | Enable or disable the metric library                              | `true`         | boolean |
| `metric.verbose.mode`    | Enable verbose metrics (1min, 5min, 15min ...)                    | `false`        | boolean |
| `metric.listeners`       | The listeners that metric library should be use to export metrics | `["io.wizzie.metrics.listeners.ConsoleMetricListener"]`               | List |
| `metric.interval`        | The interval to configure when the metric are sent (milliseconds) | `60000`        | Long |
| `application.id`         | Identifier to specific what application is exporting the metrics  | Depends of the listener                | String |
| `metric.databag`         | Static data to add to all exported metrics.                       |                | Map[String, Object] |

## Listeners
The listeners are the process that listen the reported metrics and do something with them. You can have multiple listeners at the same time.
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
| `application.id`         | Identifier to specific what application is exporting the metrics | Thread.currentThread().getName()| String |

## Work with library

To work with this library you need to add this maven repository, extension and dependency to your pom.xml:

```xml
<repository>
    <id>wizzie-release</id>
    <name>Wizzie Release Repository</name>
    <url>s3://wizzie-mvn-repo/release</url>
</repository>
```

```xml
<extension>
    <groupId>org.springframework.build</groupId>
    <artifactId>aws-maven</artifactId>
    <version>5.0.0.RELEASE</version>
</extension>
```

```xml
<dependency>
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
  
  Counter myCounter = new Counter();
  Timer myTimer = new Timer();
  
  metricsManager.registerMetric("myCounterMetric", myCounter);
  metricsManager.registerMetric("myTimerMetric", myTimer);
  
  metricsManager.start();
  
  // Now you can use the metric instances.

  myCounter.inc();
  myCounter.dec();
  myCounter.inc(10);
  
  myTimer.update(1, TimeUnit.SECONDS);
```

### Custom Listeners
You can made new listeners to do this you need to implement the [MetricListener Class](https://github.com/wizzie-io/metrics-library/blob/master/src/main/java/io/wizzie/metrics/listeners/MetricListener.java). On this class you receive the metric on the method `void updateMetric(String metricName, Object metricValue);`

