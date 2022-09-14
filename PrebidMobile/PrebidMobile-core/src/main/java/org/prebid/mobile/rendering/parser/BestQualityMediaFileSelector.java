package org.prebid.mobile.rendering.parser;

import org.prebid.mobile.rendering.utils.helpers.Utils;
import org.prebid.mobile.rendering.video.vast.MediaFile;

import java.util.ArrayList;

public class BestQualityMediaFileSelector implements MediaFileSelector {
    @Override
    public String getMediaUrl(ArrayList<MediaFile> eligibleMediaFiles) {
        String myBestMediaFileURL;
        // choose the one with the highest resolution amongst all
        MediaFile best = eligibleMediaFiles.get(0);
        int bestValues = (Utils.isBlank(best.getWidth())
                ? 0
                : Integer.parseInt(best.getWidth())) * (Utils.isBlank(best.getHeight())
                ? 0
                : Integer.parseInt(best.getHeight()));
        myBestMediaFileURL = best.getValue();

        for (int i = 0; i < eligibleMediaFiles.size(); i++) {
            MediaFile current = eligibleMediaFiles.get(i);
            int currentValues = (Utils.isBlank(current.getWidth())
                    ? 0
                    : Integer.parseInt(current.getWidth())) * (Utils.isBlank(current.getHeight())
                    ? 0
                    : Integer.parseInt(current.getHeight()));
            if (currentValues > bestValues) {
                bestValues = currentValues;
                best = current;
                myBestMediaFileURL = best.getValue();
            }
        }
        return myBestMediaFileURL;
    }
}
