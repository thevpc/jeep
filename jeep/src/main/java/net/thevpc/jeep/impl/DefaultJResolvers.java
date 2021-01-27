/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep.impl;

import net.thevpc.jeep.*;
import net.thevpc.jeep.util.JConverterList2;
import net.thevpc.jeep.util.JTypeUtils;
import net.thevpc.jeep.*;
import net.thevpc.jeep.core.UtilClassJResolver;
import net.thevpc.jeep.core.imports.PlatformHelperImports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author thevpc
 */
public class DefaultJResolvers implements JResolvers {

    private final List<JResolver> resolvers = new ArrayList<>();
    private JResolvers parent;
    private JContext context;

    public DefaultJResolvers(JContext context, JResolvers parent) {
        this.parent = parent;
        this.context = context;
    }

    protected UtilClassJResolver lastDefaultUtilClassJResolver(boolean create) {
        if (resolvers.size() > 0) {
            JResolver last = resolvers.get(resolvers.size() - 1);
            if (last instanceof MergedUtilClassJResolver) {
                return (MergedUtilClassJResolver) last;
            }
        }
        if (create) {
            MergedUtilClassJResolver c = new MergedUtilClassJResolver();
            addResolver(c);
            return c;
        }
        return null;
    }

    @Override
    public JResolvers importPlatform() {
        importType(context.types().forName(PlatformHelperImports.class.getName()));
        return this;
    }

    @Override
    public JResolvers importType(String type) {
        return importType(context.types().forName(type));
    }

    @Override
    public JResolvers importType(Class type) {
        return importType(context.types().forName(type.getName()));
    }

    @Override
    public JResolvers importType(JType type) {
        lastDefaultUtilClassJResolver(true).addImportType(type);
        return this;
    }

    @Override
    public JResolvers importMethods(String type) {
        return importMethods(context.types().forName(type));
    }

    @Override
    public JResolvers importMethods(Class type) {
        return importMethods(context.types().forName(type.getName()));
    }

    @Override
    public JResolvers importMethods(JType type) {
        lastDefaultUtilClassJResolver(true).addImportMethods(type);
        return this;
    }

    @Override
    public JResolvers importFields(String type) {
        return importFields(context.types().forName(type));
    }

    @Override
    public JResolvers importFields(Class type) {
        return importFields(context.types().forName(type.getName()));
    }

    @Override
    public JResolvers importFields(JType type) {
        lastDefaultUtilClassJResolver(true).addImportFields(type);
        return this;
    }

    @Override
    public JResolvers addResolver(JResolver resolver) {
        this.resolvers.add(resolver);
        return this;
    }

    @Override
    public JResolvers removeResolver(JResolver resolver) {
        this.resolvers.remove(resolver);
        return this;
    }

    public JResolver[] getResolvers() {
        LinkedHashSet<JResolver> all = new LinkedHashSet<>();
        all.addAll(resolvers);
        if (parent != null) {
            all.addAll(Arrays.asList(parent.getResolvers()));
        }
        return all.toArray(new JResolver[0]);
    }

    @Override
    public JConverter[] getConverters(JTypePattern type) {
        JConverterList2 allImplicitConverters = new JConverterList2(type, true);
        for (JConverter ic : JTypeUtils.getTypeImplicitConversions(type, false)) {
            if (ic != null) {
                allImplicitConverters.add(ic);
            }
        }
        for (JResolver resolver : getResolvers()) {
            JConverter[] next = resolver.resolveImplicitConverters(type);
            if (next != null) {
                for (JConverter ic : next) {
                    allImplicitConverters.add(ic);
                }
            }
        }
        if (parent != null) {
            for (JConverter converter : parent.getConverters(type)) {
                allImplicitConverters.add(converter);
            }
        }
        return allImplicitConverters.toArray();
    }

    private static final class MergedUtilClassJResolver extends UtilClassJResolver {
        public MergedUtilClassJResolver() {
            super();
        }

    }
}
