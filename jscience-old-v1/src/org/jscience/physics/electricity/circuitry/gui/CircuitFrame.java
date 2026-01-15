// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL


package org.jscience.physics.electricity.circuitry.gui;

import org.jscience.mathematics.algebraic.matrices.*;
import org.jscience.physics.electricity.circuitry.CircuitElement;
import org.jscience.physics.electricity.circuitry.CircuitNode;
import org.jscience.physics.electricity.circuitry.CircuitNodeLink;
import org.jscience.physics.electricity.circuitry.Scope;
import org.jscience.physics.electricity.circuitry.elements.*;

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

public class CircuitFrame extends Frame
  implements ComponentListener, ActionListener, AdjustmentListener,
             MouseMotionListener, MouseListener, ItemListener {
    
    public Thread engine = null;

    public Dimension winSize;
    public Image dbimage;
    
    public Random random;
    public static final int sourceRadius = 7;
    public static final double freqMult = 3.14159265*2*4;
    
    public String getAppletInfo() {
	return "Circuit by Paul Falstad";
    }

    public Container main;
    public Label titleLabel;
    public Button resetButton;
    //Button dumpMatrixButton;
    public MenuItem exportItem, importItem, exitItem;
    public Menu optionsMenu;
    public Checkbox stoppedCheck;
    public CheckboxMenuItem dotsCheckItem;
    public CheckboxMenuItem voltsCheckItem;
    public CheckboxMenuItem powerCheckItem;
    public CheckboxMenuItem smallGridCheckItem;
    public CheckboxMenuItem showValuesCheckItem;
    public CheckboxMenuItem euroResistorCheckItem;
    public Scrollbar speedBar;
    public Scrollbar currentBar;
    public Label powerLabel;
    public Scrollbar powerBar;
    public double currentMult, powerMult;
    public PopupMenu elmMenu;
    public MenuItem elmEditMenuItem;
    public MenuItem elmDeleteMenuItem;
    public MenuItem elmScopeMenuItem;
    public PopupMenu scopeMenu;
    public PopupMenu transScopeMenu;
    public PopupMenu mainMenu;
    public CheckboxMenuItem scopeVMenuItem;
    public CheckboxMenuItem scopeIMenuItem;
    public CheckboxMenuItem scopeMaxMenuItem;
    public CheckboxMenuItem scopeFreqMenuItem;
    public CheckboxMenuItem scopePowerMenuItem;
    public CheckboxMenuItem scopeIbMenuItem;
    public CheckboxMenuItem scopeIcMenuItem;
    public CheckboxMenuItem scopeIeMenuItem;
    public CheckboxMenuItem scopeVbeMenuItem;
    public CheckboxMenuItem scopeVbcMenuItem;
    public CheckboxMenuItem scopeVceMenuItem;
    public Class addingClass;
    public int mouseMode = -1;
    public String mouseModeStr = "hello";
    public Font unitsFont;
    public static final double pi = 3.14159265358979323846;
    public static final int MODE_ADD_ELM = 0;
    public static final int MODE_DRAG_ALL = 1;
    public static final int MODE_DRAG_ROW = 2;
    public static final int MODE_DRAG_COLUMN = 3;
    public static final int MODE_DRAG_SELECTED = 4;
    public static final int MODE_DRAG_POST = 5;
    public static final int infoWidth = 120;
    public int dragX, dragY;
    public int selectedSource;
    public int gridSize, gridMask, gridRound;
    public boolean dragging;
    public boolean analyzeFlag;
    public boolean dumpMatrix;
    public double t;
    public int pause = 10;
    public int xpoints[], ypoints[];
    public int colorScaleCount = 32;
    public Color colorScale[];
    public int scopeSelected = -1;
    public int menuScope = -1;
    public int hintType = -1, hintItem1, hintItem2;
    public String stopMessage;
    public double timeStep;
    
    public static final int HINT_LC = 1;
    public static final int HINT_RC = 2;
    public static final int HINT_3DB_C = 3;
    public static final int HINT_TWINT = 4;
    public static final int HINT_3DB_L = 5;
    public Vector elmList;
    public Vector setupList;
    public CircuitElement dragElement, menuElement, mouseElement, stopElement;
    public int draggingPost;
    public SwitchElement heldSwitchElement;
    public double circuitMatrix[][], circuitRightSide[], origRightSide[], origMatrix[][];
    public RowInfo circuitRowInfo[];
    public int circuitPermute[];
    public boolean circuitNonLinear;
    public int voltageSourceCount;
    public double voltageRange = 5;
    public int circuitMatrixSize, circuitMatrixFullSize;
    boolean circuitNeedsMap;
    public boolean useFrame;
    public int scopeCount;
    public Scope scopes[];
    public int scopeColCount[];
    public EditDialog editDialog;
    public ImportDialog impDialog;
    public Class dumpTypes[];
    public String muString = "u";
    public String ohmString = "ohm";
    public Rectangle circuitArea;

    public int getrand(int x) {
	int q = random.nextInt();
	if (q < 0) q = -q;
	return q % x;
    }
    CircuitCanvas cv;
    CircuitApplet applet;
    public NumberFormat showFormat, shortFormat, noCommaFormat;

    public CircuitFrame(CircuitApplet a) {
	super("Circuit Simulator v1.1c");
	applet = a;
	useFrame = false;
    }

    public void init() {
	String startCircuit = null;
	String startLabel = null;
	String euroResistor = null;
	String useFrameStr = null;

	try {
	    String param = applet.getParameter("PAUSE");
	    if (param != null)
		pause = Integer.parseInt(param);
	    startCircuit = applet.getParameter("startCircuit");
	    startLabel   = applet.getParameter("startLabel");
	    euroResistor = applet.getParameter("euroResistors");
	    useFrameStr  = applet.getParameter("useFrame");
	} catch (Exception e) { }
	
	if (startLabel == null)
	    startLabel = "LRC Circuit";
	if (startCircuit == null)
	    startCircuit = "lrc.txt";
	boolean euro = (euroResistor != null && euroResistor.equalsIgnoreCase("true"));
	useFrame = (useFrameStr == null || !useFrameStr.equalsIgnoreCase("false"));
	if (useFrame)
	    main = this;
	else
	    main = applet;
	
	unitsFont = new Font("SansSerif", 0, 10);
	String os = System.getProperty("os.name");
	String jv = System.getProperty("java.class.version");
	double jvf = new Double(jv).doubleValue();
	if (jvf >= 48) {
	    muString = "\u03bc";
	    ohmString = "\u03a9";
	}
	
	dumpTypes = new Class[300];
	// these characters are reserved
	dumpTypes[(int)'o'] = Scope.class;
	dumpTypes[(int)'h'] = Scope.class;
	dumpTypes[(int)'$'] = Scope.class;

	main.setLayout(new CircuitLayout());
	cv = new CircuitCanvas(this);
	cv.addComponentListener(this);
	cv.addMouseMotionListener(this);
	cv.addMouseListener(this);
	main.add(cv);

	mainMenu = new PopupMenu();
	MenuBar mb = null;
	if (useFrame)
	    mb = new MenuBar();
	Menu m = new Menu("File");
	if (useFrame)
	    mb.add(m);
	else
	    mainMenu.add(m);
	m.add(importItem = getMenuItem("Import"));
	m.add(exportItem = getMenuItem("Export"));
	m.add(exitItem   = getMenuItem("Exit"));

	m = new Menu("Scope");
	if (useFrame)
	    mb.add(m);
	else
	    mainMenu.add(m);
	m.add(getMenuItem("Stack All", "stackAll"));
	m.add(getMenuItem("Unstack All", "unstackAll"));

	optionsMenu = m = new Menu("Options");
	if (useFrame)
	    mb.add(m);
	else
	    mainMenu.add(m);
	m.add(dotsCheckItem = getCheckItem("Show Current"));
	dotsCheckItem.setState(true);
	m.add(voltsCheckItem = getCheckItem("Show Voltage"));
	voltsCheckItem.setState(true);
	m.add(powerCheckItem = getCheckItem("Show Power"));
	m.add(showValuesCheckItem = getCheckItem("Show Values"));
	showValuesCheckItem.setState(true);
	m.add(smallGridCheckItem = getCheckItem("Small Grid"));
	m.add(euroResistorCheckItem = getCheckItem("European Resistors"));
	euroResistorCheckItem.setState(euro);
	
	Menu circuitsMenu = new Menu("Circuits");
	if (useFrame)
	    mb.add(circuitsMenu);
	else
	    mainMenu.add(circuitsMenu);
	
	mainMenu.add(getClassCheckItem("Add Wire", "WireElement"));
	mainMenu.add(getClassCheckItem("Add Resistor", "ResistorElement"));
	
	Menu passMenu = new Menu("Passive Components");
	mainMenu.add(passMenu);
	passMenu.add(getClassCheckItem("Add Capacitor", "CapacitorElement"));
	passMenu.add(getClassCheckItem("Add Inductor", "InductorElement"));
	passMenu.add(getClassCheckItem("Add Switch", "SwitchElement"));
	passMenu.add(getClassCheckItem("Add Push Switch", "PushSwitchElement"));
	passMenu.add(getClassCheckItem("Add DPST Switch", "Switch2Element"));
	passMenu.add(getClassCheckItem("Add Transformer", "TransformerElement"));
	
	Menu inputMenu = new Menu("Inputs/Outputs");
	mainMenu.add(inputMenu);
	inputMenu.add(getClassCheckItem("Add Ground", "GroundElement"));
	inputMenu.add(getClassCheckItem("Add Voltage Source (2-terminal)", "DCVoltageElement"));
	inputMenu.add(getClassCheckItem("Add A/C Source (2-terminal)", "ACVoltageElement"));
	inputMenu.add(getClassCheckItem("Add Voltage Source (1-terminal)", "RailElement"));
	inputMenu.add(getClassCheckItem("Add A/C Source (1-terminal)", "ACRailElement"));
	inputMenu.add(getClassCheckItem("Add Square Wave (1-terminal)", "SquareRailElement"));
	inputMenu.add(getClassCheckItem("Add Analog Output", "OutputElement"));
	inputMenu.add(getClassCheckItem("Add Logic Input", "LogicInputElement"));
	inputMenu.add(getClassCheckItem("Add Logic Output", "LogicOutputElement"));
	inputMenu.add(getClassCheckItem("Add Clock", "ClockElement"));
	inputMenu.add(getClassCheckItem("Add Antenna", "AntennaElement"));
	inputMenu.add(getClassCheckItem("Add Current Source", "CurrentElement"));
	inputMenu.add(getClassCheckItem("Add LED", "LEDElement"));
	
	Menu activeMenu = new Menu("Active Components");
	mainMenu.add(activeMenu);
	activeMenu.add(getClassCheckItem("Add Diode", "DiodeElement"));
	activeMenu.add(getClassCheckItem("Add Transistor (bipolar, NPN)",
				    "NTransistorElement"));
	activeMenu.add(getClassCheckItem("Add Transistor (bipolar, PNP)",
				    "PTransistorElement"));
	activeMenu.add(getClassCheckItem("Add Op Amp (- on top)", "OpAmpElement"));
	activeMenu.add(getClassCheckItem("Add Op Amp (+ on top)",
				    "OpAmpSwapElement"));
	activeMenu.add(getClassCheckItem("Add MOSFET (n-channel)",
				    "NMosfetElement"));
	activeMenu.add(getClassCheckItem("Add MOSFET (p-channel)",
				    "PMosfetElement"));
	activeMenu.add(getClassCheckItem("Add JFET (n-channel)",
					 "NJfetElement"));
	activeMenu.add(getClassCheckItem("Add Analog Switch (SPST)", "AnalogSwitchElement"));
	activeMenu.add(getClassCheckItem("Add Analog Switch (SPDT)", "AnalogSwitch2Element"));

	Menu gateMenu = new Menu("Logic Gates");
	mainMenu.add(gateMenu);
	gateMenu.add(getClassCheckItem("Add Inverter", "InverterElement"));
	gateMenu.add(getClassCheckItem("Add NAND Gate", "NandGateElement"));
	gateMenu.add(getClassCheckItem("Add NOR Gate", "NorGateElement"));
	gateMenu.add(getClassCheckItem("Add AND Gate", "AndGateElement"));
	gateMenu.add(getClassCheckItem("Add OR Gate", "OrGateElement"));
	gateMenu.add(getClassCheckItem("Add XOR Gate", "XorGateElement"));

	Menu chipMenu = new Menu("Chips");
	mainMenu.add(chipMenu);
	chipMenu.add(getClassCheckItem("Add D Flip-Flop", "DFlipFlopElement"));
	chipMenu.add(getClassCheckItem("Add JK Flip-Flop", "JKFlipFlopElement"));
	chipMenu.add(getClassCheckItem("Add 7 Segment LED", "SevenSegElement"));
	chipMenu.add(getClassCheckItem("Add VCO", "VCOElement"));
	chipMenu.add(getClassCheckItem("Add Phase Comparator", "PhaseCompElement"));
	chipMenu.add(getClassCheckItem("Add Counter", "CounterElement"));
	chipMenu.add(getClassCheckItem("Add Johnson Counter", "JohnsonElement"));
	chipMenu.add(getClassCheckItem("Add 555 Timer", "TimerElement"));
	
	Menu otherMenu = new Menu("Other");
	mainMenu.add(otherMenu);
	otherMenu.add(getClassCheckItem("Add Text", "TextElement"));
	otherMenu.add(getClassCheckItem("Add Scope Probe", "ProbeElement"));
	otherMenu.add(getCheckItem("Drag All", "DragAll"));
	otherMenu.add(getCheckItem("Drag Row", "DragRow"));
	otherMenu.add(getCheckItem("Drag Column", "DragColumn"));
	otherMenu.add(getCheckItem("Drag Selected", "DragSelected"));
	otherMenu.add(getCheckItem("Drag Post", "DragPost"));
	
	main.add(mainMenu);

	main.add(resetButton = new Button("Reset"));
	resetButton.addActionListener(this);
	/*add(dumpMatrixButton = new Button("Dump Matrix"));
	  dumpMatrixButton.addActionListener(this);*/
	stoppedCheck = new Checkbox("Stopped");
	stoppedCheck.addItemListener(this);
	main.add(stoppedCheck);
	
	main.add(new Label("Simulation Speed", Label.CENTER));
	
	main.add(speedBar = new Scrollbar(Scrollbar.HORIZONTAL, 3, 1, 1, 140));
	speedBar.addAdjustmentListener(this);

	main.add(new Label("Current Speed", Label.CENTER));
	main.add(currentBar = new Scrollbar(Scrollbar.HORIZONTAL,
				    50, 1, 1, 100));
	currentBar.addAdjustmentListener(this);

	main.add(powerLabel = new Label("Power Brightness", Label.CENTER));
	main.add(powerBar = new Scrollbar(Scrollbar.HORIZONTAL,
				    50, 1, 1, 100));
	powerBar.addAdjustmentListener(this);
	powerBar.disable();
	powerLabel.disable();

	main.add(new Label("www.falstad.com"));

	main.add(new Label(""));
	Font f = new Font("SansSerif", 0, 10);
	Label l;
	main.add(l = new Label("Current Circuit:"));
	l.setFont(f);
	main.add(titleLabel = new Label("Label"));
	titleLabel.setFont(f);

	setGrid();
	elmList = new Vector();
	setupList = new Vector();
	colorScale = new Color[colorScaleCount];
	int i;
	for (i = 0; i != colorScaleCount; i++) {
	    double v = i*2./colorScaleCount - 1;
	    if (v < 0) {
		int n1 = (int) (128*-v)+127;
		int n2 = (int) (127*(1+v));
		colorScale[i] = new Color(n1, n2, n2);
	    } else {
		int n1 = (int) (128*v)+127;
		int n2 = (int) (127*(1-v));
		colorScale[i] = new Color(n2, n1, n2);
	    }
	}

	xpoints = new int[4];
	ypoints = new int[4];
	scopes = new Scope[20];
	scopeColCount = new int[20];
	scopeCount = 0;
	
	random = new Random();
	cv.setBackground(Color.black);
	cv.setForeground(Color.lightGray);
	showFormat = DecimalFormat.getInstance();
	showFormat.setMaximumFractionDigits(2);
	shortFormat = DecimalFormat.getInstance();
	shortFormat.setMaximumFractionDigits(1);
	noCommaFormat = DecimalFormat.getInstance();
	noCommaFormat.setMaximumFractionDigits(10);
	noCommaFormat.setGroupingUsed(false);
	
	elmMenu = new PopupMenu();
	elmMenu.add(elmEditMenuItem = getMenuItem("Edit"));
	elmMenu.add(elmDeleteMenuItem = getMenuItem("Delete"));
	elmMenu.add(elmScopeMenuItem = getMenuItem("View in Scope"));
	main.add(elmMenu);
	
	scopeMenu = buildScopeMenu(false);
	transScopeMenu = buildScopeMenu(true);

	getSetupList(circuitsMenu);
	if (useFrame)
	    setMenuBar(mb);
	if (stopMessage == null)
	    readSetupFile(startCircuit, startLabel);

	if (useFrame) {
	    Dimension screen = getToolkit().getScreenSize();
	    resize(800, 640);
	    handleResize();
	    Dimension x = getSize();
	    setLocation((screen.width  - x.width)/2,
			(screen.height - x.height)/2);
	    show();
	} else {
	    hide();
	    handleResize();
	    applet.validate();
	}
	main.requestFocus();
    }

    public PopupMenu buildScopeMenu(boolean t) {
	PopupMenu m = new PopupMenu();
	m.add(getMenuItem("Remove", "remove"));
	m.add(getMenuItem("Speed 2x", "speed2"));
	m.add(getMenuItem("Speed 1/2x", "speed1/2"));
	m.add(getMenuItem("Scale 2x", "scale"));
	m.add(getMenuItem("Stack", "stack"));
	m.add(getMenuItem("Unstack", "unstack"));
	if (t) {
	    m.add(scopeIbMenuItem = getCheckItem("Show Ib"));
	    m.add(scopeIcMenuItem = getCheckItem("Show Ic"));
	    m.add(scopeIeMenuItem = getCheckItem("Show Ie"));
	    m.add(scopeVbeMenuItem = getCheckItem("Show Vbe"));
	    m.add(scopeVbcMenuItem = getCheckItem("Show Vbc"));
	    m.add(scopeVceMenuItem = getCheckItem("Show Vce"));
	} else {
	    m.add(scopeVMenuItem = getCheckItem("Show Voltage"));
	    m.add(scopeIMenuItem = getCheckItem("Show Current"));
	    m.add(scopePowerMenuItem = getCheckItem("Show Power Consumed"));
	    m.add(scopeMaxMenuItem = getCheckItem("Show Peak Value"));
	    m.add(scopeFreqMenuItem = getCheckItem("Show Frequency"));
	}
	main.add(m);
	return m;
    }
    
    public MenuItem getMenuItem(String s) {
	MenuItem mi = new MenuItem(s);
	mi.addActionListener(this);
	return mi;
    }

    public MenuItem getMenuItem(String s, String ac) {
	MenuItem mi = new MenuItem(s);
	mi.setActionCommand(ac);
	mi.addActionListener(this);
	return mi;
    }

    public CheckboxMenuItem getCheckItem(String s) {
	CheckboxMenuItem mi = new CheckboxMenuItem(s);
	mi.addItemListener(this);
	mi.setActionCommand("");
	return mi;
    }

    public CheckboxMenuItem getClassCheckItem(String s, String t) {
	try {
	    Class c = Class.forName("org.jscience.physics.electricity.circuitry.elements." + t);
	    CircuitElement elm = constructElement(c, 0, 0);
        elm.setCircuitFrame(this);
        register(c, elm);
	} catch (Exception ee) {
	    ee.printStackTrace();
	}
	return getCheckItem(s, t);
    }
    
    public CheckboxMenuItem getCheckItem(String s, String t) {
	CheckboxMenuItem mi = new CheckboxMenuItem(s);
	mi.addItemListener(this);
	mi.setActionCommand(t);
	return mi;
    }

    public void register(Class c, CircuitElement elm) {
	int t = elm.getDumpType();
	if (t == 0) {
	    System.out.println("no dump type: " + c);
	    return;
	}
	Class dclass = elm.getDumpClass();
	if (dumpTypes[t] == dclass)
	    return;
	if (dumpTypes[t] != null) {
	    System.out.println("dump type conflict: " + c + " " +
			       dumpTypes[t]);
	    return;
	}
	dumpTypes[t] = dclass;
    }
    
    public void handleResize() {
        winSize = cv.getSize();
	if (winSize.width == 0)
	    return;
	dbimage = main.createImage(winSize.width, winSize.height);
	int h = winSize.height / 5;
	circuitArea = new Rectangle(0, 0, winSize.width, winSize.height-h);
	int i;
	int minx = 1000, maxx = 0, miny = 1000, maxy = 0;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    minx = min(ce.x, min(ce.x2, minx));
	    miny = min(ce.y, min(ce.y2, miny));
	    maxx = max(ce.x, max(ce.x2, maxx));
	    maxy = max(ce.y, max(ce.y2, maxy));
	}
	// center circuit; we don't use snapGrid() because that rounds
	int dx = gridMask & ((circuitArea.width -(maxx-minx))/2-minx);
	int dy = gridMask & ((circuitArea.height-(maxy-miny))/2-miny);
	if (dx+minx < 0)
	    dx = gridMask & (-minx);
	if (dy+miny < 0)
	    dy = gridMask & (-miny);
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    ce.move(dx, dy);
	}
    }

    public boolean handleEvent(Event ev) {
        if (ev.id == Event.WINDOW_DESTROY) {
            applet.destroyFrame();
            return true;
        }
        return super.handleEvent(ev);
    }
    
    public void centerString(Graphics g, String s, int y) {
	FontMetrics fm = g.getFontMetrics();
        g.drawString(s, (winSize.width-fm.stringWidth(s))/2, y);
    }

    public void paint(Graphics g) {
	cv.repaint();
    }

    static final int resct = 6;
    long lastTime = 0, lastFrameTime, secTime = 0;
    int frames = 0;
    int steps = 0;
    int framerate = 0, steprate = 0;

    public void updateCircuit(Graphics realg) {
	if (winSize == null || winSize.width == 0)
	    return;
	if (analyzeFlag) {
	    analyzeCircuit();
	    analyzeFlag = false;
	}
	if (editDialog != null)
	    mouseElement = editDialog.elm;
	if (mouseElement == null)
	    mouseElement = stopElement;
	setupScopes();
        Graphics g = null;
	g = dbimage.getGraphics();
	g.setColor(cv.getBackground());
	g.fillRect(0, 0, winSize.width, winSize.height);
	if (!stoppedCheck.getState()) {
	    try {
		runCircuit();
	    } catch (Exception e) {
		e.printStackTrace();
		analyzeFlag = true;
		cv.repaint();
		return;
	    }
	}
	if (!stoppedCheck.getState()) {
	    long sysTime = System.currentTimeMillis();
	    if (lastTime != 0) {
		int inc = (int) (sysTime-lastTime);
		double c = currentBar.getValue();
		c = java.lang.Math.exp(c/3.5-14.2);
		currentMult = 1.7 * inc * c;
	    }
	    if (sysTime-secTime >= 1000) {
		framerate = frames; steprate = steps;
		frames = 0; steps = 0;
		secTime = sysTime;
	    }
	    lastTime = sysTime;
	} else
	    lastTime = 0;
	powerMult = Math.exp(powerBar.getValue()/4.762-7);
	
	int i;
	Font oldfont = g.getFont();
	for (i = 0; i != elmList.size(); i++) {
	    if (powerCheckItem.getState())
		g.setColor(Color.gray);
	    getElement(i).draw(g);
	}
	if (mouseMode == MODE_DRAG_ROW || mouseMode == MODE_DRAG_COLUMN)
	    for (i = 0; i != elmList.size(); i++) {
		CircuitElement ce = getElement(i);
		ce.drawPost(g, ce.x , ce.y );
		ce.drawPost(g, ce.x2, ce.y2);
	    }
	/*if (mouseElement != null) {
	    g.setFont(oldfont);
	    g.drawString("+", mouseElement.x+10, mouseElement.y);
	    }*/
	if (dragElement != null &&
	      (dragElement.x != dragElement.x2 || dragElement.y != dragElement.y2))
	    dragElement.draw(g);
	g.setFont(oldfont);
	int ct = scopeCount;
	if (stopMessage != null)
	    ct = 0;
	for (i = 0; i != ct; i++)
	    scopes[i].draw(g);
	g.setColor(Color.white);
	int ybase = circuitArea.height;
	if (stopMessage != null) {
	    g.drawString(stopMessage, 10, ybase+15);
	} else {
	    String info[] = new String[10];
	    if (mouseElement != null) {
		mouseElement.getInfo(info);
		/*for (i = 0; i != mouseElement.getPostCount(); i++)
		  info[0] += " " + mouseElement.nodes[i];*/
	    } else {
		showFormat.setMinimumFractionDigits(2);
		info[0] = "t = " + getUnitText(t, "s");
		showFormat.setMinimumFractionDigits(0);
	    }
	    if (hintType != -1) {
		for (i = 0; info[i] != null; i++)
		    ;
		String s = getHint();
		if (s == null)
		    hintType = -1;
		else
		    info[i] = s;
	    }
	    int x = 0;
	    if (ct != 0)
		x = scopes[ct-1].rightEdge() + 20;
	    x = max(x, winSize.width*2/3);
	    for (i = 0; info[i] != null; i++)
		g.drawString(info[i], x,
			     ybase+15*(i+1));
	}
	if (mouseElement == stopElement)
	    mouseElement = null;
	frames++;
	/*g.setColor(Color.white);
	g.drawString("Framerate: " + framerate, 10, 10);
	g.drawString("Steprate: " + steprate,  10, 30);
	g.drawString("Steprate/iter: " + (steprate/getIterCount()),  10, 50);
	g.drawString("iterc: " + (getIterCount()),  10, 70);*/
	lastFrameTime = lastTime;

	realg.drawImage(dbimage, 0, 0, this);
	if (!stoppedCheck.getState() && circuitMatrix != null)
	    cv.repaint(pause);
    }

    public void setupScopes() {
	int i;
	
	// check scopes to make sure the elements still exist, and remove
	// unused scopes/columns
	int pos = -1;
	for (i = 0; i < scopeCount; i++) {
	    if (locateElement(scopes[i].elm) < 0)
		scopes[i].setElement(null);
	    if (scopes[i].elm == null) {
		int j;
		for (j = i; j != scopeCount; j++)
		    scopes[j] = scopes[j+1];
		scopeCount--;
		i--;
		continue;
	    }
	    if (scopes[i].position > pos+1)
		scopes[i].position = pos+1;
	    pos = scopes[i].position;
	}
	while (scopeCount > 0 && scopes[scopeCount-1].elm == null)
	    scopeCount--;
	int h = winSize.height - circuitArea.height;
	pos = 0;
	for (i = 0; i != scopeCount; i++)
	    scopeColCount[i] = 0;
	for (i = 0; i != scopeCount; i++) {
	    pos = max(scopes[i].position, pos);
	    scopeColCount[scopes[i].position]++;
	}
	int colct = pos+1;
	int iw = infoWidth;
	if (colct <= 2)
	    iw = iw*3/2;
	int w = (winSize.width-iw) / colct;
	int marg = 10;
	if (w < marg*2)
	    w = marg*2;
	pos = -1;
	int colh = 0;
	int row = 0;
	int speed = 0;
	for (i = 0; i != scopeCount; i++) {
	    Scope s = scopes[i];
	    if (s.position > pos) {
		pos = s.position;
		colh = h / scopeColCount[pos];
		row = 0;
		speed = s.speed;
	    }
	    if (s.speed != speed) {
		s.speed = speed;
		s.resetGraph();
	    }
	    Rectangle r = new Rectangle(pos*w, winSize.height-h+colh*row,
					w-marg, colh);
	    row++;
	    if (!r.equals(s.rect))
		s.setRect(r);
	}
    }
    
    public String getHint() {
	CircuitElement c1 = getElement(hintItem1);
	CircuitElement c2 = getElement(hintItem2);
	if (c1 == null || c2 == null)
	    return null;
	if (hintType == HINT_LC) {
	    if (!(c1 instanceof InductorElement))
		return null;
	    if (!(c2 instanceof CapacitorElement))
		return null;
	    InductorElement ie = (InductorElement) c1;
	    CapacitorElement ce = (CapacitorElement) c2;
	    return "res.f = " + getUnitText(1/(2*pi*Math.sqrt(ie.inductance*
						    ce.capacitance)), "Hz");
	}
	if (hintType == HINT_RC) {
	    if (!(c1 instanceof ResistorElement))
		return null;
	    if (!(c2 instanceof CapacitorElement))
		return null;
	    ResistorElement re = (ResistorElement) c1;
	    CapacitorElement ce = (CapacitorElement) c2;
	    return "RC = " + getUnitText(re.resistance*ce.capacitance,
					 "s");
	}
	if (hintType == HINT_3DB_C) {
	    if (!(c1 instanceof ResistorElement))
		return null;
	    if (!(c2 instanceof CapacitorElement))
		return null;
	    ResistorElement re = (ResistorElement) c1;
	    CapacitorElement ce = (CapacitorElement) c2;
	    return "f.3db = " +
		getUnitText(1/(2*pi*re.resistance*ce.capacitance), "Hz");
	}
	if (hintType == HINT_3DB_L) {
	    if (!(c1 instanceof ResistorElement))
		return null;
	    if (!(c2 instanceof InductorElement))
		return null;
	    ResistorElement re = (ResistorElement) c1;
	    InductorElement ie = (InductorElement) c2;
	    return "f.3db = " +
		getUnitText(re.resistance/(2*pi*ie.inductance), "Hz");
	}
	if (hintType == HINT_TWINT) {
	    if (!(c1 instanceof ResistorElement))
		return null;
	    if (!(c2 instanceof CapacitorElement))
		return null;
	    ResistorElement re = (ResistorElement) c1;
	    CapacitorElement ce = (CapacitorElement) c2;
	    return "fc = " +
		getUnitText(1/(2*pi*re.resistance*ce.capacitance), "Hz");
	}
	return null;
    }

    public void toggleSwitch(int n) {
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    if (ce instanceof SwitchElement) {
		n--;
		if (n == 0) {
		    ((SwitchElement) ce).toggle();
		    analyzeFlag = true;
		    cv.repaint();
		    return;
		}
	    }
	}
    }
    
    public void needAnalyze() {
	analyzeFlag = true;
	cv.repaint();
    }
    
    public void drawDots(Graphics g, int x1, int y1, int x2, int y2,
		  double pos) {
	if (stoppedCheck.getState() || pos == 0 || !dotsCheckItem.getState())
	    return;
	int dx = x2-x1;
	int dy = y2-y1;
	double dn = Math.sqrt(dx*dx+dy*dy);
	g.setColor(Color.yellow);
	int ds = 16;
	pos %= ds;
	if (pos < 0)
	    pos += ds;
	double di = 0;
	for (di = pos; di < dn; di += ds) {
	    int x0 = (int) (x1+di*dx/dn);
	    int y0 = (int) (y1+di*dy/dn);
	    g.fillRect(x0-1, y0-1, 4, 4);
	}
    }

 

    public Vector nodeList;
    CircuitElement voltageSources[];

    public CircuitNode getCircuitNode(int n) {
	if (n >= nodeList.size())
	    return null;
	return (CircuitNode) nodeList.elementAt(n);
    }

    public CircuitElement getElement(int n) {
	if (n >= elmList.size())
	    return null;
	return (CircuitElement) elmList.elementAt(n);
    }
    
    public void analyzeCircuit() {
	if (elmList.isEmpty())
	    return;
	stopMessage = null;
	stopElement = null;
	int i, j;
	int vscount = 0;
	nodeList = new Vector();
	boolean gotGround = false;
	CircuitElement volt = null;
	
	// look for voltage or ground element
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    if (ce instanceof GroundElement) {
		gotGround = true;
		break;
	    }
	    if (volt == null && ce instanceof VoltageElement &&
		!(ce instanceof RailElement))
		volt = ce;
	}

	// if no ground, then the voltage elm's first terminal is ground
	if (!gotGround && volt != null) {
	    CircuitNode cn = new CircuitNode();
	    Point pt = volt.getPost(0);
	    cn.x = pt.x;
	    cn.y = pt.y;
	    nodeList.addElement(cn);
	} else {
	    // otherwise allocate extra node for ground
	    CircuitNode cn = new CircuitNode();
	    cn.x = cn.y = -1;
	    nodeList.addElement(cn);
	}

	// allocate nodes and voltage sources
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    int inodes = ce.getInternalNodeCount();
	    int ivs = ce.getVoltageSourceCount();
	    int posts = ce.getPostCount();
	    
	    // allocate a node for each post and match posts to nodes
	    for (j = 0; j != posts; j++) {
		Point pt = ce.getPost(j);
		int k;
		for (k = 0; k != nodeList.size(); k++) {
		    CircuitNode cn = getCircuitNode(k);
		    if (pt.x == cn.x && pt.y == cn.y)
			break;
		}
		if (k == nodeList.size()) {
		    CircuitNode cn = new CircuitNode();
		    cn.x = pt.x;
		    cn.y = pt.y;
		    CircuitNodeLink cnl = new CircuitNodeLink();
		    cnl.num = j;
		    cnl.elm = ce;
		    cn.links.addElement(cnl);
		    ce.setNode(j, nodeList.size());
		    nodeList.addElement(cn);
		} else {
		    CircuitNodeLink cnl = new CircuitNodeLink();
		    cnl.num = j;
		    cnl.elm = ce;
		    getCircuitNode(k).links.addElement(cnl);
		    ce.setNode(j, k);
		}
	    }
	    for (j = 0; j != inodes; j++) {
		CircuitNode cn = new CircuitNode();
		cn.x = cn.y = -1;
		cn.internal = true;
		CircuitNodeLink cnl = new CircuitNodeLink();
		cnl.num = j+posts;
		cnl.elm = ce;
		cn.links.addElement(cnl);
		ce.setNode(cnl.num, nodeList.size());
		nodeList.addElement(cn);
	    }
	    vscount += ivs;
	}
	voltageSources = new CircuitElement[vscount];
	vscount = 0;
	circuitNonLinear = false;

	// determine if circuit is nonlinear
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    if (ce.nonLinear())
		circuitNonLinear = true;
	    int ivs = ce.getVoltageSourceCount();
	    for (j = 0; j != ivs; j++) {
		voltageSources[vscount] = ce;
		ce.setVoltageSource(j, vscount++);
	    }
	}
	voltageSourceCount = vscount;

	int matrixSize = nodeList.size()-1 + vscount;
	circuitMatrix = new double[matrixSize][matrixSize];
	circuitRightSide = new double[matrixSize];
	origMatrix = new double[matrixSize][matrixSize];
	origRightSide = new double[matrixSize];
	circuitMatrixSize = circuitMatrixFullSize = matrixSize;
	circuitRowInfo = new RowInfo[matrixSize];
	circuitPermute = new int[matrixSize];
	int vs = 0;
	for (i = 0; i != matrixSize; i++)
	    circuitRowInfo[i] = new RowInfo();
	circuitNeedsMap = false;
	
	// stamp linear circuit elements
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    ce.stamp();
	}

	// determine nodes that are unconnected
	boolean closure[] = new boolean[nodeList.size()];
	boolean tempclosure[] = new boolean[nodeList.size()];
	boolean changed = true;
	closure[0] = true;
	while (changed) {
	    changed = false;
	    for (i = 0; i != elmList.size(); i++) {
		CircuitElement ce = getElement(i);
		// loop through all ce's nodes to see if they are connected
		// to other nodes not in closure
		for (j = 0; j < ce.getPostCount(); j++) {
		    if (!closure[ce.getNode(j)]) {
			if (ce.hasGroundConnection(j))
			    closure[ce.getNode(j)] = changed = true;
			continue;
		    }
		    int k;
		    for (k = 0; k != ce.getPostCount(); k++) {
			if (j == k)
			    continue;
			int kn = ce.getNode(k);
			if (ce.getConnection(j, k) && !closure[kn]) {
			    closure[kn] = true;
			    changed = true;
			}
		    }
		}
	    }
	    if (changed)
		continue;

	    // connect unconnected nodes
	    for (i = 0; i != nodeList.size(); i++)
		if (!closure[i] && !getCircuitNode(i).internal) {
		    System.out.println("node " + i + " unconnected");
		    stampResistor(0, i, 1e8);
		    closure[i] = true;
		    changed = true;
		    break;
		}
	}

	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    // look for inductors with no current path
	    if (ce instanceof InductorElement) {
		FindPathInfo fpi = new FindPathInfo(FindPathInfo.FPI_INDUCT, ce,
						    ce.getNode(1), this);
		if (!fpi.findPath(ce.getNode(0))) {
		    System.out.println(ce + " no path");
		    ce.reset();
		}
	    }
	    // look for current sources with no current path
	    if (ce instanceof CurrentElement) {
		FindPathInfo fpi = new FindPathInfo(FindPathInfo.FPI_INDUCT, ce,
						    ce.getNode(1), this);
		if (!fpi.findPath(ce.getNode(0))) {
		    stop("No path for current source!", ce);
		    return;
		}
	    }
	    // look for voltage source loops
	    if (ce instanceof VoltageElement && ce.getPostCount() == 2) {
		FindPathInfo fpi = new FindPathInfo(FindPathInfo.FPI_VOLTAGE, ce,
						    ce.getNode(1), this);
		if (fpi.findPath(ce.getNode(0))) {
		    stop("Voltage source loop with no resistance!", ce);
		    return;
		}
	    }
	    // look for shorted caps, or caps w/ voltage but no R
	    if (ce instanceof CapacitorElement) {
		FindPathInfo fpi = new FindPathInfo(FindPathInfo.FPI_SHORT, ce,
						    ce.getNode(1), this);
		if (fpi.findPath(ce.getNode(0))) {
		    System.out.println(ce + " shorted");
		    ce.reset();
		} else {
		    fpi = new FindPathInfo(FindPathInfo.FPI_CAP_V, ce, ce.getNode(1), this);
		    if (fpi.findPath(ce.getNode(0))) {
			stop("Capacitor loop with no resistance!", ce);
			return;
		    }
		}
	    }
	}

	// simplify the matrix; this speeds things up quite a bit
	for (i = 0; i != matrixSize; i++) {
	    int qm = -1, qp = -1;
	    double qv = 0;
	    RowInfo re = circuitRowInfo[i];
	    /*System.out.println("row " + i + " " +
	      re.lsChanges + " " + re.rsChanges);*/
	    if (re.lsChanges || re.dropRow || re.rsChanges)
		continue;
	    double rsadd = 0;
	    
	    // look for rows that can be removed
	    for (j = 0; j != matrixSize; j++) {
		double q = circuitMatrix[i][j];
		if (circuitRowInfo[j].type == RowInfo.ROW_CONST) {
		    // keep a running total of const values that have been
		    // removed already
		    rsadd -= circuitRowInfo[j].value*q;
		    continue;
		}
		if (q == 0)
		    continue;
		if (qp == -1) {
		    qp = j;
		    qv = q;
		    continue;
		}
		if (qm == -1 && q == -qv) {
		    qm = j;
		    continue;
		}
		break;
	    }
	    //System.out.println("line " + i + " " + qp + " " + qm);
	    /*if (qp != -1 && circuitRowInfo[qp].lsChanges) {
		System.out.println("lschanges");
		continue;
	    }
	    if (qm != -1 && circuitRowInfo[qm].lsChanges) {
		System.out.println("lschanges");
		continue;
		}*/
	    if (j == matrixSize) {
		RowInfo elt = circuitRowInfo[qp];
		if (qm == -1) {
		    // we found a row with only one nonzero entry; that value
		    // is a constant
		    while (elt.type == RowInfo.ROW_EQUAL) {
			// follow the chain
			/*System.out.println("following equal chain from " +
			  qp + " to " + elt.nodeEq);*/
			qp = elt.nodeEq;
			elt = circuitRowInfo[qp];
		    }
		    if (elt.type != RowInfo.ROW_NORMAL) {
			System.out.println("type already " + elt.type + " for " + qp + "!");
			continue;
		    }
		    elt.type = RowInfo.ROW_CONST;
		    elt.value = (circuitRightSide[i]+rsadd)/qv;
		    circuitRowInfo[i].dropRow = true;
		    //System.out.println(qp + " * " + qv + " = const " + elt.value);
		    i = -1; // start over from scratch
		} else if (circuitRightSide[i]+rsadd == 0) {
		    // we found a row with only two nonzero entries, and one
		    // is the negative of the other; the values are equal
		    if (elt.type != RowInfo.ROW_NORMAL) {
			//System.out.println("swapping");
			int qq = qm;
			qm = qp; qp = qq;
			elt = circuitRowInfo[qp];
			if (elt.type != RowInfo.ROW_NORMAL) {
			    // we should follow the chain here, but this
			    // hardly ever happens so it's not worth worrying
			    // about
			    System.out.println("swap failed");
			    continue;
			}
		    }
		    elt.type = RowInfo.ROW_EQUAL;
		    elt.nodeEq = qm;
		    circuitRowInfo[i].dropRow = true;
		    //System.out.println(qp + " = " + qm);
		}
	    }
	}

	// find size of new matrix
	int nn = 0;
	for (i = 0; i != matrixSize; i++) {
	    RowInfo elt = circuitRowInfo[i];
	    if (elt.type == RowInfo.ROW_NORMAL) {
		elt.mapCol = nn++;
		//System.out.println("col " + i + " maps to " + elt.mapCol);
		continue;
	    }
	    if (elt.type == RowInfo.ROW_EQUAL) {
		RowInfo e2 = null;
		// resolve chains of equality
		while (true) {
		    e2 = circuitRowInfo[elt.nodeEq];
		    if (e2.type != RowInfo.ROW_EQUAL)
			break;
		    elt.nodeEq = e2.nodeEq;
		    //System.out.println("following link");
		}
	    }
	    if (elt.type == RowInfo.ROW_CONST)
		elt.mapCol = -1;
	}
	for (i = 0; i != matrixSize; i++) {
	    RowInfo elt = circuitRowInfo[i];
	    if (elt.type == RowInfo.ROW_EQUAL) {
		RowInfo e2 = circuitRowInfo[elt.nodeEq];
		if (e2.type == RowInfo.ROW_CONST) {
		    // if something is equal to a const, it's a const
		    elt.type = e2.type;
		    elt.value = e2.value;
		    elt.mapCol = -1;
		    //System.out.println(i + " = [late]const " + elt.value);
		} else {
		    elt.mapCol = e2.mapCol;
		    //System.out.println(i + " maps to: " + e2.mapCol);
		}
	    }
	}

	/*System.out.println("matrixSize = " + matrixSize);
	for (j = 0; j != circuitMatrixSize; j++) {
	    for (i = 0; i != circuitMatrixSize; i++)
		System.out.print(circuitMatrix[j][i] + " ");
	    System.out.print("  " + circuitRightSide[j] + "\n");
	}
	System.out.print("\n");*/

	// make the new, simplified matrix
	int newsize = nn;
	double newmatx[][] = new double[newsize][newsize];
	double newrs  []   = new double[newsize];
	int ii = 0;
	for (i = 0; i != matrixSize; i++) {
	    RowInfo rri = circuitRowInfo[i];
	    if (rri.dropRow) {
		rri.mapRow = -1;
		continue;
	    }
	    newrs[ii] = circuitRightSide[i];
	    rri.mapRow = ii;
	    for (j = 0; j != matrixSize; j++) {
		RowInfo ri = circuitRowInfo[j];
		if (ri.type == RowInfo.ROW_CONST)
		    newrs[ii] -= ri.value*circuitMatrix[i][j];
		else
		    newmatx[ii][ri.mapCol] += circuitMatrix[i][j];
	    }
	    ii++;
	}

	circuitMatrix = newmatx;
	circuitRightSide = newrs;
	matrixSize = circuitMatrixSize = newsize;
	for (i = 0; i != matrixSize; i++)
	    origRightSide[i] = circuitRightSide[i];
	for (i = 0; i != matrixSize; i++)
	    for (j = 0; j != matrixSize; j++)
		origMatrix[i][j] = circuitMatrix[i][j];
	circuitNeedsMap = true;

	/*System.out.println("matrixSize = " + matrixSize + " " + circuitNonLinear);
	for (j = 0; j != circuitMatrixSize; j++) {
	    for (i = 0; i != circuitMatrixSize; i++)
		System.out.print(circuitMatrix[j][i] + " ");
	    System.out.print("  " + circuitRightSide[j] + "\n");
	}
	System.out.print("\n");*/

	// if a matrix is linear, we can do the LUdecomp here instead of
	// needing to do it every frame
	if (!circuitNonLinear) {
	    if (!LUdecomp(circuitMatrix, circuitMatrixSize, circuitPermute)) {
		stop("Singular matrix!", null);
		return;
	    }
	}
    }

    public void stop(String s, CircuitElement ce) {
	stopMessage = s;
	circuitMatrix = null;
	stopElement = ce;
	stoppedCheck.setState(true);
	analyzeFlag = false;
	cv.repaint();
    }
    

    // stamp independent voltage source #vs, from n1 to n2, amount v
    public void stampVoltageSource(int n1, int n2, int vs, double v) {
	int vn = nodeList.size()+vs;
	stampMatrix(vn, n1, -1);
	stampMatrix(vn, n2, 1);
	stampRightSide(vn, v);
	stampMatrix(n1, vn, 1);
	stampMatrix(n2, vn, -1);
    }

    // use this if the amount of voltage is going to be updated in doStep()
    public void stampVoltageSource(int n1, int n2, int vs) {
	int vn = nodeList.size()+vs;
	stampMatrix(vn, n1, -1);
	stampMatrix(vn, n2, 1);
	stampRightSide(vn);
	stampMatrix(n1, vn, 1);
	stampMatrix(n2, vn, -1);
    }
    
    public void updateVoltageSource(int n1, int n2, int vs, double v) {
	int vn = nodeList.size()+vs;
	stampRightSide(vn, v);
    }
    
    public void stampResistor(int n1, int n2, double r) {
	double r0 = 1/r;
	if (Double.isNaN(r0) || Double.isInfinite(r0)) {
	    System.out.print("bad resistance " + r + " " + r0 + "\n");
	    int a = 0;
	    a /= a;
	}
	stampMatrix(n1, n1, r0);
	stampMatrix(n2, n2, r0);
	stampMatrix(n1, n2, -r0);
	stampMatrix(n2, n1, -r0);
    }

    public void stampConductance(int n1, int n2, double r0) {
	stampMatrix(n1, n1, r0);
	stampMatrix(n2, n2, r0);
	stampMatrix(n1, n2, -r0);
	stampMatrix(n2, n1, -r0);
    }

    // current from cn1 to cn2 is equal to voltage from vn1 to 2, divided by g
    public void stampVCCurrentSource(int cn1, int cn2, int vn1, int vn2, double g) {
	stampMatrix(cn1, vn1, g);
	stampMatrix(cn2, vn2, g);
	stampMatrix(cn1, vn2, -g);
	stampMatrix(cn2, vn1, -g);
    }

    public void stampCurrentSource(int n1, int n2, double i) {
	stampRightSide(n1, -i);
	stampRightSide(n2, i);
    }

    // stamp value x in row i, column j, meaning that a voltage change
    // of dv in node j will increase the current into node i by x dv.
    // (Unless i or j is a voltage source node.)
    public void stampMatrix(int i, int j, double x) {
	if (i > 0 && j > 0) {
	    if (circuitNeedsMap) {
		i = circuitRowInfo[i-1].mapRow;
		RowInfo ri = circuitRowInfo[j-1];
		if (ri.type == RowInfo.ROW_CONST) {
		    //System.out.println("Stamping constant " + i + " " + j + " " + x);
		    circuitRightSide[i] -= x*ri.value;
		    return;
		}
		j = ri.mapCol;
		//System.out.println("stamping " + i + " " + j + " " + x);
	    } else {
		i--;
		j--;
	    }
	    circuitMatrix[i][j] += x;
	}
    }

    // stamp value x on the right side of row i, representing an
    // independent current source flowing into node i
    public void stampRightSide(int i, double x) {
	if (i > 0) {
	    if (circuitNeedsMap) {
		i = circuitRowInfo[i-1].mapRow;
		//System.out.println("stamping " + i + " " + x);
	    } else
		i--;
	    circuitRightSide[i] += x;
	}
    }

    // indicate that the value on the right side of row i changes in doStep()
    public void stampRightSide(int i) {
	if (i > 0)
	    circuitRowInfo[i-1].rsChanges = true;
    }
    
    // indicate that the values on the left side of row i change in doStep()
    public void stampNonLinear(int i) {
	if (i > 0)
	    circuitRowInfo[i-1].lsChanges = true;
    }

    public double getIterCount() {
	return (Math.exp((speedBar.getValue()-1)/24.) + .5);
    }
    
    public boolean converged;
    int subIterations;
    public void runCircuit() {
	if (circuitMatrix == null || elmList.size() == 0) {
	    circuitMatrix = null;
	    return;
	}
	int iter;
	//int maxIter = getIterCount();
	boolean debugprint = dumpMatrix;
	dumpMatrix = false;
	long steprate = (long) (160*getIterCount());
	for (iter = 1; ; iter++) {
	    int i, j, k, subiter;
	    for (i = 0; i != elmList.size(); i++) {
		CircuitElement ce = getElement(i);
		ce.startIteration();
	    }
	    steps++;
	    final int subiterCount = 5000;
	    for (subiter = 0; subiter != subiterCount; subiter++) {
		converged = true;
		subIterations = subiter;
		for (i = 0; i != circuitMatrixSize; i++)
		    circuitRightSide[i] = origRightSide[i];
		if (circuitNonLinear) {
		    for (i = 0; i != circuitMatrixSize; i++)
			for (j = 0; j != circuitMatrixSize; j++)
			    circuitMatrix[i][j] = origMatrix[i][j];
		}
		for (i = 0; i != elmList.size(); i++) {
		    CircuitElement ce = getElement(i);
		    ce.doStep();
		}
		if (stopMessage != null)
		    return;
		boolean printit = debugprint;
		debugprint = false;
		for (j = 0; j != circuitMatrixSize; j++) {
		    for (i = 0; i != circuitMatrixSize; i++) {
			double x = circuitMatrix[i][j];
			if (Double.isNaN(x) || Double.isInfinite(x)) {
			    stop("nan/infinite matrix!", null);
			    return;
			}
		    }
		}
		if (printit) {
		    for (j = 0; j != circuitMatrixSize; j++) {
			for (i = 0; i != circuitMatrixSize; i++)
			    System.out.print(circuitMatrix[j][i] + " ");
			System.out.print("  " + circuitRightSide[j] + "\n");
		    }
		    System.out.print("\n");
		}
		if (circuitNonLinear) {
		    if (converged && subiter > 0)
			break;
		    if (!LUdecomp(circuitMatrix, circuitMatrixSize,
				  circuitPermute)) {
			stop("Singular matrix!", null);
			return;
		    }
		}
		LUsubst(circuitMatrix, circuitMatrixSize, circuitPermute,
		       circuitRightSide);
		
		for (j = 0; j != circuitMatrixFullSize; j++) {
		    RowInfo ri = circuitRowInfo[j];
		    double res = 0;
		    if (ri.type == RowInfo.ROW_CONST)
			res = ri.value;
		    else
			res = circuitRightSide[ri.mapCol];
		    /*System.out.println(j + " " + res + " " +
		      ri.type + " " + ri.mapCol);*/
		    if (Double.isNaN(res)) {
			converged = false;
			//debugprint = true;
			break;
		    }
		    if (j < nodeList.size()-1) {
			CircuitNode cn = getCircuitNode(j+1);
			for (k = 0; k != cn.links.size(); k++) {
			    CircuitNodeLink cnl = (CircuitNodeLink)
				cn.links.elementAt(k);
			    cnl.elm.setNodeVoltage(cnl.num, res);
			}
		    } else {
			int ji = j-(nodeList.size()-1);
			voltageSources[ji].setCurrent(ji, res);
		    }
		}
		if (!circuitNonLinear)
		    break;
	    }
	    if (subiter > 5)
		System.out.print("converged after " + subiter + " iterations\n");
	    if (subiter == subiterCount) {
		stop("Convergence failed!", null);
		break;
	    }
	    t += timeStep;
	    for (i = 0; i != scopeCount; i++)
		scopes[i].timeStep();
	    long tm = System.currentTimeMillis();
	    if (iter*1000 >= steprate*(tm-lastFrameTime) ||
		(tm-lastFrameTime > 500))
		break;
	}
    }

    public int abs(int x) {
	return x < 0 ? -x : x;
    }

    public int sign(int x) {
	return (x < 0) ? -1 : (x == 0) ? 0 : 1;
    }

    public int min(int a, int b) { return (a < b) ? a : b; }
    public int max(int a, int b) { return (a > b) ? a : b; }

    public void editFuncPoint(int x, int y) {
	// XXX
	cv.repaint(pause);
    }

    public void componentHidden(ComponentEvent e){}
    public void componentMoved(ComponentEvent e){}
    public void componentShown(ComponentEvent e) {
	cv.repaint();
    }

    public void componentResized(ComponentEvent e) {
	handleResize();
	cv.repaint(100);
    }
    public void actionPerformed(ActionEvent e) {
	String ac = e.getActionCommand();
	if (e.getSource() == resetButton) {
	    int i;
	    
	    // on IE, drawImage() stops working inexplicably every once in
	    // a while.  Recreating it fixes the problem, so we do that here.
	    handleResize();
	    
	    for (i = 0; i != elmList.size(); i++)
		getElement(i).reset();
	    for (i = 0; i != scopeCount; i++)
		scopes[i].resetGraph();
	    analyzeFlag = true;
	    t = 0;
	    stoppedCheck.setState(false);
	    cv.repaint();
	}
	/*if (e.getSource() == dumpMatrixButton)
	  analyzeFlag = dumpMatrix = true;*/
	if (e.getSource() == exportItem)
	    doImport(false);
	if (e.getSource() == importItem)
	    doImport(true);
	if (e.getSource() == exitItem) {
	    applet.destroyFrame();
	    return;
	}
	if (ac.compareTo("stackAll") == 0)
	    stackAll();
	if (ac.compareTo("unstackAll") == 0)
	    unstackAll();
	if (e.getSource() == elmEditMenuItem)
	    doEdit();
	if (e.getSource() == elmDeleteMenuItem && menuElement != null)
	    deleteElement(menuElement);
	if (e.getSource() == elmScopeMenuItem && menuElement != null) {
	    int i;
	    for (i = 0; i != scopeCount; i++)
		if (scopes[i].elm == null)
		    break;
	    if (i == scopeCount) {
		if (scopeCount == scopes.length)
		    return;
		scopeCount++;
		scopes[i] = new Scope();
         scopes[i].setCircuitFrame(this);
        scopes[i].position = i;
		handleResize();
	    }
	    scopes[i].setElement(menuElement);
	}
	if (menuScope != -1) {
	    if (ac.compareTo("remove") == 0)
		scopes[menuScope].setElement(null);
	    if (ac.compareTo("speed2") == 0)
		scopes[menuScope].speedUp();
	    if (ac.compareTo("speed1/2") == 0)
		scopes[menuScope].slowDown();
	    if (ac.compareTo("scale") == 0)
		scopes[menuScope].adjustScale(.5);
	    if (ac.compareTo("stack") == 0)
		stackScope(menuScope);
	    if (ac.compareTo("unstack") == 0)
		unstackScope(menuScope);
	    cv.repaint();
	}
	if (ac.indexOf("setup ") == 0)
	    readSetupFile(ac.substring(6),
			  ((MenuItem) e.getSource()).getLabel());
    }

    public void stackScope(int s) {
	if (s == 0) {
	    if (scopeCount < 2)
		return;
	    s = 1;
	}
	if (scopes[s].position == scopes[s-1].position)
	    return;
	scopes[s].position = scopes[s-1].position;
	for (s++; s < scopeCount; s++)
	    scopes[s].position--;
    }
    
    public void unstackScope(int s) {
	if (s == 0) {
	    if (scopeCount < 2)
		return;
	    s = 1;
	}
	if (scopes[s].position != scopes[s-1].position)
	    return;
	for (; s < scopeCount; s++)
	    scopes[s].position++;
    }

    public void stackAll() {
	int i;
	for (i = 0; i != scopeCount; i++) {
	    scopes[i].position = 0;
	    scopes[i].showMax = false;
	}
    }

    public void unstackAll() {
	int i;
	for (i = 0; i != scopeCount; i++) {
	    scopes[i].position = i;
	    scopes[i].showMax = true;
	}
    }
    
    public void doEdit() {
	if (editDialog != null) {
	    requestFocus();
	    editDialog.setVisible(false);
	    editDialog = null;
	}
	editDialog = new EditDialog(menuElement, this);
	editDialog.show();
    }

    public void doImport(boolean imp) {
	if (impDialog != null) {
	    requestFocus();
	    impDialog.setVisible(false);
	    impDialog = null;
	}
	String dump = "";
	if (!imp) {
	    int i;
	    int f = (dotsCheckItem.getState()) ? 1 : 0;
	    f |= (smallGridCheckItem.getState()) ? 2 : 0;
	    f |= (voltsCheckItem.getState()) ? 0 : 4;
	    f |= (powerCheckItem.getState()) ? 8 : 0;
	    f |= (showValuesCheckItem.getState()) ? 0 : 16;
	    dump = "$ " + f + " " +
		timeStep + " " + getIterCount() + " " +
		currentBar.getValue() + " " + voltageRange + " " +
		powerBar.getValue() + "\n";
	    for (i = 0; i != elmList.size(); i++)
		dump += getElement(i).dump() + "\n";
	    for (i = 0; i != scopeCount; i++) {
		String d = scopes[i].dump();
		if (d != null)
		    dump += d + "\n";
	    }
	    if (hintType != -1)
		dump += "h " + hintType + " " + hintItem1 + " " +
		    hintItem2 + "\n";
	}
	impDialog = new ImportDialog(this, dump);
	impDialog.show();
    }
    
    public void adjustmentValueChanged(AdjustmentEvent e) {
	System.out.print(((Scrollbar) e.getSource()).getValue() + "\n");
    }

    public ByteArrayOutputStream readUrlData(URL url) throws java.io.IOException {
	Object o = url.getContent();
	FilterInputStream fis = (FilterInputStream) o;
	ByteArrayOutputStream ba = new ByteArrayOutputStream(fis.available());
	int blen = 1024;
	byte b[] = new byte[blen];
	while (true) {
	    int len = fis.read(b);
	    if (len <= 0)
		break;
	    ba.write(b, 0, len);
	}
	return ba;
    }
    
    public void getSetupList(Menu menu) {
	Menu stack[] = new Menu[6];
	int stackptr = 0;
	stack[stackptr++] = menu;
	try {
	    URL url = new URL(applet.getCodeBase() + "org/jscience/physics/electricity/circuitr/.circuits/setuplist.txt");
	    ByteArrayOutputStream ba = readUrlData(url);
	    byte b[] = ba.toByteArray();
	    int len = ba.size();
	    int p;
	    for (p = 0; p < len; ) {
		int l;
		for (l = 0; l != len-p; l++)
		    if (b[l+p] == '\n') {
			l++;
			break;
		    }
		String line = new String(b, p, l-1);
		if (line.charAt(0) == '+') {
		    Menu n = new Menu(line.substring(1));
		    menu.add(n);
		    menu = stack[stackptr++] = n;
		} else if (line.charAt(0) == '-') {
		    menu = stack[--stackptr-1];
		} else {
		    int i = line.indexOf(' ');
		    if (i > 0)
			menu.add(getMenuItem(line.substring(i+1),
					     "setup " + line.substring(0, i)));
		}
		p += l;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    stop("Can't read setuplist.txt!", null);
	}
    }

    public void readSetup(String text) {
	readSetup(text.getBytes(), text.length());
	titleLabel.setText("untitled");
    }
    
    public void readSetupFile(String str, String title) {
	t = 0;
	System.out.println(str);
	try {
	    URL url = new URL(applet.getCodeBase() + str);
	    ByteArrayOutputStream ba = readUrlData(url);
	    readSetup(ba.toByteArray(), ba.size());
	} catch (Exception e) {
	    e.printStackTrace();
	    stop("Unable to read " + str + "!", null);
	}
	titleLabel.setText(title);
    }

    public void readSetup(byte b[], int len) {
	elmList.removeAllElements();
	hintType = -1;
	timeStep = 5e-6;
	dotsCheckItem.setState(true);
	smallGridCheckItem.setState(false);
	powerCheckItem.setState(false);
	voltsCheckItem.setState(true);
	showValuesCheckItem.setState(true);
	setGrid();
	speedBar.setValue(57);
	currentBar.setValue(50);
	powerBar.setValue(50);
	voltageRange = 5;
	cv.repaint();
	int p;
	scopeCount = 0;
	for (p = 0; p < len; ) {
	    int l;
	    for (l = 0; l != len-p; l++)
		if (b[l+p] == '\n') {
		    l++;
		    break;
		}
	    String line = new String(b, p, l-1);
	    StringTokenizer st = new StringTokenizer(line);
	    while (st.hasMoreTokens()) {
		String type = st.nextToken();
		int tint = type.charAt(0);
		try {
		    if (tint == 'o') {
			Scope sc = new Scope();
                sc.setCircuitFrame(this);
            sc.position = scopeCount;
			sc.undump(st);
			scopes[scopeCount++] = sc;
			break;
		    }
		    if (tint == 'h') {
			readHint(st);
			break;
		    }
		    if (tint == '$') {
			readOptions(st);
			break;
		    }
		    if (tint >= '0' && tint <= '9')
			tint = new Integer(type).intValue();
		    int x1 = new Integer(st.nextToken()).intValue();
		    int y1 = new Integer(st.nextToken()).intValue();
		    int x2 = new Integer(st.nextToken()).intValue();
		    int y2 = new Integer(st.nextToken()).intValue();
		    int f  = new Integer(st.nextToken()).intValue();
		    CircuitElement ce = null;
		    Class cls = dumpTypes[tint];
		    if (cls == null) {
			System.out.println("unrecognized dump type: " + type);
			break;
		    }
		    // find element class
		    Class carr[] = new Class[7];
		    carr[0] = getClass();
		    carr[1] = carr[2] = carr[3] = carr[4] = carr[5] =
			int.class;
		    carr[6] = StringTokenizer.class;
		    Constructor cstr = null;
		    cstr = cls.getConstructor(carr);
		
		    // invoke constructor with starting coordinates
		    Object oarr[] = new Object[7];
		    oarr[0] = this;
		    oarr[1] = new Integer(x1);
		    oarr[2] = new Integer(y1);
		    oarr[3] = new Integer(x2);
		    oarr[4] = new Integer(y2);
		    oarr[5] = new Integer(f );
		    oarr[6] = st;
		    ce = (CircuitElement) cstr.newInstance(oarr);
		    ce.setPoints();
		    elmList.addElement(ce);
		} catch (java.lang.reflect.InvocationTargetException ee) {
		    ee.getTargetException().printStackTrace();
		    break;
		} catch (Exception ee) {
		    ee.printStackTrace();
		    break;
		}
		break;
	    }
	    p += l;
	}
	handleResize(); // for scopes
	analyzeCircuit();
    }

    public void readHint(StringTokenizer st) {
	hintType  = new Integer(st.nextToken()).intValue();
	hintItem1 = new Integer(st.nextToken()).intValue();
	hintItem2 = new Integer(st.nextToken()).intValue();
    }

    public void readOptions(StringTokenizer st) {
	int flags = new Integer(st.nextToken()).intValue();
	dotsCheckItem.setState((flags & 1) != 0);
	smallGridCheckItem.setState((flags & 2) != 0);
	voltsCheckItem.setState((flags & 4) == 0);
	powerCheckItem.setState((flags & 8) == 8);
	showValuesCheckItem.setState((flags & 16) == 0);
	timeStep = new Double (st.nextToken()).doubleValue();
	double sp = new Double(st.nextToken()).doubleValue();
	int sp2 = (int) (Math.log(sp)*24+1.5);
	speedBar  .setValue(sp2);
	currentBar.setValue(new Integer(st.nextToken()).intValue());
	voltageRange = new Double (st.nextToken()).doubleValue();
	try {
	    powerBar.setValue(new Integer(st.nextToken()).intValue());
	} catch (Exception e) {
	}
	setGrid();
    }
    
    public int snapGrid(int x) {
	return (x+gridRound) & gridMask;
    }

    public boolean doSwitch(int x, int y) {
	if (mouseElement == null || !(mouseElement instanceof SwitchElement))
	    return false;
	SwitchElement se = (SwitchElement) mouseElement;
	se.toggle();
	if (se.momentary)
	    heldSwitchElement = se;
	analyzeCircuit();
	return true;
    }

    public void deleteElement(CircuitElement elm) {
	int e = locateElement(elm);
	if (e >= 0) {
	    elmList.removeElementAt(e);
	    analyzeCircuit();
	}
    }

    public int locateElement(CircuitElement elm) {
	int i;
	for (i = 0; i != elmList.size(); i++)
	    if (elm == elmList.elementAt(i))
		return i;
	return -1;
    }
    
    public void mouseDragged(MouseEvent e) {
	if (!circuitArea.contains(e.getX(), e.getY()))
	    return;
	if (dragElement != null)
	    dragElement.drag(e.getX(), e.getY());
	switch (mouseMode) {
	case MODE_DRAG_ALL:
	    dragAll(snapGrid(e.getX()), snapGrid(e.getY()));
	    break;
	case MODE_DRAG_ROW:
	    dragRow(snapGrid(e.getX()), snapGrid(e.getY()));
	    break;
	case MODE_DRAG_COLUMN:
	    dragColumn(snapGrid(e.getX()), snapGrid(e.getY()));
	    break;
	case MODE_DRAG_POST:
	    if (mouseElement != null)
		dragPost(snapGrid(e.getX()), snapGrid(e.getY()));
	    break;
	case MODE_DRAG_SELECTED:
	    if (mouseElement != null)
		dragItem(e.getX(), e.getY());
	    break;
	}
	dragging = true;
	if (mouseMode == MODE_DRAG_SELECTED && mouseElement instanceof TextElement) {
	    dragX = e.getX(); dragY = e.getY();
	} else {
	    dragX = snapGrid(e.getX()); dragY = snapGrid(e.getY());
	}
	cv.repaint(pause);
    }

    public void dragAll(int x, int y) {
	int dx = x-dragX;
	int dy = y-dragY;
	if (dx == 0 && dy == 0)
	    return;
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    ce.move(dx, dy);
	}
	removeZeroLengthElements();
    }

    public void dragRow(int x, int y) {
	int dy = y-dragY;
	if (dy == 0)
	    return;
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    if (ce.y  == dragY)
		ce.movePoint(0, 0, dy);
	    if (ce.y2 == dragY)
		ce.movePoint(1, 0, dy);
	}
	removeZeroLengthElements();
    }
    
    public void dragColumn(int x, int y) {
	int dx = x-dragX;
	if (dx == 0)
	    return;
	int i;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    if (ce.x  == dragX)
		ce.movePoint(0, dx, 0);
	    if (ce.x2 == dragX)
		ce.movePoint(1, dx, 0);
	}
	removeZeroLengthElements();
    }

    public void dragItem(int x, int y) {
	if (!(mouseElement instanceof TextElement)) {
	    x = snapGrid(x);
	    y = snapGrid(y);
	}
	int dx = x-dragX;
	int dy = y-dragY;
	if (dx == 0 && dy == 0)
	    return;
	mouseElement.movePoint(0, dx, dy);
	mouseElement.movePoint(1, dx, dy);
	analyzeCircuit();
    }

    public void dragPost(int x, int y) {
	if (draggingPost == -1) {
	    draggingPost =
		(distanceSq(mouseElement.x , mouseElement.y , x, y) >
		 distanceSq(mouseElement.x2, mouseElement.y2, x, y)) ? 1 : 0;
	}
	int dx = x-dragX;
	int dy = y-dragY;
	if (dx == 0 && dy == 0)
	    return;
	mouseElement.movePoint(draggingPost, dx, dy);
	analyzeCircuit();
    }

    public void removeZeroLengthElements() {
	int i;
	boolean changed = false;
	for (i = elmList.size()-1; i >= 0; i--) {
	    CircuitElement ce = getElement(i);
	    if (ce.x == ce.x2 && ce.y == ce.y2) {
		elmList.removeElementAt(i);
		changed = true;
	    }
	}
	analyzeCircuit();
    }
    
    public void mouseMoved(MouseEvent e) {
	if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0)
	    return;
	int x = e.getX();
	int y = e.getY();
	dragX = snapGrid(x); dragY = snapGrid(y);
	draggingPost = -1;
	int i;
	CircuitElement origMouse = mouseElement;
	mouseElement = null;
	int bestDist = 100000;
	int bestArea = 100000;
	for (i = 0; i != elmList.size(); i++) {
	    CircuitElement ce = getElement(i);
	    if (ce.boundingBox.contains(x, y)) {
		int j;
		int area = ce.boundingBox.width * ce.boundingBox.height;
		int jn = max(ce.getPostCount(), 2);
		for (j = 0; j != jn; j++) {
		    Point pt = ce.getPost(j);
		    int dist = distanceSq(x, y, pt.x, pt.y);

		    // if multiple elements have overlapping bounding boxes,
		    // we prefer selecting elements that have posts close
		    // to the mouse pointer and that have a small bounding
		    // box area.
		    if (dist <= bestDist && area <= bestArea) {
			bestDist = dist;
			bestArea = area;
			mouseElement = ce;
		    }
		}
		if (ce.getPostCount() == 0)
		    mouseElement = ce;
	    }
	}
	scopeSelected = -1;
	if (mouseElement == null) {
	    for (i = 0; i != scopeCount; i++) {
		Scope s = scopes[i];
		if (s.rect.contains(x, y)) {
		    mouseElement = s.elm;
		    scopeSelected = i;
		}
	    }
	}
	if (mouseElement != origMouse)
	    cv.repaint();
    }

    public int distanceSq(int x1, int y1, int x2, int y2) {
	x2 -= x1;
	y2 -= y1;
	return x2*x2+y2*y2;
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {
	if (e.isPopupTrigger()) {
	    doPopupMenu(e);
	    return;
	}
	if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0)
	    return;
	if (doSwitch(e.getX(), e.getY()))
	    return;
	dragging = true;
	if (mouseMode != MODE_ADD_ELM || addingClass == null)
	    return;
	
	int x0 = snapGrid(e.getX());
	int y0 = snapGrid(e.getY());
	if (!circuitArea.contains(x0, y0))
	    return;

	dragElement = constructElement(addingClass, x0, y0);
    }

    public CircuitElement constructElement(Class c, int x0, int y0) {
	// find element class
	Class carr[] = new Class[3];
	carr[0] = getClass();
	carr[1] = carr[2] = int.class;
	Constructor cstr = null;
	try {
	    cstr = c.getConstructor(carr);
	} catch (Exception ee) {
	    ee.printStackTrace();
	    return null;
	}

	// invoke constructor with starting coordinates
	Object oarr[] = new Object[3];
	oarr[0] = this;
	oarr[1] = new Integer(x0);
	oarr[2] = new Integer(y0);
	try {
	    return (CircuitElement) cstr.newInstance(oarr);
	} catch (Exception ee) { ee.printStackTrace(); }
	return null;
    }
    
    public void doPopupMenu(MouseEvent e) {
	menuElement = mouseElement;
	if (scopeSelected != -1) {
	    PopupMenu m = scopes[scopeSelected].getMenu();
	    menuScope = scopeSelected;
	    if (m != null)
		m.show(e.getComponent(), e.getX(), e.getY());
	} else if (mouseElement != null) {
	    elmEditMenuItem .setEnabled(mouseElement.getEditInfo(0) != null);
	    elmScopeMenuItem.setEnabled(mouseElement.canViewInScope());
	    elmMenu.show(e.getComponent(), e.getX(), e.getY());
	} else {
	    doMainMenuChecks(mainMenu);
	    mainMenu.show(e.getComponent(), e.getX(), e.getY());
	}
    }

    public void doMainMenuChecks(Menu m) {
	int i;
	if (m == optionsMenu)
	    return;
	for (i = 0; i != m.getItemCount(); i++) {
	    MenuItem mc = m.getItem(i);
	    if (mc instanceof Menu)
		doMainMenuChecks((Menu) mc);
	    if (mc instanceof CheckboxMenuItem) {
		CheckboxMenuItem cmi = (CheckboxMenuItem) mc;
		cmi.setState(
		      mouseModeStr.compareTo(cmi.getActionCommand()) == 0);
	    }
	}
    }
    
    public void mouseReleased(MouseEvent e) {
	if (e.isPopupTrigger()) {
	    doPopupMenu(e);
	    return;
	}
	if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) == 0)
	    return;
	dragging = false;
	boolean circuitChanged = false;
	if (heldSwitchElement != null) {
	    heldSwitchElement.mouseUp();
	    heldSwitchElement = null;
	    circuitChanged = true;
	}
	if (dragElement != null &&
	    !(dragElement.x == dragElement.x2 && dragElement.y == dragElement.y2)) {
	    elmList.addElement(dragElement);
	    circuitChanged = true;
	}
	if (circuitChanged)
	    analyzeCircuit();
	dragElement = null;
	cv.repaint();
    }

    public void enableItems() {
	if (powerCheckItem.getState()) {
	    powerBar.enable();
	    powerLabel.enable();
	} else {
	    powerBar.disable();
	    powerLabel.disable();
	}
    }
    
    public void itemStateChanged(ItemEvent e) {
	cv.repaint(pause);
	Object mi = e.getItemSelectable();
	if (mi == stoppedCheck)
	    return;
	if (mi == smallGridCheckItem)
	    setGrid();
	if (mi == powerCheckItem) {
	    if (powerCheckItem.getState())
		voltsCheckItem.setState(false);
	    else
		voltsCheckItem.setState(true);
	}
	if (mi == voltsCheckItem && voltsCheckItem.getState())
	    powerCheckItem.setState(false);
	enableItems();
	if (menuScope != -1) {
	    Scope sc = scopes[menuScope];
	    if (mi == scopeVMenuItem)
		sc.showVoltage(scopeVMenuItem.getState());
	    if (mi == scopeIMenuItem)
		sc.showCurrent(scopeIMenuItem.getState());
	    if (mi == scopeMaxMenuItem)
		sc.showMax(scopeMaxMenuItem.getState());
	    if (mi == scopeFreqMenuItem)
		sc.showFreq(scopeFreqMenuItem.getState());
	    if (mi == scopePowerMenuItem)
		sc.setValue(Scope.SCOPEVAL_POWER);
	    if (mi == scopeIbMenuItem)
		sc.setValue(Scope.SCOPEVAL_IB);
	    if (mi == scopeIcMenuItem)
		sc.setValue(Scope.SCOPEVAL_IC);
	    if (mi == scopeIeMenuItem)
		sc.setValue(Scope.SCOPEVAL_IE);
	    if (mi == scopeVbeMenuItem)
		sc.setValue(Scope.SCOPEVAL_VBE);
	    if (mi == scopeVbcMenuItem)
		sc.setValue(Scope.SCOPEVAL_VBC);
	    if (mi == scopeVceMenuItem)
		sc.setValue(Scope.SCOPEVAL_VCE);
	}
	if (mi instanceof CheckboxMenuItem) {
	    MenuItem mmi = (MenuItem) mi;
	    mouseMode = MODE_ADD_ELM;
	    String s = mmi.getActionCommand();
	    if (s.length() > 0)
		mouseModeStr = s;
	    if (s.compareTo("DragAll") == 0)
		mouseMode = MODE_DRAG_ALL;
	    else if (s.compareTo("DragRow") == 0)
		mouseMode = MODE_DRAG_ROW;
	    else if (s.compareTo("DragColumn") == 0)
		mouseMode = MODE_DRAG_COLUMN;
	    else if (s.compareTo("DragSelected") == 0)
		mouseMode = MODE_DRAG_SELECTED;
	    else if (s.compareTo("DragPost") == 0)
		mouseMode = MODE_DRAG_POST;
	    else if (s.length() > 0) {
		try {
		    addingClass = Class.forName("CircuitFrame$" + s);
		} catch (Exception ee) {
		    ee.printStackTrace();
		}
	    }
	}
    }

    public void setGrid() {
	gridSize = (smallGridCheckItem.getState()) ? 8 : 16;
	gridMask = ~(gridSize-1);
	gridRound = gridSize/2-1;
    }
    
    public boolean comparePair(int x1, int x2, int y1, int y2) {
	return ((x1 == y1 && x2 == y2) || (x1 == y2 && x2 == y1));
    }
    
    public String getVoltageDText(double v) {
	return getUnitText(Math.abs(v), "V");
    }
    public String getVoltageText(double v) {
	return getUnitText(v, "V");
    }
    public String getUnitText(double v, String u) {
	double va = Math.abs(v);
	if (va < 1e-12)
	    return "0 " + u;
	if (va < 1e-9)
	    return showFormat.format(v*1e12) + " p" + u;
	if (va < 1e-6)
	    return showFormat.format(v*1e9) + " n" + u;
	if (va < 1e-3)
	    return showFormat.format(v*1e6) + " " + muString + u;
	if (va < 1)
	    return showFormat.format(v*1e3) + " m" + u;
	if (va < 1e3)
	    return showFormat.format(v) + " " + u;
	if (va < 1e6)
	    return showFormat.format(v*1e-3) + " k" + u;
	return showFormat.format(v*1e-6) + " M" + u;
    }
    public String getShortUnitText(double v, String u) {
	double va = Math.abs(v);
	if (va < 1e-12)
	    return null;
	if (va < 1e-9)
	    return shortFormat.format(v*1e12) + "p" + u;
	if (va < 1e-6)
	    return shortFormat.format(v*1e9) + "n" + u;
	if (va < 1e-3)
	    return shortFormat.format(v*1e6) + muString + u;
	if (va < 1)
	    return shortFormat.format(v*1e3) + "m" + u;
	if (va < 1e3)
	    return shortFormat.format(v) + u;
	if (va < 1e6)
	    return shortFormat.format(v*1e-3) + "k" + u;
	return shortFormat.format(v*1e-6) + "M" + u;
    }
    public String getCurrentText(double i) {
	return getUnitText(i, "A");
    }
    public String getCurrentDText(double i) {
	return getUnitText(Math.abs(i), "A");
    }
    
    public void drawThickLine(Graphics g, int x, int y, int x2, int y2) {
	g.drawLine(x, y, x2, y2);
	g.drawLine(x+1, y, x2+1, y2);
	g.drawLine(x, y+1, x2, y2+1);
	g.drawLine(x+1, y+1, x2+1, y2+1);
    }

    public void drawThickPolygon(Graphics g, int xs[], int ys[], int c) {
	int i;
	for (i = 0; i != c-1; i++)
	    drawThickLine(g, xs[i], ys[i], xs[i+1], ys[i+1]);
	drawThickLine(g, xs[i], ys[i], xs[0], ys[0]);
    }
    
    /*void drawDottedLine(Graphics g, int x, int y, int x2, int y2) {
	int dx = x2-x;
	int dy = y2-y;
	double dr = Math.sqrt(dx*dx+dy*dy);
	int i;
	int st = 16;
	for (i = 0; i < dr; i += st) {
	    int x1a = x + (int) (i*dx/dr);
	    int y1a = y + (int) (i*dy/dr);
	    int x2a = x + (int) ((i+4)*dx/dr);
	    int y2a = y + (int) ((i+4)*dy/dr);
	    drawThickLine(g, x1a, y1a, x2a, y2a);
	    }
	    }*/

    public void drawThickCircle(Graphics g, int cx, int cy, int ri) {
	int a;
	double m = pi/180;
	double r = ri*.98;
	for (a = 0; a != 360; a += 20) {
	    double ax = Math.cos(a*m)*r + cx;
	    double ay = Math.sin(a*m)*r + cy;
	    double bx = Math.cos((a+20)*m)*r + cx;
	    double by = Math.sin((a+20)*m)*r + cy;
	    drawThickLine(g, (int) ax, (int) ay, (int) bx, (int) by);
	}
    }
    
    //the following code from numerical recipes in C is not under GPL so we use our own counterpart
    //(our code may give slightly different results though)
    //please only use the commented code to analyse different results, not as a computation algorithm

    boolean LUdecomp(double a[][], int n, int indx[]) {
        try {
          DoubleSquareMatrix doubleSquareMatrix = new DoubleSquareMatrix(a);
          a = doubleSquareMatrix.luDecompose(indx)[1].toPrimitiveArray();
          return true;
        } catch (Exception e) {
          return false;
        }
    }

    void LUsubst(double a[][], int n, int indx[], double b[]) {
       b = LinearMathUtils.solve(new DoubleSquareMatrix(a), new DoubleVector(b)).toPrimitiveArray();
    }

    // Given a matrix a[0..n-1][0..n-1] this routine replaces it by the LU
    // decomposition of rowwise permutation of itself. a and n are
    // input. The output matrix a is rearranged, indx[0..n-1] records
    // the permutation, it will be used in the LUsubst() routine.
    // This routine is used in combination with LUsubst()
    // to solve linear equations or invert matrices.
   /** boolean LUdecomp(double a[][], int n, int indx[]) {
 	int i,imax = -1,j,k;
	double big,dum,sum,temp;
	double vv[];  /* stores the scaling of each row */

	/** vv=new double[n];
        /* get the scaling factors */
	/** for (i=0;i<n;i++) {
	    big=0.0;
	    for (j=0;j<n;j++)
		if ((temp=Math.abs(a[i][j])) > big) big=temp;
	    if (big == 0.0)
		return false;
	    vv[i]=1.0/big;
	}
        /* Crout's method */
	/** for (j=0;j<n;j++) {
	    for (i=0;i<j;i++) {
		sum=a[i][j];
		for (k=0;k<i;k++) sum -= a[i][k]*a[k][j];
		a[i][j]=sum;
	    }
	    big=0.0;
	    for (i=j;i<n;i++) {
		sum=a[i][j];
		for (k=0;k<j;k++)
		    sum -= a[i][k]*a[k][j];
		a[i][j]=sum;
		if ( (dum=vv[i]*Math.abs(sum)) >= big) {
		    big=dum;
		    imax=i;
		}
	    }
	    /* interchange rows if neccessary */
	 /**    if (j != imax) {
		for (k=0;k<n;k++) {
		    dum=a[imax][k];
		    a[imax][k]=a[j][k];
		    a[j][k]=dum;
		}
		vv[imax]=vv[j];
	    }
	    indx[j]=imax;
	    if (a[j][j] == 0.0) {
		//System.out.println("used tiny " + j);
		a[j][j]=1e-20;
	    }
	    if (j != n-1) {
		dum=1.0/(a[j][j]);
		for (i=j+1;i<n;i++) a[i][j] *= dum;
	    }
	}
	return true;
    }

    // Solves the set of n linear equations. a is the LU decomposition
    // determined by LUdecomp(). indx is the permutation vector also
    // from LUdecomp(). b[0..n-1] is the right hand side of the equations
    // as an input and returns with the solution of the equations. a,
    // n and indx is not modifed by this routine and can be left in
    // space for succesive calls with different right hand side
    // vectors.
    void LUsubst(double a[][], int n, int indx[], double b[]) {
	int i,ii=-1,ip,j;
	double sum;

	for (i=0;i<n;i++) {
	    ip=indx[i];
	    sum=b[ip];
	    b[ip]=b[i];
	    if (ii != -1)
		for (j=ii;j<=i-1;j++) sum -= a[i][j]*b[j];
	    else if (sum != 0) ii=i;
	    b[i]=sum;
	}
	for (i=n-1;i>=0;i--) {
	    sum=b[i];
	    for (j=i+1;j<n;j++) sum -= a[i][j]*b[j];
	    b[i]=sum/a[i][i];
	}
    }
    */
}
