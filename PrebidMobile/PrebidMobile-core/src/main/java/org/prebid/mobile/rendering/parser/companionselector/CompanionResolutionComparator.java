package org.prebid.mobile.rendering.parser.companionselector;

import static org.prebid.mobile.rendering.utils.helpers.Utils.getIntOrOne;

import org.prebid.mobile.rendering.video.vast.Companion;

import java.util.Comparator;

public class CompanionResolutionComparator implements Comparator<Companion> {

    @Override
    public int compare(Companion o1, Companion o2) {
        return getResolution(o1) - getResolution(o2);
    }

    private int getResolution(Companion companion) {
        int height = getIntOrOne(companion.getHeight());
        int width = getIntOrOne(companion.getWidth());
        return -(height * width);
    }
}