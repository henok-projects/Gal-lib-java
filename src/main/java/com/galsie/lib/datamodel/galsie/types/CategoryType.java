package com.galsie.lib.datamodel.galsie.types;


import com.galsie.lib.datamodel.common.SomeTypedModelObject;
import com.galsie.lib.utils.lang.Nullable;
import lombok.Getter;

@Getter
public class CategoryType extends SomeTypedModelObject {
    boolean isAbstract;

    @Nullable
    CategoryType extendsCategory;

    public CategoryType(long id, String definition, String name, boolean isAbstract, @Nullable CategoryType extendsCategory) {
        super(id, definition, name);
        this.isAbstract = isAbstract;
        this.extendsCategory = extendsCategory;
    }

    public boolean doesExtend(CategoryType categoryType){
        return extendsCategory.equals(categoryType);
    }

}
