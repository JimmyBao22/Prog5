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

    void testUpdateStack() throws IOException {
        BoggleGame game = new GameManager();
        BoggleDictionary dictionary = new GameDictionary();
        dictionary.loadDictionary("words.txt");
        int numPlayers = 2;
        game.newGame(4, numPlayers, "cubes.txt", dictionary);
    }

    @RepeatedTest(100)
    void testAddToStack() {
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
