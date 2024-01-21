package com.galsie.lib.datamodel.galsie.types.device;


import com.galsie.lib.datamodel.galsie.types.CategoryType;
import com.galsie.lib.datamodel.matter.types.MTRDeviceType;
import com.galsie.lib.utils.lang.Nullable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class DeviceType extends BaseDeviceType{

    protected CategoryType categoryType;

    public DeviceType(long id, String definition, String name,
                      @Nullable BaseDeviceType extendsDeviceType,
                      List<MTRDeviceType> possibleMTRDevices,
                      CategoryType categoryType) {
        super(id, definition, name, false, extendsDeviceType,
                possibleMTRDevices);
        this.categoryType = categoryType;
    }

}
