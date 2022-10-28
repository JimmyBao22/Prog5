package assignment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DictionaryTest {
    String makeRandomString() {
        StringBuilder ans = new StringBuilder();
        int len = 1 + (int) (Math.random() * 10);
        for (int i = 0; i < len; i++) {
            ans.append((char) ('A' + Math.random() * 26));
        }

        return ans.toString();
    }

    // randomizes the case of each character in a string
    String randomizeCase(String in) {
        StringBuilder out = new StringBuilder();
        for (char c : in.toCharArray()) {
            if (Math.random() < 0.5) {
                out.append(Character.toLowerCase(c));
            } else {
                out.append(Character.toUpperCase(c));
            }
        }

        return out.toString();
    }

    @Test
    void loadDictionary() throws IOException {
        BoggleDictionary dict = new GameDictionary();
        BufferedReader br = new BufferedReader(new FileReader("words.txt"));

        String word = br.readLine();
        Set<String> words = new HashSet<String>();
        List<String> orderedWords = new ArrayList<String>();

        while (word != null) {
            words.add(word.toUpperCase());
            orderedWords.add(word.toUpperCase());
            word = br.readLine();
        }

        dict.loadDictionary("words.txt");

        int ind = 0;
        Set<String> traversedWords = new HashSet<String>();
        List<String> traversedOrderedWords = new ArrayList<String>();
        for (String currWord : dict) {
            traversedWords.add(currWord.toUpperCase());
            traversedOrderedWords.add(currWord.toUpperCase());

            Assertions.assertTrue(words.contains(currWord.toUpperCase()));
            Assertions.assertTrue(orderedWords.get(ind).equals(currWord.toUpperCase()));
            ind++;
        }

        Assertions.assertEquals(traversedWords, words);
        Assertions.assertEquals(traversedOrderedWords, orderedWords);

        for (String currWord : words) {
            Assertions.assertTrue(dict.contains(currWord.toUpperCase()));
            Assertions.assertTrue(dict.contains(randomizeCase(currWord)));
            Assertions.assertTrue(dict.contains(currWord.toLowerCase()));

            Assertions.assertTrue(dict.contains(currWord.toUpperCase()) || dict.contains(currWord.toLowerCase()));
            for (int i = 1; i <= currWord.length(); i++) {
                String substr = currWord.substring(0, i);

                Assertions.assertTrue(dict.isPrefix(substr.toUpperCase()));
                Assertions.assertTrue(dict.isPrefix(randomizeCase(substr)));
                Assertions.assertTrue(dict.isPrefix(substr.toLowerCase()));
            }
        }

        for (int numTest = 0; numTest < 10000; numTest++) {
            String testWord;
            do {
                testWord = makeRandomString();
            } while (words.contains(testWord));

            Assertions.assertFalse(dict.contains(testWord.toUpperCase()));
            Assertions.assertFalse(dict.contains(randomizeCase(testWord.toUpperCase())));
            Assertions.assertFalse(dict.contains(testWord.toLowerCase()));
        }
    }
}