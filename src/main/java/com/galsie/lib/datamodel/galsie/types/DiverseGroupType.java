package com.galsie.lib.datamodel.galsie.types;

import com.galsie.lib.datamodel.common.SomeTypedModelObject;
import com.galsie.lib.datamodel.galsie.types.device.DeviceType;
import lombok.Getter;
import java.util.List;

@Getter
public class DiverseGroupType extends SomeTypedModelObject {
    private final boolean isSystemGroup;
    private final List<DeviceType> possibleDeviceTypes;
    public DiverseGroupType(long id, String definition, String name, boolean isSystemGroup, List<DeviceType> possibleDeviceTypes) {
        super(id, definition, name);
        this.isSystemGroup = isSystemGroup;
        this.possibleDeviceTypes = possibleDeviceTypes;
    }
}
