package koolpos.cn.goodproviderservice.constans;

/**
 * Created by caroline on 2017/6/5.
 */

public class State {
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
