package top.bhui.share.common.exception;

import lombok.Getter;

@Getter
public enum BusinessExceptionEnum {
    PHONE_NOT_EXIST("手机号不存在"),
    PHONE_EXIST("手机号已存在"),
    PASSWORD_ERROR("密码错误");

    private final String desc;

    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }

    @Override public String toString() {
        return "BusinessExceptionEnum{" + "desc='" + desc + '\'' + '}';
    }
}

