/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.social.ui.viewers.geography.backends;

import org.episteme.social.ui.viewers.geography.MapBackend;
import org.episteme.core.technical.backend.Backend;
import com.google.auto.service.AutoService;

/**
 * Backend for UnfoldingMap library.
 * Processing-based interactive map library.
 * 
 * @see <a href="https://github.com/ronit0717/unfoldingMap">UnfoldingMap</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService({MapBackend.class, Backend.class})
public class UnfoldingMapBackend implements MapBackend {

    @Override
    public String getType() {
        return "map";
    }

    @Override
    public String getId() {
        return "unfolding";
    }

    @Override
    public String getName() {
        return org.episteme.core.ui.i18n.I18N.getInstance().get("provider.unfolding.name", "Unfolding Maps");
    }

    @Override
    public String getDescription() {
        return org.episteme.core.ui.i18n.I18N.getInstance().get("provider.unfolding.desc", "Processing-based interactive map library with tile providers.");
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("de.fhpotsdam.unfolding.UnfoldingMap");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getPriority() {
        return 50; // Higher priority than JavaFX when available
    }

    @Override public boolean isSupportsLayering() { return true; }
    @Override public boolean isSupportsInteractive() { return true; }

    @Override
    public Object createBackend() {
        // Placeholder - actual implementation would create UnfoldingMap wrapper
        return null;
    }
}

