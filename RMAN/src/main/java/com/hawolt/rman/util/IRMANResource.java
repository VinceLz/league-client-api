package com.hawolt.rman.util;

import java.util.List;

/**
 * Created: 12/01/2023 23:41
 * Author: Twitter @hawolt
 **/

public interface IRMANResource {
    List<String> getTargetFiles();

    String getLocation();
}
