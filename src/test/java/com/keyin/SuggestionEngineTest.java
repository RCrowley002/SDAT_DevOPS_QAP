package com.keyin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class SuggestionEngineTest {
    private SuggestionEngine suggestionEngine = new SuggestionEngine();

    @Test
    public void testDeletes(){
        String word = "test";
        Stream<String> deletes = suggestionEngine.wordEdits(word).filter(edit -> edit.length() == word.length() - 1);
        List<String> deleteList = deletes.collect(Collectors.toList());
        Assertions.assertTrue(deleteList.contains("tst"));
        Assertions.assertTrue(deleteList.contains("tes"));
    }

    @Test
    public void testInserts(){
        String word = "test";
        Stream<String> inserts = suggestionEngine.wordEdits(word).filter(edit -> edit.length() == word.length() + 1);
        List<String> insertList = inserts.collect(Collectors.toList());
        Assertions.assertTrue(insertList.contains("tests"));
        Assertions.assertTrue(insertList.contains("atest"));
    }

    @Test
    public void testForEmptyWord(){
        suggestionEngine.setWordSuggestionDB(createMockData());
        String suggestion = suggestionEngine.generateSuggestions("");
        Assertions.assertEquals("", suggestion);
    }

    @Test
    public void testForCorrectWord(){
        String suggestion = suggestionEngine.generateSuggestions("test");
        Assertions.assertEquals("", suggestion);
    }

    @Test
    public void testForMisspelledWord(){
        suggestionEngine.setWordSuggestionDB(createMockData());
        String suggestion = suggestionEngine.generateSuggestions("tets");
        Assertions.assertEquals("test", suggestion);
    }

    @Test
    public void testPerformance(){
        suggestionEngine.setWordSuggestionDB(createMockData());
        long startTime = System.currentTimeMillis();
        suggestionEngine.generateSuggestions("wrongWord");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        Assertions.assertTrue(executeTime < 1000, "Test failed. Time exceeded.");
    }



    // creating mock date for SuggestionDatabase
    private SuggestionsDatabase createMockData(){
        SuggestionsDatabase database = new SuggestionsDatabase();
        Map<String, Integer> wordMap = new HashMap<>();
        wordMap.put("test", 1);
        database.setWordMap(wordMap);
        return database;
    }
}
