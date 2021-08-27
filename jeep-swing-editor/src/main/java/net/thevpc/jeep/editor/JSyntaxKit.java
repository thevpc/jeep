package net.thevpc.jeep.editor;

import net.thevpc.jeep.JContext;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class JSyntaxKit extends StyledEditorKit implements ViewFactory {

    public static final String UI_KEY_DEFAULT = "TextPane.foreground;textForeground;#black";
    public static final String UI_KEY_RESERVED_WORD = "Button.default.focusColor;#735db7";
    public static final String UI_KEY_RESERVED_WORD2 = "Objects.GreenAndroid;OptionPane.questionDialog.border.background;#darkgreen";
    public static final String UI_KEY_RESERVED_WORD3 = "Objects.Pink;#pink";
    public static final String UI_KEY_COMMENTS = "Label.disabledForeground;#darkgray";
    public static final String UI_KEY_LITERAL_STRING = "OptionPane.questionDialog.border.background;#2b9946";
    public static final String UI_KEY_LITERAL_STRING2 = "OptionPane.questionDialog.border.background;#2b9946";
    public static final String UI_KEY_DIRECTIVE = "OptionPane.questionDialog.border.background;#darkgreen";
    public static final String UI_KEY_LITERAL_NUMBER = "Label.errorForeground,#darkred";
    public static final String UI_KEY_SUCCESS = "Objects.GreenAndroid;OptionPane.questionDialog.border.background;#darkgreen";
    public static final String UI_KEY_ERROR = "Label.errorForeground,#darkred";
    public static final String UI_KEY_LITERAL_DATE = "OptionPane.warningDialog.titlePane.foreground;#darkgreen";
    public static final String UI_KEY_WARNING = "OptionPane.warningDialog.titlePane.foreground;#darkorange";
    public static final String UI_KEY_INFO = "OptionPane.warningDialog.titlePane.foreground;#darkcyan";
    public static final String UI_KEY_LITERAL_REGEXP = "OptionPane.warningDialog.titlePane.foreground;#darkgreen";
    public static final String UI_KEY_LITERAL_BOOLEAN = "Button.default.focusColor;#darkblue";
    public static final String UI_KEY_TYPE_PRIMITIVE = "Button.default.focusColor;#darkblue";
    public static final String UI_KEY_SEPARATOR = "ScrollBar.thumbDarkShadow;#darkred";
    public static final String UI_KEY_OPERATOR = "OptionPane.warningDialog.titlePane.foreground;#darkcyan";
    public static final String UI_KEY_FORE1 = "Objects.Blue;MenuItem.acceleratorForeground;#blue";
    public static final String UI_KEY_FORE2 = "Objects.Green;OptionPane.questionDialog.titlePane.background;#green";
    public static final String UI_KEY_FORE3 = "Objects.GreenAndroid;OptionPane.questionDialog.border.background;#darkgreen";
    public static final String UI_KEY_FORE4 = "OptionPane.warningDialog.titlePane.foreground;#darkcyan";
//    public static final String UI_KEY_FORE4 = "Objects.Gray;MenuItem.background;#gray";
    public static final String UI_KEY_FORE5 = "Objects.Pink;#pink";
    public static final String UI_KEY_FORE6 = "Objects.Purple;OptionPane.errorDialog.border.background;#purple";
    public static final String UI_KEY_FORE7 = "Objects.Red;OptionPane.errorDialog.titlePane.background;#red";
    public static final String UI_KEY_FORE8 = "Objects.RedStatus;OptionPane.errorDialog.border.background;#darkred";
    public static final String UI_KEY_FORE9 = "Objects.Yellow;OptionPane.warningDialog.titlePane.background;#yellow";
    public static final String UI_KEY_FORE10 = "Objects.YellowDark;OptionPane.warningDialog.border.background;#darkyellow";
    private static Set<String> CONTENTS = new HashSet<>();
    private static final Logger LOG = Logger.getLogger(JSyntaxKit.class.getName());
    private JContext jcontext;
    private JSyntaxStyleManager styles;

    protected JSyntaxStyle STYLE_KEYWORDS = new JSyntaxStyle("RESERVED_WORD",ColorResource.of(UI_KEY_RESERVED_WORD), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_COMMENTS = new JSyntaxStyle("COMMENTS",ColorResource.of(UI_KEY_COMMENTS), JSyntaxStyle.ITALIC);
    protected JSyntaxStyle STYLE_STRING = new JSyntaxStyle("LITERAL_STRING",ColorResource.of(UI_KEY_LITERAL_STRING), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_NUMBERS = new JSyntaxStyle("LITERAL_NUMBER",ColorResource.of(UI_KEY_LITERAL_NUMBER), JSyntaxStyle.PLAIN);
    protected JSyntaxStyle STYLE_OPERATORS = new JSyntaxStyle("OPERATOR",ColorResource.of(UI_KEY_OPERATOR), JSyntaxStyle.PLAIN);
    protected JSyntaxStyle STYLE_SEPARATORS = new JSyntaxStyle("SEPARATOR",ColorResource.of(UI_KEY_SEPARATOR), JSyntaxStyle.PLAIN);
    protected JSyntaxStyle STYLE_REGEXPS = new JSyntaxStyle("LITERAL_REGEXP",ColorResource.of(UI_KEY_LITERAL_REGEXP), JSyntaxStyle.PLAIN);
    protected JSyntaxStyle STYLE_TEMPORALS = new JSyntaxStyle("LITERAL_DATE",ColorResource.of(UI_KEY_LITERAL_DATE), JSyntaxStyle.PLAIN);
    protected JSyntaxStyle STYLE_DIRECTIVES = new JSyntaxStyle("DIRECTIVE",ColorResource.of(UI_KEY_DIRECTIVE), JSyntaxStyle.PLAIN);
    protected JSyntaxStyle STYLE_PRIMITIVE_TYPES = new JSyntaxStyle("TYPE_PRIMITIVE",ColorResource.of(UI_KEY_TYPE_PRIMITIVE), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_BOOLEAN_LITERALS = new JSyntaxStyle("LITERAL_BOOLEAN",ColorResource.of(UI_KEY_LITERAL_BOOLEAN), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_KEYWORDS2 = new JSyntaxStyle("RESERVED_WORD2",ColorResource.of(UI_KEY_RESERVED_WORD2), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_STRINGS = new JSyntaxStyle("LITERAL_STRING",ColorResource.of(UI_KEY_LITERAL_STRING), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_STRINGS2 = new JSyntaxStyle("LITERAL_STRING2",ColorResource.of(UI_KEY_LITERAL_STRING2), JSyntaxStyle.PLAIN);
    protected JSyntaxStyle STYLE_TITLE1 = new JSyntaxStyle("UI_KEY_FORE1",ColorResource.of(UI_KEY_FORE1), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_TITLE2 = new JSyntaxStyle("UI_KEY_FORE2",ColorResource.of(UI_KEY_FORE2), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_TITLE3 = new JSyntaxStyle("UI_KEY_FORE3",ColorResource.of(UI_KEY_FORE3), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_TITLE4 = new JSyntaxStyle("UI_KEY_FORE4",ColorResource.of(UI_KEY_FORE4), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_TITLE5 = new JSyntaxStyle("UI_KEY_FORE5",ColorResource.of(UI_KEY_FORE5), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_TITLE6 = new JSyntaxStyle("UI_KEY_FORE6",ColorResource.of(UI_KEY_FORE6), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_TITLE7 = new JSyntaxStyle("UI_KEY_FORE7",ColorResource.of(UI_KEY_FORE7), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_TITLE8 = new JSyntaxStyle("UI_KEY_FORE8",ColorResource.of(UI_KEY_FORE8), JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_TITLE9 = new JSyntaxStyle("UI_KEY_FORE9",ColorResource.of(UI_KEY_FORE9), JSyntaxStyle.BOLD);

    protected JSyntaxStyle STYLE_BG1 = new JSyntaxStyle("UI_KEY_BG1", null, JSyntaxStyle.FILLED).setFillColor(ColorResource.of(UI_KEY_FORE1));
    protected JSyntaxStyle STYLE_BG2 = new JSyntaxStyle("UI_KEY_BG2", null, JSyntaxStyle.FILLED).setFillColor(ColorResource.of(UI_KEY_FORE2));
    protected JSyntaxStyle STYLE_BG3 = new JSyntaxStyle("UI_KEY_BG3", null, JSyntaxStyle.FILLED).setFillColor(ColorResource.of(UI_KEY_FORE3));
    protected JSyntaxStyle STYLE_BG4 = new JSyntaxStyle("UI_KEY_BG4", null, JSyntaxStyle.FILLED).setFillColor(ColorResource.of(UI_KEY_FORE4));
    protected JSyntaxStyle STYLE_BG5 = new JSyntaxStyle("UI_KEY_BG5", null, JSyntaxStyle.FILLED).setFillColor(ColorResource.of(UI_KEY_FORE5));
    protected JSyntaxStyle STYLE_BG6 = new JSyntaxStyle("UI_KEY_BG6", null, JSyntaxStyle.FILLED).setFillColor(ColorResource.of(UI_KEY_FORE6));
    protected JSyntaxStyle STYLE_BG7 = new JSyntaxStyle("UI_KEY_BG7", null, JSyntaxStyle.FILLED).setFillColor(ColorResource.of(UI_KEY_FORE7));
    protected JSyntaxStyle STYLE_BG8 = new JSyntaxStyle("UI_KEY_BG8", null, JSyntaxStyle.FILLED).setFillColor(ColorResource.of(UI_KEY_FORE8));
    protected JSyntaxStyle STYLE_BG9 = new JSyntaxStyle("UI_KEY_BG9", null, JSyntaxStyle.FILLED).setFillColor(ColorResource.of(UI_KEY_FORE9));

    protected JSyntaxStyle STYLE_BOLD = new JSyntaxStyle("BOLD", null, JSyntaxStyle.BOLD);
    protected JSyntaxStyle STYLE_ITALIC = new JSyntaxStyle("ITALIC", null, JSyntaxStyle.ITALIC);
    protected JSyntaxStyle STYLE_CROSS_OUT = new JSyntaxStyle("CROSS_OUT", null, JSyntaxStyle.CROSS_OUT);
    protected JSyntaxStyle STYLE_UNDERLINE = new JSyntaxStyle("CROSS_OUT", null, JSyntaxStyle.UNDERLINE);
    protected JSyntaxStyle STYLE_BOLD_ITALIC = new JSyntaxStyle("BOLD_ITALIC", null, JSyntaxStyle.BOLD | JSyntaxStyle.ITALIC);
    protected JSyntaxStyle STYLE_PRE = new JSyntaxStyle("PRE", ColorResource.of("OptionPane.warningDialog.titlePane.shadow;ToolWindowTitleBarUI.background.active.end;Tree.selectionBackground"), JSyntaxStyle.PLAIN).setFillColor(ColorResource.of("#fff5b9"));
    protected JSyntaxStyle STYLE_CODE = new JSyntaxStyle("CODE", ColorResource.of("OptionPane.warningDialog.titlePane.shadow;ToolWindowTitleBarUI.background.active.end;Tree.selectionBackground"), JSyntaxStyle.PLAIN).setFillColor(ColorResource.of("#fae0ff"));
    protected JSyntaxStyle STYLE_SUCCESS = new JSyntaxStyle("UI_KEY_SUCCESS", ColorResource.of(UI_KEY_SUCCESS), JSyntaxStyle.PLAIN);
    protected JSyntaxStyle STYLE_ERROR = new JSyntaxStyle("UI_KEY_ERROR", ColorResource.of(UI_KEY_ERROR), JSyntaxStyle.PLAIN);
    protected JSyntaxStyle STYLE_WARN = new JSyntaxStyle("UI_KEY_WARNING", ColorResource.of(UI_KEY_WARNING), JSyntaxStyle.PLAIN);
    protected JSyntaxStyle STYLE_INFO = new JSyntaxStyle("UI_KEY_INFO", ColorResource.of(UI_KEY_INFO), JSyntaxStyle.PLAIN);

    //

    /**
     * Create a new Kit for the given language
     *
     * @param jcontext
     */
    public JSyntaxKit(JContext jcontext, JSyntaxStyleManager styles) {
        super();
        this.jcontext = jcontext;
        this.styles = styles;
    }

    public JSyntaxKit() {
    }

    public JSyntaxStyleManager getStyles() {
        return styles;
    }

    public JSyntaxKit setJcontext(JContext jcontext) {
        this.jcontext = jcontext;
        return this;
    }

    public JSyntaxKit setStyles(JSyntaxStyleManager styles) {
        this.styles = styles;
        return this;
    }

    @Override
    public ViewFactory getViewFactory() {
        return this;
    }

    @Override
    public View create(Element element) {
        return new JSyntaxView(element, styles);
    }

    /**
     * Install the View on the given EditorPane. This is called by Swing and can
     * be used to do anything you need on the JEditorPane control. Here I set
     * some default Actions.
     *
     * @param editorPane
     */
    @Override
    public void install(JEditorPane editorPane) {
        super.install(editorPane);
        Font font = styles.getFont();
        if (font != null) {
            editorPane.setFont(font);
        }
        Keymap km_parent = JTextComponent.getKeymap(JTextComponent.DEFAULT_KEYMAP);
        Keymap km_new = JTextComponent.addKeymap(null, km_parent);
        String kitName = this.getClass().getSimpleName();
        Color caretColor = styles.getCaretColor();
        if (caretColor == null) {
            caretColor = Color.BLACK;
        }
        editorPane.setCaretColor(caretColor);
        addSyntaxActions(km_new, kitName);
        editorPane.setKeymap(km_new);

//        installCompletion(editorPane);
        // install the components to the editor:
//        String[] components = CONFIG.getPrefixPropertyList(kitName, "Components");
//        for (String c : components) {
//            try {
//                @SuppressWarnings("unchecked")
//                Class<SyntaxComponent> compClass = (Class<SyntaxComponent>) Class.forName(c);
//                SyntaxComponent comp = compClass.newInstance();
//                comp.config(CONFIG, kitName);
//                comp.install(editorPane);
//                editorComponents.add(comp);
//            } catch (InstantiationException ex) {
//                LOG.log(Level.SEVERE, null, ex);
//            } catch (IllegalAccessException ex) {
//                LOG.log(Level.SEVERE, null, ex);
//            } catch (ClassNotFoundException ex) {
//                LOG.log(Level.SEVERE, null, ex);
//            }
//        }
    }

    @Override
    public void deinstall(JEditorPane editorPane) {
//        for (SyntaxComponent c : editorComponents) {
//            c.deinstall(editorPane);
//        }
//        editorComponents.clear();
    }

    public static void setMenuSelectedIndex(final JPopupMenu popupMenu, final int index) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for (int i = 0; i < index + 1; i++) {
                    popupMenu.dispatchEvent(new KeyEvent(popupMenu, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_DOWN, '\0'));
                }
            }
        });
    }

    /**
     * Add keyboard actions to this control using the Configuration we have
     *
     * @param map
     * @param prefix
     */
    public void addSyntaxActions(Keymap map, String prefix) {
        // look at all keys that either start with prefix.Action, or
        // that start with Action.

//        Configuration actionsConf = CONFIG.subConfig(prefix, "Action.");
//
//        for (String actionName : actionsConf.stringPropertyNames()) {
//            String[] values = Configuration.COMMA_SEPARATOR.split(
//                    actionsConf.getProperty(actionName));
//            String actionClass = values[0];
//            SyntaxAction action = editorActions.get(actionClass);
//            if (action == null) {
//                action = createAction(actionClass);
//                action.config(CONFIG, prefix, actionName);
//            }
//            String keyStrokeString = values[1];
//            KeyStroke ks = KeyStroke.getKeyStroke(keyStrokeString);
//            // KeyEvent.VK_QUOTEDBL
//            if (ks == null) {
//                throw new IllegalArgumentException("Invalid KeyStroke: " +
//                        keyStrokeString);
//            }
//            TextAction ta = action.getAction(actionName);
//            if(ta == null) {
//                throw new IllegalArgumentException("Invalid ActionName: " +
//                        actionName);
//            }
//            map.addActionForKeyStroke(ks, ta);
//        }
    }

//    private SyntaxAction createAction(String actionClassName) {
//        SyntaxAction action = null;
//        try {
//            Class clazz = Class.forName(actionClassName);
//            action = (SyntaxAction) clazz.newInstance();
//            editorActions.put(actionClassName, action);
//        } catch (InstantiationException ex) {
//            throw new IllegalArgumentException("Cannot create action class: " +
//                    actionClassName, ex);
//        } catch (IllegalAccessException ex) {
//            throw new IllegalArgumentException("Cannot create action class: " +
//                    actionClassName, ex);
//        } catch (ClassNotFoundException ex) {
//            throw new IllegalArgumentException("Cannot create action class: " +
//                    actionClassName, ex);
//        } catch (ClassCastException ex) {
//            throw new IllegalArgumentException("Cannot create action class: " +
//                    actionClassName, ex);
//        }
//        return action;
//    }
    /**
     * This is called by Swing to create a Document for the JEditorPane document
     * This may be called before you actually get a reference to the control. We
     * use it here to create a proper lexer and pass it to the SyntaxDcument we
     * return.
     *
     * @return
     */
    @Override
    public Document createDefaultDocument() {
        return new JSyntaxDocument(jcontext);
    }

    /**
     * Register the given content type to use the given class name as its kit
     * When this is called, an entry is added into the private HashMap of the
     * registered editors kits. This is needed so that the SyntaxPane library
     * has it's own registration of all the EditorKits
     *
     * @param type
     * @param classname
     */
    public static void registerContentType(String type, String classname) {
        JEditorPane.registerEditorKitForContentType(type, classname);
        CONTENTS.add(type);
    }

    /**
     * Return all the content types supported by this library. This will be the
     * content types in the file
     * WEB-INF/services/resources/jsyntaxpane.kitsfortypes
     *
     * @return sorted array of all registered content types
     */
    public static String[] getContentTypes() {
        String[] types = CONTENTS.toArray(new String[0]);
        Arrays.sort(types);
        return types;
    }

    protected JSyntaxStyle getTitleStyle(int ii){
        return getForegroundStyle(ii);
    }

    protected JSyntaxStyle getForegroundStyle(int ii){
        if (ii >= 1 && ii <= 9) {
            switch (ii){
                case 1:
                    return STYLE_TITLE1;
                case 2:return STYLE_TITLE2;
                case 3:return STYLE_TITLE3;
                case 4:return STYLE_TITLE4;
                case 5:return STYLE_TITLE5;
                case 6:return STYLE_TITLE6;
                case 7:return STYLE_TITLE7;
                case 8:return STYLE_TITLE8;
                case 9:return STYLE_TITLE9;
            }
        }
        return null;
    }
    protected JSyntaxStyle getBackgroundStyle(int ii){
        if (ii >= 1 && ii <= 9) {
            switch (ii){
                case 1:
                    return STYLE_BG1;
                case 2:return STYLE_BG2;
                case 3:return STYLE_BG3;
                case 4:return STYLE_BG4;
                case 5:return STYLE_BG5;
                case 6:return STYLE_BG6;
                case 7:return STYLE_BG7;
                case 8:return STYLE_BG8;
                case 9:return STYLE_BG9;
            }
        }
        return null;
    }
}
