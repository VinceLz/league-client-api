package com.hawolt.version;

import com.hawolt.generic.data.Platform;

/**
 * Created: 13/01/2023 12:18
 * Author: Twitter @hawolt
 **/

public interface IVersionSupplier {
    String getVersionValue(String file);

    String getVersionValue(Platform platform, String file);
}
