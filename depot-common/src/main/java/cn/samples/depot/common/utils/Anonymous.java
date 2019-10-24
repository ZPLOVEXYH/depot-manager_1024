package cn.samples.depot.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Anonymous {
    public Map<String, HttpMethod[]> getAnonymousURI() {
        return Maps.newHashMap();
    }

    public Map<HttpMethod, List<String>> getAnonymousURIList() {
        Map<HttpMethod, List<String>> map = Maps.newHashMap();
        for (Map.Entry<String, HttpMethod[]> entry : getAnonymousURI().entrySet()) {
            for (HttpMethod method : entry.getValue()) {
                List<String> list = map.computeIfAbsent(method, k -> Lists.newArrayList());
                list.add(entry.getKey());
            }

        }
        return map;
    }
}
