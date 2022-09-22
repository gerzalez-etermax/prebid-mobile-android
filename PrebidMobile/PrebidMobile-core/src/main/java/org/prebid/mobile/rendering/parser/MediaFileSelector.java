package org.prebid.mobile.rendering.parser;

import org.prebid.mobile.rendering.video.vast.MediaFile;

import java.util.ArrayList;

public interface MediaFileSelector {
    String getMediaUrl(ArrayList<MediaFile> eligibleMediaFiles, long durationMillis);
}
