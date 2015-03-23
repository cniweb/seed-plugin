package net.nemerosa.seed.jenkins.model;

import net.nemerosa.seed.jenkins.support.MissingParameterException;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Configuration {

    public static String normalise(String value) {
        return value.replaceAll("[^A-Za-z0-9._-]", "-");
    }

    private final Map<String, ?> data;

    public Configuration(Map<String, ?> data) {
        this.data = data;
    }

    public String getString(String name) {
        return getString(name, true, null);
    }

    public String getString(String name, boolean required, String defaultValue) {
        if (name.contains(".")) {
            String prefix = StringUtils.substringBefore(name, ".");
            String rest = StringUtils.substringAfter(name, ".");
            @SuppressWarnings("unchecked")
            Map<String, ?> child = (Map<String, ?>) data.get(prefix);
            return new Configuration(child).getString(rest, required, defaultValue);
        } else {
            String value = Objects.toString(data.get(name), null);
            if (value != null) {
                return value;
            } else if (required) {
                throw new MissingParameterException(name);
            } else {
                return defaultValue;
            }
        }
    }

    public boolean getBoolean(String name, boolean required, boolean defaultValue) {
        String value = getString(name, required, null);
        if (value == null) {
            return defaultValue;
        } else {
            return "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
        }
    }

    public List<Map<String, ?>> getList(String name) {
        @SuppressWarnings("unchecked")
        List<Map<String, ?>> list = (List<Map<String, ?>>) data.get(name);
        if (list != null) {
            return list;
        } else {
            return Collections.emptyList();
        }
    }
}