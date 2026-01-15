/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import java.awt.*;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.OceanTheme;


/**
 * This still needs some work in regards to some nice colors.
 *
 * @author Holger Antelmann
 */
class CustomColorTheme extends DefaultMetalTheme {
    /** DOCUMENT ME! */
    public static DefaultMetalTheme OCEAN = new OceanTheme(); // the default Tiger theme introduced in jdk 1.5

    /** DOCUMENT ME! */
    public static DefaultMetalTheme STEEL = new CustomColorTheme();

    /** DOCUMENT ME! */
    public static DefaultMetalTheme AQUA = new AquaTheme();

    /** DOCUMENT ME! */
    public static DefaultMetalTheme CHARCOAL = new CharcoalTheme();

    /** DOCUMENT ME! */
    public static DefaultMetalTheme CONTRAST = new ContrastTheme();

    /** DOCUMENT ME! */
    public static DefaultMetalTheme EMERALD = new EmeraldTheme();

    /** DOCUMENT ME! */
    public static DefaultMetalTheme RUBY = new RubyTheme();

    /** DOCUMENT ME! */
    String name = "MyColorTheme (JScience.org default)";

    /** DOCUMENT ME! */
    ColorUIResource primary1 = super.getPrimary1();

    /** DOCUMENT ME! */
    ColorUIResource primary2 = super.getPrimary2();

    /** DOCUMENT ME! */
    ColorUIResource primary3 = super.getPrimary3();

    /** DOCUMENT ME! */
    ColorUIResource secundary1 = super.getSecondary1();

    /** DOCUMENT ME! */
    ColorUIResource secundary2 = super.getSecondary2();

    /** DOCUMENT ME! */
    ColorUIResource secundary3 = super.getSecondary3();

    /** DOCUMENT ME! */
    ColorUIResource black = super.getBlack();

    /** DOCUMENT ME! */
    ColorUIResource white = super.getWhite();

/**
     * Creates a new CustomColorTheme object.
     */
    public CustomColorTheme() {
        name = super.getName();
    }

/**
     * Creates a new CustomColorTheme object.
     *
     * @param name     DOCUMENT ME!
     * @param primary1 DOCUMENT ME!
     * @param primary2 DOCUMENT ME!
     * @param primary3 DOCUMENT ME!
     */
    public CustomColorTheme(String name, Color primary1, Color primary2,
        Color primary3) {
        this.name = name;
        this.primary1 = new ColorUIResource(primary1);
        this.primary2 = new ColorUIResource(primary2);
        this.primary3 = new ColorUIResource(primary3);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DefaultMetalTheme[] getThemes() {
        return new DefaultMetalTheme[] {
            OCEAN, STEEL, AQUA, CHARCOAL, CONTRAST, EMERALD, RUBY
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getSecondary1() {
        return secundary1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getSecondary2() {
        return secundary2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected ColorUIResource getSecondary3() {
        return secundary3;
    }

    /*
    public FontUIResource getControlTextFont () { return super.getControlTextFont(); }
    
    
    public FontUIResource getMenuTextFont () { return super.getMenuTextFont(); }
    
    
    public FontUIResource getSubTextFont () { return super.getSubTextFont(); }
    
    
    public FontUIResource getSystemTextFont () { return super.getSystemTextFont(); }
    
    
    public FontUIResource getUserTextFont () { return super.getUserTextFont(); }
    
    
    public FontUIResource getWindowTitleFont () { return super.getWindowTitleFont(); }
    */
}
