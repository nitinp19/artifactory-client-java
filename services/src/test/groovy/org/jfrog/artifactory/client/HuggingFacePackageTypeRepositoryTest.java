package org.jfrog.artifactory.client;

import org.jfrog.artifactory.client.model.Repository;
import org.jfrog.artifactory.client.model.builder.RepositoryBuilders;
import org.jfrog.artifactory.client.model.impl.PackageTypeImpl;
import org.jfrog.artifactory.client.model.repository.settings.HuggingFaceRepositorySettings;
import org.jfrog.artifactory.client.model.repository.settings.impl.HuggingFaceRepositorySettingsImpl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class HuggingFacePackageTypeRepositoryTest extends BaseRepositoryTests {

    private final String localRepo = "huggingface-local-1";
    private final String federatedRepo = "huggingface-federated-1";
    private final String virtualRepo = "huggingface-virtual-1";

    @BeforeMethod
    @AfterMethod
    private void cleanup() {
        deleteRepoIfExists(localRepo);
        deleteRepoIfExists(federatedRepo);
        deleteRepoIfExists(virtualRepo);
    }

    @Test
    public void testHuggingFaceLocalRepo() {
        HuggingFaceRepositorySettings settings = new HuggingFaceRepositorySettingsImpl();
        RepositoryBuilders repositoryBuilders = artifactory.repositories().builders();
        Repository localRepository = repositoryBuilders.localRepositoryBuilder()
                .key(localRepo)
                .description("new local huggingface repo")
                .notes("some notes")
                .repositorySettings(settings)
                .build();

        // Use position 0 for create, as required by the interface
        String localCreateResult = artifactory.repositories().create(0, localRepository);
        System.out.println("[DIAG] create(local) result: " + localCreateResult);
        printEnvironmentDiagnostics();
        printRepositoryJson(localRepo);
        Repository localRepoFromServer = artifactory.repository(localRepo).get();
        assertNotNull(localRepoFromServer);
        assertEquals(localRepoFromServer.getKey(), localRepo);
        assertEquals(localRepoFromServer.getDescription(), "new local huggingface repo");
        assertEquals(localRepoFromServer.getNotes(), "some notes");
        assertEquals(localRepoFromServer.getRepoLayoutRef(), "simple-default");
        assertEquals(localRepoFromServer.getRepositorySettings().getPackageType(), PackageTypeImpl.HuggingFaceML);
    }

    @Test
    public void testHuggingFaceFederatedRepo() {
        HuggingFaceRepositorySettings settings = new HuggingFaceRepositorySettingsImpl();
        RepositoryBuilders repositoryBuilders = artifactory.repositories().builders();
        Repository federatedRepository = repositoryBuilders.federatedRepositoryBuilder()
                .key(federatedRepo)
                .description("new federated huggingface repo")
                .notes("some notes")
                .repositorySettings(settings)
                .build();

        String federatedCreateResult = artifactory.repositories().create(0, federatedRepository);
        System.out.println("[DIAG] create(federated) result: " + federatedCreateResult);
        printEnvironmentDiagnostics();
        printRepositoryJson(federatedRepo);
        Repository federatedRepoFromServer = artifactory.repository(federatedRepo).get();
        assertNotNull(federatedRepoFromServer);
        assertEquals(federatedRepoFromServer.getKey(), federatedRepo);
        assertEquals(federatedRepoFromServer.getDescription(), "new federated huggingface repo");
        assertEquals(federatedRepoFromServer.getNotes(), "some notes");
    }

    @Test
    public void testHuggingFaceVirtualRepo() {
        HuggingFaceRepositorySettings settings = new HuggingFaceRepositorySettingsImpl();
        RepositoryBuilders repositoryBuilders = artifactory.repositories().builders();
        // Ensure we have at least one backing repo to include in the virtual
        Repository localForVirtual = repositoryBuilders.localRepositoryBuilder()
                .key(localRepo)
                .description("local for virtual huggingface repo")
                .notes("some notes")
                .repositorySettings(settings)
                .build();
        artifactory.repositories().create(0, localForVirtual);
        Repository virtualRepository = repositoryBuilders.virtualRepositoryBuilder()
                .key(virtualRepo)
                .description("new virtual huggingface repo")
                .notes("some notes")
                .repositorySettings(settings)
                .repositories(java.util.Collections.singletonList(localRepo))
                .build();

        String virtualCreateResult = artifactory.repositories().create(0, virtualRepository);
        System.out.println("[DIAG] create(virtual) result: " + virtualCreateResult);
        printEnvironmentDiagnostics();
        printRepositoryJson(virtualRepo);
        Repository virtualRepoFromServer = artifactory.repository(virtualRepo).get();
        assertNotNull(virtualRepoFromServer);
        assertEquals(virtualRepoFromServer.getKey(), virtualRepo);
        assertEquals(virtualRepoFromServer.getDescription(), "new virtual huggingface repo");
        assertEquals(virtualRepoFromServer.getNotes(), "some notes");
    }

    private void printEnvironmentDiagnostics() {
        try {
            String versionJson = curl("api/system/version");
            System.out.println("[DIAG] Artifactory version: " + versionJson);
        } catch (Exception e) {
            System.out.println("[DIAG] Failed to fetch version: " + e.getMessage());
        }
        try {
            String licensesJson = curl("api/system/licenses");
            System.out.println("[DIAG] Artifactory licenses: " + licensesJson);
        } catch (Exception e1) {
            try {
                String licenseJson = curl("api/system/license");
                System.out.println("[DIAG] Artifactory license: " + licenseJson);
            } catch (Exception e2) {
                System.out.println("[DIAG] Failed to fetch license info: " + e2.getMessage());
            }
        }
    }

    private void printRepositoryJson(String repoKey) {
        try {
            String repoJson = curl("api/repositories/" + repoKey);
            System.out.println("[DIAG] Repository JSON (" + repoKey + "): " + repoJson);
        } catch (Exception e) {
            System.out.println("[DIAG] Failed to fetch repo JSON for '" + repoKey + "': " + e.getMessage());
        }
    }
}
