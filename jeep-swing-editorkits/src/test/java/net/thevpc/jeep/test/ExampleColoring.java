package net.thevpc.jeep.test;


import net.thevpc.jeep.editorkits.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author vpc
 */
public class ExampleColoring {

    private static List<SourceInfo> sources = new ArrayList<>();

    private static void prepareSources() {
        register("text/x-c++src", "C++", "cpp");
        register("text/x-c++hdr", "C++ Header", "hpp");
        register("text/x-csrc", "C", "c");
        register("text/x-chdr", "C Header", "h");
        register("text/java", "Java","java");
        register("text/javascript", "Javascript","js");
        register("text/css", "CSS","css");
        register("text/markdown", "Markdown","md");
        register("text/x-nuts-text-format", "Nuts Text Format","ntf");
        register("application/x-bash", "Bash Shell Script","sh");
        register("application/x-hadra", "Hadra Lang","hl");
        register("application/json", "JSON","json");
        register("application/x-bibtex", "BibTex","bibtex");
        register("text/xml", "Xml","xml");
        register("text/html", "HTML","html");
        register("application/x-latex", "Latex","tex");
        register("application/sql", "SQL","sql");
    }

    public static void main(String[] args) {
        prepareSources();
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
        JList box = new JList(sources.toArray());
        JEditorPane pane=new JEditorPane();
        AvailableEditorKits.installEditorKits(pane);
        panel.add(new JScrollPane(box),BorderLayout.WEST);
        JScrollPane comp = new JScrollPane(pane);
        comp.setPreferredSize(new Dimension(700,500));
        panel.add(comp,BorderLayout.CENTER);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        box.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    SourceInfo s=(SourceInfo) box.getSelectedValue();
                    if(s==null){
                        pane.setText("");
                        pane.setContentType("text/plain");
                    }else {
                        pane.setText("");
                        pane.setContentType(s.contentType);
                        pane.setText(s.source);
                    }
                }
            }
        });
    }

    private static void register(String type, String name, String srcId) {
        String url = "/net/thevpc/jeep/test/example." + srcId;
        URL r = ExampleColoring.class.getResource(url);
        if(r==null){
            throw new IllegalArgumentException("not found "+url);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[4048];
        int c;
        try (InputStream in = r.openStream()) {
            while ((c = in.read(bytes)) > 0) {
                bos.write(bytes, 0, c);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        sources.add(new SourceInfo(name, type, bos.toString()));
    }

    private static class SourceInfo {

        String name;
        String contentType;
        String source;

        public SourceInfo(String name, String contentType, String source) {
            this.name = name;
            this.contentType = contentType;
            this.source = source;
        }

        @Override
        public String toString() {
            return String.valueOf(name);
        }

    }
}
