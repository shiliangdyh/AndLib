package com.mirstone.baselib.util;

import java.util.Collection;

/**
 * @package: com.mirstone.baselib.util
 * @fileName: CollectionUtil
 * @data: 2018/8/27 16:42
 * @author: ShiLiang
 * @describe:
 */
public class CollectionUtil {
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return collection != null && !collection.isEmpty();
    }
}
