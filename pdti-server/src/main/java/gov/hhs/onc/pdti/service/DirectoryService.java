package gov.hhs.onc.pdti.service;

public interface DirectoryService<T, U> {
    public U processRequest(T queryReq);
}
