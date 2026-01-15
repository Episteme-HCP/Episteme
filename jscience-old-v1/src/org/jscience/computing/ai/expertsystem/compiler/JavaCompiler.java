package org.jscience.computing.ai.expertsystem.compiler;

/*
* JEOPS - The Java Embedded Object Production System
* Copyright (c) 2000   Carlos Figueira Filho
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*
* Contact: Carlos Figueira Filho (csff@cin.ufpe.br)
*/

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileReader;

import java.io.InputStream;
//import java.io.OutputStream;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Method;
//import java.util.StringTokenizer;
//import java.util.Vector;

/**
 * Auxiliar class that compiles java files into bytecodes (.class files).
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.02  11.08.2000 Using Runtime.exec("javac <class file>").
 * @history 0.01  11.08.2000 First version, using class sun.tools.javac.Main
 */
public class JavaCompiler {

//	/**
//	 * Auxiliar attribute used to store the directories of the classpath.
//	 */
//	private static String[] classPath;
//
//	/**
//	 * The javac compiler main class.
//	 */
//	private static Class javacClass;
//
//	/**
//	 * Static initializer.
//	 */
//	static {
//		String strClassPath = System.getProperty("java.class.path");
//		StringTokenizer st = new StringTokenizer(strClassPath, File.pathSeparator);
//		Vector aux = new Vector();
//		while (st.hasMoreTokens()) {
//			String dir = st.nextToken();
//			File f = new File(dir);
//			if (f.exists() && f.isDirectory()) {
//				if (dir.charAt(dir.length() - 1) != File.separatorChar) {
//					dir = dir + File.separatorChar;
//				}
//				aux.addElement(dir);
//			}
//		}
//		classPath = new String[aux.size()];
//		for (int i = 0; i < aux.size(); i++) {
//			classPath[i] = (String) aux.elementAt(i);
//		}
//		try {
//			javacClass = Class.forName("sun.tools.javac.Main");
//		} catch (ClassNotFoundException e) {
//			javacClass = null;
//		}
//	}

    /**
     * Compile a given .java source file.
     *
     * @param fileName the name of the .java source file.
     * @return <code>true</code> if the class could be compiled;
     *         <code>false</code> otherwise.
     * @throws JeopsException if some error occurs.
     */
    public static boolean compileJavaFile(String fileName)
            throws JeopsException {
        JeopsException error = null;
        boolean result = false;
        try {
            Process p = Runtime.getRuntime().exec("javac " + fileName);
            InputStream is = p.getErrorStream();
            int data = is.read();
            char cData = (char) data;
            result = true;
            StringBuffer sb = new StringBuffer();
            while ((data != -1) && (cData != '\n') && (cData != '\r')) {
                result = false;
                sb.append(cData);
                data = is.read();
                cData = (char) data;
            }
            is.close();
            p.destroy();
            if (!result) {
                error = new JeopsException(sb.toString(), 0, 0);
            }
        } catch (Exception e) {
        }
        if (error != null) {
            throw error;
        } else {
            return result;
        }

//		boolean result = false;
//		JeopsException error = null;
//		if (javacClass != null) {
//			try {
//				Constructor cons = javacClass.getConstructor(
//							new Class[] { OutputStream.class, String.class });
//				File tmp = File.createTempFile("org.jscience.computing.ai.expertsystem", ".tmp");
//				FileOutputStream fos = new FileOutputStream(tmp);
//				Object o = cons.newInstance(new Object[] { fos, "javac" });
//				Method met = javacClass.getMethod("compile",
//							new Class[] { String[].class });
//				String[] args = new String[] { fileName };
//				Boolean compileOk = (Boolean) met.invoke(o, new Object[] { args });
//				fos.close();
//				if (compileOk.booleanValue()) {
//					result = true;
//				} else {
//					FileReader fis = new FileReader(tmp);
//					BufferedReader br = new BufferedReader(fis);
//					String line = br.readLine();
//					error = new JeopsException(line, 0, 0);
//					br.close();
//					fis.close();
//				}
//				tmp.delete();
//			} catch (Exception e) {}
//		}
//		if (error != null) {
//			throw error;
//		} else {
//			return result;
//		}
    }

    /**
     * Test method for this class.
     *
     * @param args the name of the file to be compiled.
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                boolean result = compileJavaFile(args[0]);
                if (result) {
                    System.out.println("Compilation ok.");
                } else {
                    System.out.println("Error.");
                }
            } catch (JeopsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
