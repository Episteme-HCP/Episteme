/*****************************************************************************
Interface org.jscience.benchmarking.bench.Target
 *****************************************************************************/
package org.jscience.benchmarking.bench;

/** 
Interface for a Benchmark Target.
Code to be measured by the Bench framework should provide a class implementing
this interface.  Place the code to be measured in the execute method.

@author Bruce R. Miller (bruce.miller@nist.gov)
@author Contribution of the National Institute of Standards and Technology,
@author not subject to copyright.
*/

//code under public domain

public interface Target {
  /** The code to be measured is placed in this method.
    * @return null lets org.jscience.benchmarking.bench.Bench handle the timings.
    * Otherwise, return an array containing the one or more measured values.
    * @see org.jscience.benchmarking.bench.Bench [start|stop|reset]Timer methods for measurement tools.
    */    
  public double[] execute(Bench bench) throws Exception;
}
