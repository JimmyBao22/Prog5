package assignment;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class GameManager implements BoggleGame {

    private int size, numPlayers;
    private String cubeFile;
    private BoggleDictionary dict;
    private char[][] board;
    private int[] scores;
    private SearchTactic searchTactic;
    private HashSet<String> allWords;
    private HashSet<String>[] playerWords;
    private List<Point> lastWord;

    @Override
    public void newGame(int size, int numPlayers, String cubeFile, BoggleDictionary dict) throws IOException {
        this.size = size;
        this.numPlayers = numPlayers;
        this.cubeFile = cubeFile;
        this.dict = dict;
        if (size < 0) {
            throw new IllegalArgumentException("size is less than 0");
        }
        if (numPlayers < 0) {
            throw new IllegalArgumentException("number of players is less than 0");
        }

        // TODO check that cubefile is a correct file

        board = new char[size][size];
        scores = new int[numPlayers];
        allWords = new HashSet<String>();
        playerWords = new HashSet[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            playerWords[i] = new HashSet<String>();
        }
        lastWord = null;

    }

    @Override
    public char[][] getBoard() {
        return board;
    }

    @Override
    public int addWord(String word, int player) {
        if (allWords.contains(word) && !playerWords[player-1].contains(word)) {
            playerWords[player-1].add(word);
            return word.length();
        } else {
            return 0;
        }
    }

    @Override
    public List<Point> getLastAddedWord() {
        return lastWord;
    }

    @Override
    public void setGame(char[][] board) {

    }

    @Override
    public Collection<String> getAllWords() {
        Collection<String> words = new HashSet<String>();
        if (searchTactic.equals(SEARCH_DEFAULT)) {

        } else if (searchTactic.equals(SearchTactic.SEARCH_BOARD)) {

        } else if (searchTactic.equals(SearchTactic.SEARCH_DICT)) {

        } else {

        }

        return null;
    }

    @Override
    public void setSearchTactic(SearchTactic tactic) {
        searchTactic = tactic;
    }

    @Override
    public int[] getScores() {
        return scores;
    }
}
