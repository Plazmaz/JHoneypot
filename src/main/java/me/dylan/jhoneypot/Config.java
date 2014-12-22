package me.dylan.jhoneypot;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with love by Dylan on 12/20/2014.
 *
 * @author Dylan Thomas Katz
 */
public class Config {
    public static final String DEFAULT_CONFIG_CONTENTS = ""
            +"\n##############################################"
            +"\n## JHoneyNet - Created by Dylan Thomas Katz ##"
            +"\n##############################################"
            +"\n- listening-port: 22"
            +"\n- logging-enabled: true       # Logs will be stored in the 'logs' folder"
            +"\n- user: 'root'           # This can be set to '' to allow any username for root."
            +"\n- password: 'password1'  # This can be set to '' to allow any password for root.";
    private Map keyValues = new HashMap<>();;
    public Config(String path) throws IOException {
        File cfg = new File(path);
        if(!cfg.exists()) {
            cfg.createNewFile();
            PrintWriter out = new PrintWriter(new FileWriter(cfg));
            out.print(DEFAULT_CONFIG_CONTENTS);
        }
        InputStream inputStream = new FileInputStream(cfg);
        Yaml yaml = new Yaml();
        try {
            List<Map> tmpList = (List) yaml.load(inputStream);

            for(Map map : tmpList) {
                keyValues.putAll(map);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    public <T> T get(String key, Class<? extends T> cast) {
        return cast.cast(keyValues.get(key));
    }

    public String getString(String key) {
        return get(key, String.class);
    }


}
