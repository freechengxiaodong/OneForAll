package com.hogwarts.constants;

/**
 * 公共常量
 *
 * @author chushangming
 * @since 2022-01-21
 */
public interface Common {

    /**
     * 分页查询最小页数
     */
    public static final int MIN_PAGE_NUM = 1;

    /**
     * 分页查询最大页数
     */
    public static final int MAX_PAGE_NUM = 100;

    /**
     * 分页查询单页最大数据量
     */
    public static final int MAX_PAGE_SIZE = 10000;

    /**
     * 分页查询最大合计数据量
     */
    public static final int MAX_TOTAL_PAGE_SIZE = 100000;

    /**
     * 退款成功标识
     */
    public static final int REFUND_SUCCESS = 200;
    /**
     * 支付成功标识
     */
    public static final int PAY_ORDER_SUCCESS = 200;

    /**
     * 下支付单成功标识
     */
    public static final int TO_PAY_SUCCESS = 200;

    /**
     * 下支付单无响应标识
     */
    public static final int TO_PAY_EMPTY_RESULT = 404;

    /**
     * 下支付单失败标识
     */
    public static final int TO_PAY_ORDER_FAIL = 500;

    /**
     * 已逻辑删除
     */
    public static final int DELETED = 1;

    /**
     * 未逻辑删除
     */
    public static final int NOT_DELETED = 0;

    /**
     * 网络请求成功标识
     */
    public static final int REQUEST_SUCCESS = 200;

    /**
     * 支付渠道 ：飞凡通
     */
    public static int ACTIVITYCHN_FFANPAY = 1001;

    public static int ACTIVITYCHN_WECHATPAY = 1003;

    /**
     * 营销的支付方式 ：银用卡
     */
    public static int ACTIVITYPAYSORT_CREDITCARD = 1;

    /**
     * 营销的支付方式 ：信用卡
     */
    public static int ACTIVITYPAYSORT_DEBITCARD = 2;

    /**
     * 营销的支付方式 ：零花钱
     */
    public static int ACTIVITYPAYSORT_BALANCE = 1104;

    /**
     * 营销的支付方式 ：快意花
     */
    public static int ACTIVITYPAYSORT_KUAIYIHUA = 1105;

}
