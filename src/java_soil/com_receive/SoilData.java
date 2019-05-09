package com_receive;

public class SoilData {
    /**
     * 土壤水势信息
     */
    private Integer Id;
    private Double Data;
    private String Init_time;
    private String update_time;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Double getData() {
        return Data;
    }

    public void setData(Double data) {
        Data = data;
    }

    public String getInit_time() {
        return Init_time;
    }

    public void setInit_time(String init_time) {
        Init_time = init_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
