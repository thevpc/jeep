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
    public static final String UI_KEY_RESERVED_WORD2 = "Objects.Green;OptionPane.questionDialog.titlePane.background;#green";
    public static final String UI_KEY_RESERVED_WORD3 = "Objects.Purple;OptionPane.errorDialog.border.background;#purple";
    public static final String UI_KEY_COMMENTS = "Label.disabledForeground;#darkgray";
    public static final String UI_KEY_LITERAL_STRING = "OptionPane.questionDialog.border.background;#2b9946";
    public static final String UI_KEY_LITERAL_STRING2 = "OptionPane.questionDialog.border.background;#2b9946";
    public static final String UI_KEY_DIRECTIVE = "OptionPane.questionDialog.border.background;#darkgreen";
    public static final String UI_KEY_LITERAL_NUMBER = "Label.errorForeground,#darkred";
    public static final String UI_KEY_LITERAL_DATE = "OptionPane.warningDialog.titlePane.foreground;#darkgreen";
    public static final String UI_KEY_LITERAL_REGEXP = "OptionPane.warningDialog.titlePane.foreground;#darkgreen";
    public static final String UI_KEY_LITERAL_BOOLEAN = "Button.default.focusColor;#darkblue";
    public static final String UI_KEY_TYPE_PRIMITIVE = "Button.default.focusColor;#darkblue";
    public static final String UI_KEY_SEPARATOR = "ScrollBar.thumbDarkShadow;#darkred";
    public static final String UI_KEY_OPERATOR = "OptionPane.warningDialog.titlePane.foreground;#darkcyan";
    public static final String UI_KEY_FORE1 = "Objects.Blue;MenuItem.acceleratorForeground;#blue";
    public static final String UI_KEY_FORE2 = "Objects.Green;OptionPane.questionDialog.titlePane.background;#green";
    public static final String UI_KEY_FORE3 = "Objects.GreenAndroid;OptionPane.questionDialog.border.background;#darkgreen";
    public static final String UI_KEY_FORE4 = "Objects.Gray;MenuItem.background;#gray";
    public static final String UI_KEY_FORE5 = "Objects.Pink;#pink";
    public static final String UI_KEY_FORE6 = "Objects.Purple;OptionPane.errorDialog.border.background;#purple";
    public static final String UI_KEY_FORE7 = "Objects.Red;OptionPane.errorDialog.titlePane.background;#red";
    public static final String UI_KEY_FORE8 = "Objects.RedStatus;OptionPane.errorDialog.border.background;#darkred";
    public static final String UI_KEY_FORE9 = "Objects.Yellow;OptionPane.warningDialog.titlePane.background;#yellow";
    public static final String UI_KEY_FORE10 = "Objects.YellowDark;OptionPane.warningDialog.border.background;#darkyellow";
    private static Set<String> CONTENTS = new HashSet<>();
    private JContext jcontext;
    private JSyntaxStyleManager styles;
    private static final Logger LOG = Logger.getLogger(JSyntaxKit.class.getName());

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

}
