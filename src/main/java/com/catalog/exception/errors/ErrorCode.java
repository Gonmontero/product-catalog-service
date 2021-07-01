package com.catalog.exception.errors;


public enum ErrorCode {

    CONTENT_NOT_FOUND ("0001", "Content not found", 404),

    SUPPLY_CHAIN_UNEXPECTED_ERROR ("0002", "Error while sending data to Supply-Chain", 503),

    AWS_DYNAMODB_ERROR ("0003", "Error while processing the request", 503),

    AWS_SNS_ERROR ("0004", "Error while sending data to SNS Topic", 503);

    private String code;
    private String description;
    private int httpStatusCode;


    ErrorCode(String code, String description, int httpStatusCode) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}