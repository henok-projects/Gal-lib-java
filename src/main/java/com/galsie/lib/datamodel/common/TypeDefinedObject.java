package com.galsie.lib.datamodel.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TypeDefinedObject {
    protected long id;
    protected String definition;
    protected String name;


    @Override
    public boolean equals(Object o){
        if (!(o instanceof TypeDefinedObject typeDefinedObject)){
            return false;
        }
        return typeDefinedObject.id == this.id && typeDefinedObject.definition.equals(this.definition) && typeDefinedObject.name.equals(this.name);
    }

}
