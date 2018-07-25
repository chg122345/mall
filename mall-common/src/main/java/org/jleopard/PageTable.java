package org.jleopard;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-06-27 上午9:23
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */

/**
 * layui的数据表格专用
 */
public class PageTable {

    private int code; // 状态码

    private String msg; // 消息

    private Long count;     //数据记录

    private Object data;    //数据


    private PageTable() {
    }

    private PageTable(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static PageTable fail() {

        return new PageTable(400, "失败！");
    }

    public static PageTable success() {

        return new PageTable(0, "成功！");
    }

    public PageTable count(Long count) {
        this.count = count;
        return this;
    }

    public PageTable put(Object data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
