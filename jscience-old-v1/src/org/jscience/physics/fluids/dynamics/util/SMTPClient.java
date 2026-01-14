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

package org.jscience.physics.fluids.dynamics.util;

import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.KernelADFCConfiguration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.net.InetAddress;
import java.net.Socket;


/**
 * Cliente SMTP to send small notification by e-mail of certain events.
 */
public class SMTPClient {
    /** DOCUMENT ME! */
    private static int PORT_SMTP = 25;

    /** DOCUMENT ME! */
    private String ACCOUNT_SMTP;

    /** DOCUMENT ME! */
    private String HOST_SMTP;

    /** DOCUMENT ME! */
    private String HOST_SENDER_SMTP;

    /** DOCUMENT ME! */
    private String sender;

    /** DOCUMENT ME! */
    private InetAddress mailhost;

    /** DOCUMENT ME! */
    private KernelADFC kernel;

/**
     * Constructor of client SMTP.
     *
     * @param kadfc DOCUMENT ME!
     */
    public SMTPClient(KernelADFC kadfc) {
        kernel = kadfc;

        KernelADFCConfiguration config = kernel.getConfiguration();

        ACCOUNT_SMTP = config.accountSMTP;
        HOST_SMTP = config.hostSMTP;

        try {
            HOST_SENDER_SMTP = InetAddress.getLocalHost().getHostName();
            mailhost = InetAddress.getByName(HOST_SMTP);
        } catch (Exception e) {
            System.out.println("ClienteSMTP(): " + e);
        }

        sender = "adfc@" + HOST_SENDER_SMTP;

        kernel.out("<B>SMTPClient:</B> notifies to <I>" + ACCOUNT_SMTP +
            "</I>" + " from the smtp server <I>" + HOST_SMTP + "</I><BR>" +
            "<B>SMTPClient:</B> the sender will be <I>" + sender + "</I>");
    }

    /**
     * method enviar abreviado.
     *
     * @param titulo DOCUMENT ME!
     * @param msg DOCUMENT ME!
     */
    public void enviar(String titulo, String msg) {
        kernel.out("<B>ClienteSMTP:</B> enviando email <U>" + titulo +
            "</U> a <I>" + ACCOUNT_SMTP + "</I>");
        enviar(sender, ACCOUNT_SMTP, titulo, msg);
    }

    /**
     * Send a menssage.
     *
     * @param rmt sender.
     * @param dst destination.
     * @param titulo DOCUMENT ME!
     * @param msg text of the message.
     */
    void enviar(String rmt, String dst, String titulo, String msg) {
        String response;
        BufferedReader in;
        DataOutputStream out;
        Socket pipe;

        try {
            pipe = new Socket(mailhost, PORT_SMTP);
            pipe.setSoTimeout(10000);

            in = new BufferedReader(new InputStreamReader(pipe.getInputStream()));
            out = new DataOutputStream(pipe.getOutputStream());

            response = in.readLine();
            System.out.println(response);

            out.writeBytes("HELO " + HOST_SENDER_SMTP);
            out.write(13);
            out.write(10);
            System.out.println("HELO " + HOST_SENDER_SMTP);

            response = in.readLine();
            System.out.println(response);

            out.writeBytes("MAIL FROM:<" + rmt + ">" + "");
            out.write(13);
            out.write(10);
            System.out.println("MAIL FROM:<" + rmt + ">");
            response = in.readLine();
            System.out.println(response);

            out.writeBytes("RCPT TO:<" + dst + ">");
            out.write(13);
            out.write(10);
            System.out.println("RCPT TO:<" + dst + ">");
            response = in.readLine();
            System.out.println(response);

            out.writeBytes("DATA");
            out.write(13);
            out.write(10);
            System.out.println("DATA");
            out.write(13);
            out.write(10);

            out.writeBytes("Date: " + new java.util.Date());
            out.write(13);
            out.write(10);
            out.writeBytes("From: <" + rmt + ">");
            out.write(13);
            out.write(10);
            out.writeBytes("To: <" + dst + ">");
            out.write(13);
            out.write(10);
            out.writeBytes("Subject: " + titulo);
            out.write(13);
            out.write(10);
            out.write(13);
            out.write(10);

            out.writeBytes(msg);

            out.write(13);
            out.write(10);
            out.writeBytes(".");
            out.write(13);
            out.write(10);
            System.out.println("QUIT");
            out.writeBytes("QUIT");
            out.write(13);
            out.write(10);

            pipe.close();
        } catch (Exception e) {
            System.out.println("SMTPClient: " + e);
        }
    }
}
