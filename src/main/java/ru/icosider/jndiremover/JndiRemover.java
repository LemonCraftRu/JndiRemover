package ru.icosider.jndiremover;

import cpw.mods.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrLookup;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Pattern;

public class JndiRemover {
    public static final String HEART = "<3";

    private static final Pattern JNDI_PATTERN = Pattern.compile("(?i)\\$\\{jndi:[\\s\\S]*}");

    private static final Field lookupsField;
    private static final Field configField;

    private JndiRemover() {
        throw new IllegalArgumentException("Unable to create utility class object!");
    }

    public static String replaceJndi(String text) {
        if (matchJndi(text))
            return text.replaceAll(JNDI_PATTERN.pattern(), HEART);
        return text;
    }

    public static boolean matchJndi(String message) {
        return JNDI_PATTERN.matcher(message).find();
    }

    public static void lookupClean() {
        Logger logger = (Logger) LogManager.getRootLogger();
        StrLookup lookup = logger.getContext().getConfiguration().getStrSubstitutor().getVariableResolver();
        if (lookup instanceof Interpolator)
            cleanupLookup((Interpolator) lookup);
        try {
            for (Map.Entry<String, Appender> entry: logger.getAppenders().entrySet()) {
                Layout<?> layout = entry.getValue().getLayout();
                if (layout instanceof PatternLayout) {
                    PatternLayout pl = (PatternLayout) layout;
                    StrLookup st = ((Configuration) configField.get(pl)).getStrSubstitutor().getVariableResolver();
                    if (st instanceof Interpolator)
                        cleanupLookup((Interpolator) st);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void cleanupLookup(Interpolator lookup) {
        try {
            ((Map<?, ?>) lookupsField.get(lookup)).clear();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        lookupsField = ReflectionHelper.findField(Interpolator.class, "lookups");
        configField = ReflectionHelper.findField(PatternLayout.class, "config");
    }
}