package com.galsie.lib.datamodel.matter.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MTRDeviceCluster {
    MTRClusterType clusterType;
    List<MTRAttributeType> requiredAttributes; // required attributes for the device must belong to the MTRClusterType
}
