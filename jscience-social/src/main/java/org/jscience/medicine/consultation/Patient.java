package org.jscience.medicine.consultation;

import org.jscience.biology.Individual;
import org.jscience.measure.Report;
import org.jscience.sociology.Role;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Relation;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;


/**
 * The Patient class provides some useful information about the health of
 * an individual.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//some variables are relevant only for vertebrates (but this is mostly the kind of cases a veterinary or physics has)
@Persistent
public class Patient extends Role {
    //http://en.wikipedia.org/wiki/Vital_signs
    @Attribute
    private float bloodPressure; //using standard international unit Pascal

    @Attribute
    private float cardiacRate; //beat per seconds, mean over short period of seconds

    @Attribute
    private float temperature; //kelvin degrees

    @Attribute
    private float normalTemperature; //kelvin degrees

    @Attribute
    private float respiratoryRate; //beats per seconds

    @Attribute
    private float painScale; //0 to 10

    @Attribute
    private float bloodOxygen; //percent of normal

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set currentPathologies; //diseases, defects,etc a Set of Pathologies

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set treatments;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Vector medicalRecords; //Set of Records

/**
     * Creates a new Patient object.
     *
     * @param individual DOCUMENT ME!
     * @param situation  DOCUMENT ME!
     */
    public Patient(Individual individual, MedicalSituation situation) {
        super(individual, "Patient", situation, Role.CLIENT);
        this.bloodPressure = 0;
        this.cardiacRate = 0;
        this.temperature = 0;
        this.normalTemperature = 0;
        this.respiratoryRate = 0;
        this.painScale = 0;
        this.bloodOxygen = 0;
        this.currentPathologies = Collections.EMPTY_SET;
        this.treatments = Collections.EMPTY_SET;
        this.medicalRecords = new Vector();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getBloodPressure() {
        return bloodPressure;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pressure DOCUMENT ME!
     */
    public void setBloodPressure(float pressure) {
        this.bloodPressure = pressure;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getCardiacRate() {
        return cardiacRate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rate DOCUMENT ME!
     */
    public void setCardiacRate(float rate) {
        this.cardiacRate = rate;
    }

    //we could also have a field for normal cardiac rate of an adult individual from this Species at rest
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getTemperature() {
        return temperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param temperature DOCUMENT ME!
     */
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    //the normal temperature for this Species, assuming a healthy adult individual
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getNormalTemperature() {
        return normalTemperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param normalTemperature DOCUMENT ME!
     */
    public void setNormalTemperature(float normalTemperature) {
        this.normalTemperature = normalTemperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getRespiratoryRate() {
        return respiratoryRate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param respiratoryRate DOCUMENT ME!
     */
    public void setRespiratoryRate(float respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getPainScale() {
        return painScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param painScale DOCUMENT ME!
     */
    public void setPainScale(float painScale) {
        this.painScale = painScale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getBloodOxygen() {
        return bloodOxygen;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bloodOxygen DOCUMENT ME!
     */
    public void setBloodOxygen(float bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    /**
     * Returns the set of current pathologies affecting the patient.
     *
     * @return an unmodifiable set of current pathologies
     */
    public Set<Pathology> getCurrentPathologies() {
        return Collections.unmodifiableSet(currentPathologies);
    }

    /**
     * Updates the set of current pathologies. Each element in the set must be a Pathology instance.
     *
     * @param pathologies the new set of pathologies
     * @throws NullPointerException if the input set is null
     */
    public void setCurrentPathologies(Set<Pathology> pathologies) {
        this.currentPathologies = new HashSet<>(Objects.requireNonNull(pathologies, "Pathologies set cannot be null"));
    }

    /**
     * Adds a pathology to the patient's current condition.
     *
     * @param pathology the pathology to add
     * @throws NullPointerException if the pathology is null
     */
    public void addPathology(Pathology pathology) {
        currentPathologies.add(Objects.requireNonNull(pathology, "Pathology cannot be null"));
    }

    /**
     * Removes a pathology from the patient's current condition.
     *
     * @param pathology the pathology to remove
     */
    public void removePathology(Pathology pathology) {
        currentPathologies.remove(pathology);
    }

    /**
     * Returns the set of medical treatments being administered.
     *
     * @return an unmodifiable set of treatments
     */
    public Set<Treatment> getTreatments() {
        return Collections.unmodifiableSet(treatments);
    }

    /**
     * Updates the set of current treatments.
     *
     * @param treatments the new set of treatments
     * @throws NullPointerException if the input set is null
     */
    public void setTreatments(Set<Treatment> treatments) {
        this.treatments = new HashSet<>(Objects.requireNonNull(treatments, "Treatments set cannot be null"));
    }

    /**
     * Records a new medical treatment for the patient.
     *
     * @param treatment the treatment to record
     * @throws NullPointerException if the treatment is null
     */
    public void addTreatment(Treatment treatment) {
        treatments.add(Objects.requireNonNull(treatment, "Treatment cannot be null"));
    }

    /**
     * Removes a recorded treatment.
     *
     * @param treatment the treatment to remove
     */
    public void removeTreatment(Treatment treatment) {
        treatments.remove(treatment);
    }

    /**
     * Returns the complete chronologically ordered list of medical records.
     *
     * @return an unmodifiable view of medical records
     */
    public List<Report> getMedicalRecords() {
        return Collections.unmodifiableList(medicalRecords);
    }

    /**
     * Adds a medical record to the patient's history.
     *
     * @param medicalRecord the report to add
     * @throws NullPointerException if the record is null
     */
    public void addMedicalRecord(Report medicalRecord) {
        medicalRecords.add(Objects.requireNonNull(medicalRecord, "Medical record cannot be null"));
    }

    /**
     * Removes a medical record from the history.
     *
     * @param medicalRecord the record to remove
     */
    public void removeMedicalRecord(Report medicalRecord) {
        medicalRecords.remove(medicalRecord);
    }

    /**
     * Removes the most recently added medical record.
     *
     * @throws IndexOutOfBoundsException if there are no records to remove
     */
    public void removeLastMedicalRecord() {
        if (!medicalRecords.isEmpty()) {
            medicalRecords.remove(medicalRecords.size() - 1);
        }
    }

    /**
     * Sets the complete patient record history.
     *
     * @param medicalRecords the new list of medical records
     * @throws NullPointerException if the input list is null
     */
    public void setMedicalRecords(List<Report> medicalRecords) {
        this.medicalRecords = new ArrayList<>(Objects.requireNonNull(medicalRecords, "Medical records list cannot be null"));
    }
}
