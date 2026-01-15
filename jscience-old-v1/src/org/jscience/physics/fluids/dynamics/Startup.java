/*
 * Program : ADFC v2.0
 * Class : org.jscience.fluids.Inicio
 *         Start-up del program y modo plug-in.
 * Author : Alejandro "balrog" Rodriguez Gallego
 * Date  : 27/07/2001
 */
package org.jscience.physics.fluids.dynamics;

import org.jscience.physics.fluids.dynamics.mesh.LoadMesh;


/**
 * Short startup code
 */
public class Startup {
    /**
     * Function main
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("ADFC v" + CompiledData.VERSION +
            " Proyect --- Java Solver for Navier-Stokes 2D\n" +
            "   Authors : Alejandro Rodriguez Gallego <balrog@amena.com>\n" +
            "             Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>\n" +
            "       Web : http://www.dfc.icai.upco.es/invest/adfc/adfc.html\n" +
            "  Revision : " + CompiledData.REVISION + " (" + CompiledData.DATE +
            ")\n");

        String vmName = System.getProperty("java.vm.name");

        if (vmName != null) {
            if (vmName.indexOf("HotSpot") >= 0) {
                System.out.println("Check: HotSpot - Ok! (" + vmName + ")\n");
            } else {
                System.out.println("Check: HotSpot - NOT INSTALLED (" + vmName +
                    ")\n");
            }
        }

        if (args.length == 0) {
            System.out.println("* invoction form\n" +
                "  - archive JAR      :  java -jar adfc.jar [<file>] [<params> ... ]\n" +
                "  - executable Linux :  adfc-linux [<file>] [ <params> ...]\n" +
                "\n" + "* opctions\n" +
                "\t -fieldini   <x> <y>     initial value of the speed field  [ 0 0 ]\n" +
                "\t -deltaslip  <num>       distance of zeroing limit layer [0.005]\n" +
                "\t -deltat     <num>       seconds for time step [0.1]\n" +
                "\t -felec      <num>       module of the superficial electrical force [1.0]\n" +
                "\t -filedl     <file>      file to store Drag/Lift output\n" +
                "\t -gid        <dir>       directory GiD with the problem.\n" +
                "\t -nogui                  execute without graphical user interface.\n" +
                "\t -observ     <x> <y>     point (x,y) will be observed\n" +
                "\t -steps      <num>       time steps [2000]\n" +
                "\t -stepfile  <num>        steps to save results [10]\n" +
                "\t -plugin                 plugin mode for GiD\n" +
                "\t -reynolds   <num>       Reynolds number [100]\n" +
                "\t -smagorinsky            turbulence model of Smagorinsky [true]\n" +
                "\n");

            // System.exit(0);
        }

        // Nuevo Kernel
        KernelADFC kernel = new KernelADFC();
        KernelADFCConfiguration ckadfc = new KernelADFCConfiguration();
        ckadfc.analizeArguments(args);
        kernel.setConfiguration(ckadfc);

        kernel.initiateEIS();

        if (ckadfc.plugin) {
            kernel.out(
                "<FONT COLOR=#800000 SIZE=4><U>Kernel in <B>automated plugin</B>  mode</U></FONT>");

            LoadMesh loadedMesh = new LoadMesh(kernel);
            loadedMesh.loadGidDataFile(ckadfc.meshName);

            kernel.iniciarSolver();
        } else {
            kernel.out(
                "<FONT COLOR=#800000 SIZE=4><U>Kernel in <B>interactive</B> mode</U></FONT>");
            kernel.out("To load a mesh select  <B>Load -> Load Problem</B>");
        }
    }
}
