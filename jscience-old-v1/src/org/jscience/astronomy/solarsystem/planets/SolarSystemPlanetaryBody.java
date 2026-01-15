package org.jscience.astronomy.solarsystem.planets;

import org.jscience.astronomy.NaturalSatellite;


/**
 * The SolarSystemPlanetaryBody class provides some data fields for bodies
 * in our star system (primarily).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//some of the fields are actually set using the parent class
//name
//mass
//volumetric mean radius by radius
//Length of day by rotationPeriod
//Obliquity to orbit by tilt
//On the contrary we have to supply some data by ourselves
//age
//composition
//position (supplied by factories)
//velocity (supplied by factories)
//charge (always nil anyway)
//TODO We need to convert units here or in the XML files to MKSA (the Systeme International SI we use):
//mass (1E24 kg)
//volume (1E10 km3)
//equatorial radius (km)
//polar radius (km)
//volumetric mean radius (km)
//ellipticity (Flattening) (ratio)
//mean density (kg/m3)
//surface gravity (eq.) (m/s2)
//surface acceleration (eq.) (m/s2)
//escape velocity (km/s)
//GM (x 1E6 km3/s2)
//bond albedo (ratio ?)
//visual geometric albedo (ratio ?)
//visual magnitude V(1,0)
//solar irradiance (W/m2)
//black_body temperature (K)
//moment of inertia (I/MR2)
//J2 (x 10_6)
//Semi_major_axis (1E6 km)
//Sidereal_orbit_period (days)
//Tropical_orbit_period (days)
//Perihelion (1E6 km)
//Aphelion (1E6 km)
//Synodic period (days)
//Mean orbital velocity (km/s)
//Max. orbital velocity (km/s)
//Min. orbital velocity (km/s)
//Orbit inclination (deg)
//Orbit eccentricity
//Sidereal rotation period (hrs)
//Length of day (hrs)
//Obliquity to orbit (deg)
//Surface pressure bar
//Average temperature K
public class SolarSystemPlanetaryBody extends NaturalSatellite {
    /** The volume for that body. */
    private double volume;

    /** The equatorial radius for that body. */
    private double equatorial_radius;

    /** The polar radius for that body. */
    private double polar_radius;

    /** The volumetric mean radius for that body. */
    private double volumetric_mean_radius;

    /** The ellipticity for that body. */
    private double ellipticity;

    /** The mean density for that body. */
    private double mean_density;

    /** The surface gravity for that body. */
    private double surface_gravity;

    /** The surface acceleration for that body. */
    private double surface_acceleration;

    /** The escape velocity for that body. */
    private double escape_velocity;

    /** The GM for that body. */
    private double GM;

    /** The bond albedo for that body. */
    private double bond_albedo;

    /** The visual geometric albedo for that body. */
    private double visual_geometric_albedo;

    /** The visual magnitude for that body. */
    private double visual_magnitude;

    /** The solar irradiance for that body. */
    private double solar_irradiance;

    /** The black_body temperature for that body. */
    private double black_body_temperature;

    /** The moment of inertia for that body. */
    private double moment_of_inertia;

    /** The J2 for that body. */
    private double J2;

    /** The number of natural satellites for that body. */
    private int number_of_natural_satellites;

    /** The planetary ring system for that body. */
    private boolean planetary_ring_system;

    /** The semi_major axis for that body. */
    private double semi_major_axis;

    /** The sidereal orbit period for that body. */
    private double sidereal_orbit_period;

    /** The tropical orbit period for that body. */
    private double tropical_orbit_period;

    /** The perihelion for that body. */
    private double perihelion;

    /** The aphelion for that body. */
    private double aphelion;

    /** The synodic period for that body. */
    private double synodic_period;

    /** The mean orbital velocity for that body. */
    private double mean_orbital_velocity;

    /** The maximum orbital velocity for that body. */
    private double maximum_orbital_velocity;

    /** The minimum orbital velocity for that body. */
    private double minimum_orbital_velocity;

    /** The orbit inclination for that body. */
    private double orbit_inclination;

    /** The orbit eccentricity for that body. */
    private double orbit_eccentricity;

    /** The sidereal rotation period for that body. */
    private double sidereal_rotation_period;

    /** The surface pressure for that body. */
    private double surface_pressure;

    /** The average temperature for that body. */
    private double average_temperature;

    /** The atmospheric composition for that body. */
    private String atmospheric_composition;

/**
     * Constructs an SolarSystemPlanetaryBody.
     *
     * @param name DOCUMENT ME!
     */
    public SolarSystemPlanetaryBody(String name) {
        super(name);
    }

/**
     * Constructs an SolarSystemPlanetaryBody.
     *
     * @param name           DOCUMENT ME!
     * @param mass           DOCUMENT ME!
     * @param radius         DOCUMENT ME!
     * @param rotationPeriod DOCUMENT ME!
     * @param tilt           DOCUMENT ME!
     * @param age            DOCUMENT ME!
     * @param composition    DOCUMENT ME!
     */
    public SolarSystemPlanetaryBody(String name, double mass, double radius,
        double rotationPeriod, double tilt, double age, String composition) {
        super(name, mass, radius, rotationPeriod, tilt, age, composition);
    }

    /**
     * returns the volume for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getVolume() {
        return volume;
    }

    /**
     * sets the volume for that body.
     *
     * @param volume DOCUMENT ME!
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * returns the equatorial radius for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getEquatorialRadius() {
        return equatorial_radius;
    }

    /**
     * sets the equatorial radius for that body.
     *
     * @param equatorial_radius DOCUMENT ME!
     */
    public void setEquatorialRadius(double equatorial_radius) {
        this.equatorial_radius = equatorial_radius;
    }

    /**
     * returns the polar radius for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getPolarRadius() {
        return polar_radius;
    }

    /**
     * sets the polar radius for that body.
     *
     * @param polar_radius DOCUMENT ME!
     */
    public void setPolarRadius(double polar_radius) {
        this.polar_radius = polar_radius;
    }

    /**
     * returns the ellipticity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getEllipticity() {
        return ellipticity;
    }

    /**
     * sets the ellipticity for that body.
     *
     * @param ellipticity DOCUMENT ME!
     */
    public void setEllipticity(double ellipticity) {
        this.ellipticity = ellipticity;
    }

    /**
     * returns the mean density for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getMeanDensity() {
        return mean_density;
    }

    /**
     * sets the mean density for that body.
     *
     * @param mean_density DOCUMENT ME!
     */
    public void setMeanDensity(double mean_density) {
        this.mean_density = mean_density;
    }

    /**
     * returns the surface gravity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getSurfaceGravity() {
        return surface_gravity;
    }

    /**
     * sets the surface gravity for that body.
     *
     * @param surface_gravity DOCUMENT ME!
     */
    public void setSurfaceGravity(double surface_gravity) {
        this.surface_gravity = surface_gravity;
    }

    /**
     * returns the surface acceleration for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getSurfaceAcceleration() {
        return surface_acceleration;
    }

    /**
     * sets the surface acceleration for that body.
     *
     * @param surface_acceleration DOCUMENT ME!
     */
    public void setSurfaceAcceleration(double surface_acceleration) {
        this.surface_acceleration = surface_acceleration;
    }

    /**
     * returns the escape velocity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getEscapeVelocity() {
        return escape_velocity;
    }

    /**
     * sets the escape velocity for that body.
     *
     * @param escape_velocity DOCUMENT ME!
     */
    public void setEscapeVelocity(double escape_velocity) {
        this.escape_velocity = escape_velocity;
    }

    /**
     * returns the GM for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getGM() {
        return GM;
    }

    /**
     * sets the GM for that body.
     *
     * @param GM DOCUMENT ME!
     */
    public void setGM(double GM) {
        this.GM = GM;
    }

    /**
     * returns the bond albedo for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getBondAlbedo() {
        return bond_albedo;
    }

    /**
     * sets the bond albedo for that body.
     *
     * @param bond_albedo DOCUMENT ME!
     */
    public void setBondAlbedo(double bond_albedo) {
        this.bond_albedo = bond_albedo;
    }

    /**
     * returns the visual geometric albedo for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getVisualGeometricAlbedo() {
        return visual_geometric_albedo;
    }

    /**
     * sets the visual geometric albedo for that body.
     *
     * @param visual_geometric_albedo DOCUMENT ME!
     */
    public void setVisualGeometricAlbedo(double visual_geometric_albedo) {
        this.visual_geometric_albedo = visual_geometric_albedo;
    }

    /**
     * returns the visual magnitude for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getVisualMagnitude() {
        return visual_magnitude;
    }

    /**
     * sets the visual magnitude for that body.
     *
     * @param visual_magnitude DOCUMENT ME!
     */
    public void setVisualMagnitude(double visual_magnitude) {
        this.visual_magnitude = visual_magnitude;
    }

    /**
     * returns the solar_irradiance for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getSolarIrradiance() {
        return solar_irradiance;
    }

    /**
     * sets the solar_irradiance for that body.
     *
     * @param solar_irradiance DOCUMENT ME!
     */
    public void setSolarIrradiance(double solar_irradiance) {
        this.solar_irradiance = solar_irradiance;
    }

    /**
     * returns the black_body temperature for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getBlackBodyTemperature() {
        return black_body_temperature;
    }

    /**
     * sets the black_body temperature for that body.
     *
     * @param black_body_temperature DOCUMENT ME!
     */
    public void setBlackBodyTemperature(double black_body_temperature) {
        this.black_body_temperature = black_body_temperature;
    }

    /**
     * returns the moment of inertia for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getMomentOfInertia() {
        return moment_of_inertia;
    }

    /**
     * sets the moment of inertia for that body.
     *
     * @param moment_of_inertia DOCUMENT ME!
     */
    public void setMomentOfInertia(double moment_of_inertia) {
        this.moment_of_inertia = moment_of_inertia;
    }

    /**
     * returns the J2 for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getJ2() {
        return J2;
    }

    /**
     * sets the J2 for that body.
     *
     * @param J2 DOCUMENT ME!
     */
    public void setJ2(double J2) {
        this.J2 = J2;
    }

    /**
     * returns the number of natural satellites for that body.
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfNaturalSatellites() {
        return number_of_natural_satellites;
    }

    /**
     * sets the number of natural satellites for that body.
     *
     * @param number_of_natural_satellites DOCUMENT ME!
     */
    public void setNumberOfNaturalSatellites(int number_of_natural_satellites) {
        this.number_of_natural_satellites = number_of_natural_satellites;
    }

    /**
     * returns the planetary ring system for that body.
     *
     * @return DOCUMENT ME!
     */
    public boolean getPlanetaryRingSystem() {
        return planetary_ring_system;
    }

    /**
     * sets the planetary ring system for that body.
     *
     * @param planetary_ring_system DOCUMENT ME!
     */
    public void setPlanetaryRingSystem(boolean planetary_ring_system) {
        this.planetary_ring_system = planetary_ring_system;
    }

    /**
     * returns the semi_major axis for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getSemiMajorAxis() {
        return semi_major_axis;
    }

    /**
     * sets the semi_major axis for that body.
     *
     * @param semi_major_axis DOCUMENT ME!
     */
    public void setSemiMajorAxis(double semi_major_axis) {
        this.semi_major_axis = semi_major_axis;
    }

    /**
     * returns the sidereal orbit period for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getSiderealOrbitPeriod() {
        return sidereal_orbit_period;
    }

    /**
     * sets the sidereal orbit period for that body.
     *
     * @param sidereal_orbit_period DOCUMENT ME!
     */
    public void setSiderealOrbitPeriod(double sidereal_orbit_period) {
        this.sidereal_orbit_period = sidereal_orbit_period;
    }

    /**
     * returns the tropical orbit period for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getTropicalOrbitPeriod() {
        return tropical_orbit_period;
    }

    /**
     * sets the tropical orbit period for that body.
     *
     * @param tropical_orbit_period DOCUMENT ME!
     */
    public void setTropicalOrbitPeriod(double tropical_orbit_period) {
        this.tropical_orbit_period = tropical_orbit_period;
    }

    /**
     * returns the perihelion for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getPerihelion() {
        return perihelion;
    }

    /**
     * sets the perihelion for that body.
     *
     * @param perihelion DOCUMENT ME!
     */
    public void setPerihelion(double perihelion) {
        this.perihelion = perihelion;
    }

    /**
     * returns the aphelion for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getAphelion() {
        return aphelion;
    }

    /**
     * sets the aphelion for that body.
     *
     * @param aphelion DOCUMENT ME!
     */
    public void setAphelion(double aphelion) {
        this.aphelion = aphelion;
    }

    /**
     * returns the synodic period for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getSynodicPeriod() {
        return synodic_period;
    }

    /**
     * sets the synodic period for that body.
     *
     * @param synodic_period DOCUMENT ME!
     */
    public void setSynodicPeriod(double synodic_period) {
        this.synodic_period = synodic_period;
    }

    /**
     * returns the mean orbital velocity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getMeanOrbitalVelocity() {
        return mean_orbital_velocity;
    }

    /**
     * sets the mean orbital velocity for that body.
     *
     * @param mean_orbital_velocity DOCUMENT ME!
     */
    public void setMeanOrbitalVelocity(double mean_orbital_velocity) {
        this.mean_orbital_velocity = mean_orbital_velocity;
    }

    /**
     * returns the maximum orbital velocity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getMaximumOrbitalVelocity() {
        return maximum_orbital_velocity;
    }

    /**
     * sets the maximum orbital velocity for that body.
     *
     * @param maximum_orbital_velocity DOCUMENT ME!
     */
    public void setMaximumOrbitalVelocity(double maximum_orbital_velocity) {
        this.maximum_orbital_velocity = maximum_orbital_velocity;
    }

    /**
     * returns the minimum orbital velocity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getMinimumOrbitalVelocity() {
        return minimum_orbital_velocity;
    }

    /**
     * sets the minimum orbital velocity for that body.
     *
     * @param minimum_orbital_velocity DOCUMENT ME!
     */
    public void setMinimumOrbitalVelocity(double minimum_orbital_velocity) {
        this.minimum_orbital_velocity = minimum_orbital_velocity;
    }

    /**
     * returns the orbit inclination for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getOrbitInclination() {
        return orbit_inclination;
    }

    /**
     * sets the orbit inclination for that body.
     *
     * @param orbit_inclination DOCUMENT ME!
     */
    public void setOrbitInclination(double orbit_inclination) {
        this.orbit_inclination = orbit_inclination;
    }

    /**
     * returns the orbit eccentricity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getOrbitEccentricity() {
        return orbit_eccentricity;
    }

    /**
     * sets the orbit eccentricity for that body.
     *
     * @param orbit_eccentricity DOCUMENT ME!
     */
    public void setOrbitEccentricity(double orbit_eccentricity) {
        this.orbit_eccentricity = orbit_eccentricity;
    }

    /**
     * returns the sidereal rotation period for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getSiderealRotationPeriod() {
        return sidereal_rotation_period;
    }

    /**
     * sets the sidereal rotation period for that body.
     *
     * @param sidereal_rotation_period DOCUMENT ME!
     */
    public void setSiderealRotationPeriod(double sidereal_rotation_period) {
        this.sidereal_rotation_period = sidereal_rotation_period;
    }

    /**
     * returns the surface pressure for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getSurfacePressure() {
        return surface_pressure;
    }

    /**
     * sets the surface pressure for that body.
     *
     * @param surface_pressure DOCUMENT ME!
     */
    public void setSurfacePressure(double surface_pressure) {
        this.surface_pressure = surface_pressure;
    }

    /**
     * returns the average temperature for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getAverageTemperature() {
        return average_temperature;
    }

    /**
     * sets the average temperature for that body.
     *
     * @param average_temperature DOCUMENT ME!
     */
    public void setAverageTemperature(double average_temperature) {
        this.average_temperature = average_temperature;
    }

    /**
     * returns the atmospheric composition for that body.
     *
     * @return DOCUMENT ME!
     */
    public String getAtmosphericomposition() {
        return atmospheric_composition;
    }

    /**
     * sets the atmospheric composition for that body.
     *
     * @param atmospheric_composition DOCUMENT ME!
     */
    public void setAtmosphericomposition(String atmospheric_composition) {
        this.atmospheric_composition = atmospheric_composition;
    }
}
