package assignment;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestAddingWordJar {

    public static void main(String[] args) throws IOException {
        BoggleDictionary dictionary = new GameDictionary();
        dictionary.loadDictionary("/Users/jimmybao/CS/CS314H/prog5 testing/words.txt");
        BoggleGame game;
        for (int k = 0; k < 100; k++) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int numPlayers = 2;
            game = new GameManager();
            game.newGame(4, numPlayers, "/Users/jimmybao/CS/CS314H/prog5 testing/cubes.txt", dictionary);

            char[][] board = {{'E', 'E', 'C', 'A'},
                    {'A', 'L', 'E', 'P'},
                    {'H', 'N', 'B', 'O'},
                    {'Q', 'T', 'T', 'Y'}};

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
                int index = (int) (Math.random() * allSupposedWords.length);
                game.addWord(allSupposedWords[index], player);
                // System.out.println(allSupposedWords[index]);
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
                        current += board[(int) lastWord.get(j).getX()][(int) lastWord.get(j).getY()];
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
}