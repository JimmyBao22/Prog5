package assignment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Trie implements Iterable<String> {
    // represents an iterator through the trie
    private class TrieIterator implements Iterator<String> {
        // track which node we are in the trie
         TrieNode currNode;
         // store the letters in the current word
         StringBuilder currWord;
        public TrieIterator(TrieNode root) {
            // start at the root with an empty string
            currWord = new StringBuilder();
            currNode = root;
            // find the first word and initialize currWord/currNode to appropriate values
            next();
        }

        // return whether we have yet to iterate over all words
        public boolean hasNext() {
            // currNode is only null if we have reached the end
            return currNode != null;
        }

        // step up recursively in the trie until we have unexplored children, and step into them until we find the next word
        private void stepBack() {
            TrieNode prevChild = currNode;
            currNode = currNode.getParent();

            // if the parent is null, we have gone above the root, so there are no more words
            if (currNode == null) {
                return;
            }

            // remove the last letter because we stepped up one level (out of prevChild)
            currWord.deleteCharAt(currWord.length() - 1);

            // if prevChild is the last child of currNode, stepBack so we can look up another level for more children
            if (currNode.getChild(currNode.numChildren() - 1).equals(prevChild)) {
                stepBack();
            } else {
                // set currNode to the next child of the parent after prevChild
                currNode = currNode.getChild(currNode.getIndexOf(prevChild) + 1);
                // add the letter of the next child to current word
                currWord.append(currNode.getLetter());

                // keep going into the first child until we reach a word
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

            // store the word of the node right now
            String ans = currWord.toString();

            // if we have children
            if (currNode.hasChildren()) {
                // go to the first child until we hit a word
                do {
                    currNode = currNode.getChild(0);
                    currWord.append(currNode.getLetter());
                } while (!currNode.isWord());
            } else {
                // otherwise stepBack, see if parent has children
                stepBack();
            }

            return ans;
        }
    }


    // represents a single node in the trie
    private class TrieNode {
        private List<TrieNode> children;
        private TrieNode parent;
        private boolean isWord;
        private char letter;

        public TrieNode(char letter, TrieNode parent) {
            this.letter = letter;
            // this must be marked through setWord
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
                // create the list if we don't have any previous children
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
        // the root node in the trie is not a word
        // its letter should never be used, so the value doesn't matter
        root = new TrieNode(' ', null);
    }

    // find the node corresponding to a sequence of characters
    private TrieNode getNodeWithString(String s) {
        TrieNode currNode = this.root;

        // iterate until we hit a dead end or reach the end of the string
        for (int i = 0; currNode != null && i < s.length(); i++) {
            if (currNode.hasChildren()) {
                boolean foundNode = false;
                // find whether a child matches the letter we are looking for
                for (int j = 0; j < currNode.numChildren(); j++) {
                    if (currNode.getChild(j).getLetter() == s.charAt(i)) {
                        currNode = currNode.getChild(j);
                        foundNode = true;
                        break;
                    }
                }

                if (!foundNode) {
                    // there is no child with the right letter, node DNE
                    return null;
                }
            }
        }

        return currNode;
    }

    public boolean hasPrefix(String s) {
        return getNodeWithString(s) != null;
    }

    // insert a word into the trie, do nothing if it already exists
    public void add(String word) {
        TrieNode currNode = this.root;

        for (int i = 0; i < word.length(); i++) {
            boolean foundNode = false;
            // check whether a node with the correct letter exists
            for (int j = 0; j < currNode.numChildren(); j++) {
                if (currNode.getChild(j).getLetter() == word.charAt(i)) {
                    // use the existing one as the next node
                    currNode = currNode.getChild(j);
                    foundNode = true;
                    break;
                }
            }

            // if it doesn't exist, create it
            if (!foundNode) {
                currNode.addChild(new TrieNode(word.charAt(i), currNode));
                currNode = currNode.getChild(currNode.numChildren() - 1);
            }
        }

        // mark that the node represents a word
        currNode.setWord(true);
    }

    // check whether the trie contains a word
    public boolean contains(String word) {
        // find the node corresponding to the sequence of characters
        TrieNode result = getNodeWithString(word);
        return result != null && result.isWord();
    }

    public Iterator<String> iterator() {
        return new TrieIterator(this.root);
    }
}
