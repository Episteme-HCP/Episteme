/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media;

import org.episteme.core.media.VideoBackend;
import org.episteme.core.technical.backend.AbstractBackendManager;

/**
 * Manager for {@link VideoBackend} instances.
 */
public class VideoBackendManager extends AbstractBackendManager<VideoBackend> {

    private static final VideoBackendManager INSTANCE = new VideoBackendManager();

    private VideoBackendManager() {
        super(VideoBackend.class);
    }

    public static VideoBackendManager getInstance() {
        return INSTANCE;
    }

    // ---- Static convenience methods ----

    public static VideoBackend staticDefault() {
        return getInstance().managerDefault();
    }

    public static void staticSelect(String nameOrId) {
        getInstance().managerSetDefault(nameOrId);
    }

    public static java.util.Collection<VideoBackend> staticAll() {
        return getInstance().managerAll();
    }
}
