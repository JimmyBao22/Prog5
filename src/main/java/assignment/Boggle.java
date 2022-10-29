package assignment;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

public class Boggle {
    private BoggleGame manager;
    private BoggleDictionary dict;
    private BufferedReader inputReader;
    private int currPlayer;
    private Set<String> guessedWords;

    // should only be constructed within this class
    private Boggle() throws IOException {
        this.manager = new GameManager();
        this.dict = new GameDictionary();
        dict.loadDictionary("words.txt");
        this.inputReader = new BufferedReader(new InputStreamReader(System.in));
        this.currPlayer = 0;
        this.guessedWords = null;
    }

    // prompt the user and return their response
    private String getPromptResponse(String prompt) throws IOException {
        System.out.print(prompt);
        return inputReader.readLine();
    }

    // read the user's next word
    private String getUserGuess() throws IOException {
        return getPromptResponse("Player " + (currPlayer+1) + ": enter a word (RETURN if done guessing): ");
    }

    // determine whether the user wants to play another game
    private boolean promptNextGame() throws IOException {
        String ans = getPromptResponse("Do you want to play again? (Y/N): ").toLowerCase();
        // if response is non-empty and starts with y, consider it yes
        return ans.length() > 0 && ans.charAt(0) == 'y';
    }

    // display a header like "+-------+"
    private static void printBoardHeader(int maxRowWidth) {
        // display the header like +-----+
        System.out.print("+");
        // n characters, n - 1 spaces between them
        for (int i = 0; i < (maxRowWidth + maxRowWidth - 1); i++) {
            System.out.print('-');
        }
        System.out.println("+");
    }

    // display the letters in a row like "|a b c d|"
    private static void printBoardRow(char[] row) {
        if (row.length < 1) {
            return;
        }

        System.out.print('|');
        System.out.print(row[0]);

        for (int j = 1; j < row.length; j++) {
            System.out.print(" ");
            System.out.print(row[j]);
        }

        System.out.println('|');
    }

    // display the board
    private static void displayBoard(char[][] board) {
        int maxRowWidth = 0;
        for (char[] row : board) {
            maxRowWidth = Math.max(maxRowWidth, row.length);
        }

        printBoardHeader(maxRowWidth);

        // display the rows like |a b c|
        for (int i = 0; i < board.length; i++) {
            printBoardRow(board[i]);
        }

        printBoardHeader(maxRowWidth);
        System.out.println();
    }

    // update the state of the game based on the user's guess, return whether the guess scored points
    private boolean processWord(String userWord) {
        if (userWord == null || userWord.length() == 0) {
            // invalid guess
            return false;
        }

        int result = manager.addWord(userWord, currPlayer);
        if (result > 0) {
            // make a copy of the board and mark the letters of the word that was just guessed
            char[][] origBoard = manager.getBoard();
            char[][] markedBoard = new char[origBoard.length][];
            for (int row = 0; row < origBoard.length; row++) {
                markedBoard[row] = new char[origBoard[row].length];
                for (int col = 0; col < origBoard[row].length; col++) {
                    markedBoard[row][col] = origBoard[row][col];
                }
            }

            List<Point> lastWordPoints = manager.getLastAddedWord();

            for (Point p : lastWordPoints) {
                int i = (int) (p.getX());
                int j = (int) (p.getY());

                // shift the character to uppercase
                markedBoard[i][j] -= 'a';
                markedBoard[i][j] += 'A';
            }

            System.out.println('\"' + userWord + "\" was a valid guess: +" + result + " points.");
            guessedWords.add(userWord);
            displayBoard(markedBoard);
            return true;
        } else {
            // something is wrong with the guess: too short, already used, or not on board
            if (userWord.length() < 4) {
                System.out.println("The guessed word must be at least 4 letters.");
            } else if (guessedWords.contains(userWord)) {
                System.out.println("The guessed word has already been used.");
            } else if (!dict.contains(userWord)) {
                System.out.println("The guessed word is not in the dictionary.");
            } else {
                // the word must not be on the board
                System.out.println("The guessed word is not on the board.");
            }

            return false;
        }
    }

    private boolean canBeParsedAsInt(String x) {
        try {
            Integer.parseInt(x);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ask for the number of players for a new game (returns a positive integer)
    private int getNumPlayers() throws IOException {
        String ans;

        ans = getPromptResponse("Enter number of players: ");

        while (!canBeParsedAsInt(ans) || Integer.parseInt(ans) <= 0) {
            System.out.println("Please enter a positive integer.");
            ans = getPromptResponse("Enter number of players: ");
        }

        return Integer.parseInt(ans);
    }

    // display all valid words on the board that were not guessed by any player
    private void displayMissingWords(Collection<String> missingWords) {
        System.out.println("Words that were not guessed:");
        for (String missingWord : missingWords) {
            System.out.print(missingWord);
            System.out.print(' ');
        }

        System.out.println();
        System.out.println();
    }

    // show each player's score
    private void displayScores() {
        System.out.println("Scores:");
        int[] scores = manager.getScores();
        for (int player = 0; player < scores.length; player++) {
            System.out.println("Player " + (player+1) +  ": " + scores[player]);
        }
        System.out.println();
    }

    // play one game of boggle
    private void playGame() throws IOException {
        int numPlayers = getNumPlayers();
        manager.newGame(4, numPlayers, "cubes.txt", dict);

        guessedWords = new HashSet<String>();

        // store which players are still guessing
        Set<Integer> stillGuessing = new TreeSet<Integer>();
        // all players are in initially
        for (int playerIndex = 0; playerIndex < numPlayers; playerIndex++) {
            stillGuessing.add(playerIndex);
        }

        // ask each user that is still playing for a guess, until no users want to guess anymore
        do {
            // store which players are still guessing
            Iterator<Integer> guessingPlayers = stillGuessing.iterator();
            while (guessingPlayers.hasNext()) {
                currPlayer = guessingPlayers.next();

                String guessedWord;

                displayBoard(manager.getBoard());

                // ask the user for a guess until they stop guessing or the have a valid word
                do {
                    guessedWord = getUserGuess().toLowerCase();

                    if (guessedWord == null || guessedWord.length() == 0) {
                        // this player does not want to guess anymore
                        guessingPlayers.remove();
                        break;
                    }

                    // if it is legal, add to this player's score, display where the word was on the board
                    // otherwise explain what went wrong, don't add to any score
                } while (!processWord(guessedWord));
            }
        } while (!stillGuessing.isEmpty());

        // computer finds all words that human did not
        Collection<String> missingWords = manager.getAllWords();
        missingWords.removeAll(guessedWords);

        // display results
        displayMissingWords(missingWords);
        displayScores();
    }

    // keep playing games until the user wants to stop
    private void startGameLoop() throws IOException {
        do {
            playGame();
        } while (promptNextGame());
    }


    public static void main(String[] args) throws IOException {
        Boggle b = new Boggle();
        b.startGameLoop();
    }
}
