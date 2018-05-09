package utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ProtocolsLoader {
    public static Map<String,String> getProtocols() {
        Map<String,String> map = new HashMap<>();
        Properties prop = new Properties();
        try {
            prop.load(ProtocolsLoader.class.getClassLoader().getResourceAsStream("input-data/protocols.properties"));
            map.put("clientProtocol",prop.getProperty("clientProtocol"));
            map.put("serverProtocol",prop.getProperty("serverProtocol"));
        } catch (IOException ignored) {
        }
        return map;
    }
}
