/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.apps.framework;

import java.util.Stack;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Manages Undo/Redo stack for Featured Apps.
 */
public class UndoManager {

    public interface Action {
        void undo();

        void redo();

        String getName();
    }

    private final Stack<Action> undoStack = new Stack<>();
    private final Stack<Action> redoStack = new Stack<>();
    private final int limit = 100;

    private final BooleanProperty canUndo = new SimpleBooleanProperty(false);
    private final BooleanProperty canRedo = new SimpleBooleanProperty(false);

    public void push(Action action) {
        undoStack.push(action);
        if (undoStack.size() > limit) {
            undoStack.remove(0);
        }
        redoStack.clear();
        updateProperties();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Action action = undoStack.pop();
            action.undo();
            redoStack.push(action);
            updateProperties();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Action action = redoStack.pop();
            action.redo();
            undoStack.push(action);
            updateProperties();
        }
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
        updateProperties();
    }

    private void updateProperties() {
        canUndo.set(!undoStack.isEmpty());
        canRedo.set(!redoStack.isEmpty());
    }

    public BooleanProperty canUndoProperty() {
        return canUndo;
    }

    public BooleanProperty canRedoProperty() {
        return canRedo;
    }
}
