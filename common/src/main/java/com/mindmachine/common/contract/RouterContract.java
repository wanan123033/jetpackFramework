package com.mindmachine.common.contract;

public interface RouterContract {
    String PACKAGE_NAME = "://${packageName}/";
    /*****************************************主工程appmodule*************************************************************/
    String APP_MAIN_MODULE = "appmodule";
    String MAINACTIVITY = "app/mainActivity";
    String SPLSHACTIVITY = "app/splshActivity";
    String DEVICEOUTACTIVITY = "app/deviceOutActivity";
    String PARAMSETTINGACTIVITY = "app/paramSettingActivity";
    String LOGINACTIVITY = "app/loginActivity";
    String LEDACTIVITY = "app/ledActivity";
    String GROUPTESTACTIVITY = "app/groupTestActivity";
    String TCDEVICEOUTACTIVITY = "app/tcDeviceOutActivity";

    String ROUTER_MAINACTIVITY = APP_MAIN_MODULE + PACKAGE_NAME + MAINACTIVITY;
    String ROUTER_DEVICEOUTACTIVITY = APP_MAIN_MODULE + PACKAGE_NAME + DEVICEOUTACTIVITY;
    String ROUTER_PARAMSETTINGACTIVITY = APP_MAIN_MODULE + PACKAGE_NAME + PARAMSETTINGACTIVITY;
    String ROUTER_LOGINACTIVITY = APP_MAIN_MODULE + PACKAGE_NAME + LOGINACTIVITY;
    String ROUTER_LEDACTIVITY = APP_MAIN_MODULE + PACKAGE_NAME + LEDACTIVITY;
    String ROUTER_GROUPTESTACTIVITY = APP_MAIN_MODULE + PACKAGE_NAME + GROUPTESTACTIVITY;
    String ROUTER_TCDEVICEOUTACTIVITY = APP_MAIN_MODULE + PACKAGE_NAME + TCDEVICEOUTACTIVITY;

    /************************************坐位体前屈zwtqq*****************************************************************/
    String APP_MAIN_ZWTQQ = "zwtqq";
    String ZWTQQPERSONACTIVITY = "app/zwtqqPersonActivity";
    String ZWTQQGROUPACTIVITY = "app/zwtqqGroupActivity";
    String ZWTQQTCACTIVITY = "app/zwtqqTcActivity";
    String ZWTQQZYACTIVITY = "app/zwtqqZyActivity";
    String ROUTER_ZWTQQ_PERSON = APP_MAIN_ZWTQQ + PACKAGE_NAME + ZWTQQPERSONACTIVITY;
    String ROUTER_ZWTQQ_GROUP = APP_MAIN_ZWTQQ + PACKAGE_NAME + ZWTQQGROUPACTIVITY;
    String ROUTER_ZWTQQ_PERSON_TC = APP_MAIN_ZWTQQ + PACKAGE_NAME + ZWTQQTCACTIVITY;
    String ROUTER_ZWTQQ_PERSON_ZY = APP_MAIN_ZWTQQ + PACKAGE_NAME + ZWTQQZYACTIVITY;
    /************************************立定跳远ldty*****************************************************************/
    String APP_MAIN_LDTY = "ldty";
    String LDTYPERSONACTIVITY = "app/ldtyPersonActivity";
    String LDTYGROUPACTIVITY = "app/ldtyGroupActivity";
    String ROUTER_LDTY_PERSON = APP_MAIN_LDTY + PACKAGE_NAME + LDTYPERSONACTIVITY;
    String ROUTER_LDTY_GROUP = APP_MAIN_LDTY + PACKAGE_NAME + LDTYGROUPACTIVITY;

    /************************************激光测距jgcj*****************************************************************/
    String APP_MAIN_JGCJ = "jgcj";
    String JGCJPERSONACTIVITY = "app/jgcjPersonActivity";
    String JGCJGROUPACTIVITY = "app/jgcjGroupActivity";
    String ROUTER_JGCJ_PERSON = APP_MAIN_JGCJ + PACKAGE_NAME + JGCJPERSONACTIVITY;
    String ROUTER_JGCJ_GROUP = APP_MAIN_JGCJ + PACKAGE_NAME + JGCJGROUPACTIVITY;

    /************************************跳绳ts*****************************************************************/
    String APP_MAIN_TS = "ts";
    String TSPERSONACTIVITY = "app/tsPersonActivity";
    String TSGROUPACTIVITY = "app/tsGroupActivity";
    String ROUTER_TS_PERSON = APP_MAIN_TS + PACKAGE_NAME + TSPERSONACTIVITY;
    String ROUTER_TS_GROUP = APP_MAIN_TS + PACKAGE_NAME + TSGROUPACTIVITY;

    /***********************************数据管理与查询dataselectmanager****************************************************/
    String APP_MAIN_DATASELECTMANAGER = "dataselectmanager";
    String DATAMANAGERACTIVITY = "app/dataManagerActivity";
    String DATASELECTACTIVITY = "app/dataSelectActivity";
    String TEMPERATURESELECTACTIVITY = "app/temperatureSelectActivity";
    String ROUTER_DATAMANAGERACTIVITY = APP_MAIN_DATASELECTMANAGER + PACKAGE_NAME + DATAMANAGERACTIVITY;
    String ROUTER_DATASELECTACTIVITY = APP_MAIN_DATASELECTMANAGER + PACKAGE_NAME + DATASELECTACTIVITY;
    String ROUTER_TEMPERATURESELECTACTIVITY = APP_MAIN_DATASELECTMANAGER + PACKAGE_NAME + TEMPERATURESELECTACTIVITY;

    /**************************************文件管理器fileapp*********************************************************************/
    String APP_MAIN_FILEAPP = "fileexpoter";
    String FILEEXPORTACTIVITY = "app/fileExportActivity";
    String FILEEXPORTFRAGMENT = "app/fileExportFragment";
    String ROUTER_FILEEXPORTACTIVITY = APP_MAIN_FILEAPP + PACKAGE_NAME + FILEEXPORTACTIVITY;
    String ROUTER_FILEEXPORTFRAGMENT = APP_MAIN_FILEAPP + PACKAGE_NAME + FILEEXPORTFRAGMENT;



}
