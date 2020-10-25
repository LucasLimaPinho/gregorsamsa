package guru.learningjournal.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class HelloProducer {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        logger.info("Creating a Kafka Producer....");

        Properties props = new Properties();

        // Setting up 4 basic configurations for the producer to work.

        // Simple string passed to the Kafka server.
        props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID);

        /*

        BOOTSTRAP SERVER CONFIG - comma separated list of HOST & PORT
        The producer will use this information for establishing the initial connecton
        to the Kafka cluster

        If you are running on a single node Kafka, you can supply an individual host/port
        information. The bootstrap configuration is used only for the initial connection -
        Once connected, the Kafka producer will automatically query for the metadata
        and discover the full list of Kafka brokers in the cluster.

        That means you do not need to supply a complete list of Kafka brokers as a bootstrap
        configuration. However, is recommended to provide 2-3 broker addresses of a multinode
        cluster.

        */

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);

        /*

        A Kafka message must have a key/value structure. That means each message that we want to send
        to the Kafka server should have a key and a value.
        You can have a null key, but the message is still structured as a key/value pair.
        The second concept is about serializer. Kafka messages are sent over the network,
        So, the key and the value should be serialized into bytes before they are streamed over
        the network.

        Kafka Producer API comes with a bunch of ready to use serializer classes.


        */
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create an instance of the KafkaProducer
        // We need to pass the properties that we created earlier to the constructor.

        KafkaProducer<Integer, String> producer = new KafkaProducer<Integer, String>(props);

        // The third step is to start sending messages to the Kafka.
        // So, we will create a loop that executes a million times

        for (int i = 0; i < AppConfigs.numEvents; i++) {

            // The send method takes a ProducerRecord object and sends it to the Kafka cluster.
            // ProducerRecord constructor takes three arguments
            // 1. The first argument is the topic name;
            // 2. The second argument is the message key;
            // 3. The third argument is the message value;
            producer.send(new ProducerRecord<>(AppConfigs.topicName, i, "Simple String Message -" + i));

        }

        logger.info("Finished Sending Messages - Closing Producer");
        producer.close();

    }
}
