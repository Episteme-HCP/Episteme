/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.puzzle;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;


/**
 * implements a very flexible tile puzzle game with various options
 *
 * @author Holger Antelmann
 *
 * @see TilePuzzleSamples
 * @see TilePuzzlePlayer
 */
public class TilePuzzle extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 986063706390238762L;

    /**
     * DOCUMENT ME!
     */
    private Object[][] puzzle;

    /**
     * DOCUMENT ME!
     */
    private Object[][] solution;

    /**
     * DOCUMENT ME!
     */
    boolean ReverseMoveDisabled;

    /**
     * DOCUMENT ME!
     */
    boolean endWhenSolved;

/**
     * uses TilePuzzleSamples.getNumberPuzzle(3) to initialize the
     * puzzle
     *
     * @see TilePuzzleSamples
     */
    public TilePuzzle() {
        this("default Tile Puzzle (number puzzle 3x3)",
            TilePuzzleSamples.getNumberPuzzle(3));
    }

/**
     * When constructed, the puzzle's current state equals its
     * solution. You can then use functions like randomize() or
     * shuffle() to actually start the game
     * Instead of building the puzzle array yourself, you can
     * take advantage of the static methods of the class
     * TilePuzzleSamples, which provides convenient standard
     * configurations; if you build a configuration yourself,
     * the convention for int[][] puzzle is as follows.
     * <li>the arrays' width should be the same as its height</li>
     * <li>the array must contain excactly one null value</li>
     * The String name is just used as an identification
     *
     * @see TilePuzzleSamples
     */
    public TilePuzzle(String name, Object[][] puzzle) {
        super(name, 1);

        int nullCount = 0;
        this.puzzle = new Object[puzzle.length][puzzle[0].length];
        solution = new Object[puzzle.length][puzzle[0].length];

        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                if (puzzle[column][row] == null) {
                    nullCount++;
                }

                solution[column][row] = puzzle[column][row];
                this.puzzle[column][row] = solution[column][row];
            }
        }

        if (nullCount != 1) {
            String s = "only one element in the puzzle is to be null";
            s += ("; the given puzzle had " + nullCount + " null elements");
            throw new IllegalArgumentException(s);
        }

        ReverseMoveDisabled = false;
        endWhenSolved = false;
    }

/**
     * If you want to set the initial state different from the solution
     * puzzle by hand, this is the constructor to do it; be careful that
     * the conventions (see TilePuzzle (String name, Object[][] puzzle))
     * for the puzzle are maintained for both given object arrays; also,
     * both arrays must have the same dimensions and should contain the
     * same objects. This constructor does not check whether the puzzle
     * is in fact solvable.
     *
     * @see TilePuzzleSamples
     */
    public TilePuzzle(String name, Object[][] scrampledPuzzle,
        Object[][] destinationPuzzle) {
        super(name, 1);

        int nullCount1 = 0;
        int nullCount2 = 0;

        if ((scrampledPuzzle.length != destinationPuzzle.length) ||
                (scrampledPuzzle[0].length != destinationPuzzle[0].length)) {
            String s = "the dimensions of both arrays are not identical";
            throw new IllegalArgumentException(s);
        }

        puzzle = new Object[scrampledPuzzle.length][scrampledPuzzle[0].length];
        solution = new Object[destinationPuzzle.length][destinationPuzzle[0].length];

        for (int column = 0; column < destinationPuzzle.length; column++) {
            for (int row = 0; row < destinationPuzzle[column].length; row++) {
                if (scrampledPuzzle[column][row] == null) {
                    nullCount1++;
                }

                if (destinationPuzzle[column][row] == null) {
                    nullCount2++;
                }

                solution[column][row] = destinationPuzzle[column][row];
                puzzle[column][row] = scrampledPuzzle[column][row];
            }
        }

        if ((nullCount1 != 1) || (nullCount2 != 1)) {
            String s = "only one element in the puzzle is to be null";
            s += ("; the given puzzles had " + nullCount1 + " and " +
            nullCount2 + " null elements");
            throw new IllegalArgumentException(s);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                puzzle[column][row] = solution[column][row];
            }
        }

        resetLists();
    }

    /**
     * checks whether the puzzle state equals the solution and whether
     * it is impossible to reach the goal - which is done by checking whether
     * there are exactly 2 tiles exchanged.
     *
     * @return DOCUMENT ME!
     *
     * @throws PuzzleNotSolvableException if the puzzle state cannot reach the
     *         solution
     */
    public boolean isSolved() throws PuzzleNotSolvableException {
        boolean check = true;
        boolean nullCheck = false;
        int count = 0;
loop: 
        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                if (puzzle[column][row] == null) {
                    if (solution[column][row] != null) {
                        check = false;
                    } else {
                        // both empty tiles at the same spot
                        nullCheck = true;
                    }
                } else {
                    if (!puzzle[column][row].equals(solution[column][row])) {
                        check = false;

                        if (solution[column][row] != null) {
                            count++;
                        }

                        if (count > 2) {
                            break loop;
                        }
                    }
                }
            }
        }

        if (nullCheck && (count == 2)) {
            String s = "the solution cannot be reached given the current puzzle state";
            throw new PuzzleNotSolvableException(this, s);
        }

        return check;
    }

    /**
     * DOCUMENT ME!
     */
    public void shuffle() {
        shuffle(System.currentTimeMillis());
    }

    /**
     * note that calling shuffle() may make the puzzle unsolvable
     *
     * @param randomSeed DOCUMENT ME!
     */
    public void shuffle(long randomSeed) {
        Random r = new Random(randomSeed);
        Object[] array = new Object[puzzle.length * puzzle[0].length];
        int n = 0;

        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                array[n++] = puzzle[column][row];
            }
        }

        Collections.shuffle(Arrays.asList(array), r);

        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                puzzle[column][row] = array[--n];
            }
        }

        resetLists();
    }

    /**
     * DOCUMENT ME!
     *
     * @param numberOfMoves DOCUMENT ME!
     */
    public void randomize(int numberOfMoves) {
        randomize(numberOfMoves, System.currentTimeMillis());
    }

    /**
     * DOCUMENT ME!
     *
     * @param numberOfMoves DOCUMENT ME!
     * @param randomSeed DOCUMENT ME!
     */
    public void randomize(int numberOfMoves, long randomSeed) {
        Random r = new Random(randomSeed);

        for (int i = 0; i < numberOfMoves; i++) {
            GameMove[] moves = getLegalMoves();

            if (moves.length == 0) {
                break;
            }

            int n = (int) (r.nextFloat() * (float) moves.length);
            pushMove(moves[n]);
            resetLegalMoves();
        }

        resetLists();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[][] getSolutionMatrix() {
        return solution;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[][] getPuzzleMatrix() {
        return puzzle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        try {
            if (isSolved()) {
                return new int[] { 0 };
            } else {
                return null;
            }
        } catch (PuzzleNotSolvableException e) {
            return new int[] { -1 };
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param playerRole DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getResult(int playerRole) {
        throw new UnsupportedOperationException();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        if (endWhenSolved) {
            try {
                if (isSolved()) {
                    return null;
                }
            } catch (PuzzleNotSolvableException e) {
                return null;
            }
        }

        Vector<TilePuzzleMove> list = new Vector<TilePuzzleMove>(4);
        int r = 0;
        int c = 0;
loop: 
        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                if (puzzle[column][row] == null) {
                    r = row;
                    c = column;

                    break loop;
                }
            }
        }

        if (c > 0) {
            list.add(new TilePuzzleMove(TilePuzzleMove.DOWN));
        }

        if (c < (puzzle.length - 1)) {
            list.add(new TilePuzzleMove(TilePuzzleMove.UP));
        }

        if (r > 0) {
            list.add(new TilePuzzleMove(TilePuzzleMove.RIGHT));
        }

        if (r < (puzzle[0].length - 1)) {
            list.add(new TilePuzzleMove(TilePuzzleMove.LEFT));
        }

        if (ReverseMoveDisabled && (getLastMove() != null)) {
            switch (((TilePuzzleMove) getLastMove()).getType()) {
            case TilePuzzleMove.DOWN:
                list.remove(new TilePuzzleMove(TilePuzzleMove.UP));

                break;

            case TilePuzzleMove.UP:
                list.remove(new TilePuzzleMove(TilePuzzleMove.DOWN));

                break;

            case TilePuzzleMove.RIGHT:
                list.remove(new TilePuzzleMove(TilePuzzleMove.LEFT));

                break;

            case TilePuzzleMove.LEFT:
                list.remove(new TilePuzzleMove(TilePuzzleMove.RIGHT));

                break;
            }
        }

        return list.toArray(new TilePuzzleMove[list.size()]);
    }

    /**
     * 
     * @see #isEndWhenSolved()
     */
    public void setEndWhenSolved(boolean enable) {
        endWhenSolved = enable;
        resetLegalMoves();
    }

    /**
     * If isEndWhenSolved(), no legal moves are possible anymore once
     * the puzzle is solved or when it has been determined that the game has
     * no solution path.
     *
     * @return DOCUMENT ME!
     */
    public boolean isEndWhenSolved() {
        return endWhenSolved;
    }

    /**
     * 
     * @see #isReverseMoveDisabled()
     */
    public void setReverseMoveDisabled(boolean enable) {
        ReverseMoveDisabled = enable;
        resetLegalMoves();
    }

    /**
     * If isReverseMoveDisabled(), reverting the game status back to
     * the state it came from (examining the last move) is not a legal move.
     *
     * @return DOCUMENT ME!
     */
    public boolean isReverseMoveDisabled() {
        return ReverseMoveDisabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        // first find the place of the empty tile and store row & column in r and c
        int r = 0;
        int c = 0;
loop: 
        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                if (puzzle[column][row] == null) {
                    r = row;
                    c = column;

                    break loop;
                }
            }
        }

        // now switch the positions
        Object tmp = null;

        switch (((TilePuzzleMove) move).getType()) {
        case TilePuzzleMove.DOWN:
            puzzle[c][r] = puzzle[c - 1][r];
            puzzle[c - 1][r] = null;

            break;

        case TilePuzzleMove.UP:
            puzzle[c][r] = puzzle[c + 1][r];
            puzzle[c + 1][r] = null;

            break;

        case TilePuzzleMove.RIGHT:
            puzzle[c][r] = puzzle[c][r - 1];
            puzzle[c][r - 1] = null;

            break;

        case TilePuzzleMove.LEFT:
            puzzle[c][r] = puzzle[c][r + 1];
            puzzle[c][r + 1] = null;

            break;

        default:
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        // simply reverse the last move performed
        switch (((TilePuzzleMove) getLastMove()).getType()) {
        case TilePuzzleMove.UP:

            if (pushMove(new TilePuzzleMove(TilePuzzleMove.DOWN))) {
                return true;
            }

            return false;

        case TilePuzzleMove.DOWN:

            if (pushMove(new TilePuzzleMove(TilePuzzleMove.UP))) {
                return true;
            }

            return false;

        case TilePuzzleMove.LEFT:

            if (pushMove(new TilePuzzleMove(TilePuzzleMove.RIGHT))) {
                return true;
            }

            return false;

        case TilePuzzleMove.RIGHT:

            if (pushMove(new TilePuzzleMove(TilePuzzleMove.LEFT))) {
                return true;
            }

            return false;

        default:
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "\n";

        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                s += "\t";

                if (puzzle[column][row] != null) {
                    s += puzzle[column][row].toString();
                } else {
                    s += "[]";
                }
            }

            s += "\n";
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        TilePuzzle newtp = (TilePuzzle) super.clone();
        //newtp.solution = new Object[solution.length][solution[0].length];
        newtp.puzzle = new Object[puzzle.length][puzzle[0].length];

        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                //newtp.solution[column][row] = solution[column][row];
                newtp.puzzle[column][row] = puzzle[column][row];
            }
        }

        return newtp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof TilePuzzle)) {
            return false;
        }

        TilePuzzle o = (TilePuzzle) obj;

        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                if (puzzle[column][row] == null) {
                    if (o.puzzle[column][row] != null) {
                        return false;
                    }
                } else if ((o.puzzle[column][row] == null) ||
                        !puzzle[column][row].equals(o.puzzle[column][row])) {
                    return false;
                }

                if (solution[column][row] == null) {
                    if (o.solution[column][row] != null) {
                        return false;
                    }
                } else if ((o.solution[column][row] == null) ||
                        !solution[column][row].equals(o.solution[column][row])) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * rather experimental at this point; needs to be tested for
     * efficiency
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int hash = 0;

        for (int column = 0; column < puzzle.length; column++) {
            for (int row = 0; row < puzzle[column].length; row++) {
                if (puzzle[column][row] != null) {
                    hash = hash +
                        (puzzle[column][row].hashCode() * column * column * row);
                }
            }
        }

        return hash;
    }
}
