package guru.learningjournal.kafka.examples;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class HelloStreams {
    private static final Logger logger = LogManager.getLogger(HelloProducer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        // Every Kafka Stream application must be identified with a unique id
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfigs.applicationID);

        // A bootstrap_server configuration is a comma separated list of host/port pairs that streams application would use to establish the initial
        // connection with Kafka Cluster

        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<Integer, String> kStream = streamsBuilder.stream(AppConfigs.topicName);
        kStream.foreach((k, v) -> System.out.println("Key= " + k + " Value= " + v));
        //kStream.peek((k,v)-> System.out.println("Key= " + k + " Value= " + v));

        Topology topology = streamsBuilder.build();
        KafkaStreams streams = new KafkaStreams(topology, props);
        logger.info("Starting stream.");
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down stream");
            streams.close();
        }));
    }
}
