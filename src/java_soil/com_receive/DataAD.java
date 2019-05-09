package com_receive;

public class DataAD {
    private Integer id;
    private String node; //节点
    private String AD; //AD值
    private String baseAD; //基准电压
    private String time; //时间
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getAD() {
        return AD;
    }

    public void setAD(String AD) {
        this.AD = AD;
    }

    public String getBaseAD() {
        return baseAD;
    }

    public void setBaseAD(String baseAD) {
        this.baseAD = baseAD;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
