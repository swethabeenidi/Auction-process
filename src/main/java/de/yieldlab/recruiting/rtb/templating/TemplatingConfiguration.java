package de.yieldlab.recruiting.rtb.templating;

import org.springframework.context.annotation.Bean;
import org.stringtemplate.v4.ST;

import java.util.Map;

@org.springframework.context.annotation.Configuration
public class TemplatingConfiguration {

    @Bean
    public TemplatingService templating() {
        return (adContent, context) -> {
            ST template = new ST(adContent, '$', '$');
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                template.add(entry.getKey(), entry.getValue());
            }
            return template.render();
        };
    }
}
