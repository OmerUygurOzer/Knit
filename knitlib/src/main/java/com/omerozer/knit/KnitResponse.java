package com.omerozer.knit;

/**
 * Created by omerozer on 2/15/18.
 */

public class KnitResponse<A> {
    private Throwable e;

    private String errorMessage;

    private A body;

    public KnitResponse(A body){
        this.body = body;
    }

    public Throwable getError() {
        return e;
    }

    public void setError(Throwable e) {
        this.e = e;
        this.errorMessage = e.getMessage();
    }

    public A getBody() {
        return body;
    }

    public void setBody(A body) {
        this.body = body;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccessfull() {
        return errorMessage == null;
    }
}
