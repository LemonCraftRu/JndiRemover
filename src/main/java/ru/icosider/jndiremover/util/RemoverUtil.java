package ru.icosider.jndiremover.util;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrLookup;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Pattern;

public class RemoverUtil {
    private static final Pattern JNDI_PATTERN = Pattern.compile("(?i)\\$\\{(jndi|ctx|date|env|event|java|jvmrunargs|log4j|lower|main|map|marker|bundle|sd|sys|upper|):[\\s\\S]*");

    private static final Field LOOKUPS_FIELD = ObfuscationReflectionHelper.findField(Interpolator.class, "lookups");

    private RemoverUtil() {
        throw new IllegalArgumentException("Unable to create utility class object!");
    }

    public static String replaceJndi(String text) {
        if (matchJndi(text))
            return text.replaceAll(JNDI_PATTERN.pattern(), "");
        return text;
    }

    public static boolean matchJndi(String message) {
        return JNDI_PATTERN.matcher(message.replaceAll("\u00a7[a-zA-Z0-9]", "").replaceAll("(\\s|\\n\\r)", "")).find();
    }

    public static void lookupClean() {
        Logger logger = (Logger) LogManager.getRootLogger();
        StrLookup lookup = logger.getContext().getConfiguration().getStrSubstitutor().getVariableResolver();
        if (lookup instanceof Interpolator)
            cleanupLookup((Interpolator) lookup);
        for (Map.Entry<String, Appender> entry : logger.getAppenders().entrySet()) {
            Layout<?> layout = entry.getValue().getLayout();
            if (layout instanceof PatternLayout) {
                PatternLayout pl = (PatternLayout) layout;
                StrLookup st = pl.getConfiguration().getStrSubstitutor().getVariableResolver();
                if (st instanceof Interpolator)
                    cleanupLookup((Interpolator) st);
            }
        }
    }

    private static void cleanupLookup(Interpolator lookup) {
        try {
            ((Map<?, ?>) LOOKUPS_FIELD.get(lookup)).clear();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}