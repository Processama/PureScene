package com.example.purescene.bean.citybean;

import java.util.List;

public class Province {
    private int ret_code;
    private List<SpeProvince> list;

    public List<SpeProvince> getList() {
        return list;
    }

    public void setList(List<SpeProvince> list) {
        this.list = list;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }
}
