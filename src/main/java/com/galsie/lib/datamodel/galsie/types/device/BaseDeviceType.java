package com.galsie.lib.datamodel.galsie.types.device;

import com.galsie.lib.datamodel.common.SomeTypedModelObject;
import com.galsie.lib.datamodel.matter.types.MTRDeviceType;
import com.galsie.lib.utils.lang.Nullable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class BaseDeviceType extends SomeTypedModelObject {

    protected boolean isAbstract;

    @Nullable
    protected BaseDeviceType extendsDeviceType;

    protected List<MTRDeviceType> possibleMTRDeviceTypes;

    protected BaseDeviceType(long id, String definition, String name, boolean isAbstract,
                             @Nullable BaseDeviceType extendsDeviceType,
                             List<MTRDeviceType> possibleMTRDeviceTypes) {
        super(id, definition, name);
        this.isAbstract = isAbstract;
        this.extendsDeviceType = extendsDeviceType;
        this.possibleMTRDeviceTypes = possibleMTRDeviceTypes;
    }


    /**
     * Includes those of the parent classes
     * @return The list of {@link MTRDeviceType}s including this of the extended device types
     */
    public List<MTRDeviceType> getAllPossibleMTRDeviceTypes(){
        var allPossibleMTRDeviceTypes = new ArrayList<>(this.getPossibleMTRDeviceTypes());
        if (this.extendsDeviceType != null) {
            allPossibleMTRDeviceTypes.addAll(extendsDeviceType.getAllPossibleMTRDeviceTypes());
        }
        return allPossibleMTRDeviceTypes;
    }

}
