package com.test.tworldapplication.base;

import android.graphics.Bitmap;

import com.test.tworldapplication.entity.Carousel;
import com.test.tworldapplication.entity.RequestAdmin;
import com.test.tworldapplication.entity.RequestLogin;

/**
 * Created by dasiy on 16/10/17.
 */
public class BaseCom {
    public static Integer authority = 0;
    public static final long DEFAULT_TIMEOUT = 2000000;
    //public static final String BASE_URL = "http://121.46.26.224:8080/newagency/AgencyInterface/";//正式
    public static final String BASE_URL = "http://121.46.26.224:8088/newagency/AgencyInterface/";//测试
    public static final String APP_KEY = "2370E0E98942B9A1";
    public static final String APP_PWD = "205304643532A79F";
    public static final String PASSWORD0 = "HJSJ";
    public static final String PASSWORD1 = "2015GK#S";
    public static final int PARAMENT_LOWER = 20000;
    public static final int PARAMENT_UPER = 29999;
    public static final int OPERATION_LOWER = 30000;
    public static final int OPERATION_UPER = 39999;
    public static final int NORMAL = 10000;
    public static final int NORMAL0 = 30000;
    public static final int VERSIONINCORRENT = 30001;
    public static final int ERROR = 39998;
    public static final int LOSELOG = 39999;
    public static final int INNORMAL = 30006;
    public static Integer page = 1;
    public static Integer linage = 10;
    public static Integer index_home = 0;
    public static Integer index_main = 0;
    public static Carousel[] carousels = null;
    public static String SILENAME = "SILENAME";
    public static String MESSAGE = "MESSAGE";
    public static RequestLogin login = null;//用户等级,用户绑定token的id
    public static RequestAdmin admin = null;//用户信息
    //   public static final String IMG_URL = "http://192/";
    public static final String IMG_URL = "http://pic41.nipic.com/20140509/";
    public static final String SESSION = "";
    public static final String ID = "id";
    public static final String ADMIN = "admin";
    public static String phoneRecharge = "phoneRecharge";//话费充值
    public static String accountRecharge = "accountRecharge";//账户充值
    public static String transform = "transform";//过户
    public static String renewOpen = "renewOpen";//成卡开户
    public static String newOpen = "newOpen";//白卡开户
    public static String replace = "replace";//补卡
    public static String phoneBanlance = "phoneBanlance";//号码余额查询
    public static String accountRecord = "accountRecord";//账户充值查询
    public static String cardQuery = "cardQuery";//过户补卡状态查询
    public static String orderQueryRenew = "orderQueryRenew";//订单查询成卡开户
    public static String orderQueryNew = "orderQueryNew";//订单查询白卡开户
    public static String orderQueryTransform = "orderQueryTransform";//订单查询过户
    public static String orderQueryReplace = "orderQueryReplace";//订单查询补卡
    public static String orderQueryRecharge = "orderQueryRecharge";//订单查询话费充值
    public static String qdsList = "qdsList";//渠道商列表
    public static String qdsOrderList = "qdsOrderList";//渠道商订单列表

    public static String hasPassword = "hasPassword";
    public static int firstLogin = 0;
    public static Bitmap photoThree = null;
    public static Bitmap photoOne = null;
    public static Bitmap photoTwo = null;
    public static Bitmap photoFour = null;
    public static String payMethod = "";

    public static Bitmap videoOne = null;
    public static Bitmap videoTwo = null;
    public static int camera = 0;

}
