package org.jscience.sociology;

/**
 * Represents a role a Person plays in a specific Situation.
 */
public class Role {

    public final static int CLIENT = 1;
    public final static int SERVER = 2;
    public final static int SUPERVISOR = 4;
    public final static int OBSERVER = 8;

    private final Person person;
    private final String name;
    private Situation situation;
    private final int kind;

    public Role(Person person, String name, Situation situation, int kind) {
        this.person = person;
        this.name = name;
        this.situation = situation;
        this.kind = kind;

        // Linkage
        if (person != null) {
            person.addRole(this);
        }
        if (situation != null) {
            situation.addRole(this);
        }
    }

    public Person getPerson() {
        return person;
    }

    public String getName() {
        return name;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public int getKind() {
        return kind;
    }

    @Override
    public String toString() {
        return name + " (" + kindToString(kind) + ")";
    }

    private String kindToString(int k) {
        switch (k) {
            case CLIENT:
                return "Client";
            case SERVER:
                return "Server";
            case SUPERVISOR:
                return "Supervisor";
            case OBSERVER:
                return "Observer";
            default:
                return "Unknown";
        }
    }
}
