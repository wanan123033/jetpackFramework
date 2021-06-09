package com.midmachine.database;

import com.app.db.AppDatabase;
import com.jetpackframework.ContextUtil;
import com.midmachine.database.entity.Item;
import com.mindmachine.common.contract.ItemContract;

import java.util.List;

import static com.midmachine.database.entity.Item.TEST_TYPE_COUNT;
import static com.midmachine.database.entity.Item.TEST_TYPE_DISTANCE;

public class DBManager {
    private static DBManager dbManager;
    private DBManager(){
        initDB();
    }

    public void initDB() {
        AppDatabase.createDB(ContextUtil.get(),"control.db");
        initDataBaseData();
    }

    private void initDataBaseData() {
        int[] items = {ItemContract.ITEM_ZWTQQ,ItemContract.ITEM_LDTY};
        for (int item : items){
            switch (item){
                case ItemContract.ITEM_ZWTQQ:
                    insertItem(item,String.valueOf(item),"坐位体前屈","次",TEST_TYPE_COUNT);
                    break;
                case ItemContract.ITEM_LDTY:
                    insertItem(item,String.valueOf(item),"立定跳远","厘米",TEST_TYPE_DISTANCE);
                    break;
            }
        }
    }

    private void insertItem(int machineCode, String itemCode, String itemName, String unit, int itemType) {
        Item item = new Item();
        item.machineCode = machineCode;
        item.itemCode = itemCode;
        item.itemName = itemName;
        item.unit = unit;
        item.testType = itemType;
        AppDatabase.getItemDao().insert(item);
    }

    public static synchronized DBManager getInstance(){
        if (dbManager == null){
            dbManager = new DBManager();
        }
        dbManager.initDB();
        return dbManager;
    }

    public List<Item> getItems() {
        return AppDatabase.getItemDao().getItems();
    }
}
