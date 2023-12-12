package com.wordranker.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class WordBankLoader {

    private static final Logger logger = LogManager.getLogger(WordBankLoader.class);

    //regex to validate that the word is atleast 3 aplhabets and no numerics
    private static final String VALID_WORD_REGEX = "^[a-zA-Z]{3,}$";

    public Set<String> load (String dataPath) {

        Set<String> wordBank = new HashSet<>();

        try (InputStream in = this.getClass().getResourceAsStream(dataPath);
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(in))) {
            String word = null;
            while ((word = reader.readLine()) != null) {

                word = word.trim();
                if(word.matches(VALID_WORD_REGEX)){
                    wordBank.add(word);
                }
            }
        } catch (IOException ioException) {
            logger.error("Exception while reading/writing the file, {}", ioException.getMessage());
        } catch (Exception e) {
            logger.error("Exception while fetching urls from text file, {}", e.getMessage());
        }
        return wordBank;
    }


}
