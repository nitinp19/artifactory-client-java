package org.jfrog.artifactory.client;

import org.jfrog.artifactory.client.model.PackageType;
import org.jfrog.artifactory.client.model.impl.PackageTypeImpl;
import org.jfrog.artifactory.client.model.repository.settings.impl.HuggingFaceRepositorySettingsImpl;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test for HuggingFace repository settings implementation
 */
public class HuggingFaceRepositorySettingsTest {

    @Test
    public void testHuggingFaceRepositorySettings() {
        HuggingFaceRepositorySettingsImpl settings = new HuggingFaceRepositorySettingsImpl();

        // Test package type
        PackageType packageType = settings.getPackageType();
        assertEquals(packageType, PackageTypeImpl.HuggingFaceML);

        // Test default layout
        assertEquals(settings.getRepoLayout(), "simple-default");

        // Test layout setting
        settings.setRepoLayout("custom-layout");
        assertEquals(settings.getRepoLayout(), "custom-layout");

        // Test that it's not a custom package type
        assertFalse(packageType.isCustom());
    }

    @Test
    public void testConsistencyWithOtherPackageManagers() {
        HuggingFaceRepositorySettingsImpl huggingFaceSettings = new HuggingFaceRepositorySettingsImpl();

        // Verify it follows the same interface pattern as other package managers
        assertNotNull(huggingFaceSettings.getPackageType());
        assertNotNull(huggingFaceSettings.getRepoLayout());

        // Test that the default layout is properly set
        assertEquals(HuggingFaceRepositorySettingsImpl.defaultLayout, "simple-default");
    }
}
