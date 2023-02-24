package com.hawolt.rman.io.downloader;

import java.io.IOException;

/**
 * Created: 05/01/2023 14:19
 * Author: Twitter @hawolt
 **/

public interface IBundleCallback {
    void onError(IOException e);
}
