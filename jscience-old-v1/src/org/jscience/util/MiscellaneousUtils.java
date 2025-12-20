/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.util;


import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * just some miscellaneous stuff for convenience that doesn't fit anywhere else
 *
 * @author Holger Antelmann
 */
public final class MiscellaneousUtils {
    static String KEY_ALGORITHM = SecurityNames.keyAlgorithm[0];
    //static String RNG_ALGORITHM = randomNumberGenerationAlgorithm[0];

    static DecimalFormat pFormatter = new DecimalFormat("0.00%");

    private MiscellaneousUtils() {
    }

    /**
     * aquired somewhere else; to be rewritten
     */
    static int parseHexString(String str) {
        String dgts = "0123456789abcdef";
        int a = 0;
        if (str.indexOf('#') >= 0)
            str = str.substring(str.indexOf('#') + 1);
        if (str.indexOf('x') >= 0)
            str = str.substring(str.indexOf('x') + 1);
        for (int i = 0; i < str.length(); i++) {
            int b = dgts.indexOf(str.charAt(i));
            a = a + (int) Math.pow(16, str.length() - i - 1) * b;
        }
        return a;
    }

    /**
     * This method attempts to start the given url with the associated
     * default application (most likely a web-browser) by calling
     * <sample>start urlPath</sample>. If this doesn't work, nothing happens;
     * errors are ignored.
     * The method uses the parameter <dfn>application.startCommand</dfn>
     * from the Properties obtained from <code>Settings.getProperties()</code>
     * to start the url. Note that a successful call to this method is platform
     * dependent; you may have to adjust the <dfn>application.startCommand</dfn>
     * parameter to make this work for your platform.
     * The default value for this parameter shipped with the antelmann.jar file
     * works for Windows NT/2000.
     *
     * @return true if no exceptions were thrown, false otherwise
     * @see Settings#getProperties()
     */
    public static boolean start(URL url) {
        Runtime runtime = Runtime.getRuntime();
        String command = Settings.getProperty("application.startCommand");
        if (command == null) return false;
        command = command.replaceAll("%1", url.toExternalForm());
        Process p = null;
        try {
            p = runtime.exec(command);
        } catch (Exception e) {
        }
        return (p != null);
    }

    /**
     * just simplifies a call to <code>Thread.sleep()</code>
     */
    public static void pause(long millisec) {
        /*
        Thread t = Thread.currentThread();
        try { synchronized (t) { t.sleep(millisec); }
        } catch (InterruptedException e) {}
        */
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String asPercent(double d) {
        return pFormatter.format(d);
    }

    public static KeyPair generateKeyPair() {
        return generateKeyPair(1024);
    }

    public static KeyPair generateKeyPair(int keySize) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            return generateKeyPair(KEY_ALGORITHM, keySize);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair generateKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(keySize);
        return keyGen.generateKeyPair();
    }
}
