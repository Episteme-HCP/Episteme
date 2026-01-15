/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.net;

import java.net.URL;
import java.util.List;

/**
 * CrawlerSetting defines callback functions that determine the behavior in which
 * a web search algorithm goes through the net and calculates its
 * results. A CrawlerSetting can be used with a org.jscience.net.Spider.
 *
 * @author Holger Antelmann
 * @see Spider
 * @see Spider#crawlWeb(CrawlerSetting,int,Logger)
 */
public interface CrawlerSetting {
    /**
     * This method decides whether either the URL itself or its content qualifies
     * for what this CrawlerSetting searches for; as this function is also called on every
     * URL encountered, it is also the place for any custom parsing this CrawlerSetting
     * wants to do.
     * The two List objects allow the CrawlerSetting to act on potential constrains
     * that may result from e.g. a maximum number of total nodes to be examined
     * (or any other custom checking imaginable).
     * Note that it is the responsibility of the calling object to ensure that
     * this function isn't called multiple times on the same URL if that's not
     * desired.
     * The url may include <i>any</i> URL, including non-HTTP protocols
     * (such as mailto:, ftp:) and image or media URLs
     *
     * @param url           the URL in question to satisfy the criteria
     * @param referer       url's referer URL
     * @param depth         link distance from the original root URL where the search began
     * @param resultURLList List of URLs that have already been found to match this CrawlerSetting's criteria
     * @param closedURLList List of URLs that have already been found not to match the CrawlerSetting's criteria
     */
    boolean matchesCriteria(URL url, URL referer, int depth, List<URL> resultURLList, List<URL> closedURLList);

    /**
     * followLinks() determines whether the given URL is to be searched for
     * its links to be examined further in the next level.
     * The three List objects allow the CrawlerSetting to act on potential constrains
     * that may result from e.g. a maximum number of total nodes to be examined
     * (or any other custom checking imaginable).
     * The url may include <i>any</i> URL, including non-HTTP protocols
     * (such as mailto:, ftp:) and image or media URLs.
     *
     * @param url                  the URL that is to be examined for its links
     * @param referer              url's referer URL
     * @param depth                distance from the original root URL where the search began
     * @param resultURLList        List of URLs that have already been found to match this CrawlerSetting's criteria
     * @param closedURLList        List of URLs that have already been found not to match the CrawlerSetting's criteria
     * @param searchURLWrapperList List of Spider.URLWrapper objects already identified to be examined in the next level
     * @see Spider.URLWrapper
     */
    boolean followLinks(URL url, URL referer, int depth, List<URL> resultURLList, List<URL> closedURLList, List<Spider.URLWrapper> searchURLWrapperList);
}
