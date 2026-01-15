//////////////////////license & copyright header///////////////////////
//                                                                   //
//                Copyright (c) 1999 by Michel Van den Bergh         //
//                                                                   //
// This library is free software; you can redistribute it and/or     //
// modify it under the terms of the GNU Lesser General Public        //
// License as published by the Free Software Foundation; either      //
// version 2 of the License, or (at your option) any later version.  //
//                                                                   //
// This library is distributed in the hope that it will be useful,   //
// but WITHOUT ANY WARRANTY; without even the implied warranty of    //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU //
// Lesser General Public License for more details.                   //
//                                                                   //
// You should have received a copy of the GNU Lesser General Public  //
// License along with this library; if not, write to the             //
// Free Software Foundation, Inc., 59 Temple Place, Suite 330,       //
// Boston, MA  02111-1307  USA, or contact the author:               //
//                                                                   //
//                  Michel Van den Bergh  <vdbergh@luc.ac.be>        //
//                                                                   //
////////////////////end license & copyright header/////////////////////
package org.jscience.net.ntp;

import java.util.Date;


/**
 * This is an abstract class encapsulating the method for setting the
 * system time on the local system. Setting the local time can  be
 * accomplished by some external process of shell-script, or more likely by
 * using the 'Java Native Interface'.
 *
 * @author Michel Van den Bergh
 * @version 1.0
 */
public abstract class TimeManager {
    /**
     * Set the local time to a given date.
     *
     * @param d DOCUMENT ME!
     */
    public abstract void setTime(Date d);

    /**
     * Add a given offset to the local time.
     *
     * @param offset DOCUMENT ME!
     */
    public void setTime(long offset) {
        setTime(new Date(System.currentTimeMillis() + offset));
    }

    /**
     * This method could be used to terminate an external helper
     * process. The default implementation does nothing.
     *
     * @throws Exception We allow this method to throw arbitrary exceptions.
     */
    public void dispose() throws Exception {
    }

    /**
     * The default implementation of this method just invokes
     * dispose().
     */
    public void finalize() {
        try {
            dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * The factory method for obtaining an instance of 'TimeManager'.
     * The default implementation returns an instance of 'LocalTimeManager'.
     * More sophisticated implementations should check the name of the current
     * operating system and act accordingly.
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception In the default implementation this method throws an
     *         exception if the class 'LocalTimeManager' is not present, or
     *         fails to load.
     *
     * @see LocalTimeManager
     */
    public static TimeManager getInstance() throws Exception {
        Class c = Class.forName("org.jscience.net.ntp.LocalTimeManager");

        return (((TimeManager) c.newInstance()));
    }
}
