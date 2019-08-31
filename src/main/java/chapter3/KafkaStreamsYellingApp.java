package chapter3;

import integrations.producer.MockDataProducer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.internals.DefaultKafkaClientSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaStreamsYellingApp {

    private static final Logger log = LoggerFactory.getLogger(KafkaStreamsYellingApp.class);

    public static void main(String[] args) throws InterruptedException {

        MockDataProducer.produceRandomStrings();

        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "yelling_app_id");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.207.131:9092, 172.16.207.135:9092, 172.16.207.191:9092");

        StreamsConfig streamsConfig = new StreamsConfig(properties);

        Serde<String> stringSerde = Serdes.String();

        StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, String> simpleFirstStream = streamsBuilder.stream("src-topic", Consumed.with(stringSerde, stringSerde));

        KStream<String, String> upperCasedStream = simpleFirstStream.mapValues((ValueMapper<String, String>) String::toUpperCase);

        upperCasedStream.to("out-topic", Produced.with(stringSerde, stringSerde));

        upperCasedStream.print(Printed.<String, String>toSysOut().withLabel("Yelling App"));

        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), streamsConfig, new DefaultKafkaClientSupplier());

        System.out.println("Hello world Yelling App Kafka Streams");
        kafkaStreams.start();
        Thread.sleep(35000);
        System.out.println("Shut down the Yelling App Kafka Streams");
        kafkaStreams.close();
        ;

    }
}
