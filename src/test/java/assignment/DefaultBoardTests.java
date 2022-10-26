package assignment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;



public class DefaultBoardTests {

    public static void main(String[] args) throws IOException {
        // note: this uses the default board given in the example in the pdf, useful for testing
    }

    @RepeatedTest(100)
    void testAddingWords() throws IOException {
        BoggleGame game = new GameManager();
        BoggleDictionary dictionary = new GameDictionary();
        dictionary.loadDictionary("words.txt");
        int numPlayers = 2;
        game.newGame(4, numPlayers, "cubes.txt", dictionary);

        char[][] board = {{'E', 'E', 'C', 'A'},
                {'A', 'L', 'E', 'P'},
                {'H', 'N', 'B', 'O'},
                {'Q', 'T', 'T', 'Y'}};

        game.setGame(board);

        String[] allSupposedWords = {"lean", "pace", "bent", "peel", "pent", "clan", "clean", "lent",
                "elan", "celeb", "cape", "capelan", "capo", "cent", "cento", "alee",
                "alec", "anele", "leant", "lane", "leap", "lento", "peace", "pele",
                "penal", "hale", "hant", "neap", "blae", "blah", "blent", "becap",
                "benthal", "bott", "open", "thae", "than", "thane", "toecap", "tope",
                "topee", "toby"};

        Set<String> used = new HashSet<String>();
        int[] scores = new int[numPlayers];
        int player = 0;

        for (int i = 0; i < allSupposedWords.length; i++) {
            // get a random word to use
            int index = (int)(Math.random() * allSupposedWords.length);
            game.addWord(allSupposedWords[index], player);
            if (!used.contains(allSupposedWords[index])) {
                scores[player] += (allSupposedWords[index].length() - 3);
            }

            for (int j = 0; j < numPlayers; j++) {
                Assertions.assertEquals(scores[j], game.getScores()[j]);
            }

            if (!used.contains(allSupposedWords[index])) {
                List<Point> lastWord = game.getLastAddedWord();
                String current = "";
                for (int j = 0; j < lastWord.size(); j++) {
                    current += board[(int)lastWord.get(j).getX()][(int)lastWord.get(j).getY()];
                }

                Assertions.assertEquals(allSupposedWords[index], current);
            }

            used.add(allSupposedWords[index]);
            player = (player + 1) % numPlayers;
        }
    }

    @Test
    void testGetAllWords() throws IOException {
        BoggleGame game = new GameManager();
        BoggleDictionary dictionary = new GameDictionary();
        dictionary.loadDictionary("words.txt");
        game.newGame(4, 2, "cubes.txt", dictionary);

        char[][] board = {{'E', 'E', 'C', 'A'},
                {'A', 'L', 'E', 'P'},
                {'H', 'N', 'B', 'O'},
                {'Q', 'T', 'T', 'Y'}};

        game.setGame(board);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Assertions.assertEquals(board[i][j], game.getBoard()[i][j]);
            }
        }

        System.out.println("Search Tactic: Search Board");
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

        System.out.println("Search Tactic: Search Dict");
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
