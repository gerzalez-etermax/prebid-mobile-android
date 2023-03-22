package org.prebid.mobile.rendering.parser.companionselector;

import static org.prebid.mobile.rendering.parser.AdResponseParserVast.RESOURCE_FORMAT_HTML;
import static org.prebid.mobile.rendering.parser.AdResponseParserVast.RESOURCE_FORMAT_IFRAME;
import static org.prebid.mobile.rendering.parser.AdResponseParserVast.RESOURCE_FORMAT_STATIC;

import org.prebid.mobile.rendering.video.vast.Companion;

import java.util.Comparator;

public class CompanionResourceFormatComparator implements Comparator<Companion> {

    @Override
    public int compare(Companion o1, Companion o2) {
        return Integer.compare(getQualityValue(o1), getQualityValue(o2));
    }

    private int getQualityValue(Companion companion) {
        if (companion.getHtmlResource() != null) {
            return RESOURCE_FORMAT_HTML;
        }
        else if (companion.getIFrameResource() != null) {
            return RESOURCE_FORMAT_IFRAME;
        }
        else if (companion.getStaticResource() != null) {
            return RESOURCE_FORMAT_STATIC;
        }
        return 0;
    }
}