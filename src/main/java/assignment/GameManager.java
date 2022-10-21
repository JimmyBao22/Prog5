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
    private Set<String> usedWords;
    private List<Point> lastWordPoints;

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
        searchTactic = SEARCH_DEFAULT;
        usedWords = new HashSet<String>();
        lastWordPoints = null;

        // sets the game based on randomized arrangement of cubes
        // TODO, based on piazza post if there are jagged grids, update this
        shuffle();
        for (int i = 0; i < cubeStrings.length; i++) {
            int index = (int)(Math.random() * cubeStrings[i].length());
            board[i / size][i % size] = cubeStrings[i].charAt(index);
        }
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
        if (board == null) {
            System.err.println("No game created");
            return -1;
        }

        if (word.length() >= 4 && !usedWords.contains(word) && searchWord(new HashSet<String>(), word)) {
            // found the word
            scores[player] += word.length();
            usedWords.add(word);
            return word.length();
        }
        return 0;
    }

    @Override
    public List<Point> getLastAddedWord() {
        if (lastWordPoints == null) {
            System.err.println("No game created");
        }
        return lastWordPoints;
    }

    @Override
    public void setGame(char[][] board) {
        this.board = board;

        // resetting instance variables
        Arrays.fill(scores, 0);
        usedWords.clear();
        lastWordPoints.clear();
        size = board.length;
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
            searchDict(words);
        } else {
            throw new IllegalArgumentException("Invalid search tactic");
        }

        return words;
    }

    // dictionary-driven search that iterates over all words in the Dictionary and checks whether these
        // words can be found on the given board
    private void searchDict(Collection<String> words) {
        for (String nextString : dict) {
            if (nextString.length() >= 4 && searchWord(words, nextString)) {
                // this string works
                words.add(nextString);
            }
        }
        lastWordPoints = null;
    }

    // searches for a specific word in the board
    private boolean searchWord(Collection<String> words, String desiredWord) {
        Queue<WordPoints> queue = new LinkedList<WordPoints>();
        // push the letters that match the start of the word
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == desiredWord.charAt(0)) {
                    addToQueue(queue, new ArrayList<Point>(), i, j, String.valueOf(board[i][j]));
                }
            }
        }

        return searchQueue(queue, words, desiredWord, false);
    }

    // board-driven search that recursively search the board for words beginning at each square on the board
    private void searchBoard(Collection<String> words) {
        Queue<WordPoints> queue = new LinkedList<WordPoints>();
        // push every letter in the board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                addToQueue(queue, new ArrayList<Point>(), i, j, String.valueOf(board[i][j]));
            }
        }

        // since we are searching for all words, there is no specific desired word
        searchQueue(queue, words, "", true);
    }

    private boolean searchQueue(Queue<WordPoints> queue, Collection<String> words, String desiredWord, boolean searchBoard) {
        while (!queue.isEmpty()) {
            WordPoints current = queue.poll();
            String currentWord = current.getWord();
            List<Point> currentPoints = current.getPoints();
            Point previousPosition = currentPoints.get(currentPoints.size() - 1);
            int x = (int)(previousPosition.getX());
            int y = (int)(previousPosition.getY());

            if (current.getVisited().contains(new Point(x, y))) {
                // already visited this point in this path
                continue;
            }
            current.getVisited().add(new Point(x, y));

            if (searchBoard) {
                int conditionCheck = searchBoardConditions(currentWord, words);
                if (conditionCheck == 0) {
                    // dictionary contains the word, we should mark it
                    words.add(currentWord);
                } else if (conditionCheck == 1) {
                    // we should not continue exploring this path
                    continue;
                }
            } else {
                int conditionCheck = searchDictConditions(currentWord, desiredWord);
                if (conditionCheck == 0) {
                    // found the word
                    lastWordPoints = currentPoints;
                    return true;
                } else if (conditionCheck == 1) {
                    // we should not continue exploring this path
                    continue;
                }
            }

            updateQueue(queue, currentWord, currentPoints, x, y);
        }

        return false;
    }

    private int searchDictConditions(String currentWord, String desiredWord) {
        if (currentWord.equals(desiredWord)) {
            // found the word
            return 0;
        }

        if (currentWord.length() >= desiredWord.length()) {
            // we have exceeded the length of the word we're trying to find, and therefore
            // the current word cannot be the word we're trying to find
            return 1;
        }

        if (!currentWord.equals(desiredWord.substring(0, currentWord.length()))) {
            // the current word is not a prefix of the word we're trying to find, so it cannot be the
            // word we're trying to find
            return 1;
        }

        return 2;
    }

    private int searchBoardConditions(String currentWord, Collection<String> words) {
        if (currentWord.length() >= 4 && !words.contains(currentWord) && dict.contains(currentWord)) {
            // dictionary contains the word, we should mark it
            return 0;
        }

        if (!dict.isPrefix(currentWord)) {
            // we should not continue exploring this path
            return 1;
        }

        return 2;
    }

    private void updateQueue(Queue<WordPoints> queue, String currentWord, List<Point> currentPoints, int x, int y) {
        // attempt go in all directions
        int[][] delta = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
        for (int i = 0; i < delta.length; i++) {
            int updatedX = x + delta[i][0];
            int updatedY = y + delta[i][1];

            if (!outOfBounds(updatedX, updatedY)) {
                String updatedWord = currentWord + board[updatedX][updatedY];
                addToQueue(queue, currentPoints, updatedX, updatedY, updatedWord);
            }
        }
    }

    private void addToQueue(Queue<WordPoints> queue, List<Point> currentPoints, int updatedX, int updatedY, String updatedWord) {
        List<Point> updatedPoints = new ArrayList<Point>(currentPoints);
        updatedPoints.add(new Point(updatedX, updatedY));
        queue.add(new WordPoints(updatedWord, updatedPoints));
    }

    private boolean outOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= board.length || y >= board[x].length;
    }

    private class WordPoints {
        private String word;
        private List<Point> points;
        private Set<Point> visited;

        public WordPoints(String word, List<Point> points) {
            this.word = word;
            this.points = points;
            visited = new HashSet<Point>();
        }

        public String getWord() {
            return word;
        }

        public List<Point> getPoints() {
            return points;
        }

        public Set<Point> getVisited() {
            return visited;
        }
    }

    @Override
    public void setSearchTactic(SearchTactic tactic) {
        if (tactic != SearchTactic.SEARCH_DICT && tactic != SearchTactic.SEARCH_BOARD) {
            searchTactic = SEARCH_DEFAULT;
        } else {
            searchTactic = tactic;
        }
    }

    @Override
    public int[] getScores() {
        if (scores == null) {
            System.err.println("No game created");
        }

        return scores;
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
}