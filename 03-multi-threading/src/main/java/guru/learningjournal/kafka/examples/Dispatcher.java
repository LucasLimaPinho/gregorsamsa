package guru.learningjournal.kafka.examples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Scanner;

public class Dispatcher implements Runnable {

    private static final Logger logger = LogManager.getLogger();
    private String fileLocation;
    private String topicName;

    // In order to send the data to a KafkaBroker we need a KafkaProducer.

    private KafkaProducer<Integer, String> producer;

    // All theses things will be given to the Dispatcher by the main application thread
    // So, let's take a constructor to take these values:

    Dispatcher(KafkaProducer<Integer, String> producer, String topicName, String fileLocation) {

        this.producer = producer;
        this.topicName = topicName;
        this.fileLocation = fileLocation;


    }


    // The Runnable Interface allow us to execute an instance of this class as a separate Thread.
    @Override
    public void run() {

        logger.info("Starting multi-thread Kafka Producer.");
        logger.info("Started processing " + fileLocation);
        File file = new File(fileLocation);
        int counter = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // producer.send() always receive a ProducerRecord with topicName, key, value
                producer.send(new ProducerRecord<>(topicName, null, line));
                counter++;
            }
            logger.info("Finished processing " + counter + " message from " + fileLocation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
