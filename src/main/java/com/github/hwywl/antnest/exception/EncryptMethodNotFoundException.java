package com.github.hwywl.antnest.exception;

/**
 * <p>加密方式未找到或未定义异常</p>
 * @author YI
 * @version 2019年6月16日
 */
public class EncryptMethodNotFoundException extends RuntimeException {

    public EncryptMethodNotFoundException() {
        super("Encryption method is not defined. (加密方式未定义)");
    }

    public EncryptMethodNotFoundException(String message) {
        super(message);
    }
}
