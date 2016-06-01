package nd.im.util;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/26.
 */
public class Map2Json {

    public static String map2json(Map<String, Object> map) {
        String result = "{";
        int flag = 1;

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            result += "\"" + entry.getKey() + "\":\"" + entry.getValue().toString() + "\"";

            if (flag < map.size()) {
                result += ",";
            }
            flag++;
        }
        result += "}";

        return result;
    }
}
