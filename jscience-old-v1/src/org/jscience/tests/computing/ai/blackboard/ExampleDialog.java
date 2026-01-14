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

/* ExampleDialog.java */

package org.jscience.tests.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.ValuePair;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.util.Vector;

/**
 * ExampleDialog class.  Subclasses java.awt.Frame to provide an interface for
 * the KBS.
 *
 * @version:  1.2, 04/26/96
 * @author:   Paul Brown
 */
// This author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/

public class ExampleDialog extends java.awt.Frame {
	/**
	 * This represents a text string (used to display a question).
	 */
	private Label label;

	/**
	 * Valid answers and appropriate printable alternatives are contained
	 * in this Vector.
	 */
	private Vector responses;

	/**
	 * This variable is used to hold a user's response.
	 */
	private Object response;

	/**
	 * A reference to the kbs thread.
	 */
	private Thread kbs_thread;

	/**
	 * Constructs a new dialog window using the supplied specifications.
	 */
	public ExampleDialog(String title, Image icon, String label, Vector responses, Thread kbs_thread) {
		super(title);
		setLayout(new BorderLayout(15, 15));
		this.label = new Label(label, Label.CENTER);
		add("Center", this.label);
		Panel button_panel = new Panel();
		button_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		this.responses = responses;
		for (int i = 0; i < responses.size(); i++)
			button_panel.add(new Button((String)((ValuePair)responses.elementAt(i)).key()));
		add("South", button_panel);
		pack();
		move(100,100);
		setBackground(Color.pink);
		setCursor(HAND_CURSOR);
		setIconImage(icon);
		this.kbs_thread = kbs_thread;
	}

	/**
	 * User interaction is handled by this method.
	 */
	public boolean action(Event e, Object arg) {
		int i;
		if (e.target instanceof Button) {
			hide();
			dispose();
			for (i = 0; !arg.equals(((ValuePair)responses.elementAt(i)).key()); i++)
				;
			response = ((ValuePair)responses.elementAt(i)).data();
			//kbs_thread.resume(); <- this operation is now illegal (pb 21/10/96)
			synchronized (kbs_thread) {
			    kbs_thread.notify();
			}
			return(true);
		} else
			return(super.action(e, arg));
	}

	/**
	 * This is an accessor method for returning the value of this class'
	 * response variable.
	 */
	public Object response() {
		return(response);
	}
}
