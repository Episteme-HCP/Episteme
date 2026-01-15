package org.jscience.astronomy;

import com.sun.j3d.utils.behaviors.interpolators.RotPosScaleTCBSplinePathInterpolator;
import com.sun.j3d.utils.geometry.Sphere;
import org.jscience.chemistry.ChemistryConstants;
import org.jscience.physics.PhysicsConstants;
import org.jscience.physics.waves.BlackBody;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The NaturalSatellite class provides support for the category of AstralBody of the same name.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a natural satellite orbitting in the star system
//may be a sun, planet or a moon of a planet or a moon of a moon... etc
public class NaturalSatellite extends AstralBody {

    public final static String TELLURIC = "Telluric";
    public final static String GAZEOUS = "Gazeous";//or jovian

    private double radius;//defaults to 5000,000m
    private double rotationPeriod;//defaults to 3600*24
    private double tilt;//defaults to 0
    private double flattening;//defaults to 0
    private double albedo;//defaults to 0.3
    private double age;//in seconds from 1/1/1970
    private String composition;//defaults to empty string
    private double temperature;//defaults to 3K, external temperature
    private boolean atmosphere;//defaults to false

    private final int SMALLEST = 1;

    private String textureName;
    private Texture2D texture;

    public NaturalSatellite(String name) {

        super(name);
        this.radius = 5000000;
        this.rotationPeriod = 3600 * 24;
        this.tilt = 0;
        this.flattening = 0;
        this.albedo = 0.3;
        this.age = 0;
        this.composition = new String("");
        this.temperature = 3;
        this.atmosphere = false;
        this.textureName = null;
        this.texture = null;

    }

    public NaturalSatellite(String name, double radius, double rotationPeriod, double tilt, double age, String composition) {

        super(name);
        this.radius = radius;
        this.rotationPeriod = rotationPeriod;
        this.tilt = tilt;
        this.flattening = 0;
        this.albedo = 0.3;
        this.age = age;
        this.composition = composition;
        this.temperature = 3;
        this.atmosphere = false;
        this.textureName = null;
        this.texture = null;

    }

    public NaturalSatellite(String name, double mass, double radius, double rotationPeriod, double tilt, double age, String composition) {

        super(name, mass);
        this.radius = radius;
        this.rotationPeriod = rotationPeriod;
        this.tilt = tilt;
        this.flattening = 0;
        this.albedo = 0.3;
        this.age = age;
        this.composition = composition;
        this.temperature = 3;
        this.atmosphere = false;
        this.textureName = null;
        this.texture = null;

    }

    public NaturalSatellite(String name, Group shape, double mass, double radius, double rotationPeriod, double tilt, double age, String composition) {

        super(name, shape, mass);
        this.radius = radius;
        this.rotationPeriod = rotationPeriod;
        this.tilt = tilt;
        this.flattening = 0;
        this.albedo = 0.3;
        this.age = age;
        this.composition = composition;
        this.temperature = 3;
        this.atmosphere = false;
        this.textureName = null;
        this.texture = null;

    }

    //iterator through direct children
    public Set getArtificialSatellites() {

        Iterator iterator;
        Object currentElement;
        HashSet result;

        result = new HashSet();
        if (hasChild()) {
            iterator = getChildren().iterator();
            while (iterator.hasNext()) {
                currentElement = iterator.next();
                if (currentElement instanceof ArtificialSatellite) {
                    result.add(currentElement);
                }
            }
        }

        return result;

    }

    public double getRadius() {

        return radius;

    }

    public void setRadius(double radius) {

        this.radius = radius;

    }

    public double computeVolume() {

        return (4 / 3) * Math.PI * getRadius() * getRadius() * getRadius();

    }

    //rotation period (day duration)
    public double getRotationPeriod() {

        return rotationPeriod;

    }

    public void setRotationPeriod(double rotationPeriod) {

        this.rotationPeriod = rotationPeriod;

    }

    public double getTilt() {

        return tilt;

    }

    public void setTilt(double tilt) {

        this.tilt = tilt;

    }

    public double getFlattening() {

        return flattening;

    }

    public void setFlattening(double flattening) {

        this.flattening = flattening;

    }

    public double getAlbedo() {

        return albedo;

    }

    public void setAlbedo(double albedo) {

        this.albedo = albedo;

    }

    //uses parents and children which therefore must be set
    public boolean isPlanet() {

        return ((getParent() != null) && (getParent().getParent() == null));
        //return getClass()!=Star.getClass()

    }

    //uses parents and children which therefore must be set
    public boolean isMoon() {

        return ((getParent() != null) && (getParent().getParent() != null));

    }

    //uses parents and children which therefore must be set
    public boolean hasMoons() {

        return ((getChildren() != null) && (getChildren().size() > 0));

    }

    public double getAge() {

        return age;

    }

    public void setAge(double age) {

        this.age = age;

    }

    public double computeSurfaceGravity() {

        return PhysicsConstants.GRAVITATION * getMass() / (getRadius() * getRadius());

    }

    //we should provide a bunch of methods to manipulate magnetic field
    //for example getters and setters for magnetic north and south poles,
    //global magnetic field dipolar value
    //as well as computation of the estimated magnetic field value and vector at a given point
    //in the local latitude, longitude coordinate system

    public double computeEscapeVelocity() {

        return Math.sqrt(2 * PhysicsConstants.GRAVITATION * getMass() / getRadius());

    }

    public double computeDensity() {

        return getMass() / computeVolume();
    }

    public String getComposition() {

        return composition;

    }

    public void setComposition(String composition) {

        if (composition != null) {
            this.composition = composition;
        } else
            throw new IllegalArgumentException("You can't set a null composition");

    }

    public double getTemperature() {

        return temperature;

    }

    public void setTemperature(double temperature) {

        this.temperature = temperature;

    }

    //per square meter
    public double computeEmittedLight() {

        BlackBody blackBody;

        blackBody = new BlackBody(getTemperature());

        return blackBody.computeAllEmittedLight();

    }

//we should also provide a method to retrieve sun rises and sun sets for every star at a given point
//around a given date
//public Date getSunrise(double latitude, double longitude, Date date)

    //result in Joules per second (Watts)
    //http://www.ldeo.columbia.edu/~visbeck/csss/ppt/Lecture2.ppt
    //http://janus.astro.umd.edu/astro/equationshelp.html

    public double computeAllEmittedLight() {

        return computeEmittedLight() * 4 * Math.PI * getRadius() * getRadius();

    }

    //0 if not in a starSystem (wanderingObject)
    //result in Joules per second (Watts)
    //this shouldn't be a star itself
    //the result does NOT take albedo into account, multiply by (1- getAlbedo()) to get actual energy
    public double computeAllReceivedLight() {

        return computeReceivedLight() * Math.PI * getRadius() * getRadius();

    }

    //we should also offer to compute received light per square meter at a given point on the surface of the NaturalSatellite
    //given it's local coordinate system
//http://solar.anu.edu.au/Sun/Irrad/Irradiation.html
    //public double computeReceivedLight(double longitude, double latitude, Date date)

    //this does not take into account the atmospheric composition and pressure which might lead to greeen house effect and therefore a higher result
    //multiply by Math.pow(2, 1/4) to have a simple greenhouse effect
    //but takes albedo into account
    //www.ldeo.columbia.edu/~visbeck/csss/ppt/Lecture2.ppt

    public double computeTemperature() {

        //getAlbedo(): reflexion coefficient
        return Math.pow(computeReceivedLight() * (1 - getAlbedo()) / (4 * ChemistryConstants.STEFAN_BOLTZMANN), 1 / 4);

    }

    //mainly for meteorits display
    public boolean hasAtmosphere() {

        return atmosphere;

    }

    /**
     * //http://janus.astro.umd.edu/astro/equationshelp.html
     * public double computeAtmosphereHeight() {
     *
     * //getMeanGazWeight(): mean molecular weight of the gas in kilos
     * //getTemperature(): surface temperature
     * return ChemistryConstants.BOLTZMANN*getTemperature()/(getMeanGazWeight()*computeSurfaceGravity());
     *
     * }
     **/

    /**
     * //http://www.personal.usyd.edu.au/~gerhard/pressure.html
     * public double computeAtmosphericPressure(double altitude) {
     * <p/>
     * //getGroundPressure(): pressure at altitude 0
     * return getGroundPressure() * Math.exp( - (computeSurfaceGravity() / PhysicsConstants.G) * altitude / computeAtmosphereHeight());
     * <p/>
     * }
     */

    //http://en.wikipedia.org/wiki/Gravitational_binding_energy
    //For a uniform, spherical mass
    //also see http://www.stardestroyer.net/Empire/Tech/Beam/DeathStar.html
    public double computeGravitationalBindingEnergy() {

        return 3 * PhysicsConstants.G * getMass() * getMass() / (5 * getRadius());

    }

    //http://en.wikipedia.org/wiki/Roche_limit
    //uses getStar()
    public double computeRigidRocheLimit() {

        return getStar().getRadius() * Math.pow(2 * getStar().computeDensity() / computeDensity(), 1 / 3);

    }

    //http://en.wikipedia.org/wiki/Roche_limit
    //uses getStar()
    public double computeFluidRocheLimit() {

        return getStar().getRadius() * Math.pow(2.423 * getStar().computeDensity() / computeDensity(), 1 / 3) * Math.pow(((1 + getMass() / (3 * getStar().getMass())) + getStar().getFlattening() * (1 + getMass() / getStar().getMass()) / 3) / (1 - getStar().getFlattening()), 1 / 3);

    }

    //http://en.wikipedia.org/wiki/Hill_sphere
    public double computeHillsphere() {

        return Math.pow(computeDistance(getStar()), 3) * Math.sqrt(getMass() / (3 * getStar().getMass()));

    }

    //through iteration of direct children
    public boolean hasRings() {

        Iterator iterator;
        boolean found;

        found = false;
        if (hasChild()) {
            iterator = getChildren().iterator();
            while (iterator.hasNext() && !found) {
                found = iterator.next() instanceof Rings;
            }
        }

        return found;

    }

    //Mean period, with which (helio)centric conjunctions of two bodies repeat, is called synodic period.
    //Synodic (relative) period of two periods P,Q is period:
    //(P,Q) = 1/(1/P-1/Q)= P*Q/(Q-P).
    //We designate synodic period with parentheses ().
    //For any periods A,B and constant k it holds:
    //(A,B) = -(B,A)
    //(k*A,k*B) = k*(A,B)
    //((A,M),(B,M)) = (A,B).
    //It is often implied, one period is orbital period of Earth.
    //returns the syndoic period with this assuming that the argument is orbiting around the same object
    public double computeSynodicPeriod(NaturalSatellite naturalSatellite) {

        if (naturalSatellite != null) {
            if (naturalSatellite.getParent() == this.getParent()) {
                return computeOrbitalPeriod() * naturalSatellite.computeOrbitalPeriod() / (naturalSatellite.computeOrbitalPeriod() - computeOrbitalPeriod());
            } else
                throw new IllegalArgumentException("This and NaturalSatellite must have the same parent.");
        } else
            throw new IllegalArgumentException("You can't compute a synodic period for a null NaturalSatellite.");

    }

     //this is above ground level

    public double computeGeostationaryAltitude() {

        //http://www.satcom.co.uk/article.asp?article=11
        return (Math.pow(PhysicsConstants.GRAVITATION * getMass() * getRotationPeriod() * getRotationPeriod() / (4 * Math.PI * Math.PI), 1 / 3)) - getRadius();

    }

    protected void createPlanet(float planetSize, int selfRotSpeed, String textureFile, TransformGroup group, boolean emit) {

        TransformGroup lastGroup;
        BoundingSphere bounds;
        Transform3D rotTrans;
        TransformGroup rotGroup;
        Alpha rotAlpha;
        RotationInterpolator rotator;
        Sphere planet;
        PointLight sunLight;

        bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        if (selfRotSpeed != 0) {
            rotTrans = new Transform3D();
            rotGroup = new TransformGroup(rotTrans);
            rotAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, selfRotSpeed, 0, 0, 0, 0, 0);
            rotator = new RotationInterpolator(rotAlpha, rotGroup);
            rotator.setSchedulingBounds(bounds);
            rotGroup.addChild(rotator);
            rotGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            group.addChild(rotGroup);
            lastGroup = rotGroup;
        } else {
            lastGroup = group;
        }

        planet = new Sphere(planetSize, Sphere.GENERATE_NORMALS + Sphere.GENERATE_TEXTURE_COORDS, 25, loadTexture(textureFile, emit));
        lastGroup.addChild(planet);

        if (emit) {
            sunLight = new PointLight(true, new Color3f(1.0f, 1.0f, 1.0f), new Point3f(0.0f, 0.0f, 0.0f), new Point3f(1.0f, 0.0f, 0.0f));
            sunLight.setInfluencingBounds(bounds);
            lastGroup.addChild(sunLight);
        }

    }

    protected void updateShape() {

        super.updateShape();

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
        XXX
        Transform3D earthRotTrans = new Transform3D();
        TransformGroup earthRotGroup = new TransformGroup(earthRotTrans);
        Alpha earthAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, 48000, 0, 0, 0, 0, 0);
        RotationInterpolator rotator = new RotationInterpolator(earthAlpha, earthRotGroup);
        rotator.setSchedulingBounds(bounds);
        earthRotGroup.addChild(rotator);
        earthRotGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        sunGroup.addChild(earthRotGroup);

    }


    /**
     * Set the appearance of this planet (sphere) whith the right texture.
     *
     * @return the Appearance of the planet with his right texture.
     */
    private Appearance createAppearance() {
        // Ambient-diffuse-reflection coefficien
        // t
        Color3f diffAmb = new Color3f(1.0f, 1.0f, 1.0f); // white

        // Diffuse-reflection coefficient
        Color3f reflDiff = new Color3f(1.0f, 1.0f, 1.0f); // white

        // Specular-reflection coefficient (reflectance function)
        Color3f reflSpec = new Color3f(1.0f, 1.0f, 1.0f); // white

        // c = shininess: cos^c in the specular reflection
        float c = 1;

        // Emitted light
        Color3f lumEmise = new Color3f(0.2f, 0.2f, 0.2f); // black

        Appearance appearance = new Appearance();

        // Set up the optical properties.
        appearance.setMaterial(new Material(diffAmb, lumEmise, reflDiff,
                reflSpec, c));

        Texture2D texture = getTexture();

        if (texture != null) {
            // Set up the texture
            appearance.setTexture(texture);

            // Application mode of the texture
            TextureAttributes texAttr = new TextureAttributes();
            texAttr.setTextureMode(TextureAttributes.MODULATE); // there still are: BLEND, DECAL,

            //                  and REPLACE
            appearance.setTextureAttributes(texAttr);
        }

        return appearance;

    }

    /**
     * Returns the texture of the current planet, assuming a name has been set using setTextureName()
     *
     * @return the texture of the current planet.
     */
    public Texture2D getTexture() {

        if (texture==null) {
         if (textureName!=null) {
        // Loading of the texture
        AstronomyTextureLoader newTextureLoader = new AstronomyTextureLoader(texture, AstronomyTextureLoader.GENERATE_MIPMAP);

        ImageComponent2D image;

        try {
            image = newTextureLoader.getImage();
        } catch (NullPointerException e) {
            return null;
        }

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

         texture = new Texture2D(Texture.MULTI_LEVEL_MIPMAP,
                Texture.RGBA, imageWidth, imageHeight);
        int imageLevel = 0;

        texture.setImage(imageLevel, image);

        while ((imageWidth > SMALLEST) || (imageWidth > SMALLEST)) {
            imageLevel++;

            if (imageWidth > SMALLEST) {
                imageWidth /= 2;
            }

            if (imageHeight > SMALLEST) {
                imageHeight /= 2;
            }

            image = newTextureLoader.getScaledImage(imageWidth, imageHeight);
            texture.setImage(imageLevel, image);
        }

        texture.setMagFilter(Texture.MULTI_LEVEL_POINT);
        texture.setMinFilter(Texture.MULTI_LEVEL_POINT);

        return texture;
        } else {
            return null;
        }
        } else {
            return texture;
        }
    }

    //may return null
    public String getTextureName() {
         return textureName;
    }

    public void setTextureName(String textureName) {
        if ((texture!=null) && (this.textureName!=textureName)) {
            this.texture = null;
        }
        this.textureName = textureName;
    }

    
    private RotPosScaleTCBSplinePathInterpolator spline;

    private RotationInterpolator rotator;
    private Alpha                rotationAlpha;
    private Alpha                animAlpha;
    private long                 alphaTime = 0;
    private Date date;

    private BranchGroup    removableCoordBG;
    private BranchGroup    removableOrbitBG;
    private TransformGroup trGrRotAndLeafs;
    private TransformGroup trGrOrbit;
    private TransformGroup trGrTendSpline;

    private ColorOrbit colorOrbit;

    private Planet planet;

    private ObjectsPositions     objPos;
    private ObjectsInformation   objInfo;
    private InitializationObject iniObject;


    /**
     * Initializes a new ScenePlanet.
     *
     * @param planet   a object reference of a given planet.
     * @param objPos   a reference to the concrete subject of
     *                 the observer pattern positions
     * @param objInfo  a reference to the concrete subject of
     *                 the observer pattern information
     */
    public ScenePlanet(Planet planet, ObjectsPositions objPos,
                       ObjectsInformation objInfo ) {
       this.planet = planet;
       this.objPos = objPos;
       this.objPos.attach(this);
       this.objInfo = objInfo;
       this.objInfo.attach(this);
       this.iniObject = objInfo.getInitializationObject();
    }

    /**
     * .Removes this object from the position observer list.
     */
    public void destroy() {
          objPos.detach(this);
       objInfo.detach(this);
       planet = null;
    }

    /**
     * Updates the position state of the object.<br>
     *
     * @param orbitPos a reference to the list containing all position observer
     */
    public void update(OrbitPositions orbitPos) {

    }

    /**
     * Updates the information state of the object.<br>
     *
     * @param id        identification of the object that has to be updated
     * @param parameter the parameter that has been updated
     */
    public void update( String id, int parameter ) {

       if ( id.equals( planet.getId() ) ||
           id.equals("all") ) {

           IniData data = (IniData)objInfo.getParameter( XMLConstants.PLANET, planet.getId() );

           switch ( parameter ) {
              case XMLConstants.VISIBLE :
                   // implemented inside SceneSolarSystem
                   break;
              case XMLConstants.COORDINATESYSTEM :
                   if(data.hasCoordinateSystem()) {
                      addCoord();
                   } else {
                      removeCoord();
                   }
                   break;
              case XMLConstants.ORBIT :
                   if(data.hasOrbit()) {
                      addOrbit();
                   } else {
                      removeOrbit();
                   }
                   break;
              case XMLConstants.COLORORBIT :
                   setOrbitColor(data.getColorOrbit());
                   break;
              case XMLConstants.ANIMATIONSPEED :
                   setAnimSpeed();
                   break;
              default : break;

           }
       }

    }

    /**
     * Returns the ID of the current planet.
     *
     * @return the ID to idendification the planet.
     */
    public String getId() {
       return planet.getId();
    }

    /**
     * Returns a CelestialObjectInfo of the current planet.
     * This object has all specific information of the current planet.
     *
     * @return a CelestialObjectInfo of the current planet.
     */
    public CelestialObjectInfo getInfo() {
       return planet.getInfo();
    }

    /**
     * Add the coordinate system to this scenegraph.
     */
    public void addCoord(){
            trGrRotAndLeafs.addChild(removableCoordBG);
    }

    /**
     * Remove the coordinate system from this scenegraph.
     */
    public void removeCoord(){
            removableCoordBG.detach();
    }

    /**
     * Add the color orbit to this scenegraph.
     */
    public void addOrbit(){
            trGrOrbit.addChild(removableOrbitBG);
    }

    /**
     * Remove the color orbit from this scenegraph.
     */
    public void removeOrbit(){
            removableOrbitBG.detach();
    }

    /**
     * Set a new orbit color.
     *
     * @param orbitColor the new color of the orbit
     */
    public void setOrbitColor(Color orbitColor){
            colorOrbit.setColor(orbitColor);
    }

    /**
     * Get the current position of the natural satellite.
     *
     * @return a Transform3D with the current position.
     */
    public Transform3D getCurrentPosition(){
            Transform3D transVec=new Transform3D();
    Transform3D transRot=new Transform3D();
    trGrTendSpline.getTransform(transVec);
    trGrRotAndLeafs.getTransform(transRot);

    Vector3d vec=new Vector3d();
    transVec.get(vec);
    transRot.setTranslation(vec);

    return transRot;
    }

    /**
     * This method handle the animation and rotation of the celestial objects.
     *
     * @param animate if true, start animation; if false stop animation.
     * @param rotate  if true, start rotation; if false stop rotation.
     */
    public void setAnimation(boolean animate, boolean rotate) {
        date = new Date();

        if (animate) {
            /*
               Set the stop time for the animation and rotation.
               Without this trick, the celestial objects are jumping around.
               Because when the animation is stop, the objects goes on
               in the background.
            */
            alphaTime = date.getTime() - alphaTime;
            rotationAlpha.setStartTime(alphaTime);
            animAlpha.setStartTime(alphaTime);
        } else {
            // save the stop time for the animation and rotation.
            alphaTime = animAlpha.getStartTime();
            alphaTime = date.getTime() - alphaTime;
        }

        // set the animation and rotation to stop or go.
        spline.setEnable(animate);
        rotator.setEnable(rotate);
    }

    /**
     * Set the animation speed for the planet.
     */
    public void setAnimSpeed() {
        date = new Date();

        AnimationSpeed animationSpeed = (AnimationSpeed) objInfo.getInitializationObject()
                .getParameter(XMLConstants.ANIMATIONSPEED);
        int factor = 0;

        switch (animationSpeed.getType()) {
            case AnimationSpeed.DAYS_PER_SECOND:
                factor = 1;

                break;

            case AnimationSpeed.HOURS_PER_SECOND:
                factor = 24;

                break;

            case AnimationSpeed.MINUTES_PER_SECOND:
                factor = 24 * 60;

                break;

            default:
        }

        int animSpeed = animationSpeed.getValue();
        long duration = (long) ((planet.getRotOwnAxis() * factor) / animSpeed);

        long increasing = rotationAlpha.getIncreasingAlphaDuration();

        // delta t = t1*(1-T2/T1)
        long diff = (long) ((date.getTime() - rotationAlpha.getStartTime()) * (1.0f -
                ((float) duration / (float) increasing)));
        rotationAlpha.setIncreasingAlphaDuration(duration);
        rotationAlpha.setStartTime(rotationAlpha.getStartTime() + diff);

        duration = (long) ((planet.getRotOrbit() * factor) / animSpeed);

        increasing = animAlpha.getIncreasingAlphaDuration();

        // delta t = t1*(1-T2/T1)
        diff = (long) ((date.getTime() - animAlpha.getStartTime()) * (1.0f -
                ((float) duration / (float) increasing)));

        animAlpha.setIncreasingAlphaDuration(duration);
        animAlpha.setStartTime(animAlpha.getStartTime() + diff);
    }

    /**
     * Returns the BranchGroup of the created planet scene. It has a planet as
     * leaf, make a rotation to the own axis and make with a TCB-spline
     * function a rotation around the sun.
     *
     * @return the BranchGroup of the given planet.
     */
    public BranchGroup createSceneGraph() {
        // A BoundingSphere instance as general bounding region.
        BoundingSphere boundsGen = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                SSS3dConstants.BOUNDRADIUS);

        // Create the first TransformGroup node trGrRotAndLeafs for the planet,
        // the coordinate system and the rotation
        trGrRotAndLeafs = new TransformGroup();

        // With the ALLOW_TRANSFORM_READ and ALLOW_TRANSFORM_WRITE
        // capabilities, we allow the modification of the TransformGroup's
        // code by the Behavior's code at run time.
        trGrRotAndLeafs.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        trGrRotAndLeafs.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        trGrRotAndLeafs.setCapability(Group.ALLOW_CHILDREN_READ);
        trGrRotAndLeafs.setCapability(Group.ALLOW_CHILDREN_WRITE);
        trGrRotAndLeafs.setCapability(Group.ALLOW_CHILDREN_EXTEND);

        // Attach the ring of the Saturn
        if (getId().equals("saturn")) {
            TransformGroup trGrRing = new TransformGroup();

            // Set the picking of the CoordinateSystem BranchGroup to false.
            trGrRing.setPickable(false);

            SaturnRing ring = new SaturnRing(1.3, 2.0,
                    new Color3f(0.99f, 0.88f, 0.79f), 160);
            trGrRing.addChild(ring);

            // Duplicate the ring strip geometry and set the
            // appearance of the new Shape3D object without culling.
            // This is done because we will see the ring on both side!!!!
            PolygonAttributes polyAttrib = new PolygonAttributes();
            polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);

            Appearance polyAppear = ring.getAppearance();
            polyAppear.setPolygonAttributes(polyAttrib);
            trGrRing.addChild(new Shape3D(ring.getGeometry(), polyAppear));
            trGrRotAndLeafs.addChild(trGrRing);
        }

        // Attach the leaf node "planet" to the TransformGroup trGrRotAndLeafs.
        trGrRotAndLeafs.addChild(planet);

        removableCoordBG = new BranchGroup();

        // Create and attach a coordinate system to the TransformGroup node
        // trGrRotAndLeafs, that is to the planet.
        removableCoordBG.addChild(new CoordinateSystem(1.2f));

        // Allow to detach the Coordinate System
        removableCoordBG.setCapability(BranchGroup.ALLOW_DETACH);

        // Set the picking of the CoordinateSystem BranchGroup to false.
        removableCoordBG.setPickable(false);

        trGrRotAndLeafs.addChild(removableCoordBG);

        // Prepare the RotationInterpolator (Behavior) for the
        // planet's rotation about its own axis.
        Transform3D tr3Drot = new Transform3D();

        float startRot;
        float endRot;

        if (planet.getRotOwnAxis() > 0) {
            // Create the alpha(t) function. Positiv rotation!
            rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0,

                    /*planet.getRotOwnAxis()*/ Long.MAX_VALUE, 0, 0, 0, 0, 0);
            startRot = 0.0f;
            endRot = (float) (2.0 * Math.PI);
        } else {
            // Create the alpha(t) function. Negativ rotation!
            rotationAlpha = new Alpha(-1, Alpha.DECREASING_ENABLE, 0, 0, 0, 0,
                    0,
                    /*Math.abs(planet.getRotOwnAxis())*/ Long.MAX_VALUE, 0, 0);
            startRot = (float) (2.0 * Math.PI);
            endRot = 0.0f;
        }

        // Create the planet's rotation about its own axis.
        rotator = new RotationInterpolator(rotationAlpha, trGrRotAndLeafs,
                tr3Drot, startRot, endRot);
        rotator.setSchedulingBounds(boundsGen);
        trGrRotAndLeafs.addChild(rotator);

        // Create the second TransformGroup node trGrTendSpline for the planet's
        // rotation around the sun and the tend.
        trGrTendSpline = new TransformGroup();

        // With the ALLOW_TRANSFORM_READ and ALLOW_TRANSFORM_WRITE
        // capabilities, we allow the modification of the TransformGroup's
        // code by the Behavior's code at run time.
        trGrTendSpline.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        trGrTendSpline.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // Attach trGrRotAndLeafs node to the trGrTendSpline node.
        trGrTendSpline.addChild(trGrRotAndLeafs);

        Transform3D yAxis = new Transform3D();

        // Create the alpha(t) function.
        animAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0,

                /*planet.getRotOrbit()*/ Long.MAX_VALUE, 0, 0, 0, 0, 0);

        // rotate the planet to his true tend
        Matrix4d mat = new Matrix4d();
        mat.rotX(Math.toRadians(planet.getDegree()));

        Quat4f r = new Quat4f();
        r.set(mat);

        // set scale of the planet
        float scale;
        boolean compressed = ((Boolean) iniObject.getParameter(XMLConstants.COMPRESSED)).booleanValue();

        if (compressed) {
            scale = planet.getLogRadius();
        } else {
            scale = planet.getRadius();
        }

        Point3f s = new Point3f(scale, scale, scale);

        Double time = (Double) iniObject.getParameter(XMLConstants.JDAY);

        // take the positions of the planet
        Point3f[] pos = objPos.getPositions(getInfo(), time.doubleValue(),
                compressed);
        int pointNrs = pos.length;
        float stepLength = 1 / (float) pointNrs;
        float stepPos = 0.0f;

        // save the positions in the TCBKeyFrame
        TCBKeyFrame[] keyFrames = new TCBKeyFrame[pointNrs + 1];

        for (int i = 0; i < pointNrs; i++) {
            keyFrames[i] = new TCBKeyFrame(stepPos, 0, pos[i], r, s, 0.0f,
                    0.0f, 0.0f);
            stepPos += stepLength;
        }

        keyFrames[pointNrs] = new TCBKeyFrame(1.0f, 0, pos[0], r, s, 0.0f,
                0.0f, 0.0f);

        // make a spline courve of the position points
        spline = new RotPosScaleTCBSplinePathInterpolator(animAlpha,
                trGrTendSpline, yAxis, keyFrames);
        spline.setSchedulingBounds(boundsGen);
        trGrTendSpline.addChild(spline);

        // Create the TransformGroup node trGrOrbit for the planet orbit.
        trGrOrbit = new TransformGroup();

        trGrOrbit.addChild(trGrTendSpline);

        trGrOrbit.setCapability(Group.ALLOW_CHILDREN_READ);
        trGrOrbit.setCapability(Group.ALLOW_CHILDREN_WRITE);
        trGrOrbit.setCapability(Group.ALLOW_CHILDREN_EXTEND);

        removableOrbitBG = new BranchGroup();

        colorOrbit = new ColorLineOrbit(Color.red);

        colorOrbit.setPositions(pos);

        // Attach the leaf node "orbit" to the BranchGroup removableOrbitBG.
        removableOrbitBG.addChild(colorOrbit);

        removableOrbitBG.setCapability(BranchGroup.ALLOW_DETACH);

        // Set the picking of the ColorOrbit BranchGroup to false.
        removableOrbitBG.setPickable(false);

        trGrOrbit.addChild(removableOrbitBG);

        BranchGroup brGrAll = new BranchGroup();
        brGrAll.addChild(trGrOrbit);

        brGrAll.setCapability(BranchGroup.ALLOW_DETACH);

        // Return the final version of the BranchGroup node brGrAll.
        return brGrAll;
    }
}


