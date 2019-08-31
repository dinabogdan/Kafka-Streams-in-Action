package util;

import com.github.javafaker.ChuckNorris;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    public static final int NUMBER_UNIQUE_CUSTOMERS = 100;
    public static final int NUMBER_UNIQUE_STORES = 15;
    public static final int DEFAULT_NUM_PURCHASES = 100;
    public static final int NUMBER_TRADED_COMPANIES = 50;
    public static final int NUM_ITERATIONS = 10;
    private static final int NUMBER_TEXT_STATEMENTS = 15;

    public static List<String> generateRandomText() {
        List<String> phrases = new ArrayList<>(NUMBER_TEXT_STATEMENTS);
        Faker faker = new Faker();

        for (int i = 0; i < NUMBER_TEXT_STATEMENTS; i++) {
            ChuckNorris chuckNorris = faker.chuckNorris();
            phrases.add(chuckNorris.fact());
        }
        return phrases;
    }
}
