package org.prebid.mobile.rendering.parser.mediaselector;

import static org.prebid.mobile.rendering.utils.helpers.Utils.getIntOrOne;
import static org.prebid.mobile.rendering.utils.helpers.Utils.getIntOrZero;

import org.prebid.mobile.rendering.video.vast.MediaFile;

import java.util.Comparator;

public class VideoQualityComparatorAsc implements Comparator<MediaFile> {

    @Override
    public int compare(MediaFile o1, MediaFile o2) {
        return Integer.compare(getQualityValue(o1), getQualityValue(o2));
    }

    private int getQualityValue(MediaFile mediaFile) {
        int bitRate = getIntOrZero(mediaFile.getBitrate());
        int height = getIntOrOne(mediaFile.getHeight());
        int width = getIntOrOne(mediaFile.getWidth());
        return bitRate + (height * width);
    }
}