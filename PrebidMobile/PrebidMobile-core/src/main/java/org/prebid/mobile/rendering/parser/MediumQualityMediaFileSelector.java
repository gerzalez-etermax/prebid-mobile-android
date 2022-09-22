package org.prebid.mobile.rendering.parser;

import org.prebid.mobile.rendering.video.vast.MediaFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class MediumQualityMediaFileSelector implements MediaFileSelector {
    private final Comparator<MediaFile> videoQualityComparator;

    public MediumQualityMediaFileSelector() {
        this.videoQualityComparator = new VideoQualityComparator();
    }

    public MediumQualityMediaFileSelector(Comparator<MediaFile> videoQualityComparator) {
        this.videoQualityComparator = videoQualityComparator;
    }

    @Override
    public String getMediaUrl(ArrayList<MediaFile> eligibleMediaFiles, long durationMillis) {
        MediaFile myBestMediaFileURL = null;

        SortedSet<MediaFile> set = new TreeSet<>(videoQualityComparator);
        set.addAll(eligibleMediaFiles);
        Iterator<MediaFile> iterator = set.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            if (count >= set.size() / 2) {
                myBestMediaFileURL = iterator.next();
                break;
            } else iterator.next();
            count++;
        }

        return myBestMediaFileURL == null ? eligibleMediaFiles.get(0).getValue() : myBestMediaFileURL.getValue();
    }
}
