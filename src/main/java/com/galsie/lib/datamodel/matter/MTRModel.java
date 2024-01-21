package com.galsie.lib.datamodel.matter;

import com.galsie.lib.datamodel.matter.types.MTRClusterControlType;
import com.galsie.lib.datamodel.matter.types.MTRClusterType;
import com.galsie.lib.datamodel.matter.types.MTRDeviceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MTRModel {
    private List<MTRClusterType> clusters;
    private List<MTRClusterControlType> clusterControlTypes;
    private List<MTRDeviceType> mtrDeviceTypes;
}
