package com.github.hwywl.antnest.advice;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import com.github.hwywl.antnest.annotation.decrypt.AESDecryptBody;
import com.github.hwywl.antnest.annotation.decrypt.DESDecryptBody;
import com.github.hwywl.antnest.bean.DecryptAnnotationInfoBean;
import com.github.hwywl.antnest.bean.DecryptHttpInputMessage;
import com.github.hwywl.antnest.config.EncryptBodyConfig;
import com.github.hwywl.antnest.enums.DecryptBodyMethod;
import com.github.hwywl.antnest.exception.DecryptBodyFailException;
import com.github.hwywl.antnest.exception.DecryptMethodNotFoundException;
import com.github.hwywl.antnest.utils.CheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * 请求数据的加密信息解密处理<br>
 * 本类只对控制器参数中含有<strong>{@link org.springframework.web.bind.annotation.RequestBody}</strong>
 * 以及package为<strong><code>com.github.hwywl.antnest.annotation.decrypt.*</code></strong>下的注解有效
 *
 * @author YI
 * @version 2019年6月16日
 * @see RequestBodyAdvice
 */
@Order(1)
@ControllerAdvice
@Slf4j
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    private final EncryptBodyConfig config;

    @Autowired
    public DecryptRequestBodyAdvice(EncryptBodyConfig config) {
        this.config = config;
    }

    /**
     * 拦截所有注解AESDecryptBody的方法和类
     *
     * @param methodParameter
     * @param targetType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Annotation[] annotations = methodParameter.getDeclaringClass().getAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof AESDecryptBody || annotation instanceof DESDecryptBody) {
                    return true;
                }
            }
        }

        return methodParameter.getMethod().isAnnotationPresent(AESDecryptBody.class) ||
                methodParameter.getMethod().isAnnotationPresent(DESDecryptBody.class);
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (inputMessage.getBody() == null) {
            return inputMessage;
        }
        String body;
        try {
            body = IOUtils.toString(inputMessage.getBody(), config.getEncoding());
        } catch (Exception e) {
            throw new DecryptBodyFailException("Unable to get request body data," +
                    " please check if the sending data body or request method is in compliance with the specification." +
                    " (无法获取请求正文数据，请检查发送数据体或请求方法是否符合规范。)");
        }
        if (StrUtil.isEmpty(body)) {
            throw new DecryptBodyFailException("The request body is NULL or an empty string, so the decryption failed." +
                    " (请求正文为NULL或为空字符串，因此解密失败。)");
        }
        String decryptBody = null;
        DecryptAnnotationInfoBean methodAnnotation = this.getMethodAnnotation(parameter);
        if (methodAnnotation != null) {
            decryptBody = switchDecrypt(body, methodAnnotation);
        } else {
            DecryptAnnotationInfoBean classAnnotation = this.getClassAnnotation(parameter.getDeclaringClass());
            if (classAnnotation != null) {
                decryptBody = switchDecrypt(body, classAnnotation);
            }
        }
        if (decryptBody == null) {
            throw new DecryptBodyFailException("Decryption error, " +
                    "please check if the selected source data is encrypted correctly." +
                    " (解密错误，请检查选择的源数据的加密方式是否正确。)");
        }
        try {
            InputStream inputStream = IOUtils.toInputStream(decryptBody, config.getEncoding());
            return new DecryptHttpInputMessage(inputStream, inputMessage.getHeaders());
        } catch (Exception e) {
            throw new DecryptBodyFailException("The string is converted to a stream format exception." +
                    " Please check if the format such as encoding is correct." +
                    " (字符串转换成流格式异常，请检查编码等格式是否正确。)");
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * 获取方法控制器上的加密注解信息
     *
     * @param methodParameter 控制器方法
     * @return 加密注解信息
     */
    private DecryptAnnotationInfoBean getMethodAnnotation(MethodParameter methodParameter) {
        if (methodParameter.getMethod().isAnnotationPresent(DESDecryptBody.class)) {
            return DecryptAnnotationInfoBean.builder()
                    .decryptBodyMethod(DecryptBodyMethod.DES)
                    .key(methodParameter.getMethodAnnotation(DESDecryptBody.class).otherKey())
                    .build();
        }

        if (methodParameter.getMethod().isAnnotationPresent(AESDecryptBody.class)) {
            return DecryptAnnotationInfoBean.builder()
                    .decryptBodyMethod(DecryptBodyMethod.AES)
                    .key(methodParameter.getMethodAnnotation(AESDecryptBody.class).otherKey())
                    .build();
        }

        return null;
    }

    /**
     * 获取类控制器上的加密注解信息
     *
     * @param clazz 控制器类
     * @return 加密注解信息
     */
    private DecryptAnnotationInfoBean getClassAnnotation(Class clazz) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof DESDecryptBody) {
                    return DecryptAnnotationInfoBean.builder()
                            .decryptBodyMethod(DecryptBodyMethod.DES)
                            .key(((DESDecryptBody) annotation).otherKey())
                            .build();
                }
                if (annotation instanceof AESDecryptBody) {
                    return DecryptAnnotationInfoBean.builder()
                            .decryptBodyMethod(DecryptBodyMethod.AES)
                            .key(((AESDecryptBody) annotation).otherKey())
                            .build();
                }
            }
        }
        return null;
    }

    /**
     * 选择加密方式并进行解密
     *
     * @param formatStringBody 目标解密字符串
     * @param infoBean         加密信息
     * @return 解密结果
     */
    private String switchDecrypt(String formatStringBody, DecryptAnnotationInfoBean infoBean) {
        DecryptBodyMethod method = infoBean.getDecryptBodyMethod();
        if (method == null) {
            throw new DecryptMethodNotFoundException();
        }
        String key = infoBean.getKey();
        if (method == DecryptBodyMethod.DES) {
            key = CheckUtils.checkAndGetKey(config.getAesKey(), key, "DES-KEY");
            // 使用默认的DES/CBC/PKCS5Padding
            DES des = SecureUtil.des(key.getBytes());

            return des.decryptStr(formatStringBody);
        }
        if (method == DecryptBodyMethod.AES) {
            key = CheckUtils.checkAndGetKey(config.getAesKey(), key, "AES-KEY");
            // 使用默认的AES/ECB/PKCS5Padding
            AES aes = SecureUtil.aes(key.getBytes());

            return aes.decryptStr(formatStringBody);
        }

        throw new DecryptBodyFailException();
    }
}
