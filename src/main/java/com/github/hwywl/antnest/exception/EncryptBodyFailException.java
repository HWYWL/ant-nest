package com.github.hwywl.antnest.exception;

/**
 * <p>加密数据失败异常</p>
 * @author YI
 * @version 2019年6月16日
 */
public class EncryptBodyFailException  extends RuntimeException {

    public EncryptBodyFailException() {
        super("Encrypted data failed. (加密数据失败)");
    }

    public EncryptBodyFailException(String message) {
        super(message);
    }
}