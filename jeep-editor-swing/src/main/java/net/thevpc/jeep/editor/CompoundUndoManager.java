package net.thevpc.jeep.editor;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * A simple UndoManager that groups the Edits in each 0.5 second.  If the time
 * difference between the current undo and the last one is less than 0.5 secs,
 * then the two edits are compound.
 * @author Ayman Al-Sairafi
 */
public class CompoundUndoManager extends UndoManager {

    /**
     * Delay between consecutive edits in ms where edits are added together.
     * If the delay is greater than this, then separate undo operations are
     * done, otherwise they are combined.
     */
    public static final int IDLE_DELAY_MS = 500;

    long startMillis = 0;
    CompoundEdit comp = null;

    public CompoundUndoManager() {
    }

    @Override
    public synchronized boolean addEdit(UndoableEdit anEdit) {
        long now = System.currentTimeMillis();
        if (comp == null) {
            comp = new CompoundEdit();
        }
        comp.addEdit(anEdit);
        if (now - startMillis > IDLE_DELAY_MS) {
            comp.end();
            super.addEdit(comp);
            comp = null;
        }
        startMillis = now;
        return true;
    }

    @Override
    public synchronized boolean canRedo() {
        commitCompound();
        return super.canRedo();
    }

    @Override
    public synchronized boolean canUndo() {
        commitCompound();
        return super.canUndo();
    }

    @Override
    public synchronized void discardAllEdits() {
        comp = null;
        super.discardAllEdits();
    }

    @Override
    public synchronized void redo() throws CannotRedoException {
        commitCompound();
        super.redo();
    }

    @Override
    public synchronized void undo() throws CannotUndoException {
        commitCompound();
        super.undo();
    }

    private void commitCompound() {
        if (comp != null) {
            comp.end();
            super.addEdit(comp);
            comp = null;
        }
    }
}
