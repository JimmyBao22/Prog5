# Word-Hunt

A word game played with random letters arranged in a square grid (aka Boggle).

The object of the game is to identify—in this grid of letters—words that satisfy the following conditions:
- The word must be at least four letters long.
- The path formed by the sequence of letters in the word must be connected horizontally, vertically, or diagonally.
- For a given word, each cube may only be used once.

The game will randomly set up a board. The game will then allow a human to identify a
list of words in the board, which the program will verify against some dictionary and for which the program will
compute a score. When the human can think of no more words, the program will create a list of legal words that the
human did not identify. 

This game also allows for multiplyer. Points are scored based on the length of the word. Four-letter words are worth 1 point, five-letter words are worth
2 points, etc
