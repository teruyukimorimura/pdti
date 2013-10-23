package gov.hhs.onc.pdti.interceptor.impl;


import gov.hhs.onc.pdti.interceptor.DirectoryInterceptor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

public class AbstractDirectoryInterceptor<T, U> extends AnnotationAwareOrderComparator implements DirectoryInterceptor<T, U> {
    @Override
    public int getOrder() {
        Order order = AnnotationUtils.findAnnotation(this.getClass(), Order.class);

        return (order != null) ? order.value() : Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public int compareTo(DirectoryInterceptor<T, U> dirInterceptor) {
        return this.compare(this, dirInterceptor);
    }
}
