package com.galsie.lib.datamodel.galsie;

import com.galsie.lib.datamodel.galsie.types.DiverseGroupType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/*
*
* The galModel objects do not have setters because they are not allowed to be mutable through the life cycle.
  So the life cycle is basically just them being loaded.
*
* */
@AllArgsConstructor
@Getter
public class GalModel {

    private CategoryTypesList categoryTypesList;

    private List<DiverseGroupType> diverseGroups;

    private DeviceTypesList deviceTypes;

}
