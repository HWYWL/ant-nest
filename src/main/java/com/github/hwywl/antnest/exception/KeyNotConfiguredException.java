package com.github.hwywl.antnest.exception;


import lombok.NoArgsConstructor;

/**
 * <p>未配置KEY运行时异常</p>
 * @author YI
 * @version 2019年6月16日
 */
@NoArgsConstructor
public class KeyNotConfiguredException extends RuntimeException {

    public KeyNotConfiguredException(String message) {
        super(message);
    }
}
