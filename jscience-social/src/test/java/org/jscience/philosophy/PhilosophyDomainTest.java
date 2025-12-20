package org.jscience.philosophy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhilosophyDomainTest {

    @Test
    public void testSyllogismLogic() {
        Premise major = new Premise("All humans are mortal", true);
        Premise minor = new Premise("Socrates is a human", true);
        Premise conclusion = new Premise("Socrates is mortal", true);

        Syllogism validArgument = new Syllogism(major, minor, conclusion);

        assertTrue(major.isTrue());
        assertTrue(validArgument.isSound());

        Premise falseMinor = new Premise("Socrates is a rock", false);
        Syllogism invalidArgument = new Syllogism(major, falseMinor, conclusion);

        assertFalse(invalidArgument.isSound(), "Argument with false premise should not be sound");
    }
}
