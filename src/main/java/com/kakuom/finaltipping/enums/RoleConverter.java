package com.kakuom.finaltipping.enums;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {
    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute.getShortRole();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return Role.getFromDb(dbData);
    }
}
