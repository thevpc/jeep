package net.thevpc.jeep.core;

import net.thevpc.jeep.JIndexQueryLookup;

import java.util.ArrayList;
import java.util.List;

public class JIndexQuery {
    private List<JIndexQueryLookup> wheres=new ArrayList<>();

    public JIndexQuery whereEq(String key,String value){
        wheres.add(new JIndexQueryLookup(key,value));
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
