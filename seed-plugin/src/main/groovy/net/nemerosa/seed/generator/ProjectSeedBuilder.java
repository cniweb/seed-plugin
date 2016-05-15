package net.nemerosa.seed.generator;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.model.Jenkins;
import net.nemerosa.seed.SeedDescriptor;
import net.nemerosa.seed.config.ProjectProperties;
import net.nemerosa.seed.config.SeedNamingStrategyHelper;
import net.nemerosa.seed.config.SeedProjectEnvironment;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Build step which creates a project folder and a project seed inside.
 */
@Deprecated
public class ProjectSeedBuilder extends AbstractSeedBuilder {

    @DataBoundConstructor
    public ProjectSeedBuilder(String project, String projectClass, String projectScmType, String projectScmUrl, String projectScmCredentials) {
        super(project, projectClass, projectScmType, projectScmUrl, projectScmCredentials);
    }

    @Override
    protected void afterGeneration(SeedProjectEnvironment projectEnvironment) {
        SeedDescriptor descriptor = Jenkins.getInstance().getDescriptorByType(SeedDescriptor.class);
        descriptor.saveProjectConfiguration(projectEnvironment);
    }

    @Override
    protected boolean useConfigurationCache() {
        return false;
    }

    @Override
    protected void configureEnvironment(EnvVars env, SeedProjectEnvironment projectEnvironment) {
        env.put("projectSeedFolder", SeedNamingStrategyHelper.getProjectSeedFolder(
                projectEnvironment.getNamingStrategy(),
                projectEnvironment.getId()
        ));
        env.put("projectSeedPath", projectEnvironment.getNamingStrategy().getProjectSeed(
                projectEnvironment.getId()
        ));
        env.put("projectDestructorEnabled", String.valueOf(projectEnvironment.getConfigurationBoolean(
                ProjectProperties.PIPELINE_DESTRUCTOR, false
        )));
        env.put("projectDestructorPath", projectEnvironment.getNamingStrategy().getProjectDestructor(
                projectEnvironment.getId()
        ));
    }

    @Override
    protected String getScriptPath() {
        return "/project-seed-generator.groovy";
    }

    @Override
    protected String replaceExtensionPoints(String script, EnvVars env, SeedProjectEnvironment projectEnvironment) {
        String result = replaceExtensionPoint(script, "authorisations", new ProjectFolderAuthorisationsExtension(projectEnvironment).generate());
        result = replaceExtensionPoint(result, "projectSeed", new ProjectSeedExtension(projectEnvironment).generate());
        return result;
    }

    @Extension
    public static class ProjectSeedBuilderDescription extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Project seed generator";
        }
    }
}
