package com.wordranker.ranker;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TopWordsRanker {

    private int k;

    private Map<String, Integer> wordCountMap;

    public TopWordsRanker(int k, Map<String, Integer> wordCountMap) {
        this.k = k;
        this.wordCountMap = wordCountMap;
    }

    public LinkedHashSet<String> getTopWords(){
        return wordCountMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(k)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
