package org.jfrog.artifactory.client;

import org.jfrog.artifactory.client.model.Repository;
import org.jfrog.artifactory.client.model.RepositoryType;
import org.jfrog.artifactory.client.model.builder.RepositoryBuilders;
import org.jfrog.artifactory.client.model.impl.PackageTypeImpl;
import org.jfrog.artifactory.client.model.repository.settings.HuggingFaceRepositorySettings;
import org.jfrog.artifactory.client.model.repository.settings.RepositorySettings;
import org.jfrog.artifactory.client.model.repository.settings.impl.HuggingFaceRepositorySettingsImpl;
import org.testng.annotations.AfterMethod;
import org.testng.SkipException;
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

    @Override
    public RepositorySettings getRepositorySettings(RepositoryType repositoryType) {
        return new HuggingFaceRepositorySettingsImpl();
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
        Repository localRepoFromServer = artifactory.repository(localRepo).get();
        
        assertNotNull(localRepoFromServer);
        assertEquals(localRepoFromServer.getKey(), localRepo);
        assertEquals(localRepoFromServer.getDescription(), "new local huggingface repo");
        assertEquals(localRepoFromServer.getNotes(), "some notes");
        assertEquals(localRepoFromServer.getRepoLayoutRef(), "simple-default");
        assertEquals(localRepoFromServer.getRepositorySettings().getPackageType(), PackageTypeImpl.huggingfaceml);
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
        Repository federatedRepoFromServer = artifactory.repository(federatedRepo).get();
        assertNotNull(federatedRepoFromServer);
        assertEquals(federatedRepoFromServer.getKey(), federatedRepo);
        assertEquals(federatedRepoFromServer.getDescription(), "new federated huggingface repo");
        assertEquals(federatedRepoFromServer.getNotes(), "some notes");
    }
}
