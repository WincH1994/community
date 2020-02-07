package com.soulout.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2001,"你找的问题丢啦，换个试试吧！"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登录，请登录后重试"),
    SYS_ERROR(2004,"服务冒烟了，要不然你稍后再试试！！！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你回复的评论丢啦，换个试试吧！"),
    COMMENT_IS_EMPTY(2007,"回复的评论不能为空"),
    READ_NOTIFICATION_FAIL(2008,"兄弟你这是都别人的信息呢？"),
    NOTIFICATION_NOT_FOUND(2009,"消息通知莫非是不翼而飞了？"),
    FILE_UPLOAD_FAILED(2010,"图片上传失败");

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {

        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
