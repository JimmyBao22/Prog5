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
                words.add(word);
            }
        } catch(IOException e) {
            // TODO
        }
    }

    @Override
    public boolean isPrefix(String prefix) {
        return words.hasPrefix(prefix);
    }

    @Override
    public boolean contains(String word) {
        return words.contains(word);
    }

    @Override
    public Iterator<String> iterator() {
        return words.iterator();
    }
}