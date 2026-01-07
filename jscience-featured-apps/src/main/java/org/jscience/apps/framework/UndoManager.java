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
