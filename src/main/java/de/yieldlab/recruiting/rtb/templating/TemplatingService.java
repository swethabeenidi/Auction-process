package de.yieldlab.recruiting.rtb.templating;

import java.util.Map;

public interface TemplatingService {

    /**
     * Replace all occurences of '$key$' with its value in the context map.
     */
    String process(String template, Map<String, Object> context);
}
