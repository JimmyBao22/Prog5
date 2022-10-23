package assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameDictionary implements BoggleDictionary {
    private String fileName;
    private Trie words;

    @Override
    public void loadDictionary(String filename) throws IOException {
        fileName = filename;
        words = new Trie();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String word = "";
            while ((word = reader.readLine()) != null) {
                // remove excess whitespace and move to lowercase
                word = word.trim();
                word = word.toLowerCase();

                // TODO check for invalid characters??

                words.add(word);
            }
        } catch(IOException e) {
            System.err.println("Error reading instruction file: " + e.getMessage());
        }
    }

    @Override
    public boolean isPrefix(String prefix) {
        prefix = prefix.toLowerCase();
        if (words == null) {
            System.err.println("No loaded dictionary");
            return false;
        }

        return words.hasPrefix(prefix);
    }

    @Override
    public boolean contains(String word) {
        word = word.toLowerCase();
        if (words == null) {
            System.err.println("No loaded dictionary");
            return false;
        }

        return words.contains(word);
    }

    @Override
    public Iterator<String> iterator() {
        if (words == null) {
            System.err.println("No loaded dictionary");
            return null;
        }

        return words.iterator();
    }
}