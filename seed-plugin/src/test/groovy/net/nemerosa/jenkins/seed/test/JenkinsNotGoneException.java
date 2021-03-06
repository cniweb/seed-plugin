package net.nemerosa.jenkins.seed.test;

import net.nemerosa.jenkins.seed.SeedException;

public class JenkinsNotGoneException extends SeedException {
    public JenkinsNotGoneException(String path) {
        super("Jenkins job at %s is present when it should have been gone.", path);
    }
}
