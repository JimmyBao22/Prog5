package assignment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Trie implements Iterable<String> {
    class TrieIterator implements Iterator<String> {
        // track which node we are in the trie
         TrieNode currNode;
         StringBuilder currWord;
        public TrieIterator(TrieNode root) {
            currWord = new StringBuilder();
            currNode = root;
            next();
        }

        public boolean hasNext() {
            return currNode != null;
        }

        // step up recursively in the trie until we have unexplored children, and step into them until we find the next word
        private void stepBack() {
            TrieNode prevChild = currNode;
            currNode = currNode.getParent();

            // remove the last letter because we stepped back
            currWord.deleteCharAt(currWord.length() - 1);

            if (currNode == null) {
                return;
            }

            // if prevChild is the last child of currNode, stepBack
            if (currNode.getChild(currNode.numChildren() - 1).equals(prevChild)) {
                stepBack();
            } else {
                // set currNode to the next child of currNode after prevChild
                currNode = currNode.getChild(currNode.getIndexOf(prevChild) + 1);
                // add the letter of the next child to current word
                currWord.append((char) ('A' + currNode.getLetter()));

                // keep going in until we reach a word
                while (!currNode.isWord()) {
                    currNode = currNode.getChild(0);
                    currWord.append(currNode.getLetter());
                }
            }
        }

        // find the next word and return it
        public String next() {
            if (!hasNext()) {
                return null;
            }

            String ans = currWord.toString();

            // if we have children
            if (currNode.hasChildren()) {
                // go to the first child until we hit a word
                do {
                    currNode = currNode.getChild(0);
                    currWord.append(currNode.getLetter());
                } while (!currNode.isWord());
            } else {
                // otherwise stepBack
                stepBack();
            }

            return ans;
        }
    }


    private class TrieNode {
        private List<TrieNode> children;
        private TrieNode parent;
        private boolean isWord;
        private char letter;

        public TrieNode(char letter, TrieNode parent) {
            this.letter = letter;
            this.isWord = false;
            this.parent = parent;
            this.children = null;
        }

        public char getLetter() {
            return letter;
        }

        public boolean isWord() {
            return isWord;
        }

        public void setWord(boolean isWord) {
            this.isWord = isWord;
        }

        public TrieNode getParent() {
            return parent;
        }

        public TrieNode getChild(int i) {
            if (hasChildren()) {
                return children.get(i);
            } else {
                return null;
            }
        }

        // assume children are added in lexographical order
        public void addChild(TrieNode child) {
            if (!hasChildren()) {
                children = new ArrayList<TrieNode>(1);
            }

            children.add(child);
        }

        public int getIndexOf(TrieNode child) {
            if (hasChildren()) {
                return children.indexOf(child);
            } else {
                return -1;
            }
        }

        public int numChildren() {
            if (hasChildren()) {
                return children.size();
            } else {
                return 0;
            }
        }

        public boolean hasChildren() {
            return children != null;
        }
    }

    private TrieNode root;

    public Trie() {
        // TODO explain that root.letter will not be used
        root = new TrieNode(' ', null);
    }

    public boolean hasPrefix(String s) {
        return getNodeWithString(s) != null;
    }

    private TrieNode getNodeWithString(String s) {
        TrieNode currNode = this.root;
        for (int i = 0; currNode != null && i < s.length(); i++) {
            if (currNode.hasChildren()) {
                boolean foundNode = false;
                for (int j = 0; j < currNode.numChildren(); j++) {
                    if (currNode.getChild(j).getLetter() == s.charAt(i)) {
                        currNode = currNode.getChild(j);
                        foundNode = true;
                        break;
                    }
                }
                if (!foundNode) {
                    return null;
                }
            }
        }

        return currNode;
    }

    public void add(String word) {
        TrieNode currNode = this.root;
        for (int i = 0; i < word.length(); i++) {
            boolean foundNode = false;
            for (int j = 0; j < currNode.numChildren(); j++) {
                if (currNode.getChild(j).getLetter() == word.charAt(i)) {
                    currNode = currNode.getChild(j);
                    foundNode = true;
                    break;
                }
            }
            if (!foundNode) {
                currNode.addChild(new TrieNode(word.charAt(i), currNode));
                currNode = currNode.getChild(currNode.numChildren() - 1);
            }
        }

        currNode.setWord(true);
    }

    public boolean contains(String word) {
        TrieNode result = getNodeWithString(word);
        return result != null && result.isWord();
    }

    public Iterator<String> iterator() {
        return new TrieIterator(this.root);
    }
}
