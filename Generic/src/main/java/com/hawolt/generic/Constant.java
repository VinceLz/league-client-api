package com.hawolt.generic;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created: 26/11/2022 13:51
 * Author: Twitter @hawolt
 **/

public class Constant {

    public static RequestBody BLANK_POST_BODY = RequestBody.create(new byte[]{}, null);
    public static MediaType APPLICATION_JSON = MediaType.parse("application/json");

}
