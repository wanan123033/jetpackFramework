package com.midmachine.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Student {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo
    public String stuName;
    @ColumnInfo
    public String stuCode;
}
