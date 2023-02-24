package com.hawolt.yaml;

import com.hawolt.generic.data.Platform;

import java.io.IOException;

/**
 * Created: 10/01/2023 18:58
 * Author: Twitter @hawolt
 **/

public interface IYamlSupplier {
    YamlWrapper getYamlResources(Platform Platform) throws IOException;
}
