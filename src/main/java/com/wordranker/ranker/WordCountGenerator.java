package com.wordranker.ranker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordCountGenerator {

    private static final Logger logger = LogManager.getLogger(WordCountGenerator.class);

    //regex to validate that the word is atleast 3 aplhabets and no numerics
    private static final String VALID_WORD_REGEX = "^[a-zA-Z]{3,}$";

    private List<String> pageContents;

    public WordCountGenerator(List<String> pageContents) {
        this.pageContents = pageContents;
    }

    public  Map<String, Integer> fetchWordCountGenerator(){

        Map<String, Integer> wordCountMap = new HashMap<>();

        for(String pageContent : pageContents){

            String[] words = pageContent.split("\\s+");

            for (String word : words) {
                word = word.toLowerCase().trim();
                if(word.matches(VALID_WORD_REGEX)){
                    wordCountMap.merge(word, 1, Integer::sum);
                }

            }
        }

        return wordCountMap;
    }
}
