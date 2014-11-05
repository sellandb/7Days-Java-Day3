package com.sellandb;

public class Main {

    public static void main(String[] args) {
	    Iterable<Page> pages = new Pages(5, "/Users/sellandb/Desktop/enwiki-20140903-pages-meta-current1.xml");
        for(Page page: pages) {
            Iterable<String> words =  page.getText();
            System.out.println(page.getText());
        }
    }
}
