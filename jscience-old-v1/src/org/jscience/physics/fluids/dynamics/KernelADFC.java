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

/*
 * KernelADFC.java
 *
 * Created on 27 of julio of 2001, 11:45
 */
package org.jscience.physics.fluids.dynamics;

import org.jscience.physics.fluids.dynamics.gui.SwingEIS;
import org.jscience.physics.fluids.dynamics.http.ServerHTTP;
import org.jscience.physics.fluids.dynamics.mesh.LoadMesh;
import org.jscience.physics.fluids.dynamics.navierstokes.InputProfile;
import org.jscience.physics.fluids.dynamics.navierstokes.NavierStokes;
import org.jscience.physics.fluids.dynamics.util.SMTPClient;


/**
 * DOCUMENT ME!
 *
 * @author Alejandro "balrog" Rodriguez Gallego
 */
public class KernelADFC extends java.lang.Object {
    /** DOCUMENT ME! */
    private KernelADFCConfiguration config;

    /** DOCUMENT ME! */
    private InputProfile perfilEntrada;

    /** DOCUMENT ME! */
    private ServerHTTP httpServer;

    /** DOCUMENT ME! */
    private String logHTML;

    /** DOCUMENT ME! */
    private SMTPClient notificador;

    /** DOCUMENT ME! */
    private ProtocoleEIS eis;

/**
     * Creates new KernelADFC
     */
    public KernelADFC() {
        System.out.println("--- Loading KernelADFC v2.0 ---");
        httpServer = null;
        notificador = null;
        logHTML = new String();
    }

    /**
     * DOCUMENT ME!
     */
    public void finishHTTPServer() {
        if (httpServer != null) {
            httpServer.terminate();
        }

        httpServer = null;

        out("<B>ServerHTTP:</B> closed");
    }

    /**
     * DOCUMENT ME!
     */
    public void initiateEIS() {
        if (config.gui) {
            eis = new SwingEIS(this);
        } else {
            eis = new ConsoleEIS(this);
        }

        eis.initiate();

        out(
            "<U><FONT COLOR=#000000 SIZE=5><B>Kernel ADFC</FONT></U><FONT COLOR=#800000 SIZE=5><SUP>2</SUP></B></FONT>");
        out("<B>Kernel revision:</B> " + CompiledData.REVISION + " (" +
            CompiledData.DATE + ")");
        out(
            "<B>Web:</B><FONT COLOR=#0000FF><U>http://www.dfc.icai.upco.es/invest/adfc/adfc.html</U></FONT>");
        out(
            "Alejandro Rodriguez Gallego <FONT COLOR=#0000FF><U>&lt;balrog@amena.com&gt;</U></FONT>");
        out(
            "Leo Gonzalez Gutierrez <FONT COLOR=#0000FF><U>&lt;leo.gonzalez@iit.upco.es&gt;</U></FONT><P>");
        out("<B>KernelADFC:</B> Loaded and linked to the GUI.<HR><P>");
    }

    /**
     * DOCUMENT ME!
     *
     * @param port DOCUMENT ME!
     * @param soloLocal DOCUMENT ME!
     */
    public void initiateHTTPServer(int port, boolean soloLocal) {
        if (httpServer != null) {
            httpServer.terminate();
        }

        httpServer = new ServerHTTP(this);
        httpServer.setPuertoHTTP(port);
        httpServer.setSoloLocal(soloLocal);
        httpServer.inicia();
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    public void loadMeshGUI(String file) {
        config.analizeGidPrbFile(file);
        config.meshName = file.substring(0, file.lastIndexOf(".prb"));
        out(config.meshName);

        LoadMesh loadedMesh = new LoadMesh(this);
        loadedMesh.loadGidDataFile(config.meshName);

        /*
         if(ventana != null)
                {
                    ventana.activateMenuItemLoad(false);
                    ventana.activateMenuItemIniciar(true);
                    ventana.activateMenuItemDetener(false);
                }
         */
        if (!config.plugin) {
            out("Para lanzar the solver seleccione  <B>Solver -> Iniciar</B>");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param account DOCUMENT ME!
     * @param host DOCUMENT ME!
     * @param intervalos DOCUMENT ME!
     * @param valorIntervalo DOCUMENT ME!
     * @param fin DOCUMENT ME!
     */
    public void configureNotifications(String account, String host,
        boolean intervalos, int valorIntervalo, boolean fin) {
        config.accountSMTP = account;
        config.hostSMTP = host;
        config.notifyIntervalos = intervalos;
        config.notificationInterval = valorIntervalo;
        config.notifyFin = fin;

        if (fin || intervalos) {
            notificador = new SMTPClient(this);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public KernelADFCConfiguration getConfiguration() {
        return config;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public InputProfile getInputProfile() {
        return perfilEntrada;
    }

    /**
     * DOCUMENT ME!
     */
    public void initiateNavierStokes() {
        long startTime = System.currentTimeMillis();

        out("Solver activo: <B>Navier-Stokes</B>");

        /*
        out("Generando points observables semicircunferencia superior");
        int N = 90;
        config.observerCoordinateX = new double[N];
        config.observerCoordinateY = new double[N];
        for(int j=0; j < N; j++)
        {
             config.observerCoordinateX[j] = -0.505 * Math.cos((double) j * Math.PI / N);
             config.observerCoordinateY[j] = +0.505 * Math.sin((double) j * Math.PI / N);
        }
        */
        perfilEntrada = new InputProfile(this);

        NavierStokes ns = new NavierStokes(this);

        ns.execute();

        System.out.println("KernelADFC: hecho in " +
            ((System.currentTimeMillis() - startTime) / 1000.0) + " segs. ");

        if (config.notifyFin && (notificador != null)) {
            notificador.enviar("Final simulacion", "La simulacion ha ended.");
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void initiatePoissonLineal() {
        /*
                long startTime = System.currentTimeMillis();
        
                out("<FONT COLOR=#800000 SIZE=4><U>Kernel in modo <B>plugin automatico</B></U></FONT>");
                out("Solver activo: <B>Poisson/Laplace</B>");
                LoadMesh loads= new LoadMesh(this);
                carga.cargaFicheroGidDat(config.nameMesh);
        
                MeshNavierStokes mesh = new MeshNavierStokes(this);
                Poisson pElip = new Poisson(this, mesh.getMatrixRigidez(), mesh.getMatrixMasas(), mesh.nDirichletl, mesh.vDirichletl);
        
                double[][] coordl = mesh.getCoordinates();
                double[] f = new double[coordl[0].length];
        
        
        
                for(int i=0; i<f.length; i++)
                {
                    EvaluarExpresion ev = new EvaluarExpresion("cos(pi*(x))*cos(pi*(y))*cos(pi*(z))*3*pi*pi");
                    f[i] = ev.result(coordl[0][i],
                    coordl[1][i],coordl[2][i]);
                }
        
                pElip.setF(f);
                pElip.calcula();
        
                double[] r = pElip.getSolucion();
        
                String dataFile = config.nameMesh;
        
                // Preparando salida
                try {
                    PrintWriter pw =  new PrintWriter(new FileWriter(
                    dataFile+".flavia.res"));
        
                    double[] rint = mesh.calcularCampoInterpolado(r);
                    pw.println("POISSON_RES              1   0    1    1    1");
                    for(int i=0; i<rint.length; i++)
                        pw.println((i+1)+"\t"+rint[i]);
        
                    double[] fint = mesh.calcularCampoInterpolado(f);
                    pw.println("POISSON_F              1   0    1    1    1");
                    for(int i=0; i<fint.length; i++)
                        pw.println((i+1)+"\t"+fint[i]);
        
                    pw.close();
                } catch(Exception ex)
                { ex.printStackTrace(); }
        
                System.out.println("KernelADFC: hecho in "+
                (System.currentTimeMillis()-startTime)/1000.0+" segs. ");
        
                out("<FONT SIZE=4>Salvado y cerrado</FONT><HR>");
                setProgresoCalculos(100);
         */
    }

    /**
     * DOCUMENT ME!
     */
    public void initiatePoisson() {
        /*
         iniciarCaracteristicas();
        
             System.exit(0);
        
             long startTime = System.currentTimeMillis();
        
             out("<FONT COLOR=#800000 SIZE=4><U>Kernel in modo <B>plugin automatico</B></U></FONT>");
             out("Solver activo: <B>Poisson/Laplace</B>");
             LoadMesh loads= new LoadMesh(this);
             carga.cargaFicheroGidDat(config.nameMesh);
        
             MeshNavierStokes mesh = new MeshNavierStokes(this);
             Poisson pElip = new Poisson(this, mesh.getMatrixRigidezQuad(), mesh.getMatrixMasasQuad(), mesh.nDirichletq, mesh.vDirichletq);
        
             double[][] coordq = mesh.getCoordinatesQuad();
             double[] f = new double[coordq[0].length];
        
        
        
             for(int i=0; i<f.length; i++)
             {
                 EvaluarExpresion ev = new EvaluarExpresion("-cos(pi*(x))*cos(pi*(y))*cos(pi*(z))*3*pi*pi");
                 f[i] = ev.result(coordq[0][i],
                 coordq[1][i],coordq[2][i]);
                 System.out.println(" "+f[i]);
             }
        
             pElip.setF(f);
             pElip.calcula();
        
             double[] r = pElip.getSolucion();
        
             String dataFile = config.nameMesh;
        
             // Preparando salida
             try {
                 PrintWriter pw =  new PrintWriter(new FileWriter(
                 dataFile+".flavia.res"));
        
        
                 pw.println("POISSON_RES              1   0    1    1    1");
                 for(int i=0; i<r.length; i++)
                     pw.println((i+1)+"\t"+r[i]);
        
                 pw.println("POISSON_F              1   0    1    1    1");
                 for(int i=0; i<f.length; i++)
                     pw.println((i+1)+"\t"+f[i]);
        
                 pw.close();
             } catch(Exception ex)
             { ex.printStackTrace(); }
        
             System.out.println("KernelADFC: hecho in "+
             (System.currentTimeMillis()-startTime)/1000.0+" segs. ");
        
             out("<FONT SIZE=4>Salvado y cerrado</FONT><HR>");
             setProgresoCalculos(100);
         */
    }

    /*
        public void iniciarCaracteristicas()
        {
            long startTime = System.currentTimeMillis();
    
            out("<FONT COLOR=#800000 SIZE=4><U>Kernel in modo <B>plugin automatico</B></U></FONT>");
            out("Solver activo: <B>Caracteristicas</B>");
            LoadMesh loads= new LoadMesh(this);
            carga.cargaFicheroGidDat(config.nameMesh);
    
            MeshNavierStokes mesh = new MeshNavierStokes(this);
    
            double[][] coordq = mesh.getCoordinatesQuad();
    
            double[][] v0 = new double[2][coordq[0].length];
            double[][] c  = new double[2][coordq[0].length];
            // double[][] v00 = new double[3][coordq[0].length];
    
            for(int j=0; j<v0[0].length; j++)
            {
                v0[0][j] = 1;
                v0[1][j] = 0;
            }
            for(int j=0; j<v0[0].length; j++)
            {
                double dx = coordq[0][j] - 0.5;
                double dy = coordq[1][j];
    
                double d = Math.sqrt(dx*dx + dy*dy);
                if(d < .25)
                    c[0][j] = c[1][j] = 1-4*d;
            }
    
            for(int j=0; j<v0[0].length; j++)
            {
    
                double dx = coordq[0][j] - 1;
                double dy = coordq[1][j];
    
    
                double d = Math.sqrt(dx*dx + dy*dy);
                if(d < .25)
                    c[0][j] = c[1][j] = 0.5-2*d;
            }
    
            double DELTA_T=0.05;
    
            CaracteristicasCuad caracteristicas =
            new CaracteristicasQuad(this, mesh, config.longitudPasoTiempo, false);
            caracteristicas.initializaCaches();
    
    
            String dataFile = config.nameMesh;
    
            // Preparando salida
            try {
                PrintWriter pw =  new PrintWriter(new FileWriter(
                dataFile+".flavia.res"));
    
                for(int paso=0; paso<config.stepsTiempo; paso++)
                {
                    caracteristicas.setCampoVelocidades(v0[0],v0[1],v0[0],v0[1]);
                    caracteristicas.calcular(c[0],c[1], null, null);
    
                    // Velocidades
                    pw.println("CAR              1   "+
                    (paso*DELTA_T)+"    2    1    1");
                    pw.println("CAR_X");
                    pw.println("CAR_Y");
                    pw.println("CAR_Z");
    
                    for(int i=0; i<c[0].length; i++)
                    {
                        // pw.println((i+1)+"       "+valx +"      " +valy);
                        pw.println((i+1)+"\t"+0.0 +"\t" +0.0+"\t"+c[0][i]);
    
                    }
                }
                pw.close();
            } catch(Exception ex)
            { ex.printStackTrace(); }
    
            System.out.println("KernelADFC: hecho in "+
            (System.currentTimeMillis()-startTime)/1000.0+" segs. ");
    
            out("<FONT SIZE=4>Salvado y cerrado</FONT><HR>");
            setProgresoCalculos(100);
    
        }
    */
    public void initiateDerivative() {
        /*
        long startTime = System.currentTimeMillis();
        
        out("<FONT COLOR=#800000 SIZE=4><U>Kernel in modo <B>plugin automatico</B></U></FONT>");
        out("Solver activo: <B>Derivada</B>");
        LoadMesh loads= new LoadMesh(this);
        carga.cargaFicheroGidDat(config.nameMesh);
        
        MeshNavierStokes mesh = new MeshNavierStokes(this);
        
        // Inicializamos the system of ecuations
        GCCholeskyImpl gcPVel = new GCCholeskyImpl(this);
        gcPVel.setMatrixCoeficientes(mesh.getMatrixMasas());
        Matrix mdivz = mesh.getMatrixDivergenceZ();
        
        double[][] coordq = mesh.getCoordinatesQuad();
        double[] f = new double[coordq[0].length];
        
        
        
        for(int i=0; i<f.length; i++)
        {
            EvaluarExpresion ev = new EvaluarExpresion("cos(pi*(x-0.5))*cos(pi*(y-0.5))*cos(pi*(z-0.5))*3*pi*pi");
            f[i] = ev.result(coordq[0][i],
            coordq[1][i],coordq[2][i]);
        }
        
        Matrix mmasl = mesh.getMatrixMasas();
        gcPVel.setB(mdivz.multiply(f,mmasl.ipos.length-1));
        gcPVel.resuelve();
        double r[] = gcPVel.getSolucion();
        
        
        
        
        
        String dataFile = config.nameMesh;
        
        // Preparando salida
        try {
            PrintWriter pw =  new PrintWriter(new FileWriter(
            dataFile+".flavia.res"));
        
            double[] rint = mesh.calcularCampoInterpolado(r);
            pw.println("POISSON_RES              1   0    1    1    1");
            for(int i=0; i<rint.length; i++)
                pw.println((i+1)+"\t"+rint[i]);
        
            pw.println("POISSON_F              1   0    1    1    1");
            for(int i=0; i<f.length; i++)
                pw.println((i+1)+"\t"+f[i]);
        
            pw.close();
        } catch(Exception ex)
        { ex.printStackTrace(); }
        
        System.out.println("KernelADFC: hecho in "+
        (System.currentTimeMillis()-startTime)/1000.0+" segs. ");
        
        out("<FONT SIZE=4>Salvado y cerrado</FONT><HR>");
        setProgresoCalculos(100);
         */
    }

    /**
     * DOCUMENT ME!
     */
    public void iniciarSolver() {
        /*
         if(ventana != null)
                {
                    ventana.activateMenuItemLoad(false);
                    ventana.activateMenuItemIniciar(false);
                    ventana.activateMenuItemDetener(true);
        
                }
         */

        /*
        
                 perfilEntrada = new PerfilEntrada(this);
               iniciarCaracteristicas();
               System.exit(0);
        */
        if (config.solver == KernelADFCConfiguration.NAVIERSTOKES) {
            initiateNavierStokes();
        } else if (config.solver == KernelADFCConfiguration.POISSON) {
            initiatePoisson();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void newWarningDialog(String text) {
        eis.warning(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void newErrorFatalDialog(String text) {
        eis.error(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean continueSerializedSimulation() {
        return eis.continueSerializedSimulation();
    }

    /**
     * DOCUMENT ME!
     *
     * @param html DOCUMENT ME!
     */
    public void out(String html) {
        eis.outHTML(html);

        logHTML = logHTML + html + "<BR>";

        if (httpServer != null) {
            httpServer.setHTMLString(logHTML);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ckadfc DOCUMENT ME!
     */
    public void setConfiguration(KernelADFCConfiguration ckadfc) {
        config = ckadfc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setEndingTime(String str) {
        eis.setEndingTime(str);
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setStartupTime(String str) {
        eis.setStartupTime(str);
    }

    /**
     * DOCUMENT ME!
     *
     * @param step DOCUMENT ME!
     * @param iter DOCUMENT ME!
     */
    public void setActualStep(int step, int iter) {
        eis.setActualStep(step, iter);

        if (config.notifyIntervalos &&
                ((step % config.notificationInterval) == 0)) {
            if (notificador != null) {
                int porcien = (int) ((step * 100) / config.stepsTime);
                notificador.enviar("Completado " + porcien + "%",
                    "Completado intervalo.");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setCalculationProgress(int p) {
        eis.setCalculationProgress(p);
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setLoadProgress(int p) {
        eis.setLoadProgress(p);
    }
}
