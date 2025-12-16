package org.jscience;

public final class JScienceVersion {
    public static final String VERSION;

    static {
        String v = "Unknown";
        try (java.io.InputStream is = JScienceVersion.class.getResourceAsStream("version.properties")) {
            if (is != null) {
                java.util.Properties p = new java.util.Properties();
                p.load(is);
                v = p.getProperty("version", "Unknown");
            }
        } catch (Exception e) {
            // Ignore
        }
        VERSION = v;
    }

    private JScienceVersion() {
    }
}
