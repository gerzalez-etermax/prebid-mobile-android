package org.prebid.mobile.rendering.parser;

import org.prebid.mobile.rendering.video.vast.MediaFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class LowQualityMediaFileSelector implements MediaFileSelector {
    private Comparator<MediaFile> videoQualityComparator;

    public LowQualityMediaFileSelector() {
        this.videoQualityComparator = new VideoQualityComparator();
    }

    public LowQualityMediaFileSelector(Comparator<MediaFile> videoQualityComparator) {
        this.videoQualityComparator = videoQualityComparator;
    }

    @Override
    public String getMediaUrl(ArrayList<MediaFile> eligibleMediaFiles) {
        if (eligibleMediaFiles.size() == 0) return "";

        SortedSet<MediaFile> set = new TreeSet<>(videoQualityComparator);
        set.addAll(eligibleMediaFiles);
        return set.first().getValue();
    }
}
