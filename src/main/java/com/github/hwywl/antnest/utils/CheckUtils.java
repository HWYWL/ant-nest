package com.github.hwywl.antnest.utils;

import cn.hutool.core.util.StrUtil;
import com.github.hwywl.antnest.exception.KeyNotConfiguredException;

/**
 * <p>辅助检测工具类</p>
 *
 * @author YI
 * @version 2019年6月14日
 */
public class CheckUtils {

    public static String checkAndGetKey(String k1, String k2, String keyName) {
        if (StrUtil.isNotEmpty(k1) && StrUtil.isNotEmpty(k2)) {
            throw new KeyNotConfiguredException(String.format("%s is not configured (未配置%s)", keyName, keyName));
        }
        if (k1 == null) return k2;
        return k1;
    }

}
