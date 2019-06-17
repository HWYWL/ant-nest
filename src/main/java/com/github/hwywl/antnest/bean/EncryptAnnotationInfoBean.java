package com.github.hwywl.antnest.bean;

import com.github.hwywl.antnest.enums.EncryptBodyMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>加密注解信息</p>
 * @author YI
 * @version 2019年6月16日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EncryptAnnotationInfoBean {

    private EncryptBodyMethod encryptBodyMethod;

    private String key;
}
