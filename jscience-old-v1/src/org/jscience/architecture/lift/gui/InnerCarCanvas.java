package org.jscience.architecture.lift.gui;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.*;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:53 $
 */
public class InnerCarCanvas extends JPanel implements CarCanvas {
    /**
     * DOCUMENT ME!
     */
    private static FontRenderContext frc;

    /**
     * DOCUMENT ME!
     */
    private static Font[] MyFontMap = null;

    /**
     * DOCUMENT ME!
     */
    final static Color DoorColor = Color.WHITE;

    /**
     * DOCUMENT ME!
     */
    final static Color NotPresentColor = Color.LIGHT_GRAY;

    /**
     * DOCUMENT ME!
     */
    final static Color CarColor = new Color(200, 200, 100);

    /**
     * DOCUMENT ME!
     */
    final static Color PassengerColor = Color.GREEN.brighter();

    /**
     * DOCUMENT ME!
     */
    final static Color NoPassengerColor = Color.GREEN.darker();

    /**
     * DOCUMENT ME!
     */
    final static int PreferredWidth = 36;

    /**
     * DOCUMENT ME!
     */
    final static int PreferredHeight = 48;

    /**
     * DOCUMENT ME!
     */
    final static Dimension PreferredSize = new Dimension(PreferredWidth,
            PreferredHeight);

    /**
     * DOCUMENT ME!
     */
    private int MaxPassNo = 0;

    /**
     * DOCUMENT ME!
     */
    private int ActPassNo = 0;

    /**
     * DOCUMENT ME!
     */
    private String[] DstStrings = null;

    /**
     * DOCUMENT ME!
     */
    private String LargestDst = null;

    /**
     * DOCUMENT ME!
     */
    boolean CloseMode = true;

    /**
     * DOCUMENT ME!
     */
    boolean CarPresent = true;

    /**
     * DOCUMENT ME!
     */
    int State = 0;

    /**
     * DOCUMENT ME!
     */
    double Progress = 0;

    /**
     * DOCUMENT ME!
     */
    public double CircleSize = 0.8;

    /**
     * DOCUMENT ME!
     */
    public double FontSize = 0.8;

    /**
     * Creates a new InnerCarCanvas object.
     */
    public InnerCarCanvas() {
        super();
        setBackground(NotPresentColor);

        setSize(36, 48);

        //		setSize(72, 96);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        return (PreferredSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewState DOCUMENT ME!
     */
    public void setState(int NewState) {
        State = NewState;
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewProgress DOCUMENT ME!
     */
    public void setProgress(double NewProgress) {
        Progress = NewProgress;
    }

    /**
     * DOCUMENT ME!
     *
     * @param NewCarPresent DOCUMENT ME!
     */
    public void setCarPresent(boolean NewCarPresent) {
        CarPresent = NewCarPresent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param DstFloors DOCUMENT ME!
     */
    public void setDstFloors(int[] DstFloors) {
        if (DstFloors != null) {
            DstStrings = new String[DstFloors.length];

            for (int i = 0; i < DstStrings.length; i++) {
                DstStrings[i] = "" + DstFloors[i];

                if ((LargestDst == null) ||
                        (LargestDst.length() < DstStrings[i].length())) {
                    LargestDst = DstStrings[i];
                }
            }

            for (int i = 0; i < DstStrings.length; i++) {
                while (LargestDst.length() > DstStrings[i].length()) {
                    DstStrings[i] = " " + DstStrings[i];
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param MaxNumber DOCUMENT ME!
     */
    public void setMaxNumber(int MaxNumber) {
        MaxPassNo = MaxNumber;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ActNumber DOCUMENT ME!
     */
    public void setActNumber(int ActNumber) {
        ActPassNo = ActNumber;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOpaque() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param Width DOCUMENT ME!
     * @param Height DOCUMENT ME!
     * @param Count DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getMaxSize(int Width, int Height, int Count) {
        return getMaxSize(Width, Height, Count, 1.0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param Width DOCUMENT ME!
     * @param Height DOCUMENT ME!
     * @param Count DOCUMENT ME!
     * @param WHRatio DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getMaxSize(int Width, int Height, int Count,
        double WHRatio) {
        int TotalArea = Math.abs(Width) * Math.abs(Height);

        int CurrentSize = (int) Math.ceil(Math.sqrt(
                    ((double) TotalArea) / ((double) Count)));

        int HCount = 0;
        int VCount = 0;

        while ((HCount * VCount) < Count) {
            CurrentSize--;
            HCount = Width / (int) (CurrentSize * WHRatio);
            VCount = Height / CurrentSize;
        }

        return (CurrentSize);
    }

    /**
     * DOCUMENT ME!
     */
    private void checkInit() {
        if (frc == null) {
            frc = ((Graphics2D) getGraphics()).getFontRenderContext();
        }

        if (MyFontMap == null) {
            MyFontMap = new Font[16];

            for (int i = 4; i < 16; i++) {
                MyFontMap[i] = Font.decode("MONOSPACE-BOLD-" + i);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param W DOCUMENT ME!
     * @param H DOCUMENT ME!
     * @param G DOCUMENT ME!
     */
    public void paintBasic(int W, int H, Graphics G) {
        int FillWidth = 0;
        int FillHeigth = 0;

        if (!CarPresent) {
            G.setColor(NotPresentColor);
            G.fillRect(0, 0, W, H);

            return;
        }

        switch (State) {
        case CLOSE_OPEN:
            G.setColor(CarColor);
            G.fillRect(0, 0, W, H);

            FillWidth = (int) (W * Progress * 0.5);

            G.setColor(DoorColor);
            G.fillRect(0, 0, FillWidth, H);
            G.fillRect(W - FillWidth, 0, W, H);

            break;

        case ARRIVING_UP:
        case GOING_UP:
        case GOING_DOWN:
        case ARRIVING_DOWN:
        case PARKING:
            G.setColor(DoorColor);
            G.fillRect(0, 0, W, H);

            break;

        case WAITING:
            G.setColor(CarColor);
            G.fillRect(0, 0, W, H);

            break;

        default:
            throw new RuntimeException("Not implemented!");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param Size DOCUMENT ME!
     * @param H DOCUMENT ME!
     * @param W DOCUMENT ME!
     * @param G DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int paintPassengers(int Size, int H, int W, Graphics G) {
        int HCount = W / Size;
        int VCount = H / Size;
        int CircleDiameter = (int) Math.floor(CircleSize * Size);
        int Shift = (Size - CircleDiameter) / 2;

        int RowShift = ((W - (Size * HCount)) / 2) + Shift;
        int ColumnShift = ((H - (Size * VCount)) / 2) + Shift;
        int Row = 0;
        int Column = 0;

        for (int i = 0; i < MaxPassNo; i++) {
            G.setColor((ActPassNo > i) ? PassengerColor : NoPassengerColor);
            G.fillOval((Row * Size) + RowShift, (Column * Size) + ColumnShift,
                CircleDiameter, CircleDiameter);
            Row++;

            if (Row == HCount) {
                Row = 0;
                Column++;
            }
        }

        return (CircleDiameter);
    }

    /**
     * DOCUMENT ME!
     *
     * @param Size DOCUMENT ME!
     * @param H DOCUMENT ME!
     * @param W DOCUMENT ME!
     * @param G DOCUMENT ME!
     */
    public void paintText(int Size, int H, int W, Graphics G) {
        int CircleDiameter = (int) Math.floor(CircleSize * Size);
        int Shift = (Size - CircleDiameter) / 2;
        int MaxFontBound = (int) (((double) CircleDiameter) * FontSize);
        int MaxFontSize = 16;

        int HCount = W / Size;
        int VCount = H / Size;

        int RowShift = ((W - (Size * HCount)) / 2) + Shift;
        int ColumnShift = ((H - (Size * VCount)) / 2) + Shift;
        int Row = 0;
        int Column = 0;

        boolean FontOK = false;

        for (int i = 15; i >= 6; i--) {
            FontOK = true;

            TextLayout layout = new TextLayout(LargestDst, MyFontMap[i], frc);
            Rectangle2D bounds = layout.getBounds();
            int BiggerSize = Math.max((int) Math.ceil(bounds.getHeight()),
                    (int) Math.ceil(bounds.getWidth()));

            if (BiggerSize > MaxFontBound) {
                FontOK = false;
            } else {
                MaxFontSize = i;

                break;
            }
        }

        if (FontOK) {
            G.setFont(MyFontMap[MaxFontSize]);
            G.setColor(Color.BLACK);
            Row = 0;
            Column = 0;
            RowShift += (CircleDiameter / 2);
            ColumnShift += (CircleDiameter / 2);

            TextLayout layout = new TextLayout(LargestDst,
                    MyFontMap[MaxFontSize], frc);
            Rectangle2D bounds = layout.getBounds();
            int FWS = ((int) Math.ceil(bounds.getWidth())) / 2;
            int FHS = ((int) Math.ceil(bounds.getHeight())) / 2;

            for (int j = 0; j < ActPassNo; j++) {
                G.drawString(DstStrings[j], ((Row * Size) + RowShift) - FWS,
                    (Column * Size) + ColumnShift + FHS);
                Row++;

                if (Row == HCount) {
                    Row = 0;
                    Column++;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param G DOCUMENT ME!
     */
    public void paintComponent(Graphics G) {
        int W = getSize().width;
        int H = getSize().height;

        checkInit();

        paintBasic(W, H, G);

        if (CarPresent) {
            int Size = getMaxSize(W, H, MaxPassNo);

            paintPassengers(Size, H, W, G);

            if (DstStrings != null) {
                paintText(Size, H, W, G);
            }

            shiftIt(G);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param G DOCUMENT ME!
     */
    private void shiftIt(Graphics G) {
        int HP = 0;
        int IHP = 0;

        int W = getSize().width;
        int H = getSize().height;

        G.setColor(NotPresentColor);

        switch (State) {
        case GOING_UP:
            HP = (int) (Math.ceil(H * Progress));
            IHP = H - HP;
            G.copyArea(0, HP, W, IHP, 0, -HP);
            G.fillRect(0, IHP, W, HP);

            break;

        case ARRIVING_UP:
            HP = (int) (Math.ceil(H * Progress));
            IHP = H - HP;
            G.copyArea(0, 0, W, H, 0, IHP);
            G.fillRect(0, 0, W, IHP);

            break;

        case GOING_DOWN:
            HP = (int) (Math.ceil(H * Progress));
            IHP = H - HP;
            G.copyArea(0, 0, W, IHP, 0, HP);
            G.fillRect(0, 0, W, HP);

            break;

        case ARRIVING_DOWN:
            HP = (int) (Math.ceil(H * Progress));
            IHP = H - HP;
            G.copyArea(0, IHP, W, H, 0, -IHP);
            G.fillRect(0, HP, W, H);

            break;

        case CLOSE_OPEN:
        case WAITING:
        case PARKING:
            break;

        default:
            throw new RuntimeException("Not Implemented");
        }
    }
}
