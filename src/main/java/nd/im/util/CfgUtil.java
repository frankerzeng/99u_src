package nd.im.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * @author zj
 */
public class CfgUtil {

    private static String cfgFile = "/salt.json";

    public static Map<String, String> get(String name) {
        try {
            Map<String, Map<String, String>> allConfigMap = JSON.parseObject(FileUtil.readConfig(cfgFile), new TypeReference<Map<String, Map<String, String>>>() {
            });
            Map<String, String> configMap = allConfigMap == null ? null : allConfigMap.get(name);
            for (String key : configMap.keySet()) {
//                System.out.println("kdddey="+key+" and value ="+configMap.get(key));
            }
            return configMap;
        } catch (Exception e) {
            throw new RuntimeException("读取配置文件出错", e);
        }
    }

    public static String get(String name, String key) {
        Map<String, String> configMap = get(name);
        return configMap == null ? "" : configMap.get(key);
    }
}
