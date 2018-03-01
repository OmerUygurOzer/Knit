package com.omerozer.knit.schedulers;

/**
 * Created by omerozer on 2/21/18.
 */

public enum KnitSchedulers {
    MAIN("main()"),
    IMMEDIATE("immediate()"),
    IO("io()"),
    HEAVY("heavy()");

    String method;

    KnitSchedulers(String method){
        this.method = method;
    }

    public String getMethod(){
        return this.method;
    }
}
