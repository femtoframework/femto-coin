package org.femtoframework.coin.api;

/**
 * API Response
 *
 * @author Sheldon Shao
 * @version 1.0
 */
public class APIResponse {
    private int code;
    private String message;
    private String content;
    private String contentType;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrorMessage(int code, String message) {
        setCode(code);
        setMessage(message);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        if (contentType == null) {
            contentType = code == 200 ? "application/json; charset=UTF8" : "text/html; charset=UTF8";
        }
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
