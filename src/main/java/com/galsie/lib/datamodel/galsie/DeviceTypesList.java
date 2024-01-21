package com.galsie.lib.datamodel.galsie;

import com.galsie.lib.datamodel.galsie.types.device.AbstractDeviceType;
import com.galsie.lib.datamodel.galsie.types.device.BaseDeviceType;
import com.galsie.lib.datamodel.galsie.types.device.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class DeviceTypesList {

    private List<AbstractDeviceType> abstractDeviceTypes;
    private List<DeviceType> normalDeviceTypes;

}