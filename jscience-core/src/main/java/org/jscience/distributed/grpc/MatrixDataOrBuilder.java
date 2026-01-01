/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.distributed.grpc;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface MatrixDataOrBuilder extends
    // @@protoc_insertion_point(interface_extends:org.jscience.computing.remote.MatrixData)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 rows = 1;</code>
   * @return The rows.
   */
  int getRows();

  /**
   * <code>int32 cols = 2;</code>
   * @return The cols.
   */
  int getCols();

  /**
   * <pre>
   * Row-major order
   * </pre>
   *
   * <code>repeated double data = 3;</code>
   * @return A list containing the data.
   */
  java.util.List<java.lang.Double> getDataList();
  /**
   * <pre>
   * Row-major order
   * </pre>
   *
   * <code>repeated double data = 3;</code>
   * @return The count of data.
   */
  int getDataCount();
  /**
   * <pre>
   * Row-major order
   * </pre>
   *
   * <code>repeated double data = 3;</code>
   * @param index The index of the element to return.
   * @return The data at the given index.
   */
  double getData(int index);
}


