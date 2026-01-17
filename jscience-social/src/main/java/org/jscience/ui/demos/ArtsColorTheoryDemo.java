/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.ui.demos;
 
 import org.jscience.ui.AbstractDemo;
 import org.jscience.ui.viewers.arts.ArtsColorTheoryViewer;
 import org.jscience.ui.i18n.I18n;
 
 /**
  * Demo for Arts & Color Theory.
  * Uses ArtsColorTheoryViewer.
  *
  * @author Silvere Martin-Michiellot
  * @author Gemini AI (Google DeepMind)
  * @since 1.0
  */
 public class ArtsColorTheoryDemo extends AbstractDemo {
 
     @Override
     public String getCategory() {
         return org.jscience.ui.i18n.I18n.getInstance().get("category.arts", "Arts");
     }
 
     @Override
     public String getName() {
         return org.jscience.ui.i18n.I18n.getInstance().get("demo.artscolortheorydemo.name", "Color Theory");
     }
 
     @Override
     public String getDescription() {
         return org.jscience.ui.i18n.I18n.getInstance().get("demo.artscolortheorydemo.desc", "Explore color harmonies and palettes.");
     }
 
     @Override
     protected javafx.scene.Node createViewerNode() {
         return new ArtsColorTheoryViewer();
     }
     
     @Override
     public String getLongDescription() {
          return org.jscience.ui.i18n.I18n.getInstance().get("demo.artscolortheorydemo.longdesc", "Interactive tool for exploring color theory concepts like complementary, analogous, and triadic harmonies.");
     }
 }
