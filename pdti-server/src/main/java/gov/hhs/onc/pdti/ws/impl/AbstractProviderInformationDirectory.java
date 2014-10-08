package gov.hhs.onc.pdti.ws.impl;

import gov.hhs.onc.pdti.service.DirectoryService;

public abstract class AbstractProviderInformationDirectory<T, U> {
    protected DirectoryService<T, U> dirService;

    public abstract void setDirectoryService(DirectoryService<T, U> dirService);
}
