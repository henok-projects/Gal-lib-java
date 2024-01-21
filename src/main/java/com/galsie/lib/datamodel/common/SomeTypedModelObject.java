package com.galsie.lib.datamodel.common;

import lombok.Getter;

@Getter
public abstract class SomeTypedModelObject extends TypeDefinedObject {
    public SomeTypedModelObject(long id, String definition, String name) {
        super(id, definition, name);
    }
}
