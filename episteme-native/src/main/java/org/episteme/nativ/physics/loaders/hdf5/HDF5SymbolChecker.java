package org.episteme.nativ.physics.loaders.hdf5;

import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;

public class HDF5SymbolChecker {
    public static void main(String[] args) {
        SymbolLookup lookup = SymbolLookup.libraryLookup("hdf5", Arena.global());
        String[] symbols = {
            "H5Pcreate", "H5Pset_chunk", "H5Pset_deflate", "H5Pset_szip", "H5Pclose",
            "H5Zfilter_avail"
        };
        for (String s : symbols) {
            System.out.println(s + ": " + lookup.find(s).isPresent());
        }
    }
}
