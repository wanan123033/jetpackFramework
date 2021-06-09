package com.mindmachine.common.contract;

import com.jetpackframework.base.JetpackApplicationDelegate;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

public class ItemContract {
    /**********************************************项目代码***********************************************************/
    public static final int ITEM_ZWTQQ = 4;  //坐位体前屈
    public static final int ITEM_LDTY = 3;   //立定跳远
    public static final int ITEM_HWJS = 5;   //红外计时
    public static final int ITEM_HWSXQ = 6;  //红外实心球
    public static final int ITEM_JGCJ = 7;   //激光测距
    public static final int ITEM_TS = 8;     //跳绳
    public static final int ITEM_YWQZ = 9;   //仰卧起坐
    /***************************************************************************************************************/
    private static int currentItem;
    private static final Map<Integer,String> personRouter = new HashMap<>();  //个人模式路由器集合
    private static final Map<Integer,String> groupRouter = new HashMap<>();   //分组模式路由器集合
    private static final Map<Integer,String> tcRouter = new HashMap<>();      //体测模式路由器集合
    private static final Map<Integer,String> zyRouter = new HashMap<>();      //自由模式路由器集合
    private static final Map<Integer,String> itemSettingRouter = new HashMap<>();   //考试版项目设置路由器集合
    private static final Map<Integer,String> tcItemSettingRouter = new HashMap<>();   //体测版项目设置路由器集合
    private static final Map<Integer,String> wanRouter = new HashMap<>();   //体测版项目设置路由器集合
    static {
        personRouter.clear();
        groupRouter.clear();
        tcRouter.clear();
        zyRouter.clear();
        tcItemSettingRouter.clear();

        personRouter.put(ITEM_ZWTQQ,RouterContract.ROUTER_ZWTQQ_PERSON);
        personRouter.put(ITEM_LDTY,RouterContract.ROUTER_LDTY_PERSON);
        personRouter.put(ITEM_JGCJ,RouterContract.ROUTER_JGCJ_PERSON);
        personRouter.put(ITEM_TS,RouterContract.ROUTER_TS_PERSON);

        groupRouter.put(ITEM_ZWTQQ,RouterContract.ROUTER_ZWTQQ_GROUP);
        groupRouter.put(ITEM_LDTY,RouterContract.ROUTER_LDTY_GROUP);
        groupRouter.put(ITEM_JGCJ,RouterContract.ROUTER_JGCJ_GROUP);
        groupRouter.put(ITEM_TS,RouterContract.ROUTER_TS_GROUP);

        tcRouter.put(ITEM_ZWTQQ,RouterContract.ROUTER_ZWTQQ_PERSON_TC);

        zyRouter.put(ITEM_ZWTQQ,RouterContract.ROUTER_ZWTQQ_PERSON_ZY);
    }

    public static void setCurrentItem(int currentItem) {
        ItemContract.currentItem = currentItem;
        MMKV mmkv = JetpackApplicationDelegate.getInstance().getMmkv();
        mmkv.putInt(MMKVContract.CURRENT_ITEM_CODE,currentItem);
    }

    public static int getCurrentItemCode() {
        if (currentItem == 0){
            MMKV mmkv = JetpackApplicationDelegate.getInstance().getMmkv();
            currentItem = mmkv.getInt(MMKVContract.CURRENT_ITEM_CODE,currentItem);
        }
        return currentItem;
    }
    /*********************************************考试版路由地址获取******************************************************/
    public static String getPersonRouterUrl(int currentItem){
        return personRouter.get(currentItem);
    }
    public static String getGroupRouterUrl(int currentItem){
        return groupRouter.get(currentItem);
    }
    public static String getItemSettingRouterUrl(int currentItem){
        return itemSettingRouter.get(currentItem);
    }

    /*********************************************体测版路由地址获取******************************************************/
    public static String getTcRouterUrl(int currentItem){
        return tcRouter.get(currentItem);
    }
    public static String getZyRouterUrl(int currentItem){
        return zyRouter.get(currentItem);
    }
    public static String getTcItemSettingRouterUrl(int currentItem){
        return tcItemSettingRouter.get(currentItem);
    }

    public static String getWanRouterUrl(int currentItem){
        return wanRouter.get(currentItem);
    }
}
