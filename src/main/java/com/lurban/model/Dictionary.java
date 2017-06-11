package com.lurban.model;

import java.util.Set;

/**
 * Created by Łukasz on 2017-04-08.
 */

/**
 * Class for all dictionaries stored in memory.
 */
public class Dictionary {

    private Set<String> dictionary;

    public Set<String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Set<String> dictionary) {
        this.dictionary = dictionary;
    }
}
