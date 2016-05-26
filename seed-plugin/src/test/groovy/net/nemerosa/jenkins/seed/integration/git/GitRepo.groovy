package net.nemerosa.jenkins.seed.integration.git

import org.apache.commons.io.FileUtils
import org.eclipse.jgit.api.Git

class GitRepo {

    /**
     * Prepares a Git repository in a directory, initialise it with some content identified by the {@code id} name
     * and returns the path of the directory.
     */
    static String prepare(String id) {
        Map<String, String> resources = [:]
        loadResource(resources, "seed-${id}.properties", "seed/seed.properties")
        loadResource(resources, "seed-${id}.groovy", "seed/seed.groovy")
        return prepare(id, resources)
    }

    static def loadResource(Map<String, String> resources, String name, String targetName) {
        def url = GitRepo.class.getResource("/acceptance/${name}")
        if (url) {
            resources.put targetName, url.text
        }
    }

    /**
     * Prepares a Git repository in a directory, initialise it with some content identified by the {@code id} name
     * and returns the path of the directory.
     */
    static String prepare(String id, Map<String, String> resources) {
        File dir = createTempDir(id)
        println "Creating Git directory for ${id} at ${dir}..."

        // Initialisation
        Git git = Git.init().setDirectory(dir).call()

        // Adding the resources
        resources.each { name, content ->
            def file = new File(dir, name)
            file.parentFile.mkdirs()
            file.text = content
            git.add().addFilepattern(name).call()
        }

        // Committing
        git.commit().setMessage("Seed files").setAuthor("Nemerosa", "nemerosa@nemerosa.net").call()

        // OK
        return dir.absolutePath
    }

    static File createTempDir(String id) {
        File file = File.createTempFile(id, '.d')
        FileUtils.forceDelete(file)
        file.mkdirs()
        file.deleteOnExit()
        return file
    }
}