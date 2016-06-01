package nd.im.util;

import java.io.*;

/**
 * @author CMM
 */
public class FileUtil {
    /**
     * @author CMM
     */
    public static String readConfig(String fileName) {
        try {
            InputStream stream = FileUtil.class.getResourceAsStream(fileName);
            InputStreamReader bb = new InputStreamReader(stream, "UTF-8");
            BufferedReader br = new BufferedReader(bb);
            StringBuilder sb = new StringBuilder();

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("配置文件错误", e);
        }
    }

    /**
     * 删除单个文件
     *
     * @author CMM
     */
    public static boolean deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }
}
