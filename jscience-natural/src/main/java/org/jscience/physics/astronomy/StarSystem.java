package org.jscience.physics.astronomy;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Star System (e.g. Solar System).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class StarSystem {

    private String name;
    private List<Star> stars = new ArrayList<>();
    private List<CelestialBody> bodies = new ArrayList<>();

    public StarSystem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Star> getStars() {
        return stars;
    }

    public void addStar(Star star) {
        this.stars.add(star);
        if (!bodies.contains(star)) {
            bodies.add(star);
        }
    }

    public List<CelestialBody> getBodies() {
        return bodies;
    }

    public void addBody(CelestialBody body) {
        this.bodies.add(body);
    }

    public void removeBody(CelestialBody body) {
        this.bodies.remove(body);
    }
}
