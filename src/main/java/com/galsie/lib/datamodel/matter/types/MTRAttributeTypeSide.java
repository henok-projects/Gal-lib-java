package com.galsie.lib.datamodel.matter.types;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum MTRAttributeTypeSide {
    SERVER("server"), CLIENT("client");

    private final String typeXmlName;
    private MTRAttributeTypeSide(String typeXmlName){
        this.typeXmlName = typeXmlName;
    }


    public static Optional<MTRAttributeTypeSide> fromXmlName(String typeXmlName){
        for (MTRAttributeTypeSide side: MTRAttributeTypeSide.values()){
            if (side.typeXmlName.equals(typeXmlName)){
                return Optional.of(side);
            }
        }
        return Optional.empty();
    }

}