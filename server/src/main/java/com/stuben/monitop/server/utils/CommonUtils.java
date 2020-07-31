package com.stuben.monitop.server.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class CommonUtils {

    /**
     * 获取mongo的ID
     */
    public static String getMongoId(String app, int pileNo, long timestamp) {
        return DigestUtils.md5Hex(app + pileNo + timestamp);
    }
}
