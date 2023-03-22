package org.prebid.mobile.rendering.parser.companionselector;

import org.prebid.mobile.rendering.utils.helpers.Utils;
import org.prebid.mobile.rendering.video.vast.Companion;

import java.util.Comparator;

public class CompanionLandscapeOrientationComparator implements Comparator<Companion> {

    @Override
    public int compare(Companion o1, Companion o2) {
        return Integer.compare(getPortraitPreference(o1), getPortraitPreference(o2));
    }

    private int getPortraitPreference(Companion companion) {
        int height = Utils.getIntOrZero(companion.getHeight());
        int width = Utils.getIntOrZero(companion.getWidth());
        int orientation = 0;
        if (height > width)
            orientation = 1;
        else if (width > height)
            orientation = -1;
        return orientation;
    }
}