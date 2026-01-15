/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.net;

import org.jscience.io.ArgumentParser;
import org.jscience.io.ExtendedFile;
import org.jscience.util.AbstractIterator;
import org.jscience.util.Filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Locale;

/**
 * ServerLogFile implements a web-server log based on a standard log file.
 * If a given logfile format is different so that it cannot be parsed by
 * this implementation, you can simply subclass this implementation and
 * overwrite the <code>parseLine(String)</code> method.
 *
 * @author Holger Antelmann
 * @see ServerLogEntry
 */
public class ServerLogFile extends ExtendedFile implements ServerLog {
    static final long serialVersionUID = 8656579990080497085L;

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);

    protected class ServerLogEntryEnumerator extends AbstractIterator<ServerLogEntry> {
        BufferedReader in;
        String lastObject = null;
        ServerLogEntry lastEntry = null;
        Filter<ServerLogEntry> filter;
        boolean finished = false;

        protected ServerLogEntryEnumerator(Filter<ServerLogEntry> filter) {
            this.filter = filter;
            try {
                in = new BufferedReader(new FileReader(ServerLogFile.this));
            } catch (Exception ex) {
            }
        }

        protected void finalize() throws IOException {
            in.close();
        }

        protected ServerLogEntry getNextObject() throws Exception {
            String line = in.readLine();
            if (line == null) in.close();
            ServerLogEntry entry = parseLine(line);
            if ((filter == null) || (filter.accept(entry))) {
                return entry;
            } else {
                return getNextObject();
            }
        }
    }

    public ServerLogFile(String fileName) {
        super(fileName);
    }

    public ServerLogFile(File file) {
        super(file);
    }

    public Enumeration<ServerLogEntry> getServerLogEntryEnumerator(Filter<ServerLogEntry> filter) {
        return new ServerLogEntryEnumerator(filter);
    }

    /**
     * parses a single line from the log file and constructs a ServerLogEntry
     */
    protected ServerLogEntry parseLine(String line) {
        try {
            String s[] = ArgumentParser.getAll(line);
            ServerLogEntry se = new ServerLogEntry();
            se.host = s[0];
            se.user = s[1];
            se.time = dateFormat.parse(s[3].substring(1, s[3].length()));
            se.method = s[5].substring(0, s[5].indexOf(" "));
            se.url = s[5].substring(s[5].indexOf(" ") + 1, s[5].length());
            se.protocol = se.url.substring(s[5].indexOf(" ") + 1, se.url.length());
            se.url = se.url.substring(0, se.url.indexOf(" "));
            se.referer = s[8];
            se.code = Short.parseShort(s[6]);
            se.agent = s[9];
            return se;
        } catch (Exception ex) {
            return null;
        }
    }
}
