package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

public class GameDictionary implements BoggleDictionary {
    private Trie words;

    public GameDictionary() {
        this.words = null;
    }

    @Override
    public void loadDictionary(String filename) throws IOException {
        words = new Trie();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String word = "";

            while ((word = reader.readLine()) != null) {
                // remove excess whitespace and move to lowercase
                word = word.trim();
                word = word.toLowerCase();

                // check for invalid characters
                for (int j = 0; j < word.length(); j++) {
                    if (word.charAt(j) - 'a' < 0 || word.charAt(j) - 'z' > 0) {
                        // invalid character found
                        System.err.println("illegal character in dictionary");
                        return;
                    }
                }

                words.add(word);
            }
        } catch(IOException e) {
            System.err.println("Error reading dictionary: " + e.getMessage());
        }
    }

    @Override
    public boolean isPrefix(String prefix) {
        if (words == null) {
            System.err.println("Dictionary must be loaded before checking for prefixes");
            return false;
        }

        prefix = prefix.toLowerCase();
        prefix = prefix.trim();

        return words.hasPrefix(prefix);
    }

    @Override
    public boolean contains(String word) {
        if (words == null) {
            System.err.println("Dictionary must be loaded before checking for contains");
            return false;
        }

        word = word.toLowerCase();
        word = word.trim();

        return words.contains(word);
    }

    @Override
    public Iterator<String> iterator() {
        if (words == null) {
            System.err.println("No loaded dictionary");
            return Collections.emptyIterator();
        }

        return words.iterator();
    }
}