/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.benchmarking.bench;
import java.net.Socket;
import java.net.URL;
import java.net.ProtocolException;
import java.io.PrintStream;
import java.io.DataInputStream;
/**
HTTPPost posts a message to an HTTP url.

NOTE: Since a socket connection is used, when executed from an Applet,
the security model requires that the server be the same as the server 
from which the applet was loaded.

@author Bruce R. Miller (bruce.miller@nist.gov)
@author Contribution of the National Institute of Standards and Technology,
@author not subject to copyright.
*/

//code under public domain

public class HTTPPost {

  /** Post a message to a URL.
    * @param url The url to the HTTP server
    * @param the message text. 
    */
  public static void post(String url, String message) throws Exception {
    URL Url=new URL(url);
    int port = Url.getPort();
    if (port < 0) port=80;
    Socket socket   = new Socket(Url.getHost(), port, true);
    PrintStream output   = new PrintStream(socket.getOutputStream());
    DataInputStream response = new DataInputStream(socket.getInputStream());
    output.println("POST "+Url.getFile()+" HTTP/1.0");
    output.println("Content-Length: "+message.length());
    output.println();
    output.print(message);
    String resp=response.readLine();
    int i0=resp.indexOf(' ')+1;
    int i1=resp.indexOf(' ',i0);
    int retcode = Integer.parseInt(resp.substring(i0,i1).trim());
    if(retcode != 100) throw new ProtocolException(resp);

    output.close();
    socket.close();
  }
}
