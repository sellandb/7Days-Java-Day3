package com.sellandb;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by bselland on 11/5/14.
 */
public class ConcurrentWordCount {

    //Naive word count implementation results (100000 entries)
    //File processing took: 1ms
    //Page Processing took: 155256ms
    public static void main(String[] args) throws Exception {
        Long start; Long end;   //Setup timing variables

        //Setup the storage queue where all pages will be queued for processing
        BlockingQueue<Page> storageQueue = new LinkedBlockingQueue<Page>();

        //Hashmap where the various final counts are stored
        HashMap<Integer ,HashMap<String, Integer>> counts = new HashMap<Integer ,HashMap<String, Integer>>();

        start = System.nanoTime();  //Start Timing

        //Start the thread that will be adding the pages to the queue for processing
        PagesThread pt = new PagesThread(100000, "/Users/sellandb/Desktop/enwiki-20140903-pages-meta-current1.xml", storageQueue);
        pt.start();

        //Setup the thread pool
        //We are using twice the total processors, minus one for the page processing thread
        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2 - 1;
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        //Create wordcount processing threads
        for (int i = 0; i < threadPoolSize; i++) {
            counts.put(i, new HashMap<String, Integer>());
            executor.execute(new CountingThread(storageQueue, counts.get(i)));
        }

        //When the XML processing thread finishes running add in poison pill pages to trigger the completion
        //of the page processing
        pt.join();
        for (int i = 0; i < threadPoolSize; i++) {
            storageQueue.add(new Page(true));
        }

        //Tell the threadpool to shutdown when it finishes everything it had to do
        executor.shutdown();

        //Wait until the threadpool has shutdown
        while(!executor.isShutdown()){
            System.out.println("Waiting for processing to complete");
            Thread.sleep(2500);
        }

        //Conclude timing and report
        end = System.nanoTime();
        System.out.println("Processing took: " + (end - start)/1000000 + "ms");

        //Output final unmerged counts
        for (int i = 0; i < threadPoolSize; i++) {
            System.out.println(counts.get(i));
        }

    }
}
