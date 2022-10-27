package assignment;

import java.io.IOException;

public class TestInvalidWordsJar {

    public static void main(String[] args) throws IOException {
        BoggleGame game = new GameManager();
        BoggleDictionary dictionary = new GameDictionary();
        dictionary.loadDictionary("/Users/jimmybao/CS/CS314H/prog5 testing/words.txt");
        int numPlayers = 2;
        game.newGame(4, numPlayers, "/Users/jimmybao/CS/CS314H/prog5 testing/cubes.txt", dictionary);

        char[][] board = {{'E', 'E', 'C', 'A'},
                {'A', 'L', 'E', 'P'},
                {'H', 'N', 'B', 'O'},
                {'Q', 'T', 'T', 'Y'}};

        game.setGame(board);

        game.addWord("crazy", 0);

        if (game.getScores()[0] != 0) {
            System.out.println("ERROR");
        }
        if (game.getLastAddedWord() != null) {
            System.out.println("ERROR");
        }

        game.addWord("wild", 0);

        if (game.getScores()[0] != 0) {
            System.out.println("ERROR");
        }
        if (game.getLastAddedWord() != null) {
            System.out.println("ERROR");
        }

        game.addWord("hi", 0);

        if (game.getScores()[0] != 0) {
            System.out.println("ERROR");
        }
        if (game.getLastAddedWord() != null) {
            System.out.println("ERROR");
        }

        game.addWord("dkfsadkfkljds", 0);

        if (game.getScores()[0] != 0) {
            System.out.println("ERROR");
        }
        if (game.getLastAddedWord() != null) {
            System.out.println("ERROR");
        }

        game.addWord("CRAZY", 0);

        if (game.getScores()[0] != 0) {
            System.out.println("ERROR");
        }
        if (game.getLastAddedWord() != null) {
            System.out.println("ERROR");
        }

        game.addWord("#letsgo", 0);

        if (game.getScores()[0] != 0) {
            System.out.println("ERROR");
        }
        if (game.getLastAddedWord() != null) {
            System.out.println("ERROR");
        }
    }
}