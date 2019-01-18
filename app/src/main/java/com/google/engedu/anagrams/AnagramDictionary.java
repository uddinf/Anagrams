/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private List<String> wordList = new ArrayList<>();
    private static final String TAG = "AnagramDictionary";
    private Set<String> wordSet = new HashSet<>();
    private Map<String, List<String>> lettersToWord = new HashMap<>();
    private Map<Integer, List<String>> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);

            // add to hash map - sorted word to its possible dictionary words
            String sortedWord = sortLetters(word);
            if(!lettersToWord.containsKey(sortedWord)) {

                // put it in the map
                lettersToWord.put(sortedWord, new ArrayList<String>());

            }

            // update the list right here in the map
            lettersToWord.get(sortedWord).add(word);


            // add to hashmap based on size of the word
            int length = word.length();
            if (!sizeToWords.containsKey(length)) {

                // put it in the map
                sizeToWords.put(length, new ArrayList<String>());
            }
            // update the list right here in the map
            sizeToWords.get(length).add(word);

        }

    }

    public boolean isGoodWord(String word, String base) {

        return wordSet.contains(word) && !word.toLowerCase().contains(base);

        // the provided word is a valid dictionary word (i.e., in wordSet)
//        if (!wordSet.contains(word)) {
//            return false;
//        }
//        // the word does not contain the base word as a substring
//        if (word.contains(base)) {
//            return false;
//        }
//
//        return true;
    }

    public String sortLetters (String word) {

        char[] wordArray = word.toCharArray();
        Arrays.sort(wordArray);
        return new String(wordArray);

    }

    public List<String> getAnagrams(String targetWord) {

        ArrayList<String> result = new ArrayList<String>();

        String targetWordSorted = sortLetters(targetWord);

        for (String word : wordList) {
            String wordSorted = sortLetters(word);
            if (wordSorted.equals(targetWordSorted)) {
                result.add(word);
            }
        }

        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {

        ArrayList<String> result = new ArrayList<String>();

        //for loop from a to z
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {

            // add a letter to the original word
            String temp = word + alphabet;

            // sort this new word
           temp = sortLetters(temp);

            if(lettersToWord.containsKey(temp)) {

                // get the list of values and add it to the result list
                ArrayList<String> tempList = new ArrayList<String>();
                tempList = (ArrayList<String>)lettersToWord.get(temp);

                result.addAll(tempList);
//                for (int i = 0; i<tempList.size(); i++) {
////                    result.add(tempList.get(i));
////                }

            }
        }
            // add a (etc) to the word
            // sortLetters(newWord)
            // check if the newWord is in the hashmap, and and then loop through its value list and add all its values to the result list



        return result;

    }

    public String pickGoodStarterWord() {
       // Log.i(TAG, "sortLetters(post) = " + sortLetters("post"));
       // Log.i(TAG, "getAnagrams(dog) test; " + getAnagrams("dog"));
        //Log.i(TAG, "lettersToWord test; " + lettersToWord.get("opst"));
        Log.i(TAG, "isGoodWord test for nonstop and post: " + isGoodWord("nonstop", "post"));
        Log.i(TAG, "get anagrams with one more letter for sam: " + getAnagramsWithOneMoreLetter("sam"));
        Log.i(TAG, "sizeToWords test for 5 letter words" + sizeToWords.get(5));

        // select random word from dictionary

        if (wordLength > MAX_WORD_LENGTH) {
            wordLength = DEFAULT_WORD_LENGTH;
        }

            Random rand = new Random();
            List<String> wordsOfLength = sizeToWords.get(wordLength);
            int index = rand.nextInt(wordsOfLength.size());
            // iterate until find a word that has at least MIN_NUM_ANAGRAMS anagrams
            int numAnagrams = 0;
            String currWord = "";

            while (numAnagrams < MIN_NUM_ANAGRAMS) {
                index++;
                if (index == wordsOfLength.size()) {
                    index = 0;
                }

                currWord = wordsOfLength.get(index);

                numAnagrams = getAnagramsWithOneMoreLetter(currWord).size();
            }

            wordLength++;

            return currWord;
    }
}
