package assignment;

import java.awt.*;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestWeirdBoardsJar {

    public static void main(String[] args) throws IOException {
        BoggleGame game = new GameManager();
        BoggleDictionary dictionary = new GameDictionary();
        dictionary.loadDictionary("words.txt");
        int numPlayers = 2;
        game.newGame(4, numPlayers, "cubes.txt", dictionary);

        char[][] board = {{'E'}};

        // should work
        game.setGame(board);

        if (1 != game.getBoard().length) {
            System.out.println("ERROR");
        }
        if (1 != game.getBoard()[0].length) {
            System.out.println("ERROR");
        }

        Collection<String> allWords = game.getAllWords();

        if (0 != allWords.size()) {
            System.out.println("ERROR");
        }

        board = new char[][]{{'E', 'X'}};

        // should throw an error
        game.setGame(board);

        board = new char[][]{{'E'}, {'X'}};

        // should throw an error
        game.setGame(board);

        board = new char[0][0];

        // should work
        game.setGame(board);

        board = new char[][]{{'e', 'E', 'C', 'A'},
                {'A', 'L', 'E', 'p'},
                {'H', 'n', 'b', 'o'},
                {'q', 'T', 't', 'Y'}};;

        // should work
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
                if (scores[j] != game.getScores()[j]) {
                    System.out.println("WRONG! Expected: " + scores[j] + " Actual: " + game.getScores()[j]);
                }
            }

            if (!used.contains(allSupposedWords[index])) {
                List<Point> lastWord = game.getLastAddedWord();
                String current = "";
                for (int j = 0; j < lastWord.size(); j++) {
                    current += board[(int)lastWord.get(j).getX()][(int)lastWord.get(j).getY()];
                }

                if (!allSupposedWords[index].equals(current.toLowerCase())) {
                    System.out.println("WRONG! Expected: " + allSupposedWords[index] + " Actual: " + current.toLowerCase());
                }
            }

            used.add(allSupposedWords[index]);
            player = (player + 1) % numPlayers;
        }
    }
}