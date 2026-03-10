package com.centria.cabbookingmvp.controller.dto;

public class ApiResponse<T> {

    private int code;
    private String msg;
    private T data;

    public ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(0,"success",data);
    }

    public static <T> ApiResponse<T> error(String msg){
        return new ApiResponse<>(1,msg,null);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

}
