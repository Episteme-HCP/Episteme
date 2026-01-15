package org.jscience.tests.computing.ai.expertsystem.fibonacci;

class Jeops_RuleBase_FibonacciBase extends org.jscience.tests.computing.ai.expertsystem.AbstractRuleBase {


    /**
     * Identifiers of rule GoDown
     */
    private String[] identifiers_GoDown = {
            "f"
    };

    /**
     * Returns the identifiers declared in rule GoDown
     *
     * @return the identifiers declared in rule GoDown
     */
    private String[] getDeclaredIdentifiers_GoDown() {
        return identifiers_GoDown;
    }

    /**
     * Returns the name of the class of one declared object for
     * rule GoDown.
     *
     * @param index the index of the declaration
     * @return the name of the class of the declared objects for
     *         this rule.
     */
    private String getDeclaredClassName_GoDown(int index) {
        switch (index) {
            case 0:
                return "org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci";
            default:
                return null;
        }
    }

    /**
     * Returns the class of one declared object for rule GoDown.
     *
     * @param index the index of the declaration
     * @return the class of the declared objects for this rule.
     */
    private Class getDeclaredClass_GoDown(int index) {
        switch (index) {
            case 0:
                return org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci.class;
            default:
                return null;
        }
    }

    /**
     * Sets an object declared in the rule GoDown.
     *
     * @param index the index of the declared object
     * @param value the value of the object being set.
     */
    private void setObject_GoDown(int index, Object value) {
        switch (index) {
            case 0:
                this.jeops_examples_fibonacci_Fibonacci_1 = (org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci) value;
                break;
        }
    }

    /**
     * Returns an object declared in the rule GoDown.
     *
     * @param index the index of the declared object
     * @return the value of the corresponding object.
     */
    private Object getObject_GoDown(int index) {
        switch (index) {
            case 0:
                return jeops_examples_fibonacci_Fibonacci_1;
            default:
                return null;
        }
    }

    /**
     * Returns all variables bound to the declarations
     * of rule GoDown
     *
     * @return an object array of the variables bound to the
     *         declarations of this rule.
     */
    private Object[] getObjects_GoDown() {
        return new Object[]{
                jeops_examples_fibonacci_Fibonacci_1
        };
    }

    /**
     * Defines all variables bound to the declarations
     * of rule GoDown
     *
     * @param objects an object array of the variables bound to the
     *                declarations of this rule.
     */
    private void setObjects_GoDown(Object[] objects) {
        jeops_examples_fibonacci_Fibonacci_1 = (org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci) objects[0];
    }

    /**
     * Condition 0 of rule GoDown.<p>
     * The original expression was:<br>
     * <code>f.getValue() == -1</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoDown_cond_0() {
        return (jeops_examples_fibonacci_Fibonacci_1.getValue() == -1);
    }

    /**
     * Condition 1 of rule GoDown.<p>
     * The original expression was:<br>
     * <code>f.getN() >= 2</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoDown_cond_1() {
        return (jeops_examples_fibonacci_Fibonacci_1.getN() >= 2);
    }

    /**
     * Condition 2 of rule GoDown.<p>
     * The original expression was:<br>
     * <code>f.getSon1() == null</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoDown_cond_2() {
        return (jeops_examples_fibonacci_Fibonacci_1.getSon1() == null);
    }

    /**
     * Checks whether some conditions of rule GoDown is satisfied.
     *
     * @param index the index of the condition to be checked.
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoDown_cond(int index) {
        switch (index) {
            case 0:
                return GoDown_cond_0();
            case 1:
                return GoDown_cond_1();
            case 2:
                return GoDown_cond_2();
            default:
                return false;
        }
    }

    /**
     * Checks whether all conditions of rule GoDown that depend only on
     * the given object are satisfied.
     *
     * @param declIndex the index of the declaration to be checked
     * @return <code>true</code> if all corresponding conditions for
     *         this rule are satisfied; <code>false</code> otherwise.
     */
    private boolean checkConditionsOnlyOf_GoDown(int declIndex) {
        switch (declIndex) {
            case 0:
                if (!GoDown_cond_0()) return false;
                if (!GoDown_cond_1()) return false;
                if (!GoDown_cond_2()) return false;
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks whether all the conditions of a rule which
     * reference some declared element of the declarations are
     * true.
     *
     * @param declIndex the index of the declared element.
     * @return <code>true</code> if the conditions that reference
     *         up to the given declaration are true;
     *         <code>false</code> otherwise.
     */
    private boolean checkCondForDeclaration_GoDown(int declIndex) {
        switch (declIndex) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    /**
     * Executes the action part of the rule GoDown
     */
    private void GoDown() {
        Fibonacci f1 = new Fibonacci(jeops_examples_fibonacci_Fibonacci_1.getN() - 1);
        Fibonacci f2 = new Fibonacci(jeops_examples_fibonacci_Fibonacci_1.getN() - 2);
        jeops_examples_fibonacci_Fibonacci_1.setSon1(f1);
        jeops_examples_fibonacci_Fibonacci_1.setSon2(f2);
        assert (f1);    // Let's tell our knowledge base
        assert (f2);    //   that these two sons exist.
        modified(jeops_examples_fibonacci_Fibonacci_1);
    }


    /**
     * Identifiers of rule BaseCase
     */
    private String[] identifiers_BaseCase = {
            "f"
    };

    /**
     * Returns the identifiers declared in rule BaseCase
     *
     * @return the identifiers declared in rule BaseCase
     */
    private String[] getDeclaredIdentifiers_BaseCase() {
        return identifiers_BaseCase;
    }

    /**
     * Returns the name of the class of one declared object for
     * rule BaseCase.
     *
     * @param index the index of the declaration
     * @return the name of the class of the declared objects for
     *         this rule.
     */
    private String getDeclaredClassName_BaseCase(int index) {
        switch (index) {
            case 0:
                return "org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci";
            default:
                return null;
        }
    }

    /**
     * Returns the class of one declared object for rule BaseCase.
     *
     * @param index the index of the declaration
     * @return the class of the declared objects for this rule.
     */
    private Class getDeclaredClass_BaseCase(int index) {
        switch (index) {
            case 0:
                return org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci.class;
            default:
                return null;
        }
    }

    /**
     * Sets an object declared in the rule BaseCase.
     *
     * @param index the index of the declared object
     * @param value the value of the object being set.
     */
    private void setObject_BaseCase(int index, Object value) {
        switch (index) {
            case 0:
                this.jeops_examples_fibonacci_Fibonacci_1 = (org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci) value;
                break;
        }
    }

    /**
     * Returns an object declared in the rule BaseCase.
     *
     * @param index the index of the declared object
     * @return the value of the corresponding object.
     */
    private Object getObject_BaseCase(int index) {
        switch (index) {
            case 0:
                return jeops_examples_fibonacci_Fibonacci_1;
            default:
                return null;
        }
    }

    /**
     * Returns all variables bound to the declarations
     * of rule BaseCase
     *
     * @return an object array of the variables bound to the
     *         declarations of this rule.
     */
    private Object[] getObjects_BaseCase() {
        return new Object[]{
                jeops_examples_fibonacci_Fibonacci_1
        };
    }

    /**
     * Defines all variables bound to the declarations
     * of rule BaseCase
     *
     * @param objects an object array of the variables bound to the
     *                declarations of this rule.
     */
    private void setObjects_BaseCase(Object[] objects) {
        jeops_examples_fibonacci_Fibonacci_1 = (org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci) objects[0];
    }

    /**
     * Condition 0 of rule BaseCase.<p>
     * The original expression was:<br>
     * <code>f.getN() <= 1</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean BaseCase_cond_0() {
        return (jeops_examples_fibonacci_Fibonacci_1.getN() <= 1);
    }

    /**
     * Condition 1 of rule BaseCase.<p>
     * The original expression was:<br>
     * <code>f.getValue() == -1</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean BaseCase_cond_1() {
        return (jeops_examples_fibonacci_Fibonacci_1.getValue() == -1);
    }

    /**
     * Checks whether some conditions of rule BaseCase is satisfied.
     *
     * @param index the index of the condition to be checked.
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean BaseCase_cond(int index) {
        switch (index) {
            case 0:
                return BaseCase_cond_0();
            case 1:
                return BaseCase_cond_1();
            default:
                return false;
        }
    }

    /**
     * Checks whether all conditions of rule BaseCase that depend only on
     * the given object are satisfied.
     *
     * @param declIndex the index of the declaration to be checked
     * @return <code>true</code> if all corresponding conditions for
     *         this rule are satisfied; <code>false</code> otherwise.
     */
    private boolean checkConditionsOnlyOf_BaseCase(int declIndex) {
        switch (declIndex) {
            case 0:
                if (!BaseCase_cond_0()) return false;
                if (!BaseCase_cond_1()) return false;
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks whether all the conditions of a rule which
     * reference some declared element of the declarations are
     * true.
     *
     * @param declIndex the index of the declared element.
     * @return <code>true</code> if the conditions that reference
     *         up to the given declaration are true;
     *         <code>false</code> otherwise.
     */
    private boolean checkCondForDeclaration_BaseCase(int declIndex) {
        switch (declIndex) {
            case 0:
                return true;
            default:
                return false;
        }
    }

    /**
     * Executes the action part of the rule BaseCase
     */
    private void BaseCase() {
        jeops_examples_fibonacci_Fibonacci_1.setValue(jeops_examples_fibonacci_Fibonacci_1.getN());
        modified(jeops_examples_fibonacci_Fibonacci_1);   // Yes, I modified f
    }


    /**
     * Identifiers of rule GoUp
     */
    private String[] identifiers_GoUp = {
            "f",
            "f1",
            "f2"
    };

    /**
     * Returns the identifiers declared in rule GoUp
     *
     * @return the identifiers declared in rule GoUp
     */
    private String[] getDeclaredIdentifiers_GoUp() {
        return identifiers_GoUp;
    }

    /**
     * Returns the name of the class of one declared object for
     * rule GoUp.
     *
     * @param index the index of the declaration
     * @return the name of the class of the declared objects for
     *         this rule.
     */
    private String getDeclaredClassName_GoUp(int index) {
        switch (index) {
            case 0:
                return "org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci";
            case 1:
                return "org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci";
            case 2:
                return "org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci";
            default:
                return null;
        }
    }

    /**
     * Returns the class of one declared object for rule GoUp.
     *
     * @param index the index of the declaration
     * @return the class of the declared objects for this rule.
     */
    private Class getDeclaredClass_GoUp(int index) {
        switch (index) {
            case 0:
                return org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci.class;
            case 1:
                return org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci.class;
            case 2:
                return org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci.class;
            default:
                return null;
        }
    }

    /**
     * Sets an object declared in the rule GoUp.
     *
     * @param index the index of the declared object
     * @param value the value of the object being set.
     */
    private void setObject_GoUp(int index, Object value) {
        switch (index) {
            case 0:
                this.jeops_examples_fibonacci_Fibonacci_1 = (org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci) value;
                break;
            case 1:
                this.jeops_examples_fibonacci_Fibonacci_2 = (org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci) value;
                break;
            case 2:
                this.jeops_examples_fibonacci_Fibonacci_3 = (org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci) value;
                break;
        }
    }

    /**
     * Returns an object declared in the rule GoUp.
     *
     * @param index the index of the declared object
     * @return the value of the corresponding object.
     */
    private Object getObject_GoUp(int index) {
        switch (index) {
            case 0:
                return jeops_examples_fibonacci_Fibonacci_1;
            case 1:
                return jeops_examples_fibonacci_Fibonacci_2;
            case 2:
                return jeops_examples_fibonacci_Fibonacci_3;
            default:
                return null;
        }
    }

    /**
     * Returns all variables bound to the declarations
     * of rule GoUp
     *
     * @return an object array of the variables bound to the
     *         declarations of this rule.
     */
    private Object[] getObjects_GoUp() {
        return new Object[]{
                jeops_examples_fibonacci_Fibonacci_1,
                jeops_examples_fibonacci_Fibonacci_2,
                jeops_examples_fibonacci_Fibonacci_3
        };
    }

    /**
     * Defines all variables bound to the declarations
     * of rule GoUp
     *
     * @param objects an object array of the variables bound to the
     *                declarations of this rule.
     */
    private void setObjects_GoUp(Object[] objects) {
        jeops_examples_fibonacci_Fibonacci_1 = (org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci) objects[0];
        jeops_examples_fibonacci_Fibonacci_2 = (org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci) objects[1];
        jeops_examples_fibonacci_Fibonacci_3 = (org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci) objects[2];
    }

    /**
     * Condition 0 of rule GoUp.<p>
     * The original expression was:<br>
     * <code>f1 != null</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoUp_cond_0() {
        return (jeops_examples_fibonacci_Fibonacci_2 != null);
    }

    /**
     * Condition 1 of rule GoUp.<p>
     * The original expression was:<br>
     * <code>f2 != null</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoUp_cond_1() {
        return (jeops_examples_fibonacci_Fibonacci_3 != null);
    }

    /**
     * Condition 2 of rule GoUp.<p>
     * The original expression was:<br>
     * <code>f1 == f.getSon1()</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoUp_cond_2() {
        return (jeops_examples_fibonacci_Fibonacci_2 == jeops_examples_fibonacci_Fibonacci_1.getSon1());
    }

    /**
     * Condition 3 of rule GoUp.<p>
     * The original expression was:<br>
     * <code>f2 == f.getSon2()</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoUp_cond_3() {
        return (jeops_examples_fibonacci_Fibonacci_3 == jeops_examples_fibonacci_Fibonacci_1.getSon2());
    }

    /**
     * Condition 4 of rule GoUp.<p>
     * The original expression was:<br>
     * <code>valf == -1</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoUp_cond_4() {
        return ((jeops_examples_fibonacci_Fibonacci_1.getValue()) == -1);
    }

    /**
     * Condition 5 of rule GoUp.<p>
     * The original expression was:<br>
     * <code>f.getN() >= 2</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoUp_cond_5() {
        return (jeops_examples_fibonacci_Fibonacci_1.getN() >= 2);
    }

    /**
     * Condition 6 of rule GoUp.<p>
     * The original expression was:<br>
     * <code>valf1 != -1</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoUp_cond_6() {
        return ((jeops_examples_fibonacci_Fibonacci_2.getValue()) != -1);
    }

    /**
     * Condition 7 of rule GoUp.<p>
     * The original expression was:<br>
     * <code>valf2 != -1</code>
     *
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoUp_cond_7() {
        return ((jeops_examples_fibonacci_Fibonacci_3.getValue()) != -1);
    }

    /**
     * Checks whether some conditions of rule GoUp is satisfied.
     *
     * @param index the index of the condition to be checked.
     * @return <code>true</code> if the condition is satisfied;
     *         <code>false</code> otherwise.
     */
    private boolean GoUp_cond(int index) {
        switch (index) {
            case 0:
                return GoUp_cond_0();
            case 1:
                return GoUp_cond_1();
            case 2:
                return GoUp_cond_2();
            case 3:
                return GoUp_cond_3();
            case 4:
                return GoUp_cond_4();
            case 5:
                return GoUp_cond_5();
            case 6:
                return GoUp_cond_6();
            case 7:
                return GoUp_cond_7();
            default:
                return false;
        }
    }

    /**
     * Checks whether all conditions of rule GoUp that depend only on
     * the given object are satisfied.
     *
     * @param declIndex the index of the declaration to be checked
     * @return <code>true</code> if all corresponding conditions for
     *         this rule are satisfied; <code>false</code> otherwise.
     */
    private boolean checkConditionsOnlyOf_GoUp(int declIndex) {
        switch (declIndex) {
            case 0:
                if (!GoUp_cond_4()) return false;
                if (!GoUp_cond_5()) return false;
                return true;
            case 1:
                if (!GoUp_cond_0()) return false;
                if (!GoUp_cond_6()) return false;
                return true;
            case 2:
                if (!GoUp_cond_1()) return false;
                if (!GoUp_cond_7()) return false;
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks whether all the conditions of a rule which
     * reference some declared element of the declarations are
     * true.
     *
     * @param declIndex the index of the declared element.
     * @return <code>true</code> if the conditions that reference
     *         up to the given declaration are true;
     *         <code>false</code> otherwise.
     */
    private boolean checkCondForDeclaration_GoUp(int declIndex) {
        switch (declIndex) {
            case 0:
                return true;
            case 1:
                if (!GoUp_cond_2()) return false;
                return true;
            case 2:
                if (!GoUp_cond_3()) return false;
                return true;
            default:
                return false;
        }
    }

    /**
     * Executes the action part of the rule GoUp
     */
    private void GoUp() {
        jeops_examples_fibonacci_Fibonacci_1.setValue((jeops_examples_fibonacci_Fibonacci_2.getValue()) + (jeops_examples_fibonacci_Fibonacci_3.getValue()));
        retract(jeops_examples_fibonacci_Fibonacci_2);  // I don't need
        retract(jeops_examples_fibonacci_Fibonacci_3);  //   them anymore...
        modified(jeops_examples_fibonacci_Fibonacci_1);
    }


    /**
     * The names of the rules in this class file
     */
    private static final String[] File_ruleNames = {
            "GoDown",
            "BaseCase",
            "GoUp"
    };

    /**
     * Returns the name of the rules in this class file.
     *
     * @return the name of the rules in this class file.
     */
    public String[] getRuleNames() {
        return File_ruleNames;
    }

    /**
     * The number of declarations of the rules in this class file.
     */
    private static final int[] File_numberOfDeclarations = {
            1,
            1,
            3
    };

    /**
     * Returns the number of declarations of the rules in this class file.
     *
     * @return the number of declarations  of the rules in this class file.
     */
    public int[] getNumberOfDeclarations() {
        return File_numberOfDeclarations;
    }

    /**
     * The number of conditions of the rules in this class file.
     */
    private static final int[] File_numberOfConditions = {
            3,
            2,
            8
    };

    /**
     * Returns the number of conditions of the rules in this class file.
     *
     * @return the number of conditions  of the rules in this class file.
     */
    public int[] getNumberOfConditions() {
        return File_numberOfConditions;
    }

    /**
     * Checks whether a condition of some rule is satisfied.
     *
     * @param ruleIndex the index of the rule to be checked
     * @param condIndex the index of the condition to be checked
     * @return <code>true</code> if the corresponding condition for the
     *         given rule is satisfied. <code>false</code> otherwise.
     */
    public boolean checkCondition(int ruleIndex, int condIndex) {
        switch (ruleIndex) {
            case 0:
                return GoDown_cond(condIndex);
            case 1:
                return BaseCase_cond(condIndex);
            case 2:
                return GoUp_cond(condIndex);
            default:
                return false;
        }
    }

    /**
     * Checks whether all conditions of some rule that depend only on
     * the given object are satisfied.
     *
     * @param ruleIndex the index of the rule to be checked
     * @param declIndex the index of the declaration to be checked
     * @return <code>true</code> if all corresponding conditions for
     *         the given rule are satisfied;
     *         <code>false</code> otherwise.
     */
    public boolean checkConditionsOnlyOf(int ruleIndex, int declIndex) {
        switch (ruleIndex) {
            case 0:
                return checkConditionsOnlyOf_GoDown(declIndex);
            case 1:
                return checkConditionsOnlyOf_BaseCase(declIndex);
            case 2:
                return checkConditionsOnlyOf_GoUp(declIndex);
            default:
                return false;
        }
    }

    /**
     * Checks whether all the conditions of a rule which
     * reference only the elements declared up to the given index
     * are true.
     *
     * @param ruleIndex the index of the rule to be checked
     * @param declIndex the index of the declaration to be checked
     * @return <code>true</code> if all the conditions of a rule which
     *         reference only the elements declared up to the given index
     *         are satisfied; <code>false</code> otherwise.
     */
    public boolean checkCondForDeclaration(int ruleIndex, int declIndex) {
        switch (ruleIndex) {
            case 0:
                return checkCondForDeclaration_GoDown(declIndex);
            case 1:
                return checkCondForDeclaration_BaseCase(declIndex);
            case 2:
                return checkCondForDeclaration_GoUp(declIndex);
            default:
                return false;
        }
    }

    /**
     * Returns the class name of an object declared in a rule.
     *
     * @param ruleIndex the index of the rule
     * @param declIndex the index of the declaration
     * @return the class name of the declared object.
     */
    public String getDeclaredClassName(int ruleIndex, int declIndex) {
        switch (ruleIndex) {
            case 0:
                return getDeclaredClassName_GoDown(declIndex);
            case 1:
                return getDeclaredClassName_BaseCase(declIndex);
            case 2:
                return getDeclaredClassName_GoUp(declIndex);
            default:
                return null;
        }
    }

    /**
     * Returns the class of an object declared in a rule.
     *
     * @param ruleIndex the index of the rule
     * @param declIndex the index of the declaration
     * @return the class of the declared object.
     */
    public Class getDeclaredClass(int ruleIndex, int declIndex) {
        switch (ruleIndex) {
            case 0:
                return getDeclaredClass_GoDown(declIndex);
            case 1:
                return getDeclaredClass_BaseCase(declIndex);
            case 2:
                return getDeclaredClass_GoUp(declIndex);
            default:
                return null;
        }
    }

    /**
     * Fires one of the rules in this rule base.
     *
     * @param ruleIndex the index of the rule to be fired
     */
    protected void internalFireRule(int ruleIndex) {
        switch (ruleIndex) {
            case 0:
                GoDown();
                break;
            case 1:
                BaseCase();
                break;
            case 2:
                GoUp();
                break;
        }
    }

    /**
     * Returns the number of rules.
     *
     * @return the number of rules.
     */
    public int getNumberOfRules() {
        return 3;
    }

    /**
     * Returns the identifiers declared in a given rule.
     *
     * @param ruleIndex the index of the rule.
     * @return an array with the identifiers of the rule declarations.
     */
    public String[] getDeclaredIdentifiers(int ruleIndex) {
        switch (ruleIndex) {
            case 0:
                return getDeclaredIdentifiers_GoDown();
            case 1:
                return getDeclaredIdentifiers_BaseCase();
            case 2:
                return getDeclaredIdentifiers_GoUp();
            default:
                return new String[0];
        }
    }

    /**
     * Sets an object that represents a declaration of some rule.
     *
     * @param ruleIndex the index of the rule
     * @param declIndex the index of the declaration in the rule.
     * @param value     the value of the object being set.
     */
    public void setObject(int ruleIndex, int declIndex, Object value) {
        switch (ruleIndex) {
            case 0:
                setObject_GoDown(declIndex, value);
                break;
            case 1:
                setObject_BaseCase(declIndex, value);
                break;
            case 2:
                setObject_GoUp(declIndex, value);
                break;
        }
    }

    /**
     * Returns an object that represents a declaration of some rule.
     *
     * @param ruleIndex the index of the rule
     * @param declIndex the index of the declaration in the rule.
     * @return the value of the corresponding object.
     */
    public Object getObject(int ruleIndex, int declIndex) {
        switch (ruleIndex) {
            case 0:
                return getObject_GoDown(declIndex);
            case 1:
                return getObject_BaseCase(declIndex);
            case 2:
                return getObject_GoUp(declIndex);
            default:
                return null;
        }
    }

    /**
     * Returns all variables bound to the declarations of
     * some rule.
     *
     * @param ruleIndex the index of the rule
     * @return an object array of the variables bound to the
     *         declarations of some rule.
     */
    public Object[] getObjects(int ruleIndex) {
        switch (ruleIndex) {
            case 0:
                return getObjects_GoDown();
            case 1:
                return getObjects_BaseCase();
            case 2:
                return getObjects_GoUp();
            default:
                return null;
        }
    }

    /**
     * Defines all variables bound to the declarations of
     * some rule.
     *
     * @param ruleIndex the index of the rule
     * @param objects   an object array of the variables bound to the
     *                  declarations of some rule.
     */
    public void setObjects(int ruleIndex, Object[] objects) {
        switch (ruleIndex) {
            case 0:
                setObjects_GoDown(objects);
                break;
            case 1:
                setObjects_BaseCase(objects);
                break;
            case 2:
                setObjects_GoUp(objects);
                break;
        }
    }

    /*
     * The variables declared in the rules.
     */
    private org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci jeops_examples_fibonacci_Fibonacci_1;
    private org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci jeops_examples_fibonacci_Fibonacci_2;
    private org.jscience.tests.computing.ai.expertsystem.fibonacci.Fibonacci jeops_examples_fibonacci_Fibonacci_3;

    /**
     * Class constructor.
     *
     * @param knowledgeBase the knowledge base that contains this rule base.
     */
    public Jeops_RuleBase_FibonacciBase(org.jscience.tests.computing.ai.expertsystem.AbstractKnowledgeBase knowledgeBase) {
        super(knowledgeBase);
    }

}

/**
 * Knowledge base created by org.jscience.tests.computing.ai.expertsystem from file FibonacciBase.rules
 *
 * @version Jun 16, 2001
 */
public class FibonacciBase extends org.jscience.tests.computing.ai.expertsystem.AbstractKnowledgeBase {

    /**
     * Creates a new knowledge base with the specified conflict set with the
     * desired conflict resolution policy.
     *
     * @param conflictSet a conflict set with the desired resolution policy
     */
    public FibonacciBase(org.jscience.tests.computing.ai.expertsystem.conflict.ConflictSet conflictSet) {
        super(conflictSet);
    }

    /**
     * Creates a new knowledge base, using the default conflict resolution
     * policy.
     */
    public FibonacciBase() {
        this(new org.jscience.tests.computing.ai.expertsystem.conflict.DefaultConflictSet());
    }

    /**
     * Factory method used to instantiate the rule base.
     */
    protected org.jscience.tests.computing.ai.expertsystem.AbstractRuleBase createRuleBase() {
        return new Jeops_RuleBase_FibonacciBase(this);
    }

}
