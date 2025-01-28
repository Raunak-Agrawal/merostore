package com.merostore.backend.store.domain.BusinessCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class BusinessCategoryConverter implements AttributeConverter<BusinessCategory, String> {

    @Override
    public String convertToDatabaseColumn(final BusinessCategory attribute) {
        return Optional.ofNullable(attribute).map(BusinessCategory::getCode).orElse(null);
    }

    @Override
    public BusinessCategory convertToEntityAttribute(final String dbData) {
        return BusinessCategory.decode(dbData);
    }
}