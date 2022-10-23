package assignment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class DefaultBoardTests {

    public static void main(String[] args) throws IOException {
        // note: this is the default board given in the example in the pdf, useful for testing


    }

    @Test
    void testGetAllWords() throws IOException {
        GameManager game = new GameManager();
        GameDictionary dictionary = new GameDictionary();
        dictionary.loadDictionary("words.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        game.newGame(4, 2, "cubes.txt", dictionary);

        char[][] board = {{'E', 'E', 'C', 'A'},
                {'A', 'L', 'E', 'P'},
                {'H', 'N', 'B', 'O'},
                {'Q', 'T', 'T', 'Y'}};

        game.setGame(board);

        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_BOARD);

        Collection<String> allWordsSet = game.getAllWords();
        List<String> allWords = new ArrayList<String>();
        for (String s : allWordsSet) allWords.add(s);
        Collections.sort(allWords);

        String[] allSupposedWords = {"lean", "pace", "bent", "peel", "pent", "clan", "clean", "lent",
                "elan", "celeb", "cape", "capelan", "capo", "cent", "cento", "alee",
                "alec", "anele", "leant", "lane", "leap", "lento", "peace", "pele",
                "penal", "hale", "hant", "neap", "blae", "blah", "blent", "becap",
                "benthal", "bott", "open", "thae", "than", "thane", "toecap", "tope",
                "topee", "toby"};

        Arrays.sort(allSupposedWords);

        Assertions.assertEquals(allWords.size(), allSupposedWords.length);

        for (int i = 0; i < allWords.size(); i++) {
            allSupposedWords[i] = allSupposedWords[i].toLowerCase();
            Assertions.assertEquals(allWords.get(i), allSupposedWords[i]);
        }

        game.setGame(board);

        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_DICT);

        allWordsSet = game.getAllWords();
        allWords = new ArrayList<String>();
        for (String s : allWordsSet) allWords.add(s);
        Collections.sort(allWords);

        Assertions.assertEquals(allWords.size(), allSupposedWords.length);

        for (int i = 0; i < allWords.size(); i++) {
            allSupposedWords[i] = allSupposedWords[i].toLowerCase();
            Assertions.assertEquals(allWords.get(i), allSupposedWords[i]);
        }
    }
}
