package com.hawolt.yaml.impl.fallback;

import com.hawolt.generic.data.Platform;
import com.hawolt.generic.runnable.IExceptionCallback;
import com.hawolt.generic.value.impl.PairedValueSupplier;
import com.hawolt.yaml.IYamlSupplier;
import com.hawolt.yaml.SystemYaml;
import com.hawolt.yaml.YamlWrapper;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created: 10/01/2023 18:59
 * Author: Twitter @hawolt
 **/

public class LocalYamlSupplier extends PairedValueSupplier<Platform, YamlWrapper> implements IYamlSupplier {
    public LocalYamlSupplier(Platform... platforms) {
        this(null, platforms);
    }

    public LocalYamlSupplier(IExceptionCallback callback, Platform... platforms) {
        super(callback);
        this.run();
    }

    @Override
    protected void execute() throws Exception {
        JSONObject yaml = SystemYaml.generate();
        for (String region : yaml.keySet()) {
            JSONObject object = yaml.getJSONObject(region);
            JSONObject o = new JSONObject();
            o.put("LEDGE", object.getString("ledge"));
            o.put("ENTITLEMENT", object.getString("entitlement"));
            o.put("CONFIG", object.getString("config"));
            o.put("EMAIL", object.getString("email"));
            o.put("QUEUE", object.getString("queue"));
            o.put("PLATFORM", object.getString("platform"));
            o.put("LCDS", object.getString("lcds"));
            YamlWrapper wrapper = new YamlWrapper(o);
            put(Platform.valueOf(region), wrapper);
        }
    }

    @Override
    public YamlWrapper getYamlResources(Platform platform) throws IOException {
        return getValue(platform);
    }
}