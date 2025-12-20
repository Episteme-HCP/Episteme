package org.jscience.biology.loaders;

import org.jscience.biology.genetics.BioSequence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reader for FASTA format sequences.
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
 */
public class FASTAReader {

    /**
     * Reads sequences from a FASTA stream.
     * 
     * @param inputStream the input stream
     * @param type        the expected type of sequence (DNA, RNA, Protein)
     * @return list of loaded bio sequences
     * @throws IOException on error
     */
    public static List<BioSequence> read(InputStream inputStream, BioSequence.Type type) throws IOException {
        List<BioSequence> sequences = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        StringBuilder currentSeq = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty())
                continue;

            if (line.startsWith(">")) {
                if (currentSeq.length() > 0) {
                    sequences.add(new BioSequence(currentSeq.toString(), type));
                    currentSeq.setLength(0);
                }
            } else {
                currentSeq.append(line);
            }
        }

        if (currentSeq.length() > 0) {
            sequences.add(new BioSequence(currentSeq.toString(), type));
        }

        return Collections.unmodifiableList(sequences);
    }
}
