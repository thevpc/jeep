/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep;

import java.util.Stack;
import java.util.concurrent.Callable;

/**
 *
 * @author thevpc
 */
public class JSharedContext {

    static ThreadLocal<Stack<Jeep>> context = new ThreadLocal<>();

    public static Jeep getCurrent() {
        Stack<Jeep> v = context.get();
        if(v==null || v.isEmpty()){
            return null;
        }
        return v.peek();
    }
    
    public static <T> T invokeSilentCallable(Jeep e, JSilentCallable<T> run) {
        Stack<Jeep> v = context.get();
        if (v == null) {
            v = new Stack<>();
            context.set(v);
        }
        v.push(e);
        try {
            return run.call();
        } finally {
            v.pop();
        }
    }

    public static <T> T invokeCallable(Jeep e, Callable<T> run) throws Exception {
        Stack<Jeep> v = context.get();
        if (v == null) {
            v = new Stack<>();
            context.set(v);
        }
        v.push(e);
        try {
            return run.call();
        } finally {
            v.pop();
        }
    }

    public static void invokeRunnable(Jeep e, Runnable run) {
        Stack<Jeep> v = context.get();
        if (v == null) {
            v = new Stack<>();
            context.set(v);
        }
        v.push(e);
        try {
            run.run();
        } finally {
            v.pop();
        }
    }
}
