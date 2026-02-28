/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.social.arts.loaders;

import java.util.ArrayList;
import java.util.List;

public class MusicXMLScore {
    private String workTitle;
    private String composer;
    private String copyright;
    private String encodingSoftware;
    private String version;
    private List<MusicXMLPart> parts = new ArrayList<>();

    public String getWorkTitle() { return workTitle; }
    public void setWorkTitle(String workTitle) { this.workTitle = workTitle; }

    public String getComposer() { return composer; }
    public void setComposer(String composer) { this.composer = composer; }

    public String getCopyright() { return copyright; }
    public void setCopyright(String copyright) { this.copyright = copyright; }

    public String getEncodingSoftware() { return encodingSoftware; }
    public void setEncodingSoftware(String encodingSoftware) { this.encodingSoftware = encodingSoftware; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public List<MusicXMLPart> getParts() { return parts; }
    public void addPart(MusicXMLPart part) { parts.add(part); }
}

