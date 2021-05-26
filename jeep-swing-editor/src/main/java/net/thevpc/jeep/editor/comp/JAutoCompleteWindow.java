package net.thevpc.jeep.editor.comp;

import net.thevpc.jeep.JCompletion;
import net.thevpc.jeep.JCompletionProposal;
import net.thevpc.jeep.core.editor.DefaultJCompletionProposal;
import net.thevpc.jeep.core.tokens.JTokensStringBuilder;
import net.thevpc.jeep.editor.util.FastListModel;
import net.thevpc.jeep.impl.tokens.JTokensStringFormat;
import net.thevpc.jeep.impl.tokens.JTokensStringFormatHtml;
import net.thevpc.jeep.util.Chronometer;
import net.thevpc.jeep.editor.JSyntaxKit;
import net.thevpc.jeep.editor.JSyntaxUtils;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class JAutoCompleteWindow extends JWindow implements CaretListener,
        ListSelectionListener, MouseListener {
    private static final int VERTICAL_SPACE = 1;
    private JTextComponent editor;
    private JList<JCompletionProposal> list;
    private JCompletionProposal lastSelection;
    private int lastLine = -1;
    private List<ReplaceableAction> actions = new ArrayList<>();
    private boolean aboveCaret;
    private int completeLevel=0;
    private JCompletion jCompletion;
    private JLabel footer=new JLabel();
    private JTokensStringFormat formatHtml=new JTokensStringFormatHtml();

    public JAutoCompleteWindow(JTextComponent editor, JCompletion jCompletion) {
        this.editor = editor;
        if(editor instanceof JEditorPane){
            JEditorPane editorPane = (JEditorPane) editor;
            if(editorPane.getEditorKit() instanceof JSyntaxKit) {
                JSyntaxKit s = (JSyntaxKit) editorPane.getEditorKit();
                if(s.getStyles().getTokensFormat()!=null) {
                    formatHtml = s.getStyles().getTokensFormat();
                }
            }
        }
        this.jCompletion = jCompletion;
        actions.add(new LeftAction());
        actions.add(new RightAction());
        actions.add(new UpAction());
        actions.add(new DownAction());
        actions.add(new PageUpAction());
        actions.add(new PageDownAction());
        actions.add(new HomeAction());
        actions.add(new EndAction());
        actions.add(new EscapeAction());
        actions.add(new CopyAction());
        actions.add(new EnterAction());
        actions.add(new TabAction());
        installKeyBindings(new ShowCompletionAction());
        JScrollPane jsp = new JScrollPane(list = new JList<>(new FastListModel<JCompletionProposal>()));
        list.setFont(editor.getFont());
        footer.setFont(editor.getFont().deriveFont(8));
        list.addListSelectionListener(this);
        list.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JCompletionProposal p=(JCompletionProposal) value;
                String svalue=formatHtml.formatDocument(p.getLhsHtml());
                return super.getListCellRendererComponent(list, svalue, index, isSelected, cellHasFocus);
            }
        });
        JPanel panel=new JPanel(new BorderLayout());
        panel.add(jsp,BorderLayout.CENTER);
        panel.add(footer,BorderLayout.SOUTH);
        add(panel);
        editor.addCaretListener(this);
    }

    public static JAutoCompleteWindow installEditor(JEditorPane editor, JCompletion jCompletionSupplier) {
        return new JAutoCompleteWindow(editor, jCompletionSupplier);
    }


    /**
     * Called when a new item is selected in the popup list.
     *
     * @param e The event.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
//            Completion value = list.getSelectedValue();
//            if (value!=null && descWindow!=null) {
//                descWindow.setDescriptionFor(value);
//                positionDescWindow();
//            }
        }
    }


    public void showPopup() {
        pack();
        setVisible(true);
    }

    public void setCompletionProposals(List<JCompletionProposal> completionProposals) {
        FastListModel<JCompletionProposal> model = getListModel();
        model.clear();
        Chronometer ch=Chronometer.start("setCompletionProposals");
        model.addAll(completionProposals);
        if(completionProposals.isEmpty()){
            model.add(new DefaultJCompletionProposal(
                    -1,
                    -10,
                    null,
                    "No Suggestions.",
                    "No Suggestions.",
                    new JTokensStringBuilder().addToken(0,0,0,"No Suggestions."),
                    null,
                    null
            ));
        }
        footer.setText(completionProposals.size()+" possibilities.");
//        System.out.println(ch);
    }

    protected FastListModel<JCompletionProposal> getListModel() {
        return (FastListModel<JCompletionProposal>) list.getModel();
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (isVisible()) { // Should always be true
            int line = getLineAtCaret();
            if (line != lastLine) {
                lastLine = -1;
                setVisible(false);
            } else {
                doAutocomplete();
            }
        }
    }

    protected int getLineAtCaret() {
        Document doc = editor.getDocument();
        Element root = doc.getDefaultRootElement();
        return root.getElementIndex(editor.getCaretPosition());
    }

    private void doAutocomplete() {
        if (isVisible()) {
            setVisible(false);
            completeLevel=1;
        }else{
            completeLevel=0;
        }

        jCompletion.setCompilationUnit(editor.getText(), JSyntaxUtils.getSourceName(editor));
        Rectangle r;
        try {
            r = editor.modelToView(editor.getCaretPosition());
        } catch (BadLocationException ble) {
            ble.printStackTrace();
            return;
        }
        List<JCompletionProposal> proposals = jCompletion.findProposals(editor.getCaretPosition(), completeLevel);
        setCompletionProposals(proposals);
        if(getListModel().size()>0) {
            Point p = new Point(r.x, r.y);
            SwingUtilities.convertPointToScreen(p, editor);
            r.x = p.x;
            r.y = p.y;
            setLocationRelativeTo(r);
            showPopup();
        }
    }

    /**
     * Sets the location of this window to be "good" relative to the specified
     * rectangle.  That rectangle should be the location of the text
     * component's caret, in screen coordinates.
     *
     * @param r The text component's caret position, in screen coordinates.
     */
    public void setLocationRelativeTo(Rectangle r) {

        // Multi-monitor support - make sure the completion window (and
        // description window, if applicable) both fit in the same window in
        // a multi-monitor environment.  To do this, we decide which monitor
        // the rectangle "r" is in, and use that one (just pick top-left corner
        // as the defining point).
        Rectangle screenBounds = JSyntaxUtils.getScreenBoundsForPoint(r.x, r.y);
        //Dimension screenSize = getToolkit().getScreenSize();

        boolean showDescWindow = false;//descWindow!=null && ac.getShowDescWindow();
        int totalH = getHeight();
        if (showDescWindow) {
            totalH = Math.max(totalH, 0/*descWindow.getHeight()*/);
        }

        // Try putting our stuff "below" the caret first.  We assume that the
        // entire height of our stuff fits on the screen one way or the other.
        aboveCaret = false;
        int y = r.y + r.height + VERTICAL_SPACE;
        if (y + totalH > screenBounds.height) {
            y = r.y - VERTICAL_SPACE - getHeight();
            aboveCaret = true;
        }

        // Get x-coordinate of completions.  Try to align left edge with the
        // caret first.
        int x = r.x;
        if (!editor.getComponentOrientation().isLeftToRight()) {
            x -= getWidth(); // RTL => align right edge
        }
        if (x < screenBounds.x) {
            x = screenBounds.x;
        } else if (x + getWidth() > screenBounds.x + screenBounds.width) { // completions don't fit
            x = screenBounds.x + screenBounds.width - getWidth();
        }

        setLocation(x, y);

        // Position the description window, if necessary.
        if (showDescWindow) {
            //positionDescWindow();
        }

    }

    public JCompletionProposal getSelection() {
        return isShowing() ? list.getSelectedValue() : lastSelection;
    }

    private void installKeyBindings() {
        for (ReplaceableAction action : actions) {
            installKeyBindings(action);
        }
    }

    private void installKeyBindings(ReplaceableAction action) {
        if (!action.installed) {
//            System.out.println("install " + action.keyEvent);
            action.installed = true;
            InputMap im = editor.getInputMap();
            ActionMap am = editor.getActionMap();
            KeyStroke ks = KeyStroke.getKeyStroke(action.keyEvent);
            action.oldKey = im.get(ks);
            im.put(ks, action.key);
            action.oldAction = am.get(action.key);
            am.put(action.key, action);
        }
    }

    private void uninstallKeyBindings(ReplaceableAction action) {
        InputMap im = editor.getInputMap();
        ActionMap am = editor.getActionMap();
        if (action.installed) {
//            System.out.println("uninstall " + action.keyEvent);
            KeyStroke ks = KeyStroke.getKeyStroke(action.keyEvent);
            am.put(im.get(ks), action.oldAction); // Original action for the "new" key
            im.put(ks, action.oldKey); // Original key for the keystroke.
            action.installed = false;
            action.oldKey = null;
            action.oldAction = null;
        }
    }

    private void uninstallKeyBindings() {
        for (ReplaceableAction action : actions) {
            uninstallKeyBindings(action);
        }
    }

    private void selectFirstItem() {
        if (getListModel().getSize() > 0) {
            list.setSelectedIndex(0);
            list.ensureIndexIsVisible(0);
        }
    }

    @Override
    public void setVisible(boolean visible) {

        if (visible != isVisible()) {

            if (visible) {
                installKeyBindings();
                lastLine = getLineAtCaret();
                selectFirstItem();
//                if (descWindow==null && ac.getShowDescWindow()) {
//                    descWindow = createDescriptionWindow();
//                    positionDescWindow();
//                }
                // descWindow needs a kick-start the first time it's displayed.
                // Also, the newly-selected item in the choices list is
                // probably different from the previous one anyway.
//                if (descWindow!=null) {
//                    Completion c = list.getSelectedValue();
//                    if (c!=null) {
//                        descWindow.setDescriptionFor(c);
//                    }
//                }
            } else {
                uninstallKeyBindings();
            }

            super.setVisible(visible);

            // Some languages, such as Java, can use quite a lot of memory
            // when displaying hundreds of completion choices.  We pro-actively
            // clear our list model here to make them available for GC.
            // Otherwise, they stick around, and consider the following:  a
            // user starts code-completion for Java 5 SDK classes, then hides
            // the dialog, then changes the "class path" to use a Java 6 SDK
            // instead.  On pressing Ctrl+space, a new array of Completions is
            // created.  If this window holds on to the previous Completions,
            // you're getting roughly 2x the necessary Completions in memory
            // until the Completions are actually passed to this window.
            if (!visible) { // Do after super.setVisible(false)
                lastSelection = list.getSelectedValue();
                getListModel().clear();
            }

            // Must set descWindow's visibility one way or the other each time,
            // because of the way child JWindows' visibility is handled - in
            // some ways it's dependent on the parent, in other ways it's not.
//            if (descWindow!=null) {
//                descWindow.setVisible(visible && ac.getShowDescWindow());
//            }

        }

    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            insertSelectedCompletion();
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {

    }

    private void insertSelectedCompletion() {

    }

    private void selectPageDownItem() {
        int visibleRowCount = list.getVisibleRowCount();
        int i = Math.min(list.getModel().getSize()-1,
                list.getSelectedIndex()+visibleRowCount);
        list.setSelectedIndex(i);
        list.ensureIndexIsVisible(i);
    }


    /**
     * Selects the completion item one "page up" from the currently selected
     * one.
     *
     * @see #selectPageDownItem()
     */
    private void selectPageUpItem() {
        int visibleRowCount = list.getVisibleRowCount();
        int i = Math.max(0, list.getSelectedIndex()-visibleRowCount);
        list.setSelectedIndex(i);
        list.ensureIndexIsVisible(i);
    }

    /**
     * Selects the last item in the completion list.
     *
     * @see #selectFirstItem()
     */
    private void selectLastItem() {
        int index = getListModel().getSize() - 1;
        if (index > -1) {
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
    }


    /**
     * Selects the next item in the completion list.
     *
     * @see #selectPreviousItem()
     */
    private void selectNextItem() {
        int index = list.getSelectedIndex();
        if (index > -1) {
            index = (index + 1) % getListModel().getSize();
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
    }



    /**
     * Selects the previous item in the completion list.
     *
     * @see #selectNextItem()
     */
    private void selectPreviousItem() {
        int index = list.getSelectedIndex();
        switch (index) {
            case 0:
                index = list.getModel().getSize() - 1;
                break;
            case -1: // Check for an empty list (would be an error)
                index = list.getModel().getSize() - 1;
                if (index == -1) {
                    return;
                }
                break;
            default:
                index = index - 1;
                break;
        }
        list.setSelectedIndex(index);
        list.ensureIndexIsVisible(index);
    }

    //////////////////////////////////////////////

    abstract class ReplaceableAction extends AbstractAction {
        boolean installed;
        private String keyEvent;
        private Object key;
        private Object oldKey;
        private Action oldAction;

        public ReplaceableAction(String keyEvent, Object key) {
            this.keyEvent = keyEvent;
            this.key = key;
        }
    }

    class ShowCompletionAction extends ReplaceableAction {
        public ShowCompletionAction() {
            super("control SPACE", "control SPACE");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doAutocomplete();
        }
    }

    class UpAction extends ReplaceableAction {
        public UpAction() {
            super("UP", "Up");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectPreviousItem();
            }
        }

    }

    class LeftAction extends ReplaceableAction {
        public LeftAction() {
            super("LEFT", "Left");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                JTextComponent comp = editor;
                Caret c = comp.getCaret();
                int dot = c.getDot();
                if (dot > 0) {
                    c.setDot(--dot);
                    // Ensure moving left hasn't moved us up a line, thus
                    // hiding the popup window.
                    if (comp.isVisible()) {
//                        if (lastLine!=-1) {
//                            doAutocomplete();
//                        }
                    }
                }
            }
        }

    }

    /**
     * Moves the caret to the right and updates the completion list accordingly.
     */
    class RightAction extends ReplaceableAction {
        public RightAction() {
            super("RIGHT", "Right");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                JTextComponent comp = editor;
                Caret c = comp.getCaret();
                int dot = c.getDot();
                if (dot < comp.getDocument().getLength()) {
                    c.setDot(++dot);
                    // Ensure moving right hasn't moved us up a line, thus
                    // hiding the popup window.
                    if (comp.isVisible()) {
                        if (lastLine != -1) {
                            doAutocomplete();
                        }
                    }
                }
            }
        }

    }

    /**
     * Moves down one page in the completion list.
     */
    class PageDownAction extends ReplaceableAction {
        public PageDownAction() {
            super("PAGE_DOWN", "PageDown");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectPageDownItem();
            }
        }

    }


    /**
     * Moves up one page in the completion list.
     */
    class PageUpAction extends ReplaceableAction {
        public PageUpAction() {
            super("PAGE_UP", "PageUp");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectPageUpItem();
            }
        }

    }


    /**
     * Hides this popup window.
     */
    class EscapeAction extends ReplaceableAction {
        public EscapeAction() {
            super("ESCAPE", "Escape");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                setVisible(false);
            }
        }

    }

    /**
     * Selects the first item.
     */
    class HomeAction extends ReplaceableAction {
        public HomeAction() {
            super("HOME", "Home");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectFirstItem();
            }
        }

    }


    /**
     * Inserts the selected completion.
     */
    class EnterAction extends ReplaceableAction {
        public EnterAction() {
            super("ENTER", "Enter");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                insertSelectedCompletion();
            }
        }

    }
    /**
     * Inserts the selected completion.
     */
    class TabAction extends ReplaceableAction {
        public TabAction() {
            super("TAB", "Tab");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                insertSelectedCompletion();
            }
        }

    }



    /**
     * Selects the previous item.
     */
    class EndAction extends ReplaceableAction {
        public EndAction() {
            super("END", "End");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectLastItem();
            }
        }

    }


    /**
     * Copies text.
     */
    class CopyAction extends ReplaceableAction {
        public CopyAction() {
            super("control C", "CtrlC");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean doNormalCopy = false;
//            if (descWindow!=null && descWindow.isVisible()) {
//                doNormalCopy = !descWindow.copy();
//            }
            if (doNormalCopy) {
                editor.copy();
            }
        }

    }

    class DownAction extends ReplaceableAction {
        public DownAction() {
            super("DOWN", "Down");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectNextItem();
            }
        }

    }

}
