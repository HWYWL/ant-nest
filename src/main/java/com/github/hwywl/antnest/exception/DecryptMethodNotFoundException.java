package com.github.hwywl.antnest.exception;

/**
 * <p>加密方式未找到或未定义异常</p>
 * @author YI
 * @version 2019年6月16日
 */
public class DecryptMethodNotFoundException extends RuntimeException {

    public DecryptMethodNotFoundException() {
        super("Decryption method is not defined. (解密方式未定义)");
    }

    public DecryptMethodNotFoundException(String message) {
        super(message);
    }
}
