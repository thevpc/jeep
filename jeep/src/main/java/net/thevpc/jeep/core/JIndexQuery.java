package net.thevpc.jeep.core;

import net.thevpc.jeep.JIndexQueryLookup;
import net.thevpc.jeep.JIndexQueryLookupOp;

import java.util.ArrayList;
import java.util.List;

public class JIndexQuery {
    private List<JIndexQueryLookup> wheres=new ArrayList<>();

    public JIndexQuery whereEq(String key,String value){
        wheres.add(new JIndexQueryLookup(key,value, JIndexQueryLookupOp.EQ));
        return this;
    }
    public JIndexQuery whereNe(String key,String value){
        wheres.add(new JIndexQueryLookup(key,value, JIndexQueryLookupOp.NE));
        return this;
    }

    public JIndexQuery whereDotStart(String key,String value){
        wheres.add(new JIndexQueryLookup(key,value, JIndexQueryLookupOp.DOT_START));
        return this;
    }

    public JIndexQuery whereExists(String key){
        wheres.add(new JIndexQueryLookup(key,null, JIndexQueryLookupOp.DOT_START));
        return this;
    }

    public JIndexQueryLookup[] getWheres() {
        return wheres.toArray(new JIndexQueryLookup[0]);
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder("Where");
        for (int i = 0; i < wheres.size(); i++) {
            JIndexQueryLookup cond = wheres.get(i);
            if(i>0){
                sb.append(" and ");
            }else{
                sb.append(" ");
            }
            sb.append(cond);
        }
        return sb.toString();
    }
}
