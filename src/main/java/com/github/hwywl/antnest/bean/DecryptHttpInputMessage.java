package com.github.hwywl.antnest.bean;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>解密信息输入流</p>
 * @author YI
 * @version 2019年6月16日
 */
@NoArgsConstructor
@AllArgsConstructor
public class DecryptHttpInputMessage implements HttpInputMessage {

    private InputStream body;

    private HttpHeaders headers;

    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}
