package gov.hhs.onc.pdti.context;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.spi.ThrowableRenderer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("dirThrowableRenderer")
@Scope("singleton")
public class DirectoryThrowableRenderer implements ThrowableRenderer {
    @Override
    public String[] doRender(Throwable th) {
        return ExceptionUtils.getRootCauseStackTrace(th);
    }
}
