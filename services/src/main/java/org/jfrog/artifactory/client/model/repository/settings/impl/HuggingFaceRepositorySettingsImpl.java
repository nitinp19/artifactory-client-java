package org.jfrog.artifactory.client.model.repository.settings.impl;

import org.jfrog.artifactory.client.model.PackageType;
import org.jfrog.artifactory.client.model.impl.PackageTypeImpl;
import org.jfrog.artifactory.client.model.repository.settings.AbstractRepositorySettings;
import org.jfrog.artifactory.client.model.repository.settings.HuggingFaceRepositorySettings;

public class HuggingFaceRepositorySettingsImpl extends AbstractRepositorySettings implements HuggingFaceRepositorySettings {
    public static String defaultLayout = "simple-default";

    public HuggingFaceRepositorySettingsImpl() {
        super(defaultLayout);
    }

    @Override
    public PackageType getPackageType() {
        return PackageTypeImpl.HuggingFaceML;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof HuggingFaceRepositorySettingsImpl;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
