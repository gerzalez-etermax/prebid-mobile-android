package org.prebid.mobile.rendering.parser;

import static org.prebid.mobile.rendering.utils.helpers.Utils.getIntOrZero;

import org.prebid.mobile.rendering.loading.FileDownloadTask;
import org.prebid.mobile.rendering.video.vast.MediaFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class HighLimitedQualityMediaFileSelector implements MediaFileSelector {
    private final Comparator<MediaFile> videoQualityComparator;

    public HighLimitedQualityMediaFileSelector() {
        this.videoQualityComparator = new VideoQualityComparator();
    }

    public HighLimitedQualityMediaFileSelector(Comparator<MediaFile> videoQualityComparator) {
        this.videoQualityComparator = videoQualityComparator;
    }

    @Override
    public String getMediaUrl(ArrayList<MediaFile> eligibleMediaFiles, long durationMillis) {
        if (eligibleMediaFiles.size() == 0) return "";

        SortedSet<MediaFile> set = new TreeSet<>(videoQualityComparator);

        for (int i = 0; i < eligibleMediaFiles.size(); i++) {
            MediaFile currentMediaFile = eligibleMediaFiles.get(i);
            if (isFileSizeOk(durationMillis, getIntOrZero(currentMediaFile.getBitrate()))) {
                set.add(currentMediaFile);
            }
        }
        if (set.size() == 0) return "";
        return set.last().getValue();
    }

    private boolean isFileSizeOk(long durationMillis, int bitrate) {
        double size = ((float) durationMillis / 60 / 1000) * bitrate * 0.0075;
        return size < FileDownloadTask.maxMBFileSize;
    }
}
