package com.midmachine.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "itemName", unique = true)})
public class Item {
    public static final int TEST_TYPE_TIME = 1;//项目类型 计时
    public static final int TEST_TYPE_COUNT = 2;//项目类型 计数
    public static final int TEST_TYPE_DISTANCE = 3;//项目类型 远度
    public static final int TEST_TYPE_POWER = 4;//项目类型 力量
    public static final int TEST_TYPE_SCORE = 5;//项目类型 计分

    @PrimaryKey(autoGenerate = true)
    public Long id;
    @ColumnInfo
    public String itemCode;
    @ColumnInfo
    public String itemName;
    @ColumnInfo
    public int machineCode;
    @ColumnInfo
    public String unit;
    @ColumnInfo
    public int digital;//保留小数位数
    @ColumnInfo
    public int testNum;//项目测试次数	表示一次检录的测试次数	 默认1次
    @ColumnInfo
    public int carryMode;//进位方式	不取舍,四舍五入,非零进一,非零舍去  与显示成绩有关 （0.不去舍，1.四舍五入 2.舍位 3.非零进取）
    @ColumnInfo
    public int testType;    //项目测量方式,1计时，2计数，3远度，4力量(重量)
    @ColumnInfo
    public int lastResultMode;//最终成绩选择模式 （1.最后成绩，2.补考成绩，3.最好）
    @ColumnInfo
    public int minValue;//最小值
    @ColumnInfo
    public int maxValue;//最大值
//    @ColumnInfo
//    public int remark1;
//    @ColumnInfo
//    public int remark2;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", machineCode=" + machineCode +
                ", unit='" + unit + '\'' +
                ", digital=" + digital +
                ", testNum=" + testNum +
                ", carryMode=" + carryMode +
                ", testType=" + testType +
                ", lastResultMode=" + lastResultMode +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                '}';
    }
}
