package com.sellandb;

import java.util.HashMap;

/**
 * Created by sellandb on 11/4/14.
 */
public class WordCount {
    private static final HashMap<String, Integer> counts = new HashMap<String, Integer>();

    //Naive word count implementation results (100000 entries)
    //File processing took: 1ms
    //Page Processing took: 155256ms
    public static void main(String[] args) throws Exception {
        Long start; Long end;   //Setup timing variables

        start = System.nanoTime();  //Start Timing

        //Load up the pages from the XML file
        Iterable<Page> pages = new Pages(1000, "/Users/sellandb/Desktop/enwiki-20140903-pages-meta-current1.xml");

        //End the timing and report
        end = System.nanoTime();
        System.out.println("File processing took: " + (end - start)/1000000 + "ms");

        start = System.nanoTime();  //Start the timing

        //Process the pages and count the words
        for (Page page : pages) {
            Iterable<String> words = page.getText();
            for (String word : words) {
                countWord(word);
            }
        }

        //End the timing and report
        end = System.nanoTime();
        System.out.println("Page Processing took: " + (end - start)/1000000 + "ms");

    }
    //Count a word
    private static void countWord(String word) {
        Integer currentCount = counts.get(word);
        if (currentCount == null){
            counts.put(word, 1);
        } else {
            counts.put(word, currentCount + 1);
        }

    }
}