package org.prebid.mobile.rendering.parser.mediaselector;

import org.prebid.mobile.rendering.utils.helpers.Utils;
import org.prebid.mobile.rendering.video.vast.MediaFile;

import java.util.Comparator;

public class VideoPortraitOrientationComparator implements Comparator<MediaFile> {

    @Override
    public int compare(MediaFile o1, MediaFile o2) {
        return Integer.compare(getQualityValue(o2), getQualityValue(o1));
    }

    private int getQualityValue(MediaFile mediaFile) {
        int height = Utils.getIntOrZero(mediaFile.getHeight());
        int width = Utils.getIntOrZero(mediaFile.getWidth());
        int orientation = 0;
        if (height > width)
            orientation = 1;
        else if (width > height)
            orientation = -1;
        return orientation;
    }
}