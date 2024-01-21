package com.galsie.lib.datamodel.matter.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MTRAttributeType {
    private  String definition;
    private MTRAttributeTypeSide side;

}