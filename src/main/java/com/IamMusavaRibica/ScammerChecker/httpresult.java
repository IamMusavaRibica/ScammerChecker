package com.IamMusavaRibica.ScammerChecker;

public class httpresult {
    private final int responseCode;
    private final String response;

    public httpresult(int responseCode, String response) {
        this.responseCode = responseCode;
        this.response = response;
    }
    public int getResponseCode() {
        return responseCode;
    }
    public String getResponse() {
        return response;
    }
}
