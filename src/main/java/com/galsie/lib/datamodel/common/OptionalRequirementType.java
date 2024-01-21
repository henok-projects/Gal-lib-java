package com.galsie.lib.datamodel.common;

import lombok.Getter;

@Getter
public enum OptionalRequirementType {
    OPTIONAL("optional"), REQUIRED("required");

    private String xmlTagName;
    private OptionalRequirementType(String xmlTagName){
        this.xmlTagName = xmlTagName;
    }

    public static OptionalRequirementType ofBoolean(boolean required) {
        return required ? REQUIRED : OPTIONAL;
    }
}
