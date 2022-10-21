package assignment;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameManager implements BoggleGame {

    private int size, numPlayers;
    private String cubeFile;
    private String[] cubeStrings;
    private BoggleDictionary dict;
    private char[][] board;
    private int[] scores;
    private SearchTactic searchTactic;
    private HashMap<String, List<Point>> allWords;
    private HashSet<String> usedWords;
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
        allWords = new HashMap<String, List<Point>>();
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

        // TODO update last word
        if (allWords.containsKey(word) && !usedWords.contains(word)) {
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
        Random rand = new Random();

        for (int i = 0; i < cubeStrings.length; i++) {
            int switchIndex = rand.nextInt(cubeStrings.length);

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
            searchBoard(words);
        } else if (searchTactic.equals(SearchTactic.SEARCH_DICT)) {

        } else {
            throw new IllegalArgumentException("Invalid search tactic");
        }

        return words;
    }

    private void searchBoard (Collection<String> words) {
        // TODO: can we use a regular queue instead of deque?
        // can we break this method into smaller pieces? would improve readability
        // can we use (custom) int based points instead of AWT double based ones?


        ArrayDeque<WordPoints> deque = new ArrayDeque<WordPoints>();
        // push every letter in the board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                List<Point> points = new ArrayList<Point>();
                points.add(new Point(i, j));
                WordPoints current = new WordPoints(String.valueOf(board[i][j]), points);
                deque.add(current);
            }
        }

        while (!deque.isEmpty()) {
            WordPoints current = deque.poll();
            String currentWord = current.getWord();
            List<Point> currentPoints = current.getPoints();
            Point previousPosition = currentPoints.get(currentPoints.size() - 1);
            int x = (int)(previousPosition.getX());
            int y = (int)(previousPosition.getY());

            // attempt go in all four directions
            int[][] delta = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int i = 0; i < delta.length; i++) {
                int updatedX = x + delta[i][0];
                int updatedY = y + delta[i][1];

                if (!outOfBounds(updatedX, updatedY)) {
                    String updatedWord = currentWord + board[updatedX][updatedY];
                    // if the current word is a word
                    if (updatedWord.length() >= 4 && !words.contains(updatedWord) && dict.contains(updatedWord)) {
                        // dictionary contains the word, we should mark it
                        words.add(updatedWord);
                        updateDeque(deque, currentPoints, updatedX, updatedY, updatedWord);
                    } else if (dict.isPrefix(updatedWord)) {
                        // we should continue exploring this path
                        updateDeque(deque, currentPoints, updatedX, updatedY, updatedWord);
                    }
                }
            }
        }
    }

    private void updateDeque(ArrayDeque<WordPoints> deque, List<Point> currentPoints, int updatedX, int updatedY, String updatedWord) {
        List<Point> updatedPoints = new ArrayList<Point>(currentPoints);
        updatedPoints.add(new Point(updatedX, updatedY));
        deque.add(new WordPoints(updatedWord, updatedPoints));
    }

    // TODO can delete if not needed
    private boolean outOfBounds(Point current) {
        return current.getX() < 0 || current.getY() < 0 || current.getX() >= board.length || current.getY() >= board[(int)(current.getX())].length;
    }

    private boolean outOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= board.length || y >= board[x].length;
    }

    private class WordPoints {
        private String word;
        private List<Point> points;

        public WordPoints(String word, List<Point> points) {
            this.word = word;
            this.points = points;
        }

        public String getWord() {
            return word;
        }

        public List<Point> getPoints() {
            return points;
        }
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