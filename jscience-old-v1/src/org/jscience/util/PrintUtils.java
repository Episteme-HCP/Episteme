/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import java.io.*;

import javax.print.*;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaName;
import javax.print.attribute.standard.OrientationRequested;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author Holger Antelmann
 */
public class PrintUtils {
    /** currently uses ISO_A4_WHITE by default */
    public static MediaName mediaName = MediaName.ISO_A4_WHITE;

    //final public static MediaName mediaName = MediaName.NA_LETTER_WHITE;
/**
     * Creates a new PrintUtils object.
     */
    private PrintUtils() {
    }

    /**
     * DOCUMENT ME!
     */
    public static void useA4() {
        mediaName = MediaName.ISO_A4_WHITE;
    }

    /**
     * DOCUMENT ME!
     */
    public static void useLetter() {
        mediaName = MediaName.NA_LETTER_WHITE;
    }

    /**
     * prints the given component after selecting the desired printer
     *
     * @param c DOCUMENT ME!
     *
     * @throws PrinterException DOCUMENT ME!
     */
    public static synchronized void print(Component c)
        throws PrinterException {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        print(printJob, c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param printJob DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @throws PrinterException DOCUMENT ME!
     */
    public static synchronized void print(PrinterJob printJob, final Component c)
        throws PrinterException {
        final RepaintManager currentManager = RepaintManager.currentManager(c);
        final boolean flag = currentManager.isDoubleBufferingEnabled();
        currentManager.setDoubleBufferingEnabled(false);

        Printable printable = new Printable() {
                public int print(Graphics g, PageFormat pageFormat,
                    int pageIndex) {
                    if (pageIndex > 0) {
                        return (NO_SUCH_PAGE);
                    } else {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.translate(pageFormat.getImageableX(),
                            pageFormat.getImageableY());
                        currentManager.setDoubleBufferingEnabled(false);
                        c.paint(g2d);
                        currentManager.setDoubleBufferingEnabled(flag);

                        return (PAGE_EXISTS);
                    }
                }
            };

        printJob.setPrintable(printable);

        if (printJob.printDialog()) {
            printJob.print();
        }
    }

    /**
     * calls <code>text(text, true)</code>
     *
     * @see #print(String,boolean)
     */
    public static void print(String text) throws PrintException {
        print(text, true);
    }

    /**
     * prints the given text to the default printer (adding a formFeed
     * if desired)
     *
     * @param text DOCUMENT ME!
     * @param formFeed DOCUMENT ME!
     *
     * @throws PrintException DOCUMENT ME!
     */
    public static void print(String text, boolean formFeed)
        throws PrintException {
        print(PrintServiceLookup.lookupDefaultPrintService(), text, formFeed);
    }

    /**
     * prints the given text to the given print service (adding a
     * formFeed if desired)
     *
     * @param service DOCUMENT ME!
     * @param text DOCUMENT ME!
     * @param formFeed DOCUMENT ME!
     *
     * @throws PrintException DOCUMENT ME!
     */
    public static void print(PrintService service, String text, boolean formFeed)
        throws PrintException {
        if (formFeed) {
            text += "\f";
        }

        InputStream stream = new ByteArrayInputStream(text.getBytes());
        Doc myDoc = new SimpleDoc(stream, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
        DocPrintJob job = service.createPrintJob();
        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(new Copies(1));
        attr.add(mediaName);
        attr.add(OrientationRequested.PORTRAIT);
        job.print(myDoc, attr);
    }

    /**
     * DOCUMENT ME!
     *
     * @param asciiFile DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws PrintException DOCUMENT ME!
     */
    public static void printTextFile(File asciiFile)
        throws IOException, PrintException {
        printTextFile(PrintServiceLookup.lookupDefaultPrintService(), asciiFile);
    }

    /**
     * DOCUMENT ME!
     *
     * @param service DOCUMENT ME!
     * @param asciiFile DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws PrintException DOCUMENT ME!
     */
    public static void printTextFile(PrintService service, File asciiFile)
        throws IOException, PrintException {
        FileInputStream stream = new FileInputStream(asciiFile);
        Doc myDoc = new SimpleDoc(stream, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
        DocPrintJob job = service.createPrintJob();
        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(new Copies(1));
        attr.add(mediaName);
        attr.add(OrientationRequested.PORTRAIT);
        job.print(myDoc, attr);

        //stream.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param service DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getInfoOn(PrintService service) {
        Attribute[] attr = service.getAttributes().toArray();
        String s = "Attributes:\n";

        for (int i = 0; i < attr.length; i++) {
            s += (attr[i].getName() + ": " + attr[i].toString() + "\n");
        }

        s += "\nSupported attribute categories: \n";

        Class[] c = service.getSupportedAttributeCategories();

        for (int i = 0; i < c.length; i++) {
            s += (c[i].getName() + "\n");
        }

        DocFlavor[] df = service.getSupportedDocFlavors();
        s += "\nDocFlavors:\n";

        for (int i = 0; i < df.length; i++) {
            s += (df[i] + "\n");
        }

        return s;
    }
}
