package com.galsie.lib.datamodel.matter.types;

import com.galsie.lib.datamodel.common.OptionalRequirementType;
import com.galsie.lib.datamodel.common.SomeTypedModelObject;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
public class MTRDeviceType extends SomeTypedModelObject {
    private final long defaultId; // default id given by the matter model
    private final Map<OptionalRequirementType, List<MTRDeviceCluster>> clusters;
    public MTRDeviceType(long id, long defaultId, String definition, String name, Map<OptionalRequirementType, List<MTRDeviceCluster>> clusters) {
        super(id, definition, name);
        this.defaultId = defaultId;
        this.clusters = clusters;
    }

    public List<MTRDeviceCluster> getClusters(OptionalRequirementType requirementType){
        return this.clusters.get(requirementType);
    }

    public List<MTRDeviceCluster> getAllClusters(){
        return clusters.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
