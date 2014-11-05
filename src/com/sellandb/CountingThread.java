package com.sellandb;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Created by bselland on 11/5/14.
 */
public class CountingThread extends Thread{
    private final BlockingQueue<Page> storageQueue;     //Store of pages that can now be processed
    private final HashMap<String, Integer> counts;      //Hash map containing the count of words found

    public CountingThread(BlockingQueue<Page> queue, HashMap<String, Integer> counts) {
        this.storageQueue = queue;
        this.counts = counts;
    }

    public void run(){
        Page p;
        //pull pages from the page queue
        //A poison pill page with isFinal true will be passed once all pages
        //have been added to the queue
        while(!(p = storageQueue.remove()).isFinal()) {

            //Get an iterable list of the words in that text
            Iterable<String> words = p.getText();
            for (String word : words) {
                //Count each word
                countWord(word);
            }
        }
    }
    //Adds a word to the hash map
    private void countWord(String word) {
        Integer currentCount = counts.get(word);
        if (currentCount == null){
            counts.put(word, 1);
        } else {
            counts.put(word, currentCount + 1);
        }

    }
}
