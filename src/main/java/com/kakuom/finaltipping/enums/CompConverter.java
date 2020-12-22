package com.kakuom.finaltipping.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CompConverter implements AttributeConverter<Comp, String> {
    @Override
    public String convertToDatabaseColumn(Comp attribute) {
        return attribute.getComp();
    }

    @Override
    public Comp convertToEntityAttribute(String dbData) {
        return Comp.getFromDb(dbData);
    }
}
