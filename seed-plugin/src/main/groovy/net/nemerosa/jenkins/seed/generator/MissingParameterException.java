package net.nemerosa.jenkins.seed.generator;

import net.nemerosa.jenkins.seed.SeedException;

public class MissingParameterException extends SeedException {
    public MissingParameterException(String name) {
        super("Missing parameter: %s", name);
    }
}
