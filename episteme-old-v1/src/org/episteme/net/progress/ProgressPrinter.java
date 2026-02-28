/*
 * Geotools 2 - OpenSource mapping toolkit
 * (C) 2003, Geotools Project Management Committee (PMC)
 * (C) 2001, Institut de Recherche pour le D�veloppement
 * (C) 1999, P�ches et Oc�ans Canada
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 * Contacts:
 *     UNITED KINGDOM: James Macgill
 *             mailto:j.macgill@geog.leeds.ac.uk
 *
 *     FRANCE: Surveillance de l'Environnement Assist�e par Satellite
 *             Institut de Recherche pour le D�veloppement / US-Espace
 *             mailto:seasnet@teledetection.fr
 *
 *     CANADA: Observatoire du Saint-Laurent
 *             Institut Maurice-Lamontagne
 *             mailto:osl@osl.gc.ca
 */
package org.episteme.net.progress;


// Gestion des entr�s/sorties
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.text.BreakIterator;
import java.text.NumberFormat;

import java.util.Arrays;


/**
 * Informe l'utilisateur des progr�s d'une op�ration � l'aide de
 * messages envoy� vers un flot. L'avancement de l'op�ration sera
 * affich� en pourcentage sur une ligne (g�n�ralement le
 * p�riph�rique de sortie standard). Cette classe peut aussi �crire des
 * avertissements, ce qui est utile entre autre lors de la lecture d'un
 * fichier de donn�es durant laquelle on veut signaler des anomalies mais
 * sans arr�ter la lecture pour autant.
 *
 * @author Martin Desruisseaux
 * @version $Id: ProgressPrinter.java,v 1.3 2007-10-23 18:21:51 virtualcall Exp $
 */
public class ProgressPrinter implements ProgressListener {
    /**
     * An array of strings containing only white spaces. Strings'
     * lengths are equal to their index + 1 in the <code>spacesFactory</code>
     * array. For example, <code>spacesFactory[4]</code> contains a string of
     * length 5. Strings are constructed only when first needed.
     */
    private static final String[] spacesFactory = new String[20];

    /**
     * Nom de l'op�ration en cours. Le pourcentage sera �cris �
     * la droite de ce nom.
     */
    private String description;

    /**
     * Flot utilis� pour l'�criture de l'�tat d'avancement d'un
     * processus ainsi que pour les �critures des commentaires.
     */
    private final PrintWriter out;

    /**
     * Indique si le caract�re '\r' ram�ne au d�but de la ligne
     * courante sur ce syst�me. On supposera que ce sera le cas si le
     * syst�me n'utilise pas la paire "\r\n" pour changer de ligne (comme le
     * system VAX-VMS).
     */
    private final boolean CR_supported;

    /**
     * Longueur maximale des lignes. L'espace utilisable sera un peu
     * moindre car quelques espaces seront laiss�s en d�but de ligne.
     */
    private final int maxLength;

    /**
     * Nombre de caract�res utilis�s lors de l'�criture de la
     * derni�re ligne. Ce champ est mis � jour par la m�thode {@link
     * #carriageReturn} chaque fois que l'on d�clare que l'on vient de
     * terminer l'�criture d'une ligne.
     */
    private int lastLength;

    /**
     * Position � laquelle commencer � �crire le pourcentage.
     * Cette information est g�r�e automatiquement par la m�thode {@link
     * #progress}. La valeur -1 signifie que ni le pourcentage ni la
     * description n'ont encore �t� �crits.
     */
    private int percentPosition = -1;

    /**
     * Dernier pourcentage �crit. Cette information est utilis�e
     * afin d'�viter d'�crire deux fois le m�me pourcentage, ce qui
     * ralentirait inutilement le syst�me. La valeur -1 signifie qu'on n'a
     * pas encore �crit de pourcentage.
     */
    private float lastPercent = -1;

    /** Format � utiliser pour �crire les pourcentages. */
    private NumberFormat format;

    /**
     * Objet utilis� pour couper les lignes correctements lors de
     * l'affichage de messages d'erreurs qui peuvent prendre plusieurs lignes.
     */
    private BreakIterator breaker;

    /**
     * Indique si cet objet a d�j� �crit des avertissements. Si
     * oui, on ne r��crira pas le gros titre "avertissements".
     */
    private boolean hasPrintedWarning;

    /**
     * Source du dernier message d'avertissement. Cette information est
     * conserv�e afin d'�viter de r�p�ter la source lors d'�ventuels
     * autres messages d'avertissements.
     */
    private String lastSource;

/**
     * Construit un objet qui �crira sur le p�riph�rique de sortie
     * standard ({@link java.lang.System#out}) l'�tat d'avancement d'une
     * op�ration. La longueur par d�faut des lignes sera de 80
     * caract�res.
     */
    public ProgressPrinter() {
        //this(new PrintWriter(Arguments.getWriter(System.out)));
        //this is not exazctly the guenuine code meaning
        this(new PrintWriter(new OutputStreamWriter(System.out)));
    }

/**
     * Construit un objet qui �crira sur le p�riph�rique de sortie
     * sp�cifi� l'�tat d'avancement d'une op�ration. La longueur par
     * d�faut des lignes sera de 80 caract�res.
     *
     * @param out DOCUMENT ME!
     */
    public ProgressPrinter(final PrintWriter out) {
        this(out, 80);
    }

/**
     * Construit un objet qui �crira sur le p�riph�rique de sortie
     * sp�cifi� l'�tat d'avancement d'une op�ration.
     *
     * @param out       p�riph�rique de sortie � utiliser pour �crire
     *                  l'�tat d'avancement.
     * @param maxLength Longueur maximale des lignes. Cette information est
     *                  utilis�e par {@link #warningOccurred} pour r�partir sur
     *                  plusieurs lignes des messages qui ferait plus que la longueur
     *                  <code>lineLength</code>.
     */
    public ProgressPrinter(final PrintWriter out, final int maxLength) {
        this.out = out;
        this.maxLength = maxLength;

        final String lineSeparator = System.getProperty("line.separator");
        CR_supported = ((lineSeparator != null) &&
            lineSeparator.equals("\r\n"));
    }

    /**
     * Efface le reste de la ligne (si n�cessaire) puis repositionne
     * le curseur au d�but de la ligne. Si les retours chariot ne sont pas
     * support�s, alors cette m�thode va plut�t passer � la ligne
     * suivante. Dans tous les cas, le curseur se trouvera au d�but d'une
     * ligne et la valeur <code>length</code> sera affect� au champ {@link
     * #lastLength}.
     *
     * @param length Nombre de caract�res qui ont �t� �crit jusqu'�
     *        maintenant sur cette ligne. Cette information est utilis�e
     *        pour ne mettre que le nombre d'espaces n�cessaires � la fin
     *        de la ligne.
     */
    private void carriageReturn(final int length) {
        if (CR_supported && (length < maxLength)) {
            for (int i = length; i < lastLength; i++) {
                out.print(' ');
            }

            out.print('\r');
            out.flush();
        } else {
            out.println();
        }

        lastLength = length;
    }

    /**
     * Ajoute des points � la fin de la ligne jusqu'� repr�senter
     * le pourcentage sp�cifi�. Cette m�thode est utilis�e pour
     * repr�senter les progr�s sur un terminal qui ne supporte pas les
     * retours chariots.
     *
     * @param percent Pourcentage accompli de l'op�ration. Cette valeur doit
     *        obligatoirement se trouver entre 0 et 100 (�a ne sera pas
     *        v�rifi�).
     */
    private void completeBar(final float percent) {
        final int end = (int) ((percent / 100) * ((maxLength - 2) -
            percentPosition)); // Round toward 0.

        while (lastLength < end) {
            out.print('.');
            lastLength++;
        }
    }

    /**
     * Retourne le message d'�crivant l'op�ration en cours. Si
     * aucun message n'a �t� d�finie, retourne <code>null</code>.
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sp�cifie un message qui d�crit l'op�ration en cours. Ce
     * message est typiquement sp�cifi�e avant le d�but de
     * l'op�ration. Toutefois, cette m�thode peut aussi �tre appel�e
     * � tout moment pendant l'op�ration sans que cela affecte le
     * pourcentage accompli. La valeur <code>null</code> signifie qu'on ne
     * souhaite plus afficher de description.
     *
     * @param description DOCUMENT ME!
     */
    public synchronized void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Indique que l'op�ration a commenc�e.
     */
    public synchronized void started() {
        int length = 0;

        if (description != null) {
            out.print(description);
            length = description.length();
        }

        if (CR_supported) {
            carriageReturn(length);
        }

        out.flush();
        percentPosition = length;
        lastPercent = -1;
        lastSource = null;
        hasPrintedWarning = false;
    }

    /**
     * Indique l'�tat d'avancement de l'op�ration. Le progr�s est
     * repr�sent� par un pourcentage variant de 0 � 100 inclusivement.
     * Si la valeur sp�cifi�e est en dehors de ces limites, elle sera
     * automatiquement ramen�e entre 0 et 100.
     *
     * @param percent DOCUMENT ME!
     */
    public synchronized void progress(float percent) {
        if (percent < 0) {
            percent = 0;
        }

        if (percent > 100) {
            percent = 100;
        }

        if (CR_supported) {
            /*
             * Si le p�riph�rique de sortie supporte les retours chariot,
             * on �crira l'�tat d'avancement comme un pourcentage apr�s
             * la description, comme dans "Lecture des donn�es (38%)".
             */
            if (percent != lastPercent) {
                if (format == null) {
                    format = NumberFormat.getPercentInstance();
                }

                final String text = format.format(percent / 100.0);
                int length = text.length();
                percentPosition = 0;

                if (description != null) {
                    out.print(description);
                    out.print(' ');
                    length += ((percentPosition = description.length()) + 1);
                }

                out.print('(');
                out.print(text);
                out.print(')');
                length += 2;
                carriageReturn(length);
                lastPercent = percent;
            }
        } else {
            /*
             * Si le p�riph�rique ne supporte par les retours chariots, on
             * �crira l'�tat d'avancement comme une s�rie de points plac�s
             * apr�s la description, comme dans "Lecture des donn�es......"
             */
            completeBar(percent);
            lastPercent = percent;
            out.flush();
        }
    }

    /**
     * Indique que l'op�ration est termin�e. L'indicateur visuel
     * informant des progr�s sera ramen� � 100% ou dispara�tra. Si des
     * messages d'erreurs ou d'avertissements �taient en attente, ils seront
     * �crits.
     */
    public synchronized void complete() {
        if (!CR_supported) {
            completeBar(100);
        }

        carriageReturn(0);
        out.flush();
    }

    /**
     * Lib�re les ressources utilis�es par cet objet.
     * L'impl�mentation par d�faut ne fait rien.
     */
    public void dispose() {
    }

    /**
     * Envoie un message d'avertissement. La premi�re fois que cette
     * m�thode est appell�e, le mot "AVERTISSEMENTS" sera �crit en
     * lettres majuscules au milieu d'une bo�te. Si une source est
     * sp�cifi�e (argument <code>source</code>), elle ne sera �crite
     * qu'� la condition qu'elle n'est pas la m�me que celle du dernier
     * avertissement. Si une note de marge est sp�cifi�e (argument
     * <code>margin</code>), elle sera �crite entre parenth�ses � la
     * gauche de l'avertissement <code>warning</code>.
     *
     * @param source Cha�ne de caract�re d�crivant la source de
     *        l'avertissement. Il s'agira par exemple du nom du fichier dans
     *        lequel une anomalie a �t� d�tect�e. Peut �tre nul si
     *        la source n'est pas connue.
     * @param margin Texte � placer dans la marge de l'avertissement
     *        <code>warning</code>, ou <code>null</code> s'il n'y en a pas. Il
     *        s'agira le plus souvent du num�ro de ligne o� s'est produite
     *        l'erreur dans le fichier <code>source</code>.
     * @param warning Message d'avertissement � �crire. Si ce message est
     *        plus long que la largeur de l'�cran (telle que sp�cifi�e
     *        au moment de la construction, alors il sera automatiquement
     *        distribu� sur plusieurs lignes correctements indent�es.
     */
    public synchronized void warningOccurred(final String source,
        String margin, final String warning) {
        carriageReturn(0);

        if (!hasPrintedWarning) {
            //printInBox(Resources.format(ResourceKeys.WARNING));
            printInBox("Warning");
            hasPrintedWarning = true;
        }

        if (!equals(source, lastSource)) {
            out.println();

            //out.println(source!=null ? source : Resources.format(ResourceKeys.UNTITLED));
            out.println((source != null) ? source : "(Untitled)");
            lastSource = source;
        }

        /*
         * Proc�de � l'�criture de l'avertissement avec (de fa�on optionnelle)
         * quelque chose dans la marge (le plus souvent un num�ro de ligne).
         */
        String prefix = "    ";
        String second = prefix;

        if (margin != null) {
            margin = trim(margin);

            if (margin.length() != 0) {
                final StringBuffer buffer = new StringBuffer(prefix);
                buffer.append('(');
                buffer.append(margin);
                buffer.append(") ");
                prefix = buffer.toString();
                buffer.setLength(0);
                second = spaces(prefix.length());
            }
        }

        int width = maxLength - prefix.length() - 1;

        if (breaker == null) {
            breaker = BreakIterator.getLineInstance();
        }

        breaker.setText(warning);

        int start = breaker.first();
        int end = start;
        int nextEnd;

        while ((nextEnd = breaker.next()) != BreakIterator.DONE) {
            while ((nextEnd - start) > width) {
                if (end <= start) {
                    end = Math.min(nextEnd, start + width);
                }

                out.print(prefix);
                out.println(warning.substring(start, end));
                prefix = second;
                start = end;
            }

            end = Math.min(nextEnd, start + width);
        }

        if (end > start) {
            out.print(prefix);
            out.println(warning.substring(start, end));
        }

        if (!CR_supported && (description != null)) {
            out.print(description);
            completeBar(lastPercent);
        }

        out.flush();
    }

    /**
     * Convenience method for testing two objects for equality. One or
     * both objects may be null.
     *
     * @param object1 DOCUMENT ME!
     * @param object2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static boolean equals(final Object object1, final Object object2) {
        return (object1 == object2) ||
        ((object1 != null) && object1.equals(object2));
    }

    /**
     * Returns a string of the specified length filled with white
     * spaces. This method tries to return a pre-allocated string if possible.
     *
     * @param length The string length. Negative values are clamped to 0.
     *
     * @return A string of length <code>length</code> filled with white spaces.
     */
    private static String spaces(int length) {
        // No need to synchronize.  In the unlikely event of two threads
        // calling this method at the same time and the two calls creating a
        // new string, the String.intern() call will take care of
        // canonicalizing the strings.
        final int last = spacesFactory.length - 1;

        if (length < 0) {
            length = 0;
        }

        if (length <= last) {
            if (spacesFactory[length] == null) {
                if (spacesFactory[last] == null) {
                    char[] blancs = new char[last];
                    Arrays.fill(blancs, ' ');
                    spacesFactory[last] = new String(blancs).intern();
                }

                spacesFactory[length] = spacesFactory[last].substring(0, length)
                                                           .intern();
            }

            return spacesFactory[length];
        } else {
            char[] blancs = new char[length];
            Arrays.fill(blancs, ' ');

            return new String(blancs);
        }
    }

    /**
     * Indique qu'une exception est survenue pendant le traitement de
     * l'op�ration. L'impl�mentation par d�faut �crit "Exception" dans
     * une bo�te, puis envoie la trace vers le p�riph�rique de sortie
     * sp�cifi�e au constructeur.
     *
     * @param exception DOCUMENT ME!
     */
    public synchronized void exceptionOccurred(final Throwable exception) {
        carriageReturn(0);

        //printInBox(Resources.format(ResourceKeys.EXCEPTION));
        printInBox("Exception");
        exception.printStackTrace(out);
        hasPrintedWarning = false;
        out.flush();
    }

    /**
     * Retourne la cha�ne <code>margin</code> sans les �ventuelles
     * parenth�ses qu'elle pourrait avoir de part et d'autre.
     *
     * @param margin DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String trim(String margin) {
        margin = margin.trim();

        int lower = 0;
        int upper = margin.length();

        while ((lower < upper) && (margin.charAt(lower + 0) == '('))
            lower++;

        while ((lower < upper) && (margin.charAt(upper - 1) == ')'))
            upper--;

        return margin.substring(lower, upper);
    }

    /**
     * �crit dans une bo�te entour� d'ast�rix le texte
     * sp�cifi� en argument. Ce texte doit �tre sur une seule ligne et
     * ne pas comporter de retour chariot. Les dimensions de la bo�te seront
     * automatiquement ajust�es.
     *
     * @param text Texte � �crire (une seule ligne).
     */
    private void printInBox(String text) {
        int length = text.length();

        for (int pass = -2; pass <= 2; pass++) {
            switch (Math.abs(pass)) {
            case 2:

                for (int j = -10; j < length; j++)
                    out.print('*');

                out.println();

                break;

            case 1:
                out.print("**");

                for (int j = -6; j < length; j++)
                    out.print(' ');

                out.println("**");

                break;

            case 0:
                out.print("**   ");
                out.print(text);
                out.println("   **");

                break;
            }
        }
    }
}
