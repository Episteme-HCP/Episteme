/*
 * Program : ADFC�
 * Class : org.jscience.fluids.gui2.SwingEIS
 *         Entorno grafico basado in Swing.
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 16/11/2001
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
 */
package org.jscience.physics.fluids.dynamics.gui;

/**
 */
import org.jscience.physics.fluids.dynamics.KernelADFC;
import org.jscience.physics.fluids.dynamics.ProtocoleEIS;
import org.jscience.physics.fluids.dynamics.WindowADFC;

import java.awt.*;

import java.io.File;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SwingEIS extends javax.swing.JFrame implements ProtocoleEIS {
    /** DOCUMENT ME! */
    private KernelADFC kernel;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** DOCUMENT ME! */
    private javax.swing.JMenuBar jMenuBar;

    /** DOCUMENT ME! */
    private javax.swing.JMenu menuFile;

    /** DOCUMENT ME! */
    private javax.swing.JMenuItem jMenuItemLoadProblem;

    /** DOCUMENT ME! */
    private javax.swing.JSeparator jSeparator1;

    /** DOCUMENT ME! */
    private javax.swing.JMenuItem jMenuItemSalir;

    /** DOCUMENT ME! */
    private javax.swing.JMenu menuSolver;

    /** DOCUMENT ME! */
    private javax.swing.JMenuItem jMenuItemInitiate;

    /** DOCUMENT ME! */
    private javax.swing.JScrollPane jScrollPane;

    /** DOCUMENT ME! */
    private javax.swing.JDesktopPane jDesktopPane;

    /** DOCUMENT ME! */
    private javax.swing.JInternalFrame jInternalFrameSolver;

    /** DOCUMENT ME! */
    private org.jscience.physics.fluids.dynamics.gui.JTexturedPanel jTexturedPanel6;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabel1;

    /** DOCUMENT ME! */
    private javax.swing.JProgressBar jProgressBarSolverProgress;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabel2;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabelSolverStep;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabel4;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabelSolverIteration;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabel6;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabelStartDate;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabel8;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabelFinalDate;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabel10;

    /** DOCUMENT ME! */
    private javax.swing.JInternalFrame jInternalFrameHTML;

    /** DOCUMENT ME! */
    private javax.swing.JScrollPane jScrollPane1;

    /** DOCUMENT ME! */
    private org.jscience.physics.fluids.dynamics.gui.PanelHTML panelHTML;

    /** DOCUMENT ME! */
    private javax.swing.JInternalFrame jInternalFrameRed;

    /** DOCUMENT ME! */
    private org.jscience.physics.fluids.dynamics.gui.JTexturedPanel jTexturedPanel1;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabel16;

    /** DOCUMENT ME! */
    private javax.swing.JPanel jPanelWeb;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabel11;

    /** DOCUMENT ME! */
    private javax.swing.JTextField jTextFieldHTTPPort;

    /** DOCUMENT ME! */
    private javax.swing.JCheckBox jCheckBoxActivateHTTServer;

    /** DOCUMENT ME! */
    private javax.swing.JButton jButtonApplyHTTPServer;

    /** DOCUMENT ME! */
    private javax.swing.JCheckBox jCheckBoxOnlyLocal;

    /** DOCUMENT ME! */
    private javax.swing.JPanel jPanel1;

    /** DOCUMENT ME! */
    private javax.swing.JTextField jTextFieldCuentaEmail;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabel3;

    /** DOCUMENT ME! */
    private javax.swing.JLabel jLabel5;

    /** DOCUMENT ME! */
    private javax.swing.JTextField jTextFieldSMTPServer;

    /** DOCUMENT ME! */
    private javax.swing.JCheckBox jCheckBoxIntervalNotification;

    /** DOCUMENT ME! */
    private javax.swing.JSlider jSliderIntervalNotification;

    /** DOCUMENT ME! */
    private javax.swing.JCheckBox jCheckBoxNotifyWhenEnded;

    /** DOCUMENT ME! */
    private javax.swing.JButton jButtonApplyNotifications;

/**
     * Creates new form VentanaPrincipal
     *
     * @param kadfc DOCUMENT ME!
     */
    public SwingEIS(KernelADFC kadfc) {
        kernel = kadfc;

        Facade portada = new Facade(
                "/org.jscience.fluids/recursos/portada-adfc2.gif");

        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
        }

        initComponents();

        Dimension dimMax;
        Dimension dimEIS;
        dimMax = getToolkit().getScreenSize();
        dimEIS = new Dimension((int) (dimMax.getWidth() * 0.75),
                (int) (dimMax.getHeight() * 0.75));

        System.out.println(" " + dimEIS.getWidth() + " " + dimEIS.getHeight());
        setSize(dimEIS);

        setLocation((dimMax.width - dimEIS.width) / 2,
            (dimMax.height - dimEIS.height) / 2);

        portada.setVisible(false);
        portada.dispose();
    }

    /**
     * This method is called from within the constructor to initialize
     * the form. WARNING: Do NOT modify this code. The content of this method
     * is always regenerated by the Form Editor.
     */
    private void initComponents() { //GEN-BEGIN:initComponents
        jMenuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        jMenuItemLoadProblem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItemSalir = new javax.swing.JMenuItem();
        menuSolver = new javax.swing.JMenu();
        jMenuItemInitiate = new javax.swing.JMenuItem();
        jScrollPane = new javax.swing.JScrollPane();
        jDesktopPane = new javax.swing.JDesktopPane();
        jInternalFrameSolver = new javax.swing.JInternalFrame();
        jTexturedPanel6 = new org.jscience.physics.fluids.dynamics.gui.JTexturedPanel();
        jLabel1 = new javax.swing.JLabel();
        jProgressBarSolverProgress = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        jLabelSolverStep = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabelSolverIteration = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelStartDate = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabelFinalDate = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jInternalFrameHTML = new javax.swing.JInternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelHTML = new org.jscience.physics.fluids.dynamics.gui.PanelHTML();
        jInternalFrameRed = new javax.swing.JInternalFrame();
        jTexturedPanel1 = new org.jscience.physics.fluids.dynamics.gui.JTexturedPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanelWeb = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldHTTPPort = new javax.swing.JTextField();
        jCheckBoxActivateHTTServer = new javax.swing.JCheckBox();
        jButtonApplyHTTPServer = new javax.swing.JButton();
        jCheckBoxOnlyLocal = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldCuentaEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldSMTPServer = new javax.swing.JTextField();
        jCheckBoxIntervalNotification = new javax.swing.JCheckBox();
        jSliderIntervalNotification = new javax.swing.JSlider();
        jCheckBoxNotifyWhenEnded = new javax.swing.JCheckBox();
        jButtonApplyNotifications = new javax.swing.JButton();

        menuFile.setText("File");
        menuFile.setFont(new java.awt.Font("Dialog", 0, 12));
        jMenuItemLoadProblem.setFont(new java.awt.Font("Dialog", 0, 12));
        jMenuItemLoadProblem.setText("Load problem...");
        jMenuItemLoadProblem.setIcon(new javax.swing.ImageIcon(
                getClass()
                    .getResource("/org.jscience.fluids/recursos/Open16.gif")));
        jMenuItemLoadProblem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemLoadProblemaActionPerformed(evt);
                }
            });

        menuFile.add(jMenuItemLoadProblem);
        menuFile.add(jSeparator1);
        jMenuItemSalir.setFont(new java.awt.Font("Dialog", 0, 12));
        jMenuItemSalir.setText("Salir");
        jMenuItemSalir.setIcon(new javax.swing.ImageIcon(getClass()
                                                             .getResource("/org.jscience.fluids/recursos/Quit16.gif")));
        jMenuItemSalir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSalirActionPerformed(evt);
                }
            });

        menuFile.add(jMenuItemSalir);
        jMenuBar.add(menuFile);
        menuSolver.setText("Solver");
        menuSolver.setFont(new java.awt.Font("Dialog", 0, 12));
        jMenuItemInitiate.setFont(new java.awt.Font("Dialog", 0, 12));
        jMenuItemInitiate.setText("Iniciar");
        jMenuItemInitiate.setIcon(new javax.swing.ImageIcon(
                getClass()
                    .getResource("/org.jscience.fluids/recursos/Play16.gif")));
        jMenuItemInitiate.setEnabled(false);
        jMenuItemInitiate.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemIniciarActionPerformed(evt);
                }
            });

        menuSolver.add(jMenuItemInitiate);
        jMenuBar.add(menuSolver);

        setTitle("ADFC 2.1");
        addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    exitForm(evt);
                }
            });

        jInternalFrameSolver.setBackground(java.awt.Color.magenta);
        jInternalFrameSolver.setIconifiable(true);
        jInternalFrameSolver.setTitle("Solver");
        jInternalFrameSolver.setVisible(true);
        jTexturedPanel6.setLayout(null);

        jTexturedPanel6.setTextura(
            "/org.jscience.fluids/recursos/fondoMetal.gif");
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setText("Progreso");
        jTexturedPanel6.add(jLabel1);
        jLabel1.setBounds(20, 20, 51, 16);

        jProgressBarSolverProgress.setFont(new java.awt.Font("Dialog", 0, 12));
        jProgressBarSolverProgress.setStringPainted(true);
        jTexturedPanel6.add(jProgressBarSolverProgress);
        jProgressBarSolverProgress.setBounds(90, 20, 170, 21);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText("paso");
        jTexturedPanel6.add(jLabel2);
        jLabel2.setBounds(20, 50, 28, 16);

        jLabelSolverStep.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabelSolverStep.setText("?");
        jTexturedPanel6.add(jLabelSolverStep);
        jLabelSolverStep.setBounds(60, 50, 50, 16);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel4.setText("iteraci\u00f3n");
        jTexturedPanel6.add(jLabel4);
        jLabel4.setBounds(120, 50, 49, 16);

        jLabelSolverIteration.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabelSolverIteration.setText("?");
        jTexturedPanel6.add(jLabelSolverIteration);
        jLabelSolverIteration.setBounds(180, 50, 40, 16);

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel6.setText("Inicio");
        jTexturedPanel6.add(jLabel6);
        jLabel6.setBounds(20, 80, 29, 16);

        jLabelStartDate.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabelStartDate.setText("??:??:??");
        jTexturedPanel6.add(jLabelStartDate);
        jLabelStartDate.setBounds(60, 80, 190, 16);

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel8.setText("Final");
        jTexturedPanel6.add(jLabel8);
        jLabel8.setBounds(20, 110, 26, 16);

        jLabelFinalDate.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabelFinalDate.setText("??:??:??");
        jTexturedPanel6.add(jLabelFinalDate);
        jLabelFinalDate.setBounds(60, 110, 190, 16);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass()
                                                       .getResource("/org.jscience.fluids/recursos/cpu.gif")));
        jTexturedPanel6.add(jLabel10);
        jLabel10.setBounds(240, 70, 48, 48);

        jInternalFrameSolver.getContentPane()
                            .add(jTexturedPanel6, java.awt.BorderLayout.CENTER);

        jDesktopPane.add(jInternalFrameSolver,
            javax.swing.JLayeredPane.DEFAULT_LAYER);
        jInternalFrameSolver.setBounds(380, 30, 310, 160);

        jInternalFrameHTML.setBackground(java.awt.Color.lightGray);
        jInternalFrameHTML.setIconifiable(true);
        jInternalFrameHTML.setMaximizable(true);
        jInternalFrameHTML.setResizable(true);
        jInternalFrameHTML.setTitle("Consola ADFC");
        jInternalFrameHTML.setVisible(true);
        jScrollPane1.setViewportView(panelHTML);

        jInternalFrameHTML.getContentPane()
                          .add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jDesktopPane.add(jInternalFrameHTML,
            javax.swing.JLayeredPane.DEFAULT_LAYER);
        jInternalFrameHTML.setBounds(40, 210, 650, 250);

        jInternalFrameRed.setIconifiable(true);
        jInternalFrameRed.setTitle("Red e Internet");
        jInternalFrameRed.setVisible(true);
        jTexturedPanel1.setLayout(null);

        jTexturedPanel1.setTextura(
            "/org.jscience.fluids/recursos/fondoMetal.gif");
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass()
                                                       .getResource("/org.jscience.fluids/recursos/red.gif")));
        jTexturedPanel1.add(jLabel16);
        jLabel16.setBounds(290, 250, 48, 48);

        jPanelWeb.setLayout(null);

        jPanelWeb.setBorder(new javax.swing.border.TitledBorder(null,
                "server HTTP",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Dialog", 0, 11), java.awt.Color.blue));
        jPanelWeb.setOpaque(false);
        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel11.setText("puerto");
        jPanelWeb.add(jLabel11);
        jLabel11.setBounds(20, 20, 38, 16);

        jTextFieldHTTPPort.setText("8080");
        jPanelWeb.add(jTextFieldHTTPPort);
        jTextFieldHTTPPort.setBounds(70, 20, 50, 20);

        jCheckBoxActivateHTTServer.setFont(new java.awt.Font("Dialog", 0, 12));
        jCheckBoxActivateHTTServer.setText("activate server");
        jCheckBoxActivateHTTServer.setOpaque(false);
        jPanelWeb.add(jCheckBoxActivateHTTServer);
        jCheckBoxActivateHTTServer.setBounds(20, 50, 114, 24);

        jButtonApplyHTTPServer.setFont(new java.awt.Font("Dialog", 0, 12));
        jButtonApplyHTTPServer.setIcon(new javax.swing.ImageIcon(
                getClass()
                    .getResource("/org.jscience.fluids/recursos/WebComponent16.gif")));
        jButtonApplyHTTPServer.setText("aplicar");
        jButtonApplyHTTPServer.setOpaque(false);
        jButtonApplyHTTPServer.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonAplicarServidorHTTPActionPerformed(evt);
                }
            });

        jPanelWeb.add(jButtonApplyHTTPServer);
        jButtonApplyHTTPServer.setBounds(160, 70, 93, 26);

        jCheckBoxOnlyLocal.setFont(new java.awt.Font("Dialog", 0, 12));
        jCheckBoxOnlyLocal.setText("solo localhost");
        jCheckBoxOnlyLocal.setOpaque(false);
        jPanelWeb.add(jCheckBoxOnlyLocal);
        jCheckBoxOnlyLocal.setBounds(20, 80, 102, 24);

        jTexturedPanel1.add(jPanelWeb);
        jPanelWeb.setBounds(20, 10, 270, 110);

        jPanel1.setLayout(null);

        jPanel1.setBorder(new javax.swing.border.TitledBorder(null,
                "notify by correo",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Dialog", 0, 11), java.awt.Color.blue));
        jPanel1.setOpaque(false);
        jTextFieldCuentaEmail.setText("yo@mihost.es");
        jPanel1.add(jTextFieldCuentaEmail);
        jTextFieldCuentaEmail.setBounds(110, 30, 140, 20);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText("account");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(50, 30, 38, 16);

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setText("server SMTP");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 60, 82, 16);

        jTextFieldSMTPServer.setText("smtp.mihost.es");
        jPanel1.add(jTextFieldSMTPServer);
        jTextFieldSMTPServer.setBounds(110, 60, 140, 20);

        jCheckBoxIntervalNotification.setFont(new java.awt.Font("Dialog", 0, 12));
        jCheckBoxIntervalNotification.setText("cada 25%");
        jCheckBoxIntervalNotification.setOpaque(false);
        jPanel1.add(jCheckBoxIntervalNotification);
        jCheckBoxIntervalNotification.setBounds(20, 90, 81, 24);

        jSliderIntervalNotification.setForeground(java.awt.Color.darkGray);
        jSliderIntervalNotification.setPaintLabels(true);
        jSliderIntervalNotification.setPaintTicks(true);
        jSliderIntervalNotification.setSnapToTicks(true);
        jSliderIntervalNotification.setValue(25);
        jSliderIntervalNotification.setOpaque(false);
        jSliderIntervalNotification.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    jSliderIntervalosNotificacionStateChanged(evt);
                }
            });

        jPanel1.add(jSliderIntervalNotification);
        jSliderIntervalNotification.setBounds(110, 90, 140, 20);

        jCheckBoxNotifyWhenEnded.setFont(new java.awt.Font("Dialog", 0, 12));
        jCheckBoxNotifyWhenEnded.setText("al terminar");
        jCheckBoxNotifyWhenEnded.setOpaque(false);
        jPanel1.add(jCheckBoxNotifyWhenEnded);
        jCheckBoxNotifyWhenEnded.setBounds(20, 120, 88, 24);

        jButtonApplyNotifications.setFont(new java.awt.Font("Dialog", 0, 12));
        jButtonApplyNotifications.setIcon(new javax.swing.ImageIcon(
                getClass()
                    .getResource("/org.jscience.fluids/recursos/SendMail16.gif")));
        jButtonApplyNotifications.setText("aplicar");
        jButtonApplyNotifications.setOpaque(false);
        jButtonApplyNotifications.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButtonAplicarNotificacionesActionPerformed(evt);
                }
            });

        jPanel1.add(jButtonApplyNotifications);
        jButtonApplyNotifications.setBounds(160, 130, 93, 26);

        jTexturedPanel1.add(jPanel1);
        jPanel1.setBounds(20, 130, 270, 170);

        jInternalFrameRed.getContentPane()
                         .add(jTexturedPanel1, java.awt.BorderLayout.CENTER);

        jDesktopPane.add(jInternalFrameRed,
            javax.swing.JLayeredPane.DEFAULT_LAYER);
        jInternalFrameRed.setBounds(10, 10, 350, 340);

        jScrollPane.setViewportView(jDesktopPane);

        getContentPane().add(jScrollPane, java.awt.BorderLayout.CENTER);

        setJMenuBar(jMenuBar);
        pack();
    } //GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    private void jButtonAplicarNotificacionesActionPerformed(
        java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButtonAplicarNotificacionesActionPerformed
        // Add your handling code here:
        kernel.configureNotifications(jTextFieldCuentaEmail.getText(),
            jTextFieldSMTPServer.getText(),
            jCheckBoxIntervalNotification.isSelected(),
            jSliderIntervalNotification.getValue(),
            jCheckBoxNotifyWhenEnded.isSelected());
    } //GEN-LAST:event_jButtonAplicarNotificacionesActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    private void jSliderIntervalosNotificacionStateChanged(
        javax.swing.event.ChangeEvent evt) { //GEN-FIRST:event_jSliderIntervalosNotificacionStateChanged
        // Add your handling code here:
        jCheckBoxIntervalNotification.setText("cada " +
            jSliderIntervalNotification.getValue() + "%");
    } //GEN-LAST:event_jSliderIntervalosNotificacionStateChanged

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    private void jButtonAplicarServidorHTTPActionPerformed(
        java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButtonAplicarServidorHTTPActionPerformed

        // Add your handling code here:
        if (jCheckBoxActivateHTTServer.isSelected()) {
            kernel.initiateHTTPServer(Integer.parseInt(
                    jTextFieldHTTPPort.getText()),
                jCheckBoxOnlyLocal.isSelected());
        } else {
            kernel.finishHTTPServer();
        }
    } //GEN-LAST:event_jButtonAplicarServidorHTTPActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    private void jMenuItemIniciarActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jMenuItemIniciarActionPerformed

        // Add your handling code here:
        new Thread() {
                public void run() {
                    kernel.iniciarSolver();
                }
            }.start();
    } //GEN-LAST:event_jMenuItemIniciarActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    private void jMenuItemSalirActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jMenuItemSalirActionPerformed
        // Add your handling code here:
        exitForm(null);
    } //GEN-LAST:event_jMenuItemSalirActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    private void jMenuItemLoadProblemaActionPerformed(
        java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jMenuItemLoadProblemaActionPerformed

        // Add your handling code here:
        // Add your handling code here:
        final JFileChooser fc = new JFileChooser();

        // Deberia coger solo directorios ".gid"
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }

                    if (f.getAbsolutePath().endsWith(".prb")) {
                        return true;
                    } else {
                        return false;
                    }
                }

                public String getDescription() {
                    return "Problemas GiD (*.prb)";
                }
            });

        fc.setFileHidingEnabled(false);

        int returnVal = fc.showDialog(this, "Load problem...");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // desactivate menu cargar!
            jMenuItemLoadProblem.setEnabled(false);

            new Thread() {
                    public void run() {
                        File file = fc.getSelectedFile();
                        kernel.loadMeshGUI(file.getAbsolutePath());
                        jMenuItemInitiate.setEnabled(true);
                        jMenuItemLoadProblem.setEnabled(false);
                    }
                }.start();
        }
    } //GEN-LAST:event_jMenuItemLoadProblemaActionPerformed

    /**
     * Exit the Application
     *
     * @param evt DOCUMENT ME!
     */
    private void exitForm(java.awt.event.WindowEvent evt) { //GEN-FIRST:event_exitForm
        new QuitDialog(kernel, this, true).show();
    } //GEN-LAST:event_exitForm

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    static public void main(String[] args) {
        new WindowADFC(null).show();
    }

    /**
     * DOCUMENT ME!
     */
    public void initiate() {
        show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setLoadProgress(int p) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param texto DOCUMENT ME!
     */
    public void error(String texto) {
        ErrorFatal err = new ErrorFatal(this, true, texto);
        err.show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param html DOCUMENT ME!
     */
    public void outHTML(String html) {
        panelHTML.out(html);
    }

    /**
     * DOCUMENT ME!
     *
     * @param texto DOCUMENT ME!
     */
    public void warning(String texto) {
        Warning adv = new Warning(this, true, texto);
        adv.show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setStartupTime(String str) {
        jLabelStartDate.setText(str);
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setEndingTime(String str) {
        jLabelFinalDate.setText(str);
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setCalculationProgress(int p) {
        jProgressBarSolverProgress.setValue(p);
    }

    /**
     * DOCUMENT ME!
     *
     * @param paso DOCUMENT ME!
     * @param iter DOCUMENT ME!
     */
    public void setActualStep(int paso, int iter) {
        jLabelSolverStep.setText("" + paso);
        jLabelSolverIteration.setText("" + iter);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean continueSerializedSimulation() {
        ContinueSerializedSimulation css = new ContinueSerializedSimulation();
        jDesktopPane.add(css, javax.swing.JLayeredPane.MODAL_LAYER);

        int xpos = 20;
        int ypos = 20;
        int width = 500;
        int height = 200;

        css.setBounds(xpos, ypos, xpos + width, ypos + height);
        css.show();

        return css.getRespuestaContinuarSimulacion();
    }

    // End of variables declaration//GEN-END:variables
}
