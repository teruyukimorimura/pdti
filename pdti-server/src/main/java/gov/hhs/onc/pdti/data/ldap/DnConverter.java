package gov.hhs.onc.pdti.data.ldap;

import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component("dnConverter")
@Scope("singleton")
public class DnConverter implements Converter<String, Dn> {
    @Override
    public Dn convert(String source) {
        try {
            return new Dn(source);
        } catch (LdapInvalidDnException e) {
            throw new ConversionFailedException(TypeDescriptor.forObject(source), TypeDescriptor.valueOf(Dn.class), source, e);
        }
    }
}
