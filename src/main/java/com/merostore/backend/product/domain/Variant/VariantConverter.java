package com.merostore.backend.product.domain.Variant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class VariantConverter implements AttributeConverter<VariantType, String> {

    @Override
    public String convertToDatabaseColumn(final VariantType attribute) {
        return Optional.ofNullable(attribute).map(VariantType::getCode).orElse(null);
    }

    @Override
    public VariantType convertToEntityAttribute(final String dbData) {
        return VariantType.decode(dbData);
    }
}
