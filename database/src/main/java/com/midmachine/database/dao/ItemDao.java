package com.midmachine.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.midmachine.database.entity.Item;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ItemDao {
    @Insert(onConflict = REPLACE)
    void insert(Item item);
    @Query("select * from item")
    List<Item> getItems();
}
