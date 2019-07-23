package com_receive;

/**
 * AD数据Dao层--存入数据库
 */
public class DataBean {

    private Integer id; //主码
    //private String node;
    private String data; //数据 格式:  1;1254;1234
    private String date; //时间
    private String time; //时间


    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
