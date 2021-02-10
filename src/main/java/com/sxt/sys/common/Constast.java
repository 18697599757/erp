package com.sxt.sys.common;

public interface Constast {


    public static final Integer OK=200;
    public static final Integer ERROR=-1;
    public static final String USER_DEFAULT_PWD="123456";

    /*
     * 菜单权限类型
     * */
    public static final String  TYPE_MENU = "menu";
    public static final String  TYPE_PERMISSION = "permission";
    /*
     * 可以状态
     * */
    public static final Object AVAILABLE_TRUE=1;
    public static final Object AVAILABLE_FLASE=0;


    /*
     * 用户类型
     * */
    public static final Integer USER_TYPE_SUPER=0;
    public static final Integer USER_TYPE_NOMAL=1;

    /*
     * 展开类型
     * */
    public static final Integer OPEN_TRUE=1;
    public static final Integer OPEN_FALSE=0;
}
