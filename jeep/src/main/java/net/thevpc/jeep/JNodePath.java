package net.thevpc.jeep;

import java.util.Arrays;
import java.util.Iterator;

public class JNodePath implements Iterable<JNode>{
    private JNode[] elements;

    public JNodePath() {
        elements = new JNode[0];
    }

    public JNodePath(JNode... elements) {
        this.elements = elements;
    }


    public JNode last() {
        return elements.length==0?null:elements[elements.length - 1];
    }

    public JNode parent(int i) {
        return elements[elements.length - 1 - i];
    }

    public JNodePath parent() {
        return elements.length==0?this:new JNodePath(Arrays.copyOfRange(elements,0,elements.length-1));
    }

    public int size() {
        return elements.length;
    }

    public JNodePath append(JNode n) {
        if(n==null){
            throw new NullPointerException();
        }
        if(elements.length>0 && n==elements[elements.length-1]){
//            System.out.println("Why?");
        }
        JNode[] p = new JNode[elements.length + 1];
        System.arraycopy(elements, 0, p, 0, elements.length);
        p[elements.length] = n;
        return new JNodePath(p);
    }

    @Override
    public Iterator<JNode> iterator() {
        return Arrays.asList(elements).iterator();
    }

    public JNodePath removeLast() {
        if(elements.length==0){
            throw new JParseException();
        }
        return new JNodePath(Arrays.copyOf(elements,elements.length-1));
    }

    public String getPathString(){
        StringBuilder sb=new StringBuilder();
        for (JNode jNode : this) {
            if(sb.length()>0){
                sb.append("/");
            }
            sb.append(jNode.getClass().getSimpleName());
            if(jNode.getChildInfo()!=null) {
                sb.append("(").append(jNode.getChildInfo()).append(")");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getPathString();
    }
}
