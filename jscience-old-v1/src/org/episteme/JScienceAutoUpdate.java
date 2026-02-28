package org.episteme;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public final class EpistemeAutoUpdate {
    /** DOCUMENT ME! */
    private final static String zipfile = "Episteme.zip";

    /** DOCUMENT ME! */
    private final static String email = "contact@episteme.org";

    /** DOCUMENT ME! */
    private URL url;

/**
     * Creates a new EpistemeAutoUpdate object.
     *
     * @param home DOCUMENT ME!
     */
    private EpistemeAutoUpdate(String home) {
        try {
            url = new URL(home + zipfile);
        } catch (MalformedURLException e) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        EpistemeVersion current = EpistemeVersion.getCurrent();
        System.out.println("Current version: " + current.toString());
        System.out.println("Checking for a later version...");

        try {
            EpistemeVersion latest = EpistemeVersion.getLatest();
            System.out.println("Latest version: " + latest.toString());

            if (latest.isLater(current)) {
                System.out.print("Downloading latest version...");

                EpistemeAutoUpdate app = new EpistemeAutoUpdate(current.home);
                app.download();
                System.out.println("done.");
            }
        } catch (IOException e) {
            System.out.println(
                "\nError transfering data - try again or contact " + email +
                " directly.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void download() throws IOException {
        final OutputStream out = new FileOutputStream(zipfile);
        final InputStream in = url.openStream();
        byte[] buf = new byte[in.available()];

        while (buf.length > 0) {
            in.read(buf);
            out.write(buf);
            buf = new byte[in.available()];
        }

        in.close();
        out.close();
    }
}
