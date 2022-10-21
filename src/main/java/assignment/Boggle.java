package assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Boggle {
    public static void main(String[] args) throws IOException {
        // do {
            GameManager game = new GameManager();
            GameDictionary dictionary = new GameDictionary();
            dictionary.loadDictionary("words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            do {
                game.newGame(4, 2, "cubes.txt", dictionary);
                char[][] board = game.getBoard();
                for (int i = 0; i < board.length; i++) {
                    System.out.println(Arrays.toString(board[i]));
                }
                System.out.println(Arrays.toString(game.getScores()));
                System.out.println(game.getAllWords());
                // display new board
                // take user entered word
                // if it is legal, add to score, display where the word was on the board
                // otherwise explain what went wrong, don't add to score
            } while (!reader.readLine().equals("!"));

            // computer finds all words that human did not
            // display results
        // } while (/* user wants to play another game*/);
    }
}
