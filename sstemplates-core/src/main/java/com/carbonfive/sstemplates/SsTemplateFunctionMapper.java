package com.carbonfive.sstemplates;

import javax.el.FunctionMapper;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SsTemplateFunctionMapper extends FunctionMapper {
    Map<String, Method> map = Collections.emptyMap();

    public Method resolveFunction(String prefix, String localName) {
        return this.map.get(prefix + ":" + localName);
    }

    public void setFunction(String prefix, String localName, Method method) {
        if (this.map.isEmpty()) {
            this.map = new HashMap<>();
        }

        this.map.put(prefix + ":" + localName, method);
    }
}
