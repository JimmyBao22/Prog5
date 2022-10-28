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

    // Creates a new Boggle game using a size x size board and the cubes specified in the file cubeFile.
    @Override
    public void newGame(int size, int numPlayers, String cubeFile, BoggleDictionary dict) throws IOException {
        this.size = size;
        this.numPlayers = numPlayers;
        this.cubeFile = cubeFile;
        this.dict = dict;

        if (size <= 0) {
            System.err.println("size needs to be a positive integer");
            return;
        }

        if (numPlayers <= 0) {
            System.err.println("number of players needs to be a positive integer");
            return;
        }

        if (numPlayers > 1000000) {
            System.err.println("too many players, please limit to under 1000000.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(cubeFile));
            cubeStrings = new String[size * size];
            for (int i = 0; i < cubeStrings.length; i++) {
                String next = reader.readLine();
                if (next != null) {
                    cubeStrings[i] = next;
                } else {
                    // if there are not enough cubes in the file, repeat a past cube
                    cubeStrings[i] = cubeStrings[(int)(Math.random() * i)];
                }
                cubeStrings[i] = cubeStrings[i].toLowerCase();
                // check for invalid characters
                for (int j = 0; j < cubeStrings[i].length(); j++) {
                    if (cubeStrings[i].charAt(j) - 'a' < 0 || cubeStrings[i].charAt(j) - 'z' > 0) {
                        // invalid character found
                        System.err.println("illegal cube character");
                        return;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading cube file: " + e.getMessage());
            return;
        }

        board = new char[size][size];
        scores = new int[numPlayers];
        searchTactic = SEARCH_DEFAULT;
        usedWords = new HashSet<String>();
        lastWordPoints = null;

        // sets the game based on randomized arrangement of cubes
        shuffle();
        for (int i = 0; i < cubeStrings.length; i++) {
            int index = (int)(Math.random() * cubeStrings[i].length());
            board[i / size][i % size] = cubeStrings[i].charAt(index);
        }
    }

    // gets the current board state
    @Override
    public char[][] getBoard() {
        if (board == null) {
            System.err.println("No game created");
        }

        return board;
    }

    // adds a word to the player's list and updates their score (if the word is valid) and returns
    // the point value of the word
    @Override
    public int addWord(String word, int player) {
        if (board == null) {
            System.err.println("No game created");
            return 0;
        }
        if (word == null || player < 0 || player >= numPlayers) {
            System.err.println("invalid input");
            return 0;
        }

        word = word.toLowerCase();
        if (word.length() >= 4 && !usedWords.contains(word) && dict.contains(word) && searchWord(word)) {
            // found the word
            scores[player] += (word.length() - 3);
            usedWords.add(word);
            return (word.length() - 3);
        }
        return 0;
    }

    // returns the positions of the characters of the last added word
    @Override
    public List<Point> getLastAddedWord() {
        if (lastWordPoints == null) {
            System.err.println("No added last word");
        }
        return lastWordPoints;
    }

    // sets the board state and resets certain instance variables
    @Override
    public void setGame(char[][] board) {
        if (board == null) {
            System.err.println("invalid input");
            return;
        }

        int height = board.length;

        // change all of board's characters to lowercase
        for (int i = 0; i < board.length; i++) {
            if (board[i].length != height) {
                System.err.println("invalid inputted board dimensions");
                return;
            }
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = (String.valueOf(board[i][j]).toLowerCase()).charAt(0);
                if (board[i][j] - 'a' < 0 || board[i][j] - 'z' > 0) {
                    System.err.println("invalid board character");
                    return;
                }
            }
        }

        if (this.board == null) {
            // game hasn't been initialized yet, don't need to reset instance variables
            this.board = board;
            return;
        }

        this.board = board;

        // resetting instance variables
        Arrays.fill(scores, 0);
        usedWords.clear();
        if (lastWordPoints != null) lastWordPoints.clear();
        size = board.length;
    }

    // returns a collection containing all valid words in the current Boggle board using the current search tactic
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
            System.err.println("Invalid search tactic");
        }

        return words;
    }

    // dictionary-driven search that iterates over all words in the Dictionary and checks whether these
    // words can be found on the given board
    private void searchDict(Collection<String> words) {
        for (String nextString : dict) {
            if (nextString.length() >= 4 && searchWord(nextString)) {
                // this string works, add it to the collection
                words.add(nextString);
            }
        }
        lastWordPoints = null;
    }

    // searches for a specific word in the board
    private boolean searchWord(String desiredWord) {
        Stack<WordPoints> stack = new Stack<WordPoints>();

        // push all the letters that match the first character of the word into the stack
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == desiredWord.charAt(0)) {
                    addToStack(stack, new ArrayList<Point>(), i, j, String.valueOf(board[i][j]), new boolean[size][size]);
                }
            }
        }

        return searchStack(stack, null, desiredWord, false);
    }

    // board-driven search that recursively search the board for words beginning at each square on the board
    private void searchBoard(Collection<String> words) {
        Stack<WordPoints> stack = new Stack<WordPoints>();

        // push every letter in the board into the stack
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                addToStack(stack, new ArrayList<Point>(), i, j, String.valueOf(board[i][j]), new boolean[size][size]);
            }
        }

        // since we are searching for all words, there is no specific desired word
        searchStack(stack, words, "", true);
    }

    // searches the board for either a specific word, or for all words (depending on whether this function
    // was called from searchWord or searchBoard (uses a boolean to keep track of which))
    private boolean searchStack(Stack<WordPoints> stack, Collection<String> words, String desiredWord, boolean searchBoard) {
        // while there are still elements in the stack, continue to search for words
        while (!stack.isEmpty()) {
            WordPoints current = stack.pop();
            String currentWord = current.getWord();
            List<Point> currentPoints = current.getPoints();
            Point previousPosition = currentPoints.get(currentPoints.size() - 1);
            int x = (int)(previousPosition.getX());
            int y = (int)(previousPosition.getY());
            boolean[][] visited = current.getVisited();

            if (visited[x][y]) {
                // already visited this point in this path
                continue;
            }
            visited[x][y] = true;

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

            // update the stack by appending characters around the current position to the current word
            updateStack(stack, currentWord, currentPoints, x, y, visited);
        }

        return false;
    }

    // checks dictionary-driven search conditions
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

        // the current word is a prefix of the desired word, continue searching
        return 2;
    }

    // checks board-driven search conditions
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

    // updates the stack by attempting to go in all directions and appending to the current word based on the current position
    private void updateStack(Stack<WordPoints> stack, String currentWord, List<Point> currentPoints, int x, int y, boolean[][] visited) {
        // attempts go in all directions
        int[][] delta = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
        for (int i = 0; i < delta.length; i++) {
            int updatedX = x + delta[i][0];
            int updatedY = y + delta[i][1];

            if (!outOfBounds(updatedX, updatedY)) {
                String updatedWord = currentWord + board[updatedX][updatedY];
                addToStack(stack, currentPoints, updatedX, updatedY, updatedWord, visited);
            }
        }
    }

    // adds this specific updated word and updated positions to the stack
    private void addToStack(Stack<WordPoints> stack, List<Point> currentPoints, int updatedX, int updatedY, String updatedWord, boolean[][] visited) {
        List<Point> updatedPoints = new ArrayList<Point>(currentPoints);
        updatedPoints.add(new Point(updatedX, updatedY));
        stack.add(new WordPoints(updatedWord, updatedPoints, visited));
    }

    // checks if the given position is out of bounds of the board
    private boolean outOfBounds(int x, int y) {
        return x < 0 || y < 0 || x >= board.length || y >= board[x].length;
    }

    // class that stores the current word, as well as the list of points and positions visited
    private class WordPoints {
        private String word;
        private List<Point> points;
        private boolean[][] visited;

        public WordPoints(String word, List<Point> points, boolean[][] visited) {
            this.word = word;
            this.points = points;

            this.visited = new boolean[visited.length][visited[0].length];
            if (visited != null) {
                // copy the previous visited array
                for (int i = 0; i < visited.length; i++) {
                    this.visited[i] = Arrays.copyOf(visited[i], visited[i].length);
                }
            }
        }

        public String getWord() {
            return word;
        }

        public List<Point> getPoints() {
            return points;
        }

        public boolean[][] getVisited() {
            return visited;
        }
    }

    // Sets the search tactic (used by getAllWords()) to the given tactic
    @Override
    public void setSearchTactic(SearchTactic tactic) {
        if (tactic != SearchTactic.SEARCH_DICT && tactic != SearchTactic.SEARCH_BOARD) {
            // if the search tactic parameter is invalid, then set the search tactic to the default one
            searchTactic = SEARCH_DEFAULT;
        } else {
            searchTactic = tactic;
        }
    }

    // gets the current scores for all players
    @Override
    public int[] getScores() {
        if (scores == null) {
            System.err.println("No game created");
        }

        return scores;
    }

    // shuffles the cube strings array
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