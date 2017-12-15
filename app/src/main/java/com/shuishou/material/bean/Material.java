package com.shuishou.material.bean;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Administrator on 2017-12-06.
 */

//@Table("material")
public class Material implements Serializable {
    @SerializedName(value = "id")
    private int id;

    private String name;

    private int sequence;

    private double leftAmount;

    private String unit;

    private double alarmAmount;

    private int alarmStatus;

    private MaterialCategory materialCategory;

    private String barCode;

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

    public double getLeftAmount() {
        return leftAmount;
    }

    public void setLeftAmount(double leftAmount) {
        this.leftAmount = leftAmount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getAlarmAmount() {
        return alarmAmount;
    }

    public void setAlarmAmount(double alarmAmount) {
        this.alarmAmount = alarmAmount;
    }

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public MaterialCategory getMaterialCategory() {
        return materialCategory;
    }

    public void setMaterialCategory(MaterialCategory materialCategory) {
        this.materialCategory = materialCategory;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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
        Material other = (Material) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Material [name=" + name + "]";
    }



}
