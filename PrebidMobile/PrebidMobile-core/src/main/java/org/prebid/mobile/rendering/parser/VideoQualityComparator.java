package org.prebid.mobile.rendering.parser;

import org.prebid.mobile.rendering.utils.helpers.Utils;
import org.prebid.mobile.rendering.video.vast.MediaFile;

import java.util.Comparator;

public class VideoQualityComparator implements Comparator<MediaFile> {

    @Override
    public int compare(MediaFile o1, MediaFile o2) {
        return Integer.compare(getQualityValue(o1), getQualityValue(o2));
    }

    private int getQualityValue(MediaFile mediaFile) {
        int bitRate = getIntOrOne(mediaFile.getBitrate());
        int height = getIntOrOne(mediaFile.getHeight());
        int width = getIntOrOne(mediaFile.getWidth());
        return bitRate + (height * width);
    }

    private int getIntOrOne(String stringValue) {
        return Utils.isBlank(stringValue)
                ? 1 : Integer.parseInt(stringValue);
    }

}