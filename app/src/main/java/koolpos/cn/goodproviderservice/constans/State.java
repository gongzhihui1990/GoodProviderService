package koolpos.cn.goodproviderservice.constans;

import java.io.Serializable;

/**
 * Created by caroline on 2017/6/5.
 */

public class State implements Serializable{
    private static final long serialVersionUID = -2125274840180679021L;
    private String desc;
    private StateEnum stateEnum;

    public StateEnum getEnum() {
        return stateEnum;
    }

    public State(StateEnum stateEnum, String desc){
        this.desc=desc;
        this.stateEnum=stateEnum;
    }

    public String getMessage() {
        return desc;
    }
}
