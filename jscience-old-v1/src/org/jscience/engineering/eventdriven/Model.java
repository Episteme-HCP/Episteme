package org.jscience.engineering.eventdriven;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * <p/>
 * A Model object describes a state model by holding all the information about
 * the valid states and events and the transitions that occur when events are
 * delivered to entities that are in a given state. It also identifies the
 * class of entities that are valid for the model and the initial state that
 * new entities are created in.
 * </p>
 * <p/>
 * <p/>
 * Action code for each state is actually in the entity class. While this may
 * seem strange, most state code manipulates entity data and if the state
 * methods were implemented in the state class the entity reference would
 * require a cast on entry to almost every state method. Placing the state
 * code in the entity avoids this and improves overall performance.
 * </p>
 * <p/>
 * <p/>
 * Model objects can be set up by instantiating a Model object then adding
 * states, events etc. Alternatively, and probably preferable for most
 * applications, subclass Model and add the model details in the subclass
 * constructor.
 * </p>
 * <p/>
 * <p/>
 * There are some basic rules that <b>must </b> be adhered to:
 * <p/>
 * <ul>
 * <li>
 * States can not be registered before the entity class has been specified;
 * </li>
 * <li>
 * The initial state can not be defined before that state has been registered;
 * </li>
 * <li>
 * Transitions can not be specified until the states and events involved have
 * been registered.
 * </li>
 * </ul>
 * </p>
 * <p/>
 * <p/>
 * The suggested order to do things, then, is:
 * <p/>
 * <ol>
 * <li>
 * Specify the entity class;
 * </li>
 * <li>
 * Add the states;
 * </li>
 * <li>
 * Set the initial state;
 * </li>
 * <li>
 * Add the events;
 * </li>
 * <li>
 * Add the transitions.
 * </li>
 * </ol>
 * </p>
 *
 * @author Pete Ford, May 29, 2005 &copy;Pete Ford &amp; CodeXombie.com 2005
 */

//**********************************************
//
//This package is rebundled after the code from JSpasm
//
// Project Homepage : http://jspasm.sourceforge.net/
// Original Developer : Pete Ford
// Official Domain : CodeXombie.com
//
//**********************************************

public class Model {
    /**
     * <p/>
     * The model's name.
     * </p>
     */
    String id;

    /**
     * <p/>
     * The class of entities for this model.
     * </p>
     */
    Class entityClass;

    /**
     * <p/>
     * Model's event queue, for thread-per-model scheme.
     * </p>
     */
    EventQueue eventQueue;

    /**
     * <p/>
     * Maps event spec names to event spec objects.
     * </p>
     */
    Map eventSpecMap;

    /**
     * <p/>
     * The initial state for new entities.
     * </p>
     */
    State initialState;

    /**
     * <p/>
     * Maps state names to state objects.
     * </p>
     */
    Map stateMap;

    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param id The model name.
     * @throws NullPointerException DOCUMENT ME!
     */
    public Model(String id) {
        if (id == null) {
            throw new NullPointerException("Null argument");
        }

        this.id = id;
        entityClass = null;
        eventQueue = null;
        eventSpecMap = new HashMap();
        initialState = null;
        stateMap = new HashMap();
    }

    /**
     * <p/>
     * Adds an event spec to the model, where the event's arguments are defined
     * by a class array.
     * </p>
     *
     * @param eventSpecId The name to give to the event spec.
     * @param argClasses  The event's argument class list. Use <code>new
     *                    Class[0]</code> when the event has no arguments.
     * @throws StateModelConfigurationException
     *          if any arguments are null, the
     *          event spec name already exists in the model, or the entity
     *          class has not been set.
     */
    public final void addEventSpec(String eventSpecId, Class[] argClasses)
            throws StateModelConfigurationException {
        if ((eventSpecId == null) || (argClasses == null)) {
            throw new StateModelConfigurationException("Null argument");
        }

        if (eventSpecMap.containsKey(eventSpecId)) {
            throw new StateModelConfigurationException("EventSpec <" +
                    eventSpecId + "> already defined");
        }

        if (entityClass == null) {
            throw new StateModelConfigurationException(
                    "Entity class has not been set");
        }

        EventSpec eventSpec = new EventSpec(eventSpecId, argClasses);
        eventSpecMap.put(eventSpecId, eventSpec);
    }

    /**
     * <p/>
     * Adds an event spec to the model, where the event's arguments are defined
     * by a comma/space-separated String of fully-qualified class names.
     * </p>
     *
     * @param eventSpecId     The name to give to the event spec.
     * @param classListString Comma/space-separated list of argument class
     *                        names. Use the empty string <code>""</code> when no arguments
     *                        are required.
     * @throws StateModelConfigurationException
     *          if any arguments are null, any
     *          of the named classes can not be loaded, the event spec name
     *          already exists in the model, or the entity class has not been
     *          set.
     */
    public final void addEventSpec(String eventSpecId, String classListString)
            throws StateModelConfigurationException {
        if ((eventSpecId == null) || (classListString == null)) {
            throw new StateModelConfigurationException("Null argument");
        }

        Class[] argClasses;

        try {
            argClasses = MiscellaneousUtils.classesFromString(classListString);
        } catch (ClassNotFoundException ex) {
            throw new StateModelConfigurationException("Error in class list <" +
                    classListString + ">", ex);
        }

        addEventSpec(eventSpecId, argClasses);
    }

    /**
     * <p/>
     * Adds a state to the model, where the state's arguments are defined by a
     * class array.
     * </p>
     *
     * @param stateId    The name to give to the state.
     * @param methodName The name of the state's action method in the entity
     *                   class.
     * @param argClasses The state's argument class list. Use <code>new
     *                   Class[0]</code> when the event has no arguments.
     * @throws StateModelConfigurationException
     *          if any arguments are null, the
     *          state name is already registered, the entity class has not been
     *          set or the specified method can not be located.
     */
    public final void addState(String stateId, String methodName,
                               Class[] argClasses) throws StateModelConfigurationException {
        if ((id == null) || (methodName == null) || (argClasses == null)) {
            throw new StateModelConfigurationException("Null argument");
        }

        if (stateMap.containsKey(id)) {
            throw new StateModelConfigurationException("State <" + id +
                    "> already defined");
        }

        if (entityClass == null) {
            throw new StateModelConfigurationException(
                    "Entity class has not been set");
        }

        Method method;

        try {
            method = entityClass.getMethod(methodName, argClasses);
        } catch (NoSuchMethodException ex) {
            throw new StateModelConfigurationException("Cannot find method <" +
                    methodName + "(" + MiscellaneousUtils.stringFromClasses(argClasses) +
                    ")" + "> in class <" + entityClass.getName() + ">");
        }

        State state = new State(stateId, method, argClasses);
        stateMap.put(stateId, state);
    }

    /**
     * <p/>
     * Adds a state to the model, where the state's arguments are defined by a
     * comma/space-separated String of fully-qualified class names.
     * </p>
     *
     * @param stateId         The name to give to the state.
     * @param methodName      The name of the state's action method in the entity
     *                        class.
     * @param classListString Comma/space-separated list of argument class
     *                        names. Use the empty string <code>""</code> when no arguments
     *                        are required.
     * @throws StateModelConfigurationException
     *          if any arguments are null, any
     *          of the named classes can not be loaded, the state name is
     *          already registered, the entity class has not been set or the
     *          specified method can not be located.
     */
    public final void addState(String stateId, String methodName,
                               String classListString) throws StateModelConfigurationException {
        if ((id == null) || (methodName == null) || (classListString == null)) {
            throw new StateModelConfigurationException("Null argument");
        }

        Class[] argClasses;

        try {
            argClasses = MiscellaneousUtils.classesFromString(classListString);
        } catch (ClassNotFoundException ex) {
            throw new StateModelConfigurationException("Error in class list <" +
                    classListString + ">", ex);
        }

        addState(stateId, methodName, argClasses);
    }

    /**
     * <p/>
     * Add a transition to the model.
     * </p>
     *
     * @param startStateId   The start state.
     * @param eventSpecId    The event that caused the transition.
     * @param endStateId     The end state (must be <code>null</code> for IGNORE
     *                       transitions).
     * @param transitionType The transition type code (use the constants
     *                       defined in the <code>ITransitionType</code> interface).
     * @throws StateModelConfigurationException
     *          if the transition type is
     *          invalid, the start state or event name is null, the end state
     *          is null for non-IGNORE transitions ot non-null for IGNORE
     *          transitions, the entity class has not been defined, or any of
     *          the state or event names have not been registered, or the
     *          arguments for the event do not match those for the end state.
     */
    public final void addTransition(String startStateId, String eventSpecId,
                                    String endStateId, int transitionType)
            throws StateModelConfigurationException {
        if ((transitionType < ITransitionType.MIN_VALUE) ||
                (transitionType > ITransitionType.MAX_VALUE)) {
            throw new StateModelConfigurationException(
                    "Illegal transition type value <" + transitionType + ">");
        }

        if ((startStateId == null) || (eventSpecId == null)) {
            throw new StateModelConfigurationException("Null argument");
        }

        if ((endStateId == null) && (transitionType != ITransitionType.IGNORE)) {
            throw new StateModelConfigurationException("Null argument");
        }

        if ((endStateId != null) && (transitionType == ITransitionType.IGNORE)) {
            throw new StateModelConfigurationException(
                    "Non-null end-state for IGNORE transition");
        }

        if (entityClass == null) {
            throw new StateModelConfigurationException(
                    "Entity class has not been set");
        }

        if (!stateMap.containsKey(startStateId)) {
            throw new StateModelConfigurationException("No such state <" +
                    startStateId + ">");
        }

        if (!eventSpecMap.containsKey(eventSpecId)) {
            throw new StateModelConfigurationException("No such event <" +
                    eventSpecId + ">");
        }

        if ((endStateId != null) && (!stateMap.containsKey(endStateId))) {
            throw new StateModelConfigurationException("No such state <" +
                    endStateId + ">");
        }

        State startState = (State) stateMap.get(startStateId);
        EventSpec eventSpec = (EventSpec) eventSpecMap.get(eventSpecId);
        State endState = (endStateId != null)
                ? (State) stateMap.get(endStateId) : null;

        if ((endState != null) &&
                (transitionType != ITransitionType.DO_NOT_EXECUTE)) {
            if (!MiscellaneousUtils.arrayIsAssignable(eventSpec.argClasses,
                    endState.argClasses)) {
                throw new StateModelConfigurationException(
                        "Argument mismatch between event <" + eventSpecId +
                                "> and state <" + endStateId + ">");
            }
        }

        Transition transition = new Transition(transitionType, endState);
        startState.addTransition(eventSpec, transition);
    }

    /**
     * <p/>
     * Add an EXCURSION transition. In these transitions the entity reverts to
     * the original state after the end state action code has been executed.
     * </p>
     *
     * @param startStateId The start state.
     * @param eventSpecId  The event that caused the transition.
     * @param endStateId   The end state (must be <code>null</code> for IGNORE
     *                     transitions).
     * @throws StateModelConfigurationException
     *          under the same conditions as
     *          for <code>addTransition()</code>.
     */
    public final void addExcursionTransition(String startStateId,
                                             String eventSpecId, String endStateId)
            throws StateModelConfigurationException {
        addTransition(startStateId, eventSpecId, endStateId,
                ITransitionType.EXCURSION);
    }

    /**
     * <p/>
     * Add an IGNORE transition. In these transitions the target entity does
     * not change state and no state code is executed.
     * </p>
     *
     * @param startStateId The start state.
     * @param eventSpecId  The event that caused the transition.
     * @throws StateModelConfigurationException
     *          under the same conditions as
     *          for <code>addTransition()</code>.
     */
    public final void addIgnoreTransition(String startStateId,
                                          String eventSpecId) throws StateModelConfigurationException {
        addTransition(startStateId, eventSpecId, null, ITransitionType.IGNORE);
    }

    /**
     * <p/>
     * Add a NO_EXECUTE transition. In these transitions the entity state
     * changes in the usual way but the end state's action code is not
     * executed.
     * </p>
     *
     * @param startStateId The start state.
     * @param eventSpecId  The event that caused the transition.
     * @param endStateId   The end state (must be <code>null</code> for IGNORE
     *                     transitions).
     * @throws StateModelConfigurationException
     *          under the same conditions as
     *          for <code>addTransition()</code>.
     */
    public final void addNoExecuteTransition(String startStateId,
                                             String eventSpecId, String endStateId)
            throws StateModelConfigurationException {
        addTransition(startStateId, eventSpecId, endStateId,
                ITransitionType.DO_NOT_EXECUTE);
    }

    /**
     * <p/>
     * Add an NORMAL transition.
     * </p>
     *
     * @param startStateId The start state.
     * @param eventSpecId  The event that caused the transition.
     * @param endStateId   The end state (must be <code>null</code> for IGNORE
     *                     transitions).
     * @throws StateModelConfigurationException
     *          under the same conditions as
     *          for <code>addTransition()</code>.
     */
    public final void addNormalTransition(String startStateId,
                                          String eventSpecId, String endStateId)
            throws StateModelConfigurationException {
        addTransition(startStateId, eventSpecId, endStateId,
                ITransitionType.NORMAL);
    }

    /**
     * <p/>
     * Creates an entity of the model's defined entity class, puts it into the
     * initial state and executes the initial state's action method with the
     * provided arguments against the new entity.
     * </p>
     *
     * @param entityId    The name to give to the entity.
     * @param initArgs    The argument values to pass to the initial state method.
     * @param ownerEngine The Engine from which the entity will be driven.
     * @param eQueue      The entity's event queue.
     * @return a new Entity.
     * @throws StateProcessingException if any of the arguments are null, the
     *                                  entity class or initial state has not been defined in the
     *                                  model, or there is a problem calling the initial state's action
     *                                  method.
     * @throws IllegalAccessException   usually means the initial state's action
     *                                  method is not <code>public</code>.
     * @throws InstantiationException   if a problem occurs when creating the
     *                                  entity.
     */
    final AbsEntity createEntity(String entityId, Object[] initArgs,
                                 Engine ownerEngine, EventQueue eQueue)
            throws StateProcessingException, IllegalAccessException,
            InstantiationException {
        if ((entityId == null) || (initArgs == null) || (ownerEngine == null)) {
            throw new StateProcessingException("Null argument");
        }

        if (entityClass == null) {
            throw new StateProcessingException(
                    "Entity class has not been set in model <" + id + ">");
        }

        if (initialState == null) {
            throw new StateProcessingException(
                    "Initial state has not been set in model <" + id + ">");
        }

        AbsEntity entity = (AbsEntity) entityClass.newInstance();
        entity.id = entityId;
        entity.currentState = initialState;
        entity.ownerEngine = ownerEngine;
        entity.ownerModel = this;
        entity.eventQueue = eQueue;
        ownerEngine.entityMap.put(entityId, entity);

        //        try
        //        {
        //            initialState.actionMethod.invoke(entity, initArgs);
        //        }
        //        catch (Exception ex)
        //        {
        //            throw new StateProcessingException("Error initializing entity <" +
        // entityId
        //                    + ">",
        //                    ex);
        //        }
        // Null eventSpec indicates initialization event
        ownerEngine.generateEvent(entity, null, initArgs, false);

        return entity;
    }

    /**
     * <p/>
     * Set the entity class associated with the model.
     * </p>
     *
     * @param entityClass The entity class.
     * @throws StateModelConfigurationException
     *          if the argument is null or an
     *          entity class has already been defined for this model.
     */
    public final void setEntityClass(Class entityClass)
            throws StateModelConfigurationException {
        if (entityClass == null) {
            throw new StateModelConfigurationException("Null argument");
        }

        if (this.entityClass != null) {
            throw new StateModelConfigurationException(
                    "Entity class has already been set");
        }

        if (!(AbsEntity.class.isAssignableFrom(entityClass))) {
            throw new StateModelConfigurationException("Class <" +
                    entityClass.getName() + "> is not a subclass of <" +
                    AbsEntity.class.getName() + ">");
        }

        this.entityClass = entityClass;
    }

    /**
     * <p/>
     * Set the entity class associated with the model.
     * </p>
     *
     * @param entityClassName The entity class name.
     * @throws ClassNotFoundException if the named class can not be loaded.
     * @throws StateModelConfigurationException
     *                                if an entity class has already
     *                                been defined for this model.
     */
    public final void setEntityClass(String entityClassName)
            throws ClassNotFoundException, StateModelConfigurationException {
        if (entityClassName == null) {
            throw new StateModelConfigurationException("Null argument");
        }

        setEntityClass(Class.forName(entityClassName));
    }

    /**
     * <p/>
     * Set the initial state for new entities created in this model.
     * </p>
     *
     * @param initialStateId The name of the initial state.
     * @throws StateModelConfigurationException
     *          if the argument is null, the
     *          initial state has already been set, or the state name does not
     *          exist in the model.
     */
    public final void setInitialState(String initialStateId)
            throws StateModelConfigurationException {
        if (initialStateId == null) {
            throw new StateModelConfigurationException("Null argument");
        }

        if (initialState != null) {
            throw new StateModelConfigurationException(
                    "Initial state has already been set");
        }

        if (!stateMap.containsKey(initialStateId)) {
            throw new StateModelConfigurationException("No such state <" +
                    initialStateId + ">");
        }

        initialState = (State) stateMap.get(initialStateId);
    }
}
