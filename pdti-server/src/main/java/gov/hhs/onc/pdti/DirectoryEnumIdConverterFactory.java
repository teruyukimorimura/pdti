package gov.hhs.onc.pdti;

import java.util.EnumSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component("dirEnumIdConverterFactory")
@Scope("singleton")
public class DirectoryEnumIdConverterFactory implements ConverterFactory<String, Enum> {
    private static class DirectoryEnumIdConverter<T extends Enum<T>> implements Converter<String, T> {
        private Class<T> dirEnumClass;

        public DirectoryEnumIdConverter(Class<T> dirEnumClass) {
            this.dirEnumClass = dirEnumClass;
        }

        @Override
        public T convert(String source) {
            for (T dirEnumId : EnumSet.allOf(this.dirEnumClass)) {
                if (StringUtils.equalsIgnoreCase(((DirectoryEnumId) dirEnumId).getId(), source)) {
                    return dirEnumId;
                }
            }

            return null;
        }
    }

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new DirectoryEnumIdConverter(targetType);
    }
}
