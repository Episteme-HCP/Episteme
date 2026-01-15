package org.jscience.architecture.lift;

import org.apache.xerces.parsers.DOMParser;

import org.jscience.architecture.lift.ca.*;
import org.jscience.architecture.lift.gui.Nice_GUI;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FilenameFilter;

import java.util.ArrayList;

import javax.swing.*;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is the main file of JLESA. It creates the {@link World} and the
 * necessary {@link Car}s, {@link CA}s, {@link PassengerGenerator}s, {@link
 * PassengerProcessor}s, GUIs, etc.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:51 $
 */
public class EntryPoint {
    /**
     * DOCUMENT ME!
     */
    private static Timer AnimatedT = null;

    /**
     * DOCUMENT ME!
     */
    private static int AnimationSpeed = 0;

    /**
     * DOCUMENT ME!
     */
    private static Timer ButtonT = null;

    /**
     * DOCUMENT ME!
     */
    private static int CACounter = 0;

    /**
     * DOCUMENT ME!
     */
    private static int MaximumNumbeOfTicks = 0;

    /**
     * DOCUMENT ME!
     */
    private static Nice_GUI NG;

    /**
     * DOCUMENT ME!
     */
    private static int TickingBlocks = 0;

    /**
     * DOCUMENT ME!
     */
    private static World W1;

    /**
     * DOCUMENT ME!
     */
    private static boolean ForceContinue = false;

    /**
     * {@code Args[0]} and {@code Args[1]} are parsed. They must
     * contain either {@code -f} or an XML config file name.
     *
     * @param Args DOCUMENT ME!
     */
    public static void main(String[] Args) {
        String ConfigFile = null;
        ForceContinue = false;

        for (int i = 0; i < Args.length; i++) {
            String Arg = ((Args[i] == null) ? null : Args[i].toLowerCase().trim());

            if ("-f".equals(Args[i])) {
                ForceContinue = true;
            } else {
                ConfigFile = "data/jlesa/config/" + Args[i];
            }
        }

        if (ConfigFile == null) {
            File ConfigDir = new File("data/jlesa/config/");
            String[] ConfigFiles = ConfigDir.list(new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            return ((name != null) &&
                            (name.trim().toLowerCase().endsWith(".xml")));
                        }
                    });

            if ((ConfigFiles == null) || (ConfigFiles.length == 0)) {
                JOptionPane.showMessageDialog(null, "NO CONFIG FILES FOUND",
                    "NO CONFIG FILES FOUND", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }

            ConfigFile = (String) JOptionPane.showInputDialog(null,
                    "Please select configuration file",
                    "Configuration selection", JOptionPane.QUESTION_MESSAGE,
                    null, ConfigFiles, ConfigFiles[0]);

            if (ConfigFile == null) {
                JOptionPane.showMessageDialog(null, "NO CONFIG FILES FOUND",
                    "NO CONFIG FILES FOUND", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            } else {
                ConfigFile = "data/jlesa/config/" + ConfigFile;
            }
        }

        DOMParser myDOMParser = new DOMParser();

        try {
            myDOMParser.setFeature("http://xml.org/sax/features/validation",
                true);
        } catch (SAXException SE) {
            System.err.println(SE.toString());
        }

        try {
            myDOMParser.parse(ConfigFile);
        } catch (Exception E) {
            System.err.println(E.toString());
        }

        Document doc = myDOMParser.getDocument();
        System.out.println("Parsing configuration file \"" + ConfigFile + "\"");
        traverseParse(doc);
        System.out.println("Parsing finished.");

        W1.MaximumNumberOfTicks = MaximumNumbeOfTicks;

        /*
                double[] Gammas = new double[World.getNoF()];
                for (int i = 0; i < Gammas.length; i++) {
                    Gammas[i] = 0.00;
                }
                PoissonPassengerGenerator PPG = new PoissonPassengerGenerator(Gammas);
                PoissonPassengerGenerator PPG2 = new PoissonPassengerGenerator(Gammas);
                PoissonPassengerGenerator PPG3 = new PoissonPassengerGenerator(Gammas);
                PPG.setGamma((World.getNoF() / 3), 0.03);
                PPG2.setGamma(((2 * World.getNoF()) / 3), 0.03);
        */
        if (NG != null) {
            NG.setup();
            NG.render();
            NG.setVisible(true);
            NG.TickPanel.TickNumber.setText("" + TickingBlocks);

            int AS = AnimationSpeed;
            NG.TickPanel.Animated.setSelected(0 != AS);

            if (AS != 0) {
                NG.TickPanel.TickTimeSelector.setSelectedIndex(AS - 1);
            }

            AnimatedT = new Timer(NG.getTickValue(),
                    new ActionListener() {
                        public void actionPerformed(ActionEvent AE) {
                            AnimatedT.setDelay(NG.getTickValue());

                            if (NG.getA()) {
                                makeTicks(NG, W1);
                            }
                        }
                    });
            ButtonT = new Timer(50,
                    new ActionListener() {
                        public void actionPerformed(ActionEvent AE) {
                            if ((!NG.getA()) && NG.getTicked()) {
                                makeTicks(NG, W1);
                            }
                        }
                    });
            AnimatedT.start();
            ButtonT.start();
        } else {
            while ((W1.MaximumNumberOfTicks != 0) &&
                    (W1.getTotalTicks() <= W1.MaximumNumberOfTicks)) {
                W1.Tick(1);

                if ((W1.getTotalTicks() % TickingBlocks) == 0) {
                    System.out.println("Ticks: " + W1.getTotalTicks());
                }
            }

            if (!W1.Tick(1)) {
                System.exit(0);
            }
        }
    }

    /**
     * Called from different Tick sources.
     *
     * @param NG DOCUMENT ME!
     * @param W1 DOCUMENT ME!
     */
    private static void makeTicks(Nice_GUI NG, World W1) {
        String TT = NG.TickPanel.TickNumber.getText().trim();

        try {
            int TN = (TT.equals("") ? 1 : Integer.parseInt(TT));

            if (TN > 0) {
                if (!W1.Tick(TN)) {
                    NG.TickPanel.Animated.setSelected(false);
                    NG.TickPanel.TickButton.setEnabled(false);
                }

                NG.render();
            }
        } catch (NumberFormatException E) {
            System.err.println("Invalid ticknumber!");
        }
    }

    /**
     * Travels through space and time... No, wait, it travels through
     * the config file.
     *
     * @param CurrentNode DOCUMENT ME!
     */
    private static void traverseParse(Node CurrentNode) {
        NodeList Childs = CurrentNode.getChildNodes();
        boolean ParseChilds = true;

        if (CurrentNode.getNodeType() == Node.ELEMENT_NODE) {
            System.out.println("Parsing " + CurrentNode.getNodeName());

            NamedNodeMap Attributes = CurrentNode.getAttributes();

            if ("JLESAcfg".equals(CurrentNode.getNodeName())) {
                System.out.println("Config file \"" +
                    Attributes.getNamedItem("name").getNodeValue() +
                    "\", created at " +
                    Attributes.getNamedItem("created").getNodeValue() + " by " +
                    Attributes.getNamedItem("author").getNodeValue() + ".");
            } else if ("Building".equals(CurrentNode.getNodeName())) {
                int MaxFloor = Integer.parseInt(Attributes.getNamedItem(
                            "HighestFloor").getNodeValue());
                int MinFloor = Integer.parseInt(Attributes.getNamedItem(
                            "LowestFloor").getNodeValue());
                MaximumNumbeOfTicks = Integer.parseInt(Attributes.getNamedItem(
                            "MaxNumberOfTicks").getNodeValue());
                TickingBlocks = Integer.parseInt(Attributes.getNamedItem(
                            "TickBlocks").getNodeValue());
                System.out.println("Creating World with floors [" + MinFloor +
                    "," + MaxFloor + "]");
                W1 = new World(MinFloor, MaxFloor);
            } else if ("GUI".equals(CurrentNode.getNodeName())) {
                boolean VisibleGUI = Attributes.getNamedItem("Visible")
                                               .getNodeValue().toLowerCase()
                                               .matches("true|t|yes|y|1");
                AnimationSpeed = Integer.parseInt(Attributes.getNamedItem(
                            "AnimationSpeed").getNodeValue());
                NG = VisibleGUI ? (new Nice_GUI()) : null;
            } else if ("CA".equals(CurrentNode.getNodeName())) {
                if (CACounter == 0) {
                    CACounter++;
                } else {
                    System.err.print(
                        "ERROR: Sorry, multi-CA buildings are not yet implemented.");

                    if (!ForceContinue) {
                        System.err.print(
                            "Halting. Use \"-f\" to force simulation anyway!");
                        System.exit(-1);
                    }

                    System.err.println();
                }
            } else if ("Car".equals(CurrentNode.getNodeName())) {
                ParseChilds = false;

                Car C = parseCar(CurrentNode);

                if (C != null) {
                    W1.add(C);
                } else {
                    System.err.print(
                        "ERROR: LoadDependentKinematicModel is not fully configured.");

                    if (!ForceContinue) {
                        System.err.print(
                            "Halting. Use \"-f\" to force simulation anyway!");
                        System.exit(-1);
                    }

                    System.err.println();
                }
            } else if ("DynZoneCA".equals(CurrentNode.getNodeName())) {
                boolean VisibleGUI = Attributes.getNamedItem("VisibleGUI")
                                               .getNodeValue().toLowerCase()
                                               .matches("true|t|yes|y|1");
                W1.setCA(new DynZoneCA(VisibleGUI));
            } else if ("TrivialCA".equals(CurrentNode.getNodeName())) {
                W1.setCA(new TrivialCA());
            } else if ("ManualCA".equals(CurrentNode.getNodeName())) {
                try {
                    boolean VisibleGUI = Attributes.getNamedItem("VisibleGUI")
                                                   .getNodeValue().toLowerCase()
                                                   .matches("true|t|yes|y|1");
                    String GUILocation = Attributes.getNamedItem("VisibleGUI")
                                                   .getNodeValue();
                    String[] Splitted = GUILocation.split(",");
                    int X = Integer.parseInt(Splitted[0]);
                    int Y = Integer.parseInt(Splitted[1]);
                    W1.setCA(new ManualCA(X, Y, VisibleGUI));
                } catch (Exception E) {
                    System.err.print(
                        "ERROR: Illegal parameter found while parsing ManualCA! " +
                        E.toString());

                    if (!ForceContinue) {
                        System.err.print(
                            "Halting. Use \"-f\" to force simulation anyway!");
                        System.exit(-1);
                    }

                    System.err.println();
                }
            } else if ("SimplexCA".equals(CurrentNode.getNodeName())) {
                W1.setCA(new SimpleCA());
            } else if ("OtherCA".equals(CurrentNode.getNodeName())) {
                System.err.println(
                    "ERROR: Sorry, this type of CA is not yet implemented!");
            } else if ("FileBasedPassengerGenerator".equals(
                        CurrentNode.getNodeName())) {
                String InputFile = Attributes.getNamedItem("InputFile")
                                             .getNodeValue();

                try {
                    if (goodFileName(InputFile, "data/jlesa/")) {
                        W1.add(new FileBasedPassengerGenerator(InputFile));
                    } else {
                        System.out.print(
                            "ERROR! Illegal filename found, skipping...");

                        if (!ForceContinue) {
                            System.err.print(
                                "Halting. Use \"-f\" to force simulation anyway!");
                            System.exit(-1);
                        }
                    }
                } catch (Exception E) {
                    System.out.print("ERROR! " + E + ", skipping...");
                }
            } else if ("PoissonPassengerGenerator".equals(
                        CurrentNode.getNodeName())) {
                ParseChilds = false;

                String RandomSeedS = Attributes.getNamedItem("RandomSeed")
                                               .getNodeValue().toLowerCase()
                                               .trim();
                PoissonPassengerGenerator PPG = null;

                if ("random".equals(RandomSeedS)) {
                    PPG = new PoissonPassengerGenerator();
                } else {
                    long RandomSeedL = Long.parseLong(RandomSeedS);
                    PPG = new PoissonPassengerGenerator(RandomSeedL);
                }

                for (int i = 0; i < Childs.getLength(); i++) {
                    Node NextNode = Childs.item(i);

                    if (NextNode.getNodeType() == Node.ELEMENT_NODE) {
                        NamedNodeMap NextAttributes = NextNode.getAttributes();

                        try {
                            int Floor = Integer.parseInt(NextAttributes.getNamedItem(
                                        "Floor").getNodeValue());
                            String GammaS = NextAttributes.getNamedItem("Gamma")
                                                          .getNodeValue();
                            double Gamma = Double.parseDouble(GammaS.replaceAll(
                                        "[,]", "."));
                            PPG.setGamma(World.toAbsFloor(Floor), Gamma);
                        } catch (Exception E) {
                            System.err.println(E);
                        }
                    }
                }

                if (PPG == null) {
                    System.out.print("ERROR! Cannot parse random seed...");

                    if (!ForceContinue) {
                        System.err.print(
                            "Halting. Use \"-f\" to force simulation anyway!");
                        System.exit(-1);
                    }
                } else {
                    W1.add(PPG);
                }
            } else if ("SimplePassengerProcessor".equals(
                        CurrentNode.getNodeName())) {
                String LogFile = Attributes.getNamedItem("OutputFile")
                                           .getNodeValue();

                if (goodFileName(LogFile, "data/jlesa/")) {
                    W1.add(new SimplePassengerProcessor(LogFile));
                } else {
                    System.out.print(
                        "ERROR! Illegal filename found, skipping...");

                    if (!ForceContinue) {
                        System.err.print(
                            "Halting. Use \"-f\" to force simulation anyway!");
                        System.exit(-1);
                    }
                }
            } else if ("LoggerPassengerProcessor".equals(
                        CurrentNode.getNodeName())) {
                String GenPasFile = Attributes.getNamedItem(
                        "GeneratedPassengersOutputFile").getNodeValue();
                String PasTimeFile = Attributes.getNamedItem(
                        "PassengerTimesOutputFile").getNodeValue();
                String QueuFile = Attributes.getNamedItem(
                        "QueuLengthsOutputFile").getNodeValue();

                if ((goodFileName(GenPasFile, "data/jlesa/")) &&
                        (goodFileName(PasTimeFile, "data/jlesa/")) &&
                        (goodFileName(QueuFile, "data/jlesa/"))) {
                    W1.add(new LoggerPassengerProcessor(GenPasFile,
                            PasTimeFile, QueuFile));
                } else {
                    System.out.print(
                        "ERROR! Illegal filename found, skipping...");

                    if (!ForceContinue) {
                        System.err.print(
                            "Halting. Use \"-f\" to force simulation anyway!");
                        System.exit(-1);
                    }
                }
            } else {
                System.err.print("ERROR! Unknown Node in XML config file: \"" +
                    CurrentNode.getNodeName() + "\"!");

                if (!ForceContinue) {
                    System.err.print(
                        "Halting. Use \"-f\" to force simulation anyway!");
                    System.exit(-1);
                }

                System.err.println();
            }
        }

        if (ParseChilds) {
            for (int i = 0; i < Childs.getLength(); i++) {
                Node NextNode = Childs.item(i);

                if (NextNode.getNodeType() == Node.ELEMENT_NODE) {
                    //					System.err.println(CurrentNode.getNodeName() + "=>" + NextNode.getNodeName());
                    traverseParse(NextNode);
                }
            }
        }
    }

    /**
     * A little util method that checks if the {@code FileName} is
     * really located in the {@code Directory} to prevent {@code
     * "../../../vmlinuz"} attacks and other nasty things. It has only a
     * special use to check filenames in config files to protect the careless
     * user from malicous or malformed config files (even from config viruses :).<br>
     * {@code goodFileName("A","B")} returns {@code true} if the Canonical Path
     * of {@code A} starts with {@code B}.<br>
     * Note that some {@code getCanonicalPath()} implementations are known to
     * be broken. {@code goodFileName("../test.txt",X)} returns {@code false}
     * for any {@code X} I know It returns {@code true} in case of {@code
     * broken symlink}s even when it should return {@code false}.
     *
     * @param FileName DOCUMENT ME!
     * @param Directory DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean goodFileName(String FileName, String Directory) {
        try {
            if ((new File(Directory + FileName)).getCanonicalPath()
                     .startsWith((new File(Directory + ".")).getCanonicalPath())) {
                return (true);
            }
        } catch (Exception E) {
            ;
        }

        return (false);
    }

    /**
     * Parses a {@code Car} node in the XML config file.
     *
     * @param CarNode DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Car parseCar(Node CarNode) {
        NamedNodeMap Attributes = CarNode.getAttributes();
        int StartingFloor = Integer.parseInt(Attributes.getNamedItem(
                    "StartingFloor").getNodeValue());
        int SpaceCapacity = Integer.parseInt(Attributes.getNamedItem(
                    "SpaceCapacity").getNodeValue());
        NodeList Childs = CarNode.getChildNodes();
        Node KinematicModel = null;

        for (int i = 0; i < Childs.getLength(); i++) {
            if (Childs.item(i).getNodeName().endsWith("KinematicModel")) {
                KinematicModel = Childs.item(i);
            }
        }

        Attributes = KinematicModel.getAttributes();

        if (KinematicModel.getNodeName().equals("SimpleKinematicModel")) {
            int BaseTime = Integer.parseInt(Attributes.getNamedItem("BaseTime")
                                                      .getNodeValue());
            int FloorFligthTime = Integer.parseInt(Attributes.getNamedItem(
                        "FloorFligthTime").getNodeValue());
            int DoorOpenTime = Integer.parseInt(Attributes.getNamedItem(
                        "DoorOpenTime").getNodeValue());
            int DoorCloseTime = Integer.parseInt(Attributes.getNamedItem(
                        "DoorCloseTime").getNodeValue());
            SimpleKinematicModel KM = new SimpleKinematicModel(BaseTime,
                    FloorFligthTime, DoorOpenTime, DoorCloseTime);
            Car C = new SimulatedCar(StartingFloor, KM);
            C.setCapacity(SpaceCapacity);

            return (C);
        } else {
            Childs = KinematicModel.getChildNodes();

            ArrayList LSs = new ArrayList(Childs.getLength());

            for (int i = 0; i < Childs.getLength(); i++) {
                Node ActNode = Childs.item(i);

                if ("LoadSpeed".equals(ActNode.getNodeName())) {
                    Attributes = ActNode.getAttributes();

                    int BaseTime = Integer.parseInt(Attributes.getNamedItem(
                                "BaseTime").getNodeValue());
                    int FloorFligthTime = Integer.parseInt(Attributes.getNamedItem(
                                "FloorFligthTime").getNodeValue());
                    int DoorOpenTime = Integer.parseInt(Attributes.getNamedItem(
                                "DoorOpenTime").getNodeValue());
                    int DoorCloseTime = Integer.parseInt(Attributes.getNamedItem(
                                "DoorCloseTime").getNodeValue());
                    int FromLoad = Integer.parseInt(Attributes.getNamedItem(
                                "FromLoad").getNodeValue());
                    int ToLoad = Integer.parseInt(Attributes.getNamedItem(
                                "ToLoad").getNodeValue());
                    LoadSpeed LS = new LoadSpeed(BaseTime, FloorFligthTime,
                            DoorOpenTime, DoorCloseTime, FromLoad, ToLoad);
                    LSs.add(LS);
                }
            }

            LoadSpeed[] LoadSpeeds = new LoadSpeed[LSs.size()];
            LSs.toArray(LoadSpeeds);

            LoadDependentKinematicModel KM = new LoadDependentKinematicModel(LoadSpeeds);
            Car C = new SimulatedCar(StartingFloor, KM);
            C.setCapacity(SpaceCapacity);

            return ((SpaceCapacity < KM.getLoadTablesLength()) ? C : null);
        }
    }
}
