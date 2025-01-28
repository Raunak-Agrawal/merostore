package com.merostore.backend.product.domain.Unit;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class UnitConverter implements AttributeConverter<Unit, String> {

    @Override
    public String convertToDatabaseColumn(final Unit attribute) {
        return Optional.ofNullable(attribute).map(Unit::getCode).orElse(null);
    }

    @Override
    public Unit convertToEntityAttribute(final String dbData) {
        return Unit.decode(dbData);
    }
}
