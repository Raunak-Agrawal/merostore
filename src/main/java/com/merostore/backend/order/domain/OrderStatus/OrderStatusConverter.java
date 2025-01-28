package com.merostore.backend.order.domain.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(final OrderStatus attribute) {
        return Optional.ofNullable(attribute).map(OrderStatus::getCode).orElse(null);
    }

    @Override
    public OrderStatus convertToEntityAttribute(final String dbData) {
        return OrderStatus.decode(dbData);
    }
}
