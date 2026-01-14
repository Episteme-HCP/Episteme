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

/* ExampleApplet.java */

package org.jscience.tests.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.BlackboardSystem;
import org.jscience.computing.ai.blackboard.util.SymbolTable;
import org.jscience.computing.ai.blackboard.util.ValuePair;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.util.Hashtable;
import java.util.Vector;

/**
 * ExampleApplet class.  This class implements a KBS client application.
 *
 * @version:  1.2, 04/26/96
 * @author:   Paul Brown
 */

// This author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/

public class ExampleApplet extends java.applet.Applet implements pb.pbf.KBSClient, Runnable {
	/**
	 * An instance of a KBS.
	 */
	private BlackboardSystem kbs;

	/**
	 * The thread in which the KBS runs.
	 */
	private Thread kbs_thread;

	/**
	 * A graphical component used to initiate KBS execution.
	 */
	private Button start_button;

	/**
	 * A window used for user communication.
	 */
	private ExampleDialog dialog;

	/**
	 * This variable contains a reference to the currently selected animal picture.
	 */
	private Image picture;

	/**
	 * This contains the current applet status.
	 */
	private String status;

	/**
	 * Iconised images are stored in this array.
	 */
	private Image icons[];

	/**
	 * This is a collection of animal images.
	 */
	private Hashtable animals;

	/**
	 * When the applet is first loaded this method is called, it performs
	 * general resource initialisations.
	 */
	public void init() {
		String pics[] = {"unknown", "albatross", "cheetah", "giraffe", "ostrich", "penguin", "tiger", "zebra"};
		icons = new Image[2];
		icons[0] = getImage(this.getDocumentBase(), "../images/qstn.gif");
		icons[1] = getImage(this.getDocumentBase(), "../images/stmnt.gif");
		animals = new Hashtable(pics.length, 1.0f);
		for (int i = 0; i < pics.length; i++) {
			animals.put(pics[i], getImage(this.getDocumentBase(), "../images/" + pics[i] + ".gif"));
		}
		picture = (Image)animals.get("unknown");
		status = "Initialising PBF... ";
		kbs = new ExampleKBS(this);
		kbs.initKBS();
		setBackground(Color.gray);
		setLayout(new BorderLayout(15, 15));
		Panel button_panel = new Panel();
		button_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		start_button = new Button("Start");
		button_panel.add(start_button);
		add("South", button_panel);
		status = "PBF Ready";
	}

	/**
	 * This is the body of the kbs thread.
	 */
	public void run() {
		status = "PBF Running";
		kbs.execute();
		status = "PBF Ready";
	}

	/**
	 * Applet updating is facilitated by this method.
	 */
	public void paint(Graphics g) {
		g.drawImage(picture, 20, 20, this);
		showStatus(status);
	}

	/**
	 * When the applet is obscured from view this method is automatically
	 * called, the main task of this method is to terminate any running
	 * threads.
	 */
	public void stop() {
		if ((dialog != null) && (dialog.isShowing())) {
			dialog.hide();
			dialog.dispose();
		}
		dialog = null;
		if ((kbs_thread != null) && (kbs_thread.isAlive()))
			kbs_thread.stop();
		kbs_thread = null;
		kbs.reset();
		status = "PBF Ready";
	}

	/**
	 * All user interaction is monitored by this method.
	 */
	public boolean handleEvent(Event e) {
		showStatus(status);
		if ((e.id == Event.ACTION_EVENT) && (e.target == start_button)) {
			picture = (Image)animals.get("unknown");
			repaint();
			if ((kbs_thread != null) && (!kbs_thread.isAlive()))
				kbs_thread = null;
			if (kbs_thread == null) {
				kbs_thread = new Thread(this);
				kbs_thread.start();
			}
		}
		return(true);
	}

	/**
	 * This method is unused by the KBS and is therefore non-functional.
	 */
	public Object read(ValuePair args[]) {
		return(null);
	}

	/**
	 * Called by the KBS, this method creates a window and uses it to
	 * display output to a user.
	 */
	public void write(ValuePair args[], Hashtable bindings, SymbolTable symbols) {
		StringBuffer buffer = new StringBuffer();
		Vector responses = new Vector();
		for (int i = 0; i < args.length; i++)
			if (args[i].key().equals(ExampleKBS.STRING_VAL))
				buffer.append((String)args[i].data());
			else if (args[i].key().equals(ExampleKBS.STRING_VAR))
				buffer.append(symbols.get((Integer)bindings.get(symbols.put((String)args[i].data()))));
			else if (args[i].key().equals(ExampleKBS.APPLET_PIC)) {
				picture = (Image)animals.get(symbols.get((Integer)bindings.get(symbols.put((String)args[i].data()))));
				repaint();
			}
		System.out.println("! " + buffer.toString());
		System.out.println("========================================");
		responses.addElement(new ValuePair("Okay", null));
		dialog = new ExampleDialog("Statement", icons[1], buffer.toString(), responses, kbs_thread);
		dialog.show();
		//Thread.currentThread().suspend(); <- this operation is now illegal (pb 21/10/96)
		synchronized (kbs_thread) {
		    try {
		        kbs_thread.wait();
		    } catch (InterruptedException e) {
		        // do nothing
		    }
		}
	}

	/**
	 * Called by the KBS, this method constructs a query window and waits
	 * for a user response.
	 */
	public Object query(ValuePair args[], Hashtable bindings, SymbolTable symbols) {
		StringBuffer buffer = new StringBuffer();
		Vector responses = new Vector();
		for (int i = 0; i < args.length; i++)
			if (args[i].key().equals(ExampleKBS.STRING_VAL))
				buffer.append((String)args[i].data());
			else if (args[i].key().equals(ExampleKBS.STRING_VAR))
				buffer.append(symbols.get((Integer)bindings.get(symbols.put((String)args[i].data()))));
			else if (args[i].key().equals(ExampleKBS.INPUT_VAL))
				responses.addElement(args[i].data());
			else if (args[i].key().equals(ExampleKBS.APPLET_PIC)) {
				picture = (Image)animals.get(symbols.get((Integer)bindings.get(symbols.put((String)args[i].data()))));
				repaint();
			}
		dialog = new ExampleDialog("Question", icons[0], buffer.toString(), responses, kbs_thread);
		dialog.show();
		//Thread.currentThread().suspend(); <- this operation is now illegal (pb 21/10/96)
		synchronized (kbs_thread) {
		    try {
		        kbs_thread.wait();
		    } catch (InterruptedException e) {
		        // do nothing
		    }
		}
		System.out.println("? " + buffer.toString());
		for (int j = 0; j < responses.size(); j++)
			if (dialog.response().equals(((ValuePair)responses.elementAt(j)).data()))
				System.out.println("! " + ((ValuePair)responses.elementAt(j)).key().toString());
		return(dialog.response());
	}
}
