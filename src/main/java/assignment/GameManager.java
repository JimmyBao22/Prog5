package assignment;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class GameManager implements BoggleGame {

    private int size, numPlayers;
    private String cubeFile;
    private String[] cubeStrings;
    private BoggleDictionary dict;
    private char[][] board;
    private int[] scores;
    private SearchTactic searchTactic;
    private HashSet<String> allWords, usedWords;
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

        try {
            BufferedReader reader = new BufferedReader(new FileReader(cubeFile));
            cubeStrings = new String[size * size];
            for (int i = 0; i < cubeStrings.length; i++) {
                cubeStrings[i] = reader.readLine();
            }
        } catch (IOException e) {
            System.err.println("Error reading instruction file: " + e.getMessage());
            return;
        }

        board = new char[size][size];
        scores = new int[numPlayers];
        allWords = new HashSet<String>();
        usedWords = new HashSet<String>();
        lastWord = null;

        setGame(board);
    }

    @Override
    public char[][] getBoard() {
        if (board == null) {
            System.err.println("No game created");
        }
        return board;
    }

    @Override
    public int addWord(String word, int player) {
        if (allWords == null) {
            System.err.println("No game created");
            return -1;
        }
        if (allWords.contains(word) && !usedWords.contains(word)) {
            usedWords.add(word);
            return word.length();
        } else {
            return 0;
        }
    }

    @Override
    public List<Point> getLastAddedWord() {
        if (lastWord == null) {
            System.err.println("No game created");
        }
        return lastWord;
    }

    @Override
    public void setGame(char[][] board) {
        if (cubeStrings == null) {
            System.err.println("No game created");
            return;
        }
        if (size * size != cubeStrings.length) {
            throw new IllegalArgumentException("Invalid input for the amount of cubes");
        }

        // TODO, based on piazza post if there are jagged grids, update this
        shuffle();
        for (int i = 0; i < cubeStrings.length; i++) {
            int index = (int)(Math.random() * cubeStrings[i].length());
            board[i / size][i % size] = cubeStrings[i].charAt(index);
        }
    }

    private void shuffle() {
        Random ordering = new Random();
        for (int i = 0; i < cubeStrings.length; i++) {
            int switchIndex = ordering.nextInt(cubeStrings.length);

            // swap the strings at indices i and switch index
            String temp = cubeStrings[i];
            cubeStrings[i] = cubeStrings[switchIndex];
            cubeStrings[switchIndex] = temp;
        }
    }

    @Override
    public Collection<String> getAllWords() {
        if (board == null) {
            System.err.println("No game created");
            return null;
        }
        Collection<String> words = new HashSet<String>();
        if (searchTactic.equals(SearchTactic.SEARCH_BOARD)) {

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
        if (scores == null) {
            System.err.println("No game created");
        }
        return scores;
    }
}
