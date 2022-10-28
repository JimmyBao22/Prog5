package assignment;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

public class TestGetAllWordsJar {

    public static void main(String[] args) throws IOException {
        BoggleDictionary dictionary = new GameDictionary();
        dictionary.loadDictionary("words.txt");
        BoggleGame game = new GameManager();
        game.newGame(4, 2, "cubes.txt", dictionary);

        char[][] board = {{'E', 'E', 'C', 'A'},
                {'A', 'L', 'E', 'P'},
                {'H', 'N', 'B', 'O'},
                {'Q', 'T', 'T', 'Y'}};

        game.setGame(board);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != game.getBoard()[i][j]) {
                    System.out.println("WRONG! Expected: " + board[i][j] + " Actual: " + game.getBoard()[i][j]);
                }
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

        if (allWords.size() != allSupposedWords.length) {
            System.out.println("WRONG! Expected: " + allSupposedWords.length + " Actual: " + allWords.size());
        }

        for (int i = 0; i < allWords.size(); i++) {
            allSupposedWords[i] = allSupposedWords[i].toLowerCase();
            if (!allWords.get(i).toLowerCase().equals(allSupposedWords[i])) {
                System.out.println("WRONG! Expected: " + allSupposedWords[i] + " Actual: " + allWords.get(i).toLowerCase());
            }
        }

        game.setGame(board);

        System.out.println("Search Tactic: Search Dict");
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_DICT);

        allWordsSet = game.getAllWords();
        allWords = new ArrayList<String>();
        for (String s : allWordsSet) allWords.add(s);
        Collections.sort(allWords);

        if (allWords.size() != allSupposedWords.length) {
            System.out.println("WRONG! Expected: " + allSupposedWords.length + " Actual: " + allWords.size());
        }

        for (int i = 0; i < allWords.size(); i++) {
            allSupposedWords[i] = allSupposedWords[i].toLowerCase();
            if (!allWords.get(i).toLowerCase().equals(allSupposedWords[i])) {
                System.out.println("WRONG! Expected: " + allSupposedWords[i] + " Actual: " + allWords.get(i).toLowerCase());
            }
        }
    }
}