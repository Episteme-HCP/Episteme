package org.jscience.tests.io.fitsbrowser;

import eap.filter.*;

import org.jscience.io.fits.*;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;


/**
 * Component for displaying a single FITS file. This class makes up the
 * core of both the standalone and applet versions of the browser. You may
 * embed this component in some other tool which needs to display or edit the
 * contents of a FITS file.
 */
public class FITSFileDisplay extends JTabbedPane {
    /** DOCUMENT ME! */
    JMenuBar menu_bar;

    /** DOCUMENT ME! */
    JFileChooser file_chooser;

    /** DOCUMENT ME! */
    JTabbedPane tabs;

    /** DOCUMENT ME! */
    FitsFile fits;

    /** DOCUMENT ME! */
    IOException background_exception;

    /** DOCUMENT ME! */
    FilterBroker filters;

/**
     * Create a new display which does not display any file.
     */
    public FITSFileDisplay() {
        filters = new FilterBroker();

        try {
            filters.addDefaultFilters();
        } catch (IOException e) {
        }

        filters.setPasswordProvider(new PopupPasswordProvider(this));
    } // end of constructor

    /**
     * Set the password provider to be used when reading an encrypted
     * data stream By default this class uses a {@link
     * eap.filter.PopupPasswordProvider}.
     *
     * @param password DOCUMENT ME!
     */
    public void setPasswordProvider(PasswordProvider password) {
        filters.setPasswordProvider(password);
    }

    /**
     * Display the given file or URL.
     *
     * @param string file name or URL to be displayed
     *
     * @throws IOException DOCUMENT ME!
     */
    public void load(String string) throws IOException {
        try {
            load(new URL(string));

            return;
        } catch (MalformedURLException e) {
        }

        load(new File(string));
    } // end of load String method

    /**
     * Display the given file.
     *
     * @param file The file to be displayed
     *
     * @throws IOException DOCUMENT ME!
     */
    public void load(File file) throws IOException {
        if (filters.hasFilter(file.getName())) {
            /**
             * we need to filter the data and that means using
             * an InputStream as the data source
             */
            InputStream in = new FileInputStream(file);
            InputStream filtered = filters.filter(in, file.getName());

            if (filtered != in) {
/**
                 * some filtering was actually done
                 */
                load(filtered);

                return;
            }
        } // end if it looked like we should filter

/**
         * Either there wasn't any filtering to do, or the data didn't meet the
         * filters expectations. In either case we are reading the raw data,
         * so we might as well do that with a random access FITS file, since
         * that's better for FITS I/O
         */
        load(new RandomAccessFitsFile(new RandomAccessFile(file, "r")));
    } // end of openFile method

    /**
     * Displays a URL
     *
     * @param url the URL of a FITS file to display
     *
     * @throws IOException DOCUMENT ME!
     */
    public void load(URL url) throws IOException {
/**
         * open the URL, possibly filtering
         */
        load(filters.filter(url.openStream(), url.getPath()));
    } // end of load URL method

    /**
     * Display the FITS file which can be read from the given input
     * stream.
     *
     * @param in data stream from which to read FITS data.
     *
     * @throws IOException DOCUMENT ME!
     */
    public void load(InputStream in) throws IOException {
        load(new InputStreamFitsFile(in));
    } // end of load InputStream method

    /**
     * Write the file currently displayed.
     *
     * @param file The file to be written.
     *
     * @throws IOException DOCUMENT ME!
     */
    public void save(File file) throws IOException {
        fits.write(new FileOutputStream(file));
    } // end of save method

    /**
     * Display a FITS file which has previously been read by some other
     * means.
     *
     * @param fits The FITS file to display.
     *
     * @throws IOException DOCUMENT ME!
     */
    public void load(FitsFile fits) throws IOException {
        this.fits = fits;
/**
         * clear out any old FITS file
         */
        removeAll();

/**
         * loop over all HDUs
         */
        try {
            for (int i = 0;; ++i) {
                FitsHDU hdu = fits.getHDU(i);
                FitsData data = hdu.getData();
                FitsHeader header = hdu.getHeader();

                add(i + ". " + header.getName() + " (" + header.getType() +
                    ")", new HDUDisplay(hdu));

                //       pack();
                //       validate();
            }
        } catch (NoSuchFitsHDUException e) {
        }
    } // end of displayFitsFile method
} // end of FITSFileDisplay class
