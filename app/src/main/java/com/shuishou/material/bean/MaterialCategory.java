package com.shuishou.material.bean;

import com.google.gson.annotations.SerializedName;
//import com.litesuits.orm.db.annotation.Column;
//import com.litesuits.orm.db.annotation.Mapping;
//import com.litesuits.orm.db.annotation.PrimaryKey;
//import com.litesuits.orm.db.annotation.Table;
//import com.litesuits.orm.db.enums.AssignType;
//import com.litesuits.orm.db.enums.Relation;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017-12-06.
 */

//@Table("material_category")
public class MaterialCategory implements Serializable {

//    @SerializedName(value = "id")
//    @PrimaryKey(value = AssignType.BY_MYSELF)
    private int id;

//    @Column("name")
    private String name;

//    @Column("sequence")
    private int sequence;

//    @SerializedName(value = "materials")
//    @Mapping(Relation.OneToMany)
    private List<Material> materials;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MaterialCategory other = (MaterialCategory) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MaterialCategory [name=" + name + "]";
    }



}

