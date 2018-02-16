package com.omerozer.knit;

import android.os.Bundle;

/**
 * Created by omerozer on 2/13/18.
 */

public interface PresenterInterface extends MemoryEntity {
    void onViewApplied(Object viewObject,Bundle bundle);
    void onCurrentViewReleased();
}
