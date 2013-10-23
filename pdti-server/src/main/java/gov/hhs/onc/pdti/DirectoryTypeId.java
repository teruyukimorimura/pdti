package gov.hhs.onc.pdti;

public enum DirectoryTypeId implements DirectoryEnumId {
    MAIN("main"), FEDERATED("federated");

    private String id;

    DirectoryTypeId(String id) {
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
