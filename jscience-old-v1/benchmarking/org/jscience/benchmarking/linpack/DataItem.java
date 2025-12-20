package org.jscience.benchmarking.linpack;

//code from http://www.netlib.org/benchmark/linpackjava/
//reused under GPL with premission from Jack Dongarra

class DataItem {
  double mflops;
  String label;
  int itemtype;
  String info;

  DataItem (double mflops, int itemtype, String info, String label) {
    this.mflops = mflops;
    this.label = label;
    this.itemtype = itemtype;
    this.info = info;
  }
}
