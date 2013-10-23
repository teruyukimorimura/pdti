package gov.hhs.onc.pdti.interceptor;


import org.springframework.core.PriorityOrdered;

public interface DirectoryInterceptor<T, U> extends Comparable<DirectoryInterceptor<T, U>>, PriorityOrdered {
}
