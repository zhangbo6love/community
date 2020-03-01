package com.fukuadiary.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUNT(2001, "非法操作(你找的问题不在了)，悬赏奖金加1元"),

    TARGET_PARAM_NOT_FOUNT(2002, "未选中任何问题或评论进行回复"),

    NO_LOGIN(2003, "当前操作需要登录，请登录后重试"),

    SYS_ERROR(2004,"正在做非法事件，你已经被通缉了，悬赏奖金为2元！"),

    TYPE_PARAM_WRONG(2005, "评论类型错误或者不存在"),

    COMMENT_NOT_FOUNT(2006, "回复的评论不存在了，要不换个试试？"),

    CONTENT_IS_EMPTY(2007, "输入内容不能为空，要不要换个试试？"),

    READ_NOTIFICATION_FAIL(2008, "禁止读取别人信息？"),
    NOTIFICATION_NOT_FOUND(2009, "消息去哪了？"),;


    @Override
    public String getMassage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
