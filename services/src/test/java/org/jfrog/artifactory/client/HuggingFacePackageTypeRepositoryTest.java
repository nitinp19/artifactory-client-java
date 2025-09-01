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

public class HuggingFacePackageTypeRepositoryTest extends ArtifactoryTestsBase {

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
        artifactory.repositories().create(0, localRepository);
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

        artifactory.repositories().create(0, federatedRepository);
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
        Repository virtualRepository = repositoryBuilders.virtualRepositoryBuilder()
                .key(virtualRepo)
                .description("new virtual huggingface repo")
                .notes("some notes")
                .repositorySettings(settings)
                .build();

        artifactory.repositories().create(0, virtualRepository);
        Repository virtualRepoFromServer = artifactory.repository(virtualRepo).get();
        assertNotNull(virtualRepoFromServer);
        assertEquals(virtualRepoFromServer.getKey(), virtualRepo);
        assertEquals(virtualRepoFromServer.getDescription(), "new virtual huggingface repo");
        assertEquals(virtualRepoFromServer.getNotes(), "some notes");
    }
}
