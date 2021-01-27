/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep;

/**
 * @author thevpc
 */
public class JUplet {
    private String name;
    private Object[] elements;
    private JType[] types;

    public JUplet(String name, Object[] elements, JType[] types) {
        this.name = name;
        this.elements = elements;
        this.types = types;
    }

    public JType type(int i) {
        return types[i];
    }

    public Object elem(int i) {
        return elements[i];
    }

    public JType[] types() {
        return types;
    }

    public String name() {
        return name;
    }

    public Object[] elements() {
        return elements;
    }

    public int size() {
        return elements.length;
    }

}
