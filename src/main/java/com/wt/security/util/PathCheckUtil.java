package com.wt.security.util;

import java.util.List;
import java.util.function.Consumer;


public class PathCheckUtil {

    private static final String RULE_0 = "**";

    private static final String RULE_1 = "/";

    public static void pathMatch(List<String> configureUrl, String url, Consumer<Boolean> consumer) {
        boolean b = configureUrl.stream()
                .anyMatch(i -> {
                    String ir = i.replace(RULE_0, "");
                    return i.equals(url) || (i.endsWith(RULE_0) && url.startsWith(ir)) || ir.equals(url + RULE_1);
                });
        if(b){
            consumer.accept(b);
        }

    }


}
