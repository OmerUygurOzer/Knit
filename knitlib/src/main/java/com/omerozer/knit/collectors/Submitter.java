package com.omerozer.knit.collectors;

import com.omerozer.knit.KnitResponse;

/**
 * Created by omerozer on 2/14/18.
 */

public interface Submitter<K> {
    void submit(KnitResponse<K> response);
}
