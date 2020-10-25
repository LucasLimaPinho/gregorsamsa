package guru.learningjournal.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DispatcherDemo {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        Properties props = new Properties();
        try {

            InputStream inputStream = new FileInputStream(AppConfigs.kafkaConfigFileLocation);
            props.load(inputStream);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create an instance of the producer providing the Producer Configuration with the
        // object props from the class Properties.class

        KafkaProducer<Integer, String> producer = new KafkaProducer<Integer, String>(props);
        // Ready to create dispatcher threads. Each element of the Thread[] array will hold
        // the handle for the Thread so I can join them to wait for their completion.
        // The number of threads here must be equal to the number of files;

        Thread[] dispatchers = new Thread[AppConfigs.eventFiles.length];
        logger.info("Starting dispatcher threads...");
        for (int i = 0; i < AppConfigs.eventFiles.length; i++) {

            // Inside the loop: creating a new Thread using the Dispatcher constructor.
            // All the Threads using the same producer object;

            dispatchers[i] = new Thread(new Dispatcher(producer,
                    AppConfigs.topicName,
                    AppConfigs.eventFiles[i]));
            // Start the thread.
            dispatchers[i].start();
        }

        try {

            // Loop through the Thread handles and join them together.
            // This join will allow the MAIN THREAD to wait for all the threads to complete
            for (Thread t : dispatchers) t.join();
        } catch(InterruptedException e) {
            logger.error("Main Thread interrupted");

        } finally {
            producer.close();
            logger.info("Finished dispatcher demo");
        }

    }

}
