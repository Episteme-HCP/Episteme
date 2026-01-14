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

package org.jscience.benchmarking.linpack;

//code from http://www.netlib.org/benchmark/linpackjava/
//reused under GPL with premission from Jack Dongarra

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.URL;
import java.util.Vector;

class StatsGraph extends Canvas {

  double max_mflops = 0.0;

  Vector data_list = new Vector(10);

  boolean couldnt_load = false;

  double mflops_result = 0.0;
  double residn_result = 0.0;
  double time_result = 0.0;
  double eps_result = 0.0;

  String dataURL = "http://www.netlib.org/benchmark/linpackjava/linpackData";
//    String dataURL = "http://netlib3.cs.utk.edu/benchmark/linpackjava/linpackData";
  void load_graph() {
    /* open the linpackData file */
    InputStream is;

    try {
      is = new URL(dataURL).openStream();
    } catch (Exception e) {
      couldnt_load = true;
      System.out.println(e.getMessage());
      return;
    }

    StreamTokenizer st = new StreamTokenizer(new InputStreamReader(is));

    /* allow various commenting methods */
    st.commentChar('#');
    st.slashStarComments(true);
    st.slashSlashComments(true);

    st.quoteChar('"');

    st.parseNumbers(); // TT_NUMBER , nval

    try {
      while (st.TT_EOF != st.nextToken()) {
        double mflops = 0.0;
        String label = "";
        String info = "";
        int itemtype = 0;
        DataItem item;

        // first item label
        while ('"'!=st.ttype && st.TT_EOF != st.nextToken())
          /*spin*/;

        if ('"'==st.ttype)
          label = st.sval;

        // second item itemtype
        while (st.TT_NUMBER!=st.ttype && st.TT_EOF != st.nextToken())
          /*spin*/;

        if (st.TT_NUMBER==st.ttype)
          itemtype = (int)st.nval;

        // third item info
        while ('"'!=st.ttype && st.TT_EOF != st.nextToken())
          /*spin*/;

        if ('"'==st.ttype)
          info = st.sval;

        // second fourth mflops
        while (st.TT_NUMBER!=st.ttype && st.TT_EOF != st.nextToken())
          /*spin*/;

        if (st.TT_NUMBER==st.ttype) {
          mflops = st.nval;
          if (mflops > max_mflops)
            max_mflops = mflops;
          item = new DataItem(mflops, itemtype, info, label);
          data_list.addElement(item);
        }
      }
    } catch (Exception e) {
      couldnt_load = true;
      return;
    }

    try { 
      is.close();
    } catch (Exception e) {
      couldnt_load = true;
      return;
    }
  }

  public void paint(Graphics g) {

    Font font = g.getFont();	// save the default font
    Font minifont = new Font(font.getName(), font.getStyle(), 8);	// smaller

    int pad=20;

    if (mflops_result > max_mflops)
      max_mflops = mflops_result;

    // clear background
    g.setColor(getBackground());
    g.fillRect(1, 1, getSize().width - 2, getSize().height - 2);

    g.setColor(getForeground());
    if (couldnt_load) {
      g.drawString("Error: couldn't load Linpack data file", 10,50);
      return;
    }

    DataItem item;
    int i;

    // pad is size of border around bars area, same on all 3 sides
    // 0 element is not displayed, it is the scale (max value)

    int x, y2;
    int w,h;	// width and height excluding pad

    w = getSize().width - pad*2;
    h = getSize().height- pad*2;

    for (i=1; i<data_list.size(); i++) {
      item = (DataItem)data_list.elementAt(i);
      x = ( w * (i-1)/(data_list.size()-1) ) +pad;
      y2 = (int)(h - (h * item.mflops / max_mflops) +pad);

      g.setColor(getForeground());
      g.drawLine(x, h+pad, x, y2);
      g.drawString(item.label, x-5, h+pad+15);
      g.setFont(minifont);
      drawStringRising(g, 1, ""+item.mflops+" / "+item.info, x-10, y2-10);
      g.setFont(font);

      if (item.itemtype==1)
        g.setColor(Color.red);
      else if (item.itemtype==2)
        g.setColor(Color.green);
      else if (item.itemtype==3)
        g.setColor(Color.orange);
      else if (item.itemtype==4)
        g.setColor(Color.blue);
      else if (item.itemtype==5)
        g.setColor(Color.pink);

      g.fillPolygon(make_diamond(x,y2));
    }

    if (mflops_result!=0.0) {
      g.setColor(Color.red);
      x = w+pad;
      y2 = (int)(h - (h * mflops_result / max_mflops) +pad);
      g.drawLine(x, h+pad, x, y2);
      g.drawString("this", w+pad-5, h+pad+15);
      g.fillPolygon(make_diamond(x,y2));
      g.drawString(""+mflops_result, x-5, y2-10);
      g.setColor(Color.blue);

      String s = "Mflop/s: "+mflops_result
         +"  Time: "+time_result+" secs"
         +"  Norm Res: "+residn_result
         +"  Precision: "+eps_result;

      g.drawString(s, (getSize().width - g.getFontMetrics().stringWidth(s)) /2, 15);

      if (residn_result>100) {
        g.setColor(Color.yellow);
        g.drawString("Warning: there appears to be a problem with", 30, 30);
        g.drawString("the floating point arithmetic on this machine!!", 30, 50);
      }
    }
  }

  void drawStringRising(Graphics g, int rise, String s, int x, int y) {
    // for each char : draw, rise

    char chars[] = s.toCharArray();

    String _s;

    FontMetrics fm = g.getFontMetrics();
    int l=s.length();

    for (int i=0; i<l; i++) {
      _s = String.valueOf(chars[i]);
      g.drawString(_s, x,y);
      x += fm.stringWidth(_s) +1;
      y -= rise;
    }
  }

  Polygon make_diamond(int x, int y) {
    Polygon p = new Polygon();
    p.addPoint(x,y+6);
    p.addPoint(x+6,y);
    p.addPoint(x,y-6);
    p.addPoint(x-6,y);

    return p;
  }

  void setCurrentRun( double mflops_result, double residn_result, 
    double time_result, double eps_result)
  {
    this.mflops_result = mflops_result;
    this.residn_result = residn_result;
    this.time_result = time_result;
    this.eps_result = eps_result;
  }
}
