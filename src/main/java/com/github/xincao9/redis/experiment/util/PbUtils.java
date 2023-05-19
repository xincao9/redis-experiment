package com.github.xincao9.redis.experiment.util;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PbUtils {
    private static final Logger log = LoggerFactory.getLogger(PbUtils.class);

    public static String toJSONString(Message message) {
        if (message == null) {
            return null;
        } else {
            try {
                return JsonFormat.printer().includingDefaultValueFields().omittingInsignificantWhitespace().print(message);
            } catch (Throwable var2) {
                log.error("toJSONString error", var2);
                return null;
            }
        }
    }
}
