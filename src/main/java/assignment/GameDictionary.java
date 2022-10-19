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
    private List<String> words;

    @Override
    public void loadDictionary(String filename) throws IOException {
        fileName = filename;

        // TODO check if file exists

        words = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String word = "";
        while ((word = reader.readLine()) != null) {
            words.add(word);
        }
    }

    @Override
    public boolean isPrefix(String prefix) {

        return false;
    }

    @Override
    public boolean contains(String word) {

        return false;
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }
}