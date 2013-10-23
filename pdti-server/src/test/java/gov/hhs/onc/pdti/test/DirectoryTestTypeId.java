package gov.hhs.onc.pdti.test;

import gov.hhs.onc.pdti.DirectoryEnumId;

public enum DirectoryTestTypeId implements DirectoryEnumId {
    DUPLICATE_REQUEST_ID("duplicate_request_id");

    private String id;

    DirectoryTestTypeId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
