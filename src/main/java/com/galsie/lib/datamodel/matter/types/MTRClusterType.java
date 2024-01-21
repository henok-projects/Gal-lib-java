package com.galsie.lib.datamodel.matter.types;


import com.galsie.lib.datamodel.common.OptionalRequirementType;
import com.galsie.lib.datamodel.common.SomeTypedModelObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MTRClusterType extends SomeTypedModelObject {
    private Map<OptionalRequirementType, List<MTRAttributeType>> attributes;

    public MTRClusterType(long id, String definition, String name, Map<OptionalRequirementType, List<MTRAttributeType>> attributes) {
        super(id, definition, name);
        this.attributes = attributes;
    }

    public List<MTRAttributeType> getAllAttributes(){
        return attributes.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
