package com.sellandb;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by sellandb on 11/4/14.
 */
public class Page {
    private final String text;          //The text for this document
    private final Boolean finalPage;    //The poison pill indicator, will cause a thread to stop processing

    public Page(String text) {
        this.finalPage = false;
        this.text = text;
    }
    public Page(Boolean isFinal) {
        this.finalPage = isFinal;
        this.text = "";
    }

    //Returns an array containing all of the words in the text split on white space
    public Iterable<String> getText() {
        return Arrays.asList(text.split("\\s+"));
    }

    public Boolean isFinal() {
        return finalPage;
    }
}
