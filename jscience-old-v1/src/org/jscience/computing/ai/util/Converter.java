/*
 * Java Robotics Library (JRL) Copyright (c) 2004, Levent Bayindir, All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution. Neither the name of the odejava nor the
 * names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jscience.computing.ai.util;

/**
 * Utility class for conversions
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public class Converter {
    /**
     * Returns string representation of the given bit.
     *
     * @param bit boolean value of the given bit
     *
     * @return if bit is true then 1 is returned, else 0 is returned
     */
    public static String booleanToString(boolean bit) {
        if (bit) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * Returns string representation of the given array of bits. Each
     * bit is converted using booleanToString(...) method and concatenated.
     *
     * @param booleanArray boolean array of the given bits
     *
     * @return string representation of given array of bits
     */
    public static String booleanArrayToString(boolean[] booleanArray) {
        String tmpString = new String();

        for (int i = 0; i < booleanArray.length; i++) {
            tmpString = tmpString + booleanToString(booleanArray[i]);
        }

        return tmpString;
    }

    /**
     * Returns int value of the given array of bits.
     * booleanArrayToString(...) method is called for the given array of bits,
     * then the returned value is converted to int value
     *
     * @param booleanArray boolean array of the given bits
     *
     * @return int value of the given array of bits
     */
    public static int booleanArrayToInt(boolean[] booleanArray) {
        return binaryString2Int(booleanArrayToString(booleanArray));
    }

    /**
     * Returns integer value of given bits
     *
     * @param binary string representation of given bits
     *
     * @return integer value of given string
     */
    public static int binaryString2Int(String binary) {
        return Integer.valueOf(binary, 2).intValue();
    }
}
