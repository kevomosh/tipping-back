package com.kakuom.finaltipping.responses;

public class BasicResponse {
    private String message;

    public BasicResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
