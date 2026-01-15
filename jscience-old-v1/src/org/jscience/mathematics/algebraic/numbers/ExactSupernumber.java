package org.jscience.mathematics.algebraic.numbers;

import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;

import java.io.Serializable;

/**
 * The ExactSupernumber class encapsulates supernumbers.
 * They are actually implemented as elements of the Grassmann algebra <img border=0 alt="Lambda" src="doc-files/ulambda.gif"><sub>4</sub>
 * rather than the full <img border=0 alt="Lambda" src="doc-files/ulambda.gif"><sub><img border=0 alt="infinity" src="doc-files/infinity.gif"></sub>.
 *
 * @author Silvere Martin-Michiellot
 * @version 0.1
 * @planetmath Supernumber
 */

public final class ExactSupernumber extends Number implements Cloneable, Serializable,
        Ring.Member {
    private final int N = 4;
    private ExactComplex body = ExactComplex.ZERO;
    private ExactComplex[] soul1 = {
            ExactComplex.ZERO, ExactComplex.ZERO, ExactComplex.ZERO, ExactComplex.ZERO
    };
    private ExactComplex[] soul2 = {
            ExactComplex.ZERO, ExactComplex.ZERO, ExactComplex.ZERO, ExactComplex.ZERO, ExactComplex.ZERO,
            ExactComplex.ZERO
    };
    private ExactComplex[] soul3 = {
            ExactComplex.ZERO, ExactComplex.ZERO, ExactComplex.ZERO, ExactComplex.ZERO
    };
    private ExactComplex soul4 = ExactComplex.ZERO;

    //The Class instance representing the type.
    public static final Class TYPE = new ExactSupernumber().getClass();

    /**
     * Constructs a supernumber.
     */
    public ExactSupernumber() {
    }

    /**
     * Constructs a supernumber. We don't perform any check though we should.
     */
    public ExactSupernumber(final ExactSupernumber s) {

        this.body = s.body;
        this.soul1 = s.soul1;
        this.soul2 = s.soul2;
        this.soul3 = s.soul3;
        this.soul4 = s.soul4;

    }

    /**
     * Constructs a supernumber. We don't perform any check though we should.
     */
    public ExactSupernumber(final Supernumber s) {

        this.body = new ExactComplex(s.getBody());
        this.soul1[0] = new ExactComplex(s.getSoul1(0));
        this.soul1[1] = new ExactComplex(s.getSoul1(1));
        this.soul1[2] = new ExactComplex(s.getSoul1(2));
        this.soul1[3] = new ExactComplex(s.getSoul1(3));
        this.soul2[0] = new ExactComplex(s.getSoul2(0));
        this.soul2[1] = new ExactComplex(s.getSoul2(1));
        this.soul2[2] = new ExactComplex(s.getSoul2(2));
        this.soul2[3] = new ExactComplex(s.getSoul2(3));
        this.soul2[4] = new ExactComplex(s.getSoul2(4));
        this.soul2[5] = new ExactComplex(s.getSoul2(5));
        this.soul3[0] = new ExactComplex(s.getSoul3(0));
        this.soul3[1] = new ExactComplex(s.getSoul3(1));
        this.soul3[2] = new ExactComplex(s.getSoul3(2));
        this.soul3[3] = new ExactComplex(s.getSoul3(3));
        this.soul4 = new ExactComplex(s.getSoul4());

    }

    /**
     * Constructs a supernumber. We don't perform any check though we should.
     */
    public ExactSupernumber(final ExactComplex body, final ExactComplex[] soul1, final ExactComplex[] soul2, final ExactComplex[] soul3, final ExactComplex soul4) {

        this.body = body;
        this.soul1 = soul1;
        this.soul2 = soul2;
        this.soul3 = soul3;
        this.soul4 = soul4;

    }

    /**
     * Returns a string representing the value of this supernumber.
     */
    public String toString() {
        final StringBuffer buf = new StringBuffer(100);
        buf.append("(").append(body.toString()).append(")1+\n(");
        buf.append(soul1[0].toString()).append(", ");
        buf.append(soul1[1].toString()).append(", ");
        buf.append(soul1[2].toString()).append(", ");
        buf.append(soul1[3].toString()).append(")S1+\n(");
        buf.append(soul2[0].toString()).append(", ");
        buf.append(soul2[1].toString()).append(", ");
        buf.append(soul2[2].toString()).append(", ");
        buf.append(soul2[3].toString()).append(", ");
        buf.append(soul2[4].toString()).append(", ");
        buf.append(soul2[5].toString()).append(")S2+\n(");
        buf.append(soul3[0].toString()).append(", ");
        buf.append(soul3[1].toString()).append(", ");
        buf.append(soul3[2].toString()).append(", ");
        buf.append(soul3[3].toString()).append(")S3+\n(");
        buf.append(soul4.toString()).append(")S4");

        return buf.toString();
    }

    /**
     * Returns the body (rank 0) of this supernumber.
     */
    public ExactComplex getBody() {
        return body;
    }

    /**
     * Sets the body (rank 0) of this supernumber.
     */
    public void setBody(final ExactComplex b) {
        body = b;
    }

    /**
     * Returns the a-number soul (rank 1) of this supernumber.
     */
    public ExactComplex getSoul1(final int i) {
        return soul1[i];
    }

    /**
     * Sets the a-number soul (rank 1) of this supernumber. i should be from 0 to 3.
     */
    public void setSoul1(final int i, final ExactComplex s) {
        soul1[i] = s;
    }

    /**
     * Returns the c-number soul (rank 2) of this supernumber.
     */
    public ExactComplex getSoul2(final int i) {
        return soul2[i];
    }

    /**
     * Sets the c-number soul (rank 2) of this supernumber. i should be from 0 to 5.
     */
    public void setSoul2(final int i, final ExactComplex s) {
        soul2[i] = s;
    }

    /**
     * Returns the a-number soul (rank 3) of this supernumber.
     */
    public ExactComplex getSoul3(final int i) {
        return soul3[i];
    }

    /**
     * Sets the a-number soul (rank 3) of this supernumber. i should be from 0 to 3.
     */
    public void setSoul3(final int i, final ExactComplex s) {
        soul3[i] = s;
    }

    /**
     * Returns the c-number soul (rank 4) of this supernumber.
     */
    public ExactComplex getSoul4() {
        return soul4;
    }

    /**
     * Sets the c-number soul (rank 4) of this supernumber.
     */
    public void setSoul4(final ExactComplex s) {
        soul4 = s;
    }

    /**
     * Returns the dimension.
     */
    public int dimension() {
        return 1 << N;
    }

    //we currently return 0, if anyone knows something more useful to be returned, let us know
    public int intValue() {
        return 0;
    }

    //we currently return 0, if anyone knows something more useful to be returned, let us know
    public long longValue() {
        return 0;
    }

    //we currently return 0, if anyone knows something more useful to be returned, let us know
    public double doubleValue() {
        return 0;
    }

    //we currently return 0, if anyone knows something more useful to be returned, let us know
    public float floatValue() {
        return 0;
    }

    /**
     * Returns the negative of this number.
     */
    public AbelianGroup.Member negate() {
        ExactSupernumber ans = new ExactSupernumber();
        ans.body = (ExactComplex) body.negate();
        ans.soul4 = (ExactComplex) soul4.negate();

        for (int i = 0; i < N; i++) {
            ans.soul1[i] = (ExactComplex) soul1[i].negate();
            ans.soul3[i] = (ExactComplex) soul3[i].negate();
        }

        for (int i = 0; i < soul2.length; i++)
            ans.soul2[i] = (ExactComplex) soul2[i].negate();

        return ans;
    }

    // ADDITION

    /**
     * Returns the addition of this number and another.
     */
    public AbelianGroup.Member add(final AbelianGroup.Member z) {
        if (z instanceof ExactSupernumber)
            return add((ExactSupernumber) z);
        if (z instanceof Supernumber)
            return add(new ExactSupernumber((Supernumber) z));
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the addition of this supernumber and another.
     *
     * @param z a supernumber
     */
    public ExactSupernumber add(ExactSupernumber z) {
        ExactSupernumber ans = new ExactSupernumber();
        ans.body = body.add(z.body);
        ans.soul4 = soul4.add(z.soul4);

        for (int i = 0; i < N; i++) {
            ans.soul1[i] = soul1[i].add(z.soul1[i]);
            ans.soul3[i] = soul3[i].add(z.soul3[i]);
        }

        for (int i = 0; i < soul2.length; i++)
            ans.soul2[i] = soul2[i].add(z.soul2[i]);

        return ans;
    }

    // SUBTRACTION

    /**
     * Returns the subtraction of this number and another.
     */
    public AbelianGroup.Member subtract(final AbelianGroup.Member z) {
        if (z instanceof ExactSupernumber)
            return subtract((ExactSupernumber) z);
        if (z instanceof Supernumber)
            return subtract(new ExactSupernumber((Supernumber) z));
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the subtraction of this supernumber and another.
     *
     * @param z a supernumber
     */
    public ExactSupernumber subtract(ExactSupernumber z) {
        ExactSupernumber ans = new ExactSupernumber();
        ans.body = body.subtract(z.body);
        ans.soul4 = soul4.subtract(z.soul4);

        for (int i = 0; i < N; i++) {
            ans.soul1[i] = soul1[i].subtract(z.soul1[i]);
            ans.soul3[i] = soul3[i].subtract(z.soul3[i]);
        }

        for (int i = 0; i < soul2.length; i++)
            ans.soul2[i] = soul2[i].subtract(z.soul2[i]);

        return ans;
    }

    // MULTIPLICATION

    /**
     * Returns the multiplication of this number and another.
     */
    public Ring.Member multiply(final Ring.Member z) {
        if (z instanceof ExactSupernumber)
            return multiply((ExactSupernumber) z);
        if (z instanceof Supernumber)
            return multiply(new ExactSupernumber((Supernumber) z));
        else
            throw new IllegalArgumentException("Member class not recognised by this method.");
    }

    /**
     * Returns the multiplication of this supernumber and another.
     *
     * @param z a supernumber
     */
    public ExactSupernumber multiply(ExactSupernumber z) {
        ExactSupernumber ans = new ExactSupernumber();
        ans.body = body.multiply(z.body);
        ans.soul4 = (body.multiply(z.soul4)).add(soul1[0].multiply(z.soul3[1]))
                .subtract(soul1[1].multiply(z.soul3[2]))
                .add(soul1[2].multiply(z.soul3[3]))
                .subtract(soul1[3].multiply(z.soul3[0]))
                .add(soul2[0].multiply(z.soul2[2]))
                .subtract(soul2[1].multiply(z.soul2[3]))
                .add(soul2[2].multiply(z.soul2[0]))
                .subtract(soul2[3].multiply(z.soul2[1]))
                .subtract(soul2[4].multiply(z.soul2[5]))
                .subtract(soul2[5].multiply(z.soul2[4]))
                .add(soul3[0].multiply(z.soul1[3]))
                .subtract(soul3[3].multiply(z.soul1[2]))
                .add(soul3[2].multiply(z.soul1[1]))
                .subtract(soul3[1].multiply(z.soul1[0])).add(soul4.multiply(z.body));

        for (int i = 0; i < N; i++)
            ans.soul1[i] = body.multiply(z.soul1[i]).add(soul1[i].multiply(z.body));

        ans.soul2[0] = (body.multiply(z.soul2[0])).add(soul1[0].multiply(z.soul1[1])).subtract(soul1[1].multiply(z.soul1[0])).add(soul2[0].multiply(z.body));
        ans.soul2[1] = (body.multiply(z.soul2[1])).add(soul1[1].multiply(z.soul1[2])).subtract(soul1[2].multiply(z.soul1[1])).add(soul2[1].multiply(z.body));
        ans.soul2[2] = (body.multiply(z.soul2[2])).add(soul1[2].multiply(z.soul1[3])).subtract(soul1[3].multiply(z.soul1[2])).add(soul2[2].multiply(z.body));
        ans.soul2[3] = (body.multiply(z.soul2[3])).add(soul1[3].multiply(z.soul1[0])).subtract(soul1[0].multiply(z.soul1[3])).add(soul2[3].multiply(z.body));
        ans.soul2[4] = (body.multiply(z.soul2[4])).add(soul1[0].multiply(z.soul1[2])).subtract(soul1[2].multiply(z.soul1[0])).add(soul2[4].multiply(z.body));
        ans.soul2[5] = (body.multiply(z.soul2[5])).add(soul1[1].multiply(z.soul1[3])).subtract(soul1[3].multiply(z.soul1[1])).add(soul2[5].multiply(z.body));
        ans.soul3[0] = (body.multiply(z.soul3[0])).add(soul1[0].multiply(z.soul2[1])).subtract(soul1[1].multiply(z.soul2[4]))
                .add(soul1[2].multiply(z.soul2[0]))
                .add(soul2[0].multiply(z.soul1[2]))
                .subtract(soul2[4].multiply(z.soul1[1]))
                .add(soul2[1].multiply(z.soul1[0])).add(soul3[0].multiply(z.body));
        ans.soul3[1] = (body.multiply(z.soul3[1])).add(soul1[1].multiply(z.soul2[2])).subtract(soul1[2].multiply(z.soul2[5]))
                .add(soul1[3].multiply(z.soul2[1]))
                .add(soul2[1].multiply(z.soul1[3]))
                .subtract(soul2[5].multiply(z.soul1[2]))
                .add(soul2[2].multiply(z.soul1[1])).add(soul3[1].multiply(z.body));
        ans.soul3[2] = (body.multiply(z.soul3[2])).add(soul1[2].multiply(z.soul2[3])).add(soul1[3].multiply(z.soul2[4]))
                .add(soul1[0].multiply(z.soul2[2]))
                .add(soul2[2].multiply(z.soul1[0]))
                .add(soul2[4].multiply(z.soul1[3]))
                .add(soul2[3].multiply(z.soul1[2])).add(soul3[2].multiply(z.body));
        ans.soul3[3] = (body.multiply(z.soul3[3])).add(soul1[3].multiply(z.soul2[0])).add(soul1[0].multiply(z.soul2[5]))
                .add(soul1[1].multiply(z.soul2[3]))
                .add(soul2[3].multiply(z.soul1[1]))
                .add(soul2[5].multiply(z.soul1[0]))
                .add(soul2[0].multiply(z.soul1[3])).add(soul3[3].multiply(z.body));

        return ans;
    }

    public Object clone() {
        return new ExactSupernumber(this);
    }

}
