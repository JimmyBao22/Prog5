package assignment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class SearchWordsTests {

    public static void main(String[] args) {

    }

    @RepeatedTest(10000)
    void testSearchDictConditions() throws IOException {
        GameManager g = new GameManager();

        String desiredWord = generateWord();
        int length = desiredWord.length();

        double prob = Math.random();
        if (prob < 0.33) {
            // test that it's the right word
            String guessWord = "";
            for (int i = 0; i < length; i++) {
                guessWord += desiredWord.charAt(i);
            }
            Assertions.assertEquals(0, g.searchDictConditions(guessWord, desiredWord));
        } else if (prob < 0.67) {
            // test that it's either longer or not a prefix of the current word
            String current = "";
            for (int i = 0; i < length; i++) {
                if (desiredWord.charAt(i) == 'a') {
                    current += 'b';
                } else {
                    current += 'a';
                }
                Assertions.assertEquals(1, g.searchDictConditions(current, desiredWord));
            }

            // add a few extra characters
            for (int i = 0; i < 100; i++) {
                current += 'a';
            }
            Assertions.assertEquals(1, g.searchDictConditions(current, desiredWord));
        } else {
            // test that it is a prefix
            String current = "";
            for (int i = 0; i < length - 1; i++) {
                current += desiredWord.charAt(i);
                Assertions.assertEquals(2, g.searchDictConditions(current, desiredWord));
            }
        }
    }

    @RepeatedTest(100)
    void testSearchBoardConditions() throws IOException {
        /*
        GameManager game = new GameManager();
        BoggleDictionary dictionary = new GameDictionary();
        dictionary.loadDictionary("words.txt");
        int numPlayers = 2;
        game.newGame(4, numPlayers, "cubes.txt", dictionary);

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
        for (int i = 0; i < 100; i++) {
            if (Math.random() < 0.5) {
                // add a random word
                int index = (int)(Math.random() * allSupposedWords.length);
                game.addWord(allSupposedWords[index], 0);
                used.add(allSupposedWords[index]);
            }

            // guess a random word
            int index = (int)(Math.random() * allSupposedWords.length);

            // note: tests only work when changing the set used in searchBoardConditions to the 'usedWords' set
            if (used.contains(allSupposedWords[index])) {
                Assertions.assertEquals(2, game.searchBoardConditions(allSupposedWords[index], new HashSet<>()));
            } else {
                Assertions.assertEquals(0, game.searchBoardConditions(allSupposedWords[index], new HashSet<>()));
            }

            // check if it returns 1 --> generate a random string of length 3
            String word = "";
            for (int j = 0; j < 3; j++) {
                word += (char)((int)(Math.random() * 26) + 'a');
            }

            if (dictionary.isPrefix(word)) {
                Assertions.assertEquals(2, game.searchBoardConditions(word, new HashSet<>()));
            } else {
                Assertions.assertEquals(1, game.searchBoardConditions(word, new HashSet<>()));
            }
        }
         */
    }

    @Test
    void testUpdateStack() throws IOException {
        /*
        GameManager game = new GameManager();
        BoggleDictionary dictionary = new GameDictionary();
        dictionary.loadDictionary("words.txt");
        int numPlayers = 2;
        int size = 4;
        game.newGame(size, numPlayers, "cubes.txt", dictionary);

        char[][] board = {{'E', 'E', 'C', 'A'},
                {'A', 'L', 'E', 'P'},
                {'H', 'N', 'B', 'O'},
                {'Q', 'T', 'T', 'Y'}};

        game.setGame(board);

        String word = "";
        List<Point> points = new ArrayList<>();
        Stack<GameManager.WordPoints> stack = new Stack<>();
        boolean[][] visited = new boolean[size][size];

        // element 0, 0
        game.updateStack(stack, word, points, 0, 0, visited);

        Assertions.assertEquals(3, stack.size());

        GameManager.WordPoints current = stack.pop();
        Assertions.assertEquals("l", current.getWord());
        Assertions.assertEquals(1, current.getPoints().get(0).getX());
        Assertions.assertEquals(1, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("e", current.getWord());
        Assertions.assertEquals(0, current.getPoints().get(0).getX());
        Assertions.assertEquals(1, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("a", current.getWord());
        Assertions.assertEquals(1, current.getPoints().get(0).getX());
        Assertions.assertEquals(0, current.getPoints().get(0).getY());


        // element 2,3 - 'O'
        game.updateStack(stack, word, points, 2, 3, visited);

        Assertions.assertEquals(5, stack.size());

        current = stack.pop();
        Assertions.assertEquals("t", current.getWord());
        Assertions.assertEquals(3, current.getPoints().get(0).getX());
        Assertions.assertEquals(2, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("e", current.getWord());
        Assertions.assertEquals(1, current.getPoints().get(0).getX());
        Assertions.assertEquals(2, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("b", current.getWord());
        Assertions.assertEquals(2, current.getPoints().get(0).getX());
        Assertions.assertEquals(2, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("p", current.getWord());
        Assertions.assertEquals(1, current.getPoints().get(0).getX());
        Assertions.assertEquals(3, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("y", current.getWord());
        Assertions.assertEquals(3, current.getPoints().get(0).getX());
        Assertions.assertEquals(3, current.getPoints().get(0).getY());


        // element 1,1 - 'L'
        game.updateStack(stack, word, points, 1, 1, visited);

        Assertions.assertEquals(8, stack.size());

        current = stack.pop();
        Assertions.assertEquals("c", current.getWord());
        Assertions.assertEquals(0, current.getPoints().get(0).getX());
        Assertions.assertEquals(2, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("h", current.getWord());
        Assertions.assertEquals(2, current.getPoints().get(0).getX());
        Assertions.assertEquals(0, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("e", current.getWord());
        Assertions.assertEquals(0, current.getPoints().get(0).getX());
        Assertions.assertEquals(0, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("b", current.getWord());
        Assertions.assertEquals(2, current.getPoints().get(0).getX());
        Assertions.assertEquals(2, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("a", current.getWord());
        Assertions.assertEquals(1, current.getPoints().get(0).getX());
        Assertions.assertEquals(0, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("e", current.getWord());
        Assertions.assertEquals(1, current.getPoints().get(0).getX());
        Assertions.assertEquals(2, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("e", current.getWord());
        Assertions.assertEquals(0, current.getPoints().get(0).getX());
        Assertions.assertEquals(1, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("n", current.getWord());
        Assertions.assertEquals(2, current.getPoints().get(0).getX());
        Assertions.assertEquals(1, current.getPoints().get(0).getY());


        // element 3,1 - 'T'
        game.updateStack(stack, word, points, 3, 1, visited);

        Assertions.assertEquals(5, stack.size());

        current = stack.pop();
        Assertions.assertEquals("b", current.getWord());
        Assertions.assertEquals(2, current.getPoints().get(0).getX());
        Assertions.assertEquals(2, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("h", current.getWord());
        Assertions.assertEquals(2, current.getPoints().get(0).getX());
        Assertions.assertEquals(0, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("q", current.getWord());
        Assertions.assertEquals(3, current.getPoints().get(0).getX());
        Assertions.assertEquals(0, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("t", current.getWord());
        Assertions.assertEquals(3, current.getPoints().get(0).getX());
        Assertions.assertEquals(2, current.getPoints().get(0).getY());

        current = stack.pop();
        Assertions.assertEquals("n", current.getWord());
        Assertions.assertEquals(2, current.getPoints().get(0).getX());
        Assertions.assertEquals(1, current.getPoints().get(0).getY());

         */
    }

    @RepeatedTest(100)
    void testAddToStack() {
        /*
        GameManager g = new GameManager();
        Stack<GameManager.WordPoints> stack = new Stack<>();
        List<Point> list = new ArrayList<>();
        int size = 4;
        boolean[][] visited = new boolean[size][size];
//        int[][] oldPoints = new int[(int)(Math.random() * 100)][2];
//        for (int i = 0; i < oldPoints.length; i++) {
//            oldPoints[i][0] = (int)(Math.random() * 100);
//            oldPoints[i][1] = (int)(Math.random() * 100);
//            list.add(new Point(oldPoints[i][0], oldPoints[i][1]));
//        }
        int[][] newPoints = new int[(int)(Math.random() * 100)][2];
        for (int i = 0; i < newPoints.length; i++) {
            newPoints[i][0] = (int)(Math.random() * 100);
            newPoints[i][1] = (int)(Math.random() * 100);
        }

//        for (int i = 0; i < size; i++) {
//            for (int j = 0; j < size; j++) {
//                if (Math.random() < 0.5) visited[i][j] = true;
//            }
//        }

        String word = "";

        for (int i = 0; i < newPoints.length; i++) {
            // add something random to word
            word += (char)((int)(Math.random() * 26) + 'a');
            g.addToStack(stack, list, newPoints[i][0], newPoints[i][1], word, visited);
            list.add(new Point(newPoints[i][0], newPoints[i][1]));
        }

        Assertions.assertEquals(newPoints.length, stack.size());

        for (int i = 0; i < newPoints.length; i++) {
            GameManager.WordPoints wordPoints = stack.pop();
            String actualWord = wordPoints.getWord();
            Assertions.assertEquals(word.substring(0, actualWord.length()), actualWord);
            Assertions.assertEquals(newPoints.length - i, wordPoints.getPoints().size());

            for (int j = 0; j < wordPoints.getPoints().size(); j++) {
                Assertions.assertEquals(newPoints[j][0], wordPoints.getPoints().get(j).getX());
                Assertions.assertEquals(newPoints[j][1], wordPoints.getPoints().get(j).getY());
            }
        }
         */
    }

    @RepeatedTest(100)
    void testWordPointsClass() {
        /*
        String word = generateWord();

        List<Point> list = new ArrayList<Point>();
        int listSize = (int)(Math.random() * 100);
        for (int i = 0; i < listSize; i++) {
            list.add(new Point((int)(Math.random() * 100), (int)(Math.random() * 100)));
        }

        boolean[][] visited = new boolean[100][100];
        boolean[][] copy = new boolean[100][100];
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                if (Math.random() < 0.5) {
                    visited[i][j] = true;
                }
                copy[i][j] = visited[i][j];
            }
        }

        GameManager.WordPoints wordPoints= new GameManager.WordPoints(word, list, visited);

        Assertions.assertEquals(word, wordPoints.getWord());

        for (int i = 0; i < listSize; i++) {
            Assertions.assertEquals(list.get(i).getX(), wordPoints.getPoints().get(i).getX());
            Assertions.assertEquals(list.get(i).getY(), wordPoints.getPoints().get(i).getY());
        }

        // change visited array
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                if (Math.random() < 0.5) {
                    visited[i][j] = !visited[i][j];
                }
            }
        }

        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                if (Math.random() < 0.5) {
                    Assertions.assertEquals(copy[i][j], wordPoints.getVisited()[i][j]);
                }
            }
        }
         */
    }

    String generateWord() {
        int length = (int)(Math.random() * 1000);
        String word = "";
        for (int i = 0; i < length; i++) {
            word += (char)((int)(Math.random() * 26) + 'a');
        }
        return word;
    }
}
