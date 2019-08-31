package integrations.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DataGenerator;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MockDataProducer {

    private final static Logger log = LoggerFactory.getLogger(MockDataProducer.class);
    private static final String YELLING_APP_TOPIC = "src-topic";
    private static final int YELLING_APP_ITERATIONS = 10;
    private static Producer<String, String> producer;
    private static Callback callback;
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void produceRandomStrings() {
        Runnable generateTask = () -> {
            init();
            int counter = 0;
            while (counter++ < YELLING_APP_ITERATIONS) {
                List<String> textValues = DataGenerator.generateRandomText();
                for (String value : textValues) {
                    ProducerRecord<String, String> record = new ProducerRecord<>(YELLING_APP_TOPIC, null, value);
                    producer.send(record, callback);
                }
                log.info("Text batch sent");
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            log.info("Done generating text data");
        };
        executorService.submit(generateTask);
    }

    private static void init() {
        if (producer == null) {
            log.info("Initializing the producer");
            Properties properties = new Properties();
            properties.put("bootstrap.servers", "172.16.207.131:9092, 172.16.207.135:9092, 172.16.207.191:9092");
            properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.put("acks", "1");
            properties.put("retries", "3");

            producer = new KafkaProducer<>(properties);

            callback = (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                }
            };
            log.info("Producer initialized");
        }
    }
}
