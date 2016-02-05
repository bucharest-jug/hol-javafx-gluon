/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.record.model;

/**
 * Model class for alert message
 * @author mihael.buzdugan
 */
public class AlertMessage {
    //numerical code for error message
    private byte errorCode;
    private String message;

    public AlertMessage(byte errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public byte getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(byte errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }    
}
