package com.sellandb;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by sellandb on 11/4/14.
 */
public class Page {
    private final String text;

    public Page(String text) {
        this.text = text;
    }

    public Iterable<String> getText() {
        return Arrays.asList(text.split("\\s+"));
    }
}
