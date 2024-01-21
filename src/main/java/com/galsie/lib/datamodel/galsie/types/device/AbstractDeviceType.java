package com.galsie.lib.datamodel.galsie.types.device;


import com.galsie.lib.datamodel.matter.types.MTRDeviceType;
import com.galsie.lib.utils.lang.Nullable;

import java.util.List;

/*
*  Abstract device types don't have a category
* */
public class AbstractDeviceType extends BaseDeviceType{
    public AbstractDeviceType(long id, String definition, String name, @Nullable BaseDeviceType extendsDeviceType, List<MTRDeviceType> possibleMTRDevices) {
        super(id, definition, name, true, extendsDeviceType, possibleMTRDevices);
    }
}
