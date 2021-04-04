package net.thevpc.jeep.editor;

import net.thevpc.jeep.JContext;
import net.thevpc.jeep.JToken;
import net.thevpc.jeep.core.tokens.BlocCommentsPattern;

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.undo.UndoManager;

/**
 * A document that supports being highlighted. The document maintains an
 * internal List of all the Tokens. The Tokens are updated using a Lexer, passed
 * to it during construction.
 *
 * @author Ayman Al-Sairafi
 */
public class JSyntaxDocument extends PlainDocument {

    // our logger instance...
    private static final Logger LOG = Logger.getLogger(JSyntaxDocument.class.getName());

    JContext jcontext;
    List<JToken> tokens;
    UndoManager undo = new CompoundUndoManager();

    public JSyntaxDocument(JContext jcontext) {
        super();
        putProperty(PlainDocument.tabSizeAttribute, 4);
        this.jcontext = jcontext;
        // Listen for undo and redo events
        addUndoableEditListener(new UndoableEditListener() {

            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
                if (evt.getEdit().isSignificant()) {
                    undo.addEdit(evt.getEdit());
                }
            }
        });
    }

    public UndoManager getUndoManager() {
        return undo;
    }

    /**
     * Parse the entire document and return list of tokens that do not already
     * exist in the tokens list. There may be overlaps, and replacements, which
     * we will cleanup later.
     *
     * @return list of tokens that do not exist in the tokens field
     */
    private void parse() {
        // if we have no lexer, then we must have no tokens...
        if (jcontext == null) {
            tokens = null;
            return;
        }
        List<JToken> toks = new ArrayList<JToken>(getLength() / 5);
        long ts = System.nanoTime();
        int len = getLength();
        try {
            Segment seg = new Segment();
            getText(0, getLength(), seg);
            CharArrayReader reader = new CharArrayReader(seg.array, seg.offset, seg.count);
            for (JToken token : jcontext.newContext().tokens().of(reader)) {
                toks.add(token);
            }
        } catch (Exception ex) {
            // This will not be thrown from the Lexer
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest(String.format("Parsed %d in %d ms, giving %d tokens\n",
                        len, (System.nanoTime() - ts) / 1000000, toks.size()));
            }
            tokens = toks;
        }
    }

    @Override
    protected void fireChangedUpdate(DocumentEvent e) {
        parse();
        super.fireChangedUpdate(e);
    }

    @Override
    protected void fireInsertUpdate(DocumentEvent e) {
        parse();
        super.fireInsertUpdate(e);
    }

    @Override
    protected void fireRemoveUpdate(DocumentEvent e) {
        parse();
        super.fireRemoveUpdate(e);
    }

    @Override
    protected void fireUndoableEditUpdate(UndoableEditEvent e) {
        parse();
        super.fireUndoableEditUpdate(e);
    }

    /**
     * Replace the token with the replacement string
     *
     * @param token
     * @param replacement
     */
    public void replaceToken(JToken token, String replacement) {
        try {
            replace(token.startCharacterNumber, token.endCharacterNumber - token.startCharacterNumber, replacement, null);
        } catch (BadLocationException ex) {
            LOG.log(Level.WARNING, "unable to replace token: " + token, ex);
        }
    }

    /**
     * This class is used to iterate over tokens between two positions
     *
     */
    class TokenIterator implements ListIterator<JToken> {

        int start;
        int end;
        int ndx = 0;

        @SuppressWarnings("unchecked")
        private TokenIterator(int start, int end) {
            this.start = start;
            this.end = end;
            if (tokens != null && !tokens.isEmpty()) {
                JToken token = new JToken();
                token.startCharacterNumber = start;
                token.endCharacterNumber = end;
                token.startLineNumber = 0;
                token.endLineNumber = 0;
                token.startColumnNumber = start;
                token.endColumnNumber = end;
                token.def = BlocCommentsPattern.DEFAULT;
                ndx = Collections.binarySearch((List) tokens, token);
                // we will probably not find the exact token...
                if (ndx < 0) {
                    // so, start from one before the token where we should be...
                    // -1 to get the location, and another -1 to go back..
                    ndx = (-ndx - 1 - 1 < 0) ? 0 : (-ndx - 1 - 1);
                    JToken t = tokens.get(ndx);
                    // if the prev token does not overlap, then advance one
                    if (t.endCharacterNumber <= start) {
                        ndx++;
                    }

                }
            }
        }

        @Override
        public boolean hasNext() {
            if (tokens == null) {
                return false;
            }
            if (ndx >= tokens.size()) {
                return false;
            }
            JToken t = tokens.get(ndx);
            if (t.startCharacterNumber >= end) {
                return false;
            }
            return true;
        }

        @Override
        public JToken next() {
            return tokens.get(ndx++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasPrevious() {
            if (tokens == null) {
                return false;
            }
            if (ndx <= 0) {
                return false;
            }
            JToken t = tokens.get(ndx);
            if (t.endCharacterNumber <= start) {
                return false;
            }
            return true;
        }

        @Override
        public JToken previous() {
            return tokens.get(ndx--);
        }

        @Override
        public int nextIndex() {
            return ndx + 1;
        }

        @Override
        public int previousIndex() {
            return ndx - 1;
        }

        @Override
        public void set(JToken e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(JToken e) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Return an iterator of tokens between p0 and p1.
     *
     * @param start start position for getting tokens
     * @param end position for last token
     * @return Iterator for tokens that overal with range from start to end
     */
    public Iterator<JToken> getTokens(int start, int end) {
        return new TokenIterator(start, end);
    }

    /**
     * Find the token at a given position. May return null if no token is found
     * (whitespace skipped) or if the position is out of range:
     *
     * @param pos
     * @return
     */
    public JToken getTokenAt(int pos) {
        if (tokens == null || tokens.isEmpty() || pos > getLength()) {
            return null;
        }
        JToken tok = null;
        JToken tKey = new JToken();
        tKey.startCharacterNumber = 0;
        tKey.endCharacterNumber = 1;
        tKey.startLineNumber = 0;
        tKey.endLineNumber = 0;
        tKey.startColumnNumber = 0;
        tKey.endColumnNumber = 1;
        tKey.def = BlocCommentsPattern.DEFAULT;

        @SuppressWarnings("unchecked")
        int ndx = Collections.binarySearch((List) tokens, tKey);
        if (ndx < 0) {
            // so, start from one before the token where we should be...
            // -1 to get the location, and another -1 to go back..
            ndx = (-ndx - 1 - 1 < 0) ? 0 : (-ndx - 1 - 1);
            JToken t = tokens.get(ndx);
            if ((t.startCharacterNumber <= pos) && (pos <= t.endCharacterNumber)) {
                tok = t;
            }
        } else {
            tok = tokens.get(ndx);
        }
        return tok;
    }

    /**
     * This is used to return the other part of a paired token in the document.
     * A paired part has token.pairValue &lt;&gt; 0, and the paired token will
     * have the negative of t.pairValue. This method properly handles nesting of
     * same pairValues, but overlaps are not checked. if The document does not
     * contain a paired
     *
     * @param t
     * @return the other pair's token, or null if nothing is found.
     */
    public JToken getPairFor(JToken t) {
//        if(true){
        return null;
//        }
//        if (t == null || t.pairValue == 0) {
//            return null;
//        }
//        Token p = null;
//        int index = tokens.indexOf(t);
//        // w will be similar to a stack. The openners weght is added to it
//        // and the closers are subtracted from it (closers are already negative)
//        int w = t.pairValue;
//        int direction = (t.pairValue > 0) ? 1 : -1;
//        boolean done = false;
//        int v = Math.abs(t.pairValue);
//        while (!done) {
//            index += direction;
//            if (index < 0 || index >= tokens.size()) {
//                break;
//            }
//            Token current = tokens.get(index);
//            if (Math.abs(current.pairValue) == v) {
//                w += current.pairValue;
//                if (w == 0) {
//                    p = current;
//                    done = true;
//                }
//            }
//        }
//
//        return p;
    }

    /**
     * Perform an undo action, if possible
     */
    public void doUndo() {
        if (undo.canUndo()) {
            undo.undo();
            parse();
        }
    }

    /**
     * Perform a redo action, if possible.
     */
    public void doRedo() {
        if (undo.canRedo()) {
            undo.redo();
            parse();
        }
    }

    /**
     * Helper method to get the length of an element and avoid getting a too
     * long element at the end of the document
     *
     * @param e
     * @return
     */
    private int getElementLength(Element e) {
        int end = e.getEndOffset();
        if (end >= (getLength() - 1)) {
            end--;
        }
        return end - e.getStartOffset();
    }

//    /**
//     * Gets the text without the comments. For example for the string
//     * <code>{ // it's a comment</code> this method will return "{ ".
//     * @param aStart start of the text.
//     * @param anEnd end of the text.
//     * @return String for the line without comments (if exists).
//     */
//    public synchronized String getUncommentedText(int aStart, int anEnd) {
//        readLock();
//        StringBuilder result = new StringBuilder();
//        Iterator<JToken> iter = getTokens(aStart, anEnd);
//        while (iter.hasNext()) {
//            JToken t = iter.next();
//            if (JToken.TT_BLOCK_COMMENTS != t.ttype
//                    && JToken.TT_LINE_COMMENTS != t.ttype) {
//                result.append(t.image);
//            }
//        }
//        readUnlock();
//        return result.toString();
//    }
    /**
     * Returns the starting position of the line at pos
     *
     * @param pos
     * @return starting position of the line
     */
    public int getLineStartOffset(int pos) {
        return getParagraphElement(pos).getStartOffset();
    }

    /**
     * Returns the end position of the line at pos. Does a bounds check to
     * ensure the returned value does not exceed document length
     *
     * @param pos
     * @return
     */
    public int getLineEndOffset(int pos) {
        int end = 0;
        end = getParagraphElement(pos).getEndOffset();
        if (end >= getLength()) {
            end = getLength();
        }
        return end;
    }

    /**
     * Return the number of lines in this document
     *
     * @return
     */
    public int getLineCount() {
        Element e = getDefaultRootElement();
        int cnt = e.getElementCount();
        return cnt;
    }

    /**
     * Return the line number at given position. The line numbers are zero based
     *
     * @param pos
     * @return
     */
    public int getLineNumberAt(int pos) {
        int lineNr = getDefaultRootElement().getElementIndex(pos);
        return lineNr;
    }

    /**
     * This will discard all undoable edits
     */
    public void clearUndos() {
        undo.discardAllEdits();
    }

    @Override
    public String toString() {
        return "JSyntaxDocument(" + ((tokens == null) ? 0 : tokens.size()) + " tokens)@"
                + hashCode();
    }

}
