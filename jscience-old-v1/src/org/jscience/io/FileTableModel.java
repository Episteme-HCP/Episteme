/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.io;

import org.jscience.util.StringUtils;

import java.io.*;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


/**
 * creates an in-memory TableModel based on an import file where the first
 * line contains headers
 *
 * @author Holger Antelmann
 */
public class FileTableModel extends DefaultTableModel {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -626726082690271183L;

/**
     * enables to create a FileTableModel from any model, so that it can be saved as a file
     */
    public FileTableModel(TableModel model) {
        super();

        Object[] header = new String[model.getColumnCount()];

        for (int i = 0; i < header.length; i++)
            header[i] = model.getColumnName(i);

        Object[][] data = new Object[model.getRowCount()][model.getColumnCount()];

        for (int row = 0; row < model.getRowCount(); row++) {
            for (int column = 0; column < model.getColumnCount(); column++) {
                data[row][column] = model.getValueAt(row, column);
            }
        }

        setDataVector(data, header);
    }

/**
     * assumes that the first line contains header
     */
    public FileTableModel(File file, String fieldSeparator, boolean usesQuotes)
        throws IOException {
        this(file, true, fieldSeparator, usesQuotes);
    }

    /**
     * Creates a new FileTableModel object.
     *
     * @param file DOCUMENT ME!
     * @param firstLineContainsHeader DOCUMENT ME!
     * @param fieldSeparator DOCUMENT ME!
     * @param usesQuotes DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public FileTableModel(File file, boolean firstLineContainsHeader,
        String fieldSeparator, boolean usesQuotes) throws IOException {
        super();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        ArrayList<String> headers = new ArrayList<String>();

        if (firstLineContainsHeader) {
            // add columns from headers
            if (usesQuotes) {
                for (String column : ArgumentParser.getAll(line, fieldSeparator)) {
                    headers.add(column);
                }
            } else {
                StringTokenizer st = new StringTokenizer(line, fieldSeparator);

                while (st.hasMoreTokens()) {
                    headers.add(st.nextToken());
                }
            }

            line = reader.readLine();
        } else {
            int len;

            if (usesQuotes) {
                ArgumentParser ap = new ArgumentParser(line, fieldSeparator);
                len = ArgumentParser.getAll(line, fieldSeparator).length;
            } else {
                StringTokenizer st = new StringTokenizer(line, fieldSeparator);
                len = st.countTokens();
            }

            System.out.println("debug " + len);

            for (int i = 1; i <= len; i++) {
                headers.add(String.valueOf(i));
            }
        }

        for (String h : headers)
            addColumn(h);

        while (line != null) {
            Vector<Object> rowData = new Vector<Object>();

            if (usesQuotes) {
                ArgumentParser ap = new ArgumentParser(line, fieldSeparator);

                while (ap.hasMoreArguments()) {
                    rowData.add(ap.nextArgument());
                }
            } else {
                StringTokenizer st = new StringTokenizer(line, fieldSeparator);

                while (st.hasMoreTokens()) {
                    rowData.add(st.nextToken());
                }
            }

            addRow(rowData);
            line = reader.readLine();
        }

        reader.close();
    }

    /**
     * will always write a header line. If the model didn't contain
     * header, some default header will be provided (1, 2, ..).
     *
     * @param file DOCUMENT ME!
     * @param fieldSeparator DOCUMENT ME!
     * @param usesQuotes DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void saveToFile(File file, String fieldSeparator, boolean usesQuotes)
        throws IOException {
        FileWriter writer = new FileWriter(file);

        // header
        for (int i = 0; i < columnIdentifiers.size(); i++) {
            String h = columnIdentifiers.get(i).toString();

            if (usesQuotes) {
                h = "\"" + h + "\"";
            }

            writer.write(h);

            if (i < (columnIdentifiers.size() - 1)) {
                writer.write(fieldSeparator);
            }
        }

        writer.write(StringUtils.lb);

        // table data
        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                Object value = getValueAt(row, column);

                if (value == null) {
                    value = "";
                }

                if (usesQuotes) {
                    value = "\"" + value + "\"";
                }

                writer.write(value.toString());

                if (column < (getColumnCount() - 1)) {
                    writer.write(fieldSeparator);
                }
            }

            writer.write(StringUtils.lb);
        }

        writer.flush();
        writer.close();
    }
}
