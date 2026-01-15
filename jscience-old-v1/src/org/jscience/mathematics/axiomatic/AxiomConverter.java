package org.jscience.mathematics.axiomatic;


//author: Greg Bush
//email: GBush@HealthMarket.com
import java.io.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class AxiomConverter {
    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static String readExpression(Reader in) throws IOException {
        char c = (char) in.read();

        if (c == '~') {
            return "~" + readExpression(in);
        } else if (c == '(') {
            String left = readExpression(in);
            c = (char) in.read();

            if (c == '>') {
                String right = readExpression(in);
                c = (char) in.read();

                if (c != ')') {
                    System.out.println("Why is there a " + c + "?");
                }

                return ">" + left + right;
            } else if (c == '<') {
                in.skip(1);

                String right = readExpression(in);
                c = (char) in.read();

                if (c != ')') {
                    System.out.println("Why is there a " + c + "?");
                }

                return "&|" + left + right + "|~" + left + "~" + right;
            } else if (c == '^') {
                String right = readExpression(in);
                c = (char) in.read();

                if (c != ')') {
                    System.out.println("Why is there a " + c + "?");
                }

                return "&" + left + right;
            } else if (c == 'v') {
                String right = readExpression(in);
                c = (char) in.read();

                if (c != ')') {
                    System.out.println("Why is there a " + c + "?");
                }

                return "|" + left + right;
            } else if (c == ')') {
                return left;
            } else {
                System.out.println("Why is there a " + c + "?");

                return null;
            }
        } else if (c == 'P') {
            return "a";
        } else if (c == 'Q') {
            return "b";
        } else if (c == 'R') {
            return "c";
        } else {
            System.out.println("Why is there a " + c + "?");

            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        Reader in = new BufferedReader(new FileReader("kalmbach.txt"));

        for (;;) {
            StringBuffer name = new StringBuffer();
            char c;
            in.mark(1);

            if (in.read() == -1) {
                break;
            }

            in.reset();

            for (;;) {
                c = (char) in.read();

                if (c != '.') {
                    name.append(c);
                } else {
                    break;
                }
            }

            for (;;) {
                in.mark(1);
                c = (char) in.read();

                if (c != ' ') {
                    break;
                }
            }

            in.reset();

            StringBuffer proof = new StringBuffer();
            boolean blankLine = true;

            for (;;) {
                c = (char) in.read();

                if (!Character.isWhitespace(c) && (c != '-')) {
                    proof.append(c);
                    blankLine = false;
                } else if (c == '\n') {
                    if (blankLine) {
                        break;
                    }

                    blankLine = true;
                }
            }

            String convertedProof = readExpression(new StringReader(
                        proof.toString()));
            System.out.println(name + "=" + convertedProof);
        }
    }
}
