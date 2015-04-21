package net.nemerosa.seed.jenkins.docker

import org.junit.Rule
import org.junit.Test

/**
 * Is the Jenkins application available?
 */
class JenkinsBaseTest {

    @Rule
    public JenkinsAccessRule jenkins = new JenkinsAccessRule()

    @Test
    void 'Jenkins started'() {
    }

    @Test
    void 'Default seed job created'() {
        // TODO Checks the job
        jenkins.job('seed')
    }

}
