package com.github.hwywl.antnest.exception;

/**
 * <p>解密数据失败异常</p>
 * @author YI
 * @version 2019年6月16日
 */
public class DecryptBodyFailException extends RuntimeException {

    public DecryptBodyFailException() {
        super("Decrypting data failed. (解密数据失败)");
    }

    public DecryptBodyFailException(String message) {
        super(message);
    }
}