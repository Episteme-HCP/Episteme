/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The class CGI provides a convenient way to post parameters to a
 * web-based CGI script. <p>
 * Here is a complete code example that assumes that the script specified
 * with the given URL takes two input values: 'name' and 'text':
 * <pre>
 * try {
 *     URL url = new URL("http://myserver/cgi-bin/myscript.cgi");
 *     CGI cgi = new CGI(url);
 *     cgi.setHeader("cookie", "my cookie value");
 *     HashMap params = new HashMap();
 *     params.put("name", "Me");
 *     params.put("text", "first line\nsecond line\n<mailto:me@there>");
 *     int response = cgi.post(params);
 *     if (response != 200) {
 *         System.out.println("something went wrong");
 *     }
 *     // see the complete response the script sent back
 *     System.out.println(cgi.getResponse());
 * } catch (IOException e) { e.printStackTrace(); }
 * </pre>
 * Note that you do not need to 'escape' the values, as this is done
 * by the <code>post(Map)</code> method.
 *
 * @author Holger Antelmann
 * @since 5/5/2002
 */
public class CGI {
    static String lineBreak = System.getProperty("line.separator");

    URL url;
    String responseString = null;
    LinkedHashMap<String, String> header = new LinkedHashMap<String, String>();

    /**
     * requires a URL based on the HTTP protocol specifying the script
     * you are posting to
     *
     * @throws UnsupportedOperationException if the protocol is not HTTP
     */
    public CGI(URL cgiUrl) throws UnsupportedOperationException {
        this.url = cgiUrl;
        if (!"http".equals(url.getProtocol())) {
            throw new UnsupportedOperationException("not a supported protocol: " + url.getProtocol());
        }
    }

    /**
     * can be used to retrieve the full text of the last server response
     * after the <code>post(Map)</code> method was called.
     *
     * @see #post(Map)
     */
    public synchronized String getResponse() {
        return responseString;
    }

    /**
     * allows to add additional header information to the request or
     * overwrite existing ones.
     * The order of how headers are added is maintained as the Map
     * used is a LinkedHashMap.
     *
     * @see java.net.URLConnection#setRequestProperty(String,String)
     */
    public synchronized void setHeader(String key, String value) {
        header.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return header;
    }

    public synchronized void clearHeaders() {
        header.clear();
    }

    /**
     * convenience method to post a single parameter/value pair
     */
    public int post(String parameter, String value) throws IOException {
        return post(new String[]{parameter}, new String[]{value});
    }

    /**
     * posts the given parameter/value pairs to the embedded URL in the exact
     * specified order. <p>
     * The value entries must not be encoded; this is done within this method.
     * To retrieve the full text returned by the server after you called
     * this method, use <code>getResponse()</code>
     *
     * @return the response code from the server
     * @see #getResponse()
     */
    public int post(String[] param, String[] value) throws IOException {
        String post = null;
        for (int i = 0; (i < param.length) && (i < value.length); i++) {
            if (post == null) {
                post = param[i] + "=" + URLEncoder.encode(value[i], "UTF-8");
            } else {
                post += "&" + param[i] + "=" + URLEncoder.encode(value[i], "UTF-8");
            }
        }
        return post(post);
    }

    /**
     * posts the given parameters to the instance's URL via POST.
     * The parameters map each input name to its value.
     * Both, key and value, are used as strings; you must not 'escape' the
     * strings (replacing spaces, \"&\"'s etc.) as this is automatically done.
     * The method takes an Object-to-Object Map, so that Properties may be
     * passed as parameter.
     * To retrieve the full text returned by the server after you called
     * this method, use <code>getResponse()</code>
     *
     * @return the response code from the server
     * @see #getResponse()
     */
    public int post(Map<Object, Object> parameters) throws IOException {
        Iterator i = parameters.keySet().iterator();
        String post = null;
        while (i.hasNext()) {
            String key = i.next().toString();
            String value = URLEncoder.encode(parameters.get(key).toString(), "UTF-8");
            if (post == null) {
                post = key + "=" + value;
            } else {
                post += "&" + key + "=" + value;
            }
        }
        return post(post);
    }

    /**
     * posts the encoded string directly to the URL per HTTP POST.
     * To retrieve the full text returned by the server after you called
     * this method, use <code>getResponse()</code>
     *
     * @return the response code from the server
     * @see #getResponse()
     */
    public synchronized int post(String content) throws IOException {
        responseString = null;
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setUseCaches(false);
        con.setDoOutput(true);
        Iterator i = header.keySet().iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            con.setRequestProperty(key, (String) header.get(key));
        }
        con.connect();
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(content);
        out.flush();
        out.close();
        BufferedReader in;
        int responseCode = con.getResponseCode();
        if (responseCode == 200) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String line;
        responseString = "";
        while ((line = in.readLine()) != null) {
            responseString += line + lineBreak;
        }
        con.disconnect();
        return responseCode;
    }

}
