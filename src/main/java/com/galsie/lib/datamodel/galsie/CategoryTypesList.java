package com.galsie.lib.datamodel.galsie;

import com.galsie.lib.datamodel.galsie.types.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class CategoryTypesList {

    private List<CategoryType> categoryTypes;

    public List<CategoryType> getCategoriesThatExtend(CategoryType categoryType, boolean includeAbstract){
        return this.categoryTypes.stream().filter(normalCategoryType -> normalCategoryType.doesExtend(categoryType) && (includeAbstract || !normalCategoryType.isAbstract())).toList();
    }
}

