package org.jscience.engineering.eventdriven;

import java.io.InputStream;
import java.net.URL;
import java.util.*;


/**
 * <p/>
 * This is the run-time core of the state engine system. This thread is
 * responsible for controlling the dispatchers and data structures of the
 * whole system.
 * </p>
 * <p/>
 * <p/>
 * The engine can be configured to run with one of three thread schemes:
 * <p/>
 * <ul>
 * <li>
 * Thread-per-engine: All event delivery and state processing is handled by one
 * thread. This is the default.
 * </li>
 * <li>
 * Thread-per-model: One delivery thread is created for each model and handles
 * processing for all entities using tha model.
 * </li>
 * <li>
 * Thread-per-entity: Every entity has its own event queue and
 * delivery/processing thread.
 * </li>
 * </ul>
 * <p/>
 * The thread-per-model and thread-per-entity schemes may improve performance
 * on systems with more than one processor. The thread scheme is set by
 * calling <code>setThreadScheme()</code> with a string parameter value of
 * <code>"engine"</code>,<code>"model"</code> or <code>"entity"</code> (the
 * string is not case-sensitive and can contain leading or trailing white
 * space). Passing a null value or an empty string sets the default mode.
 * </p>
 * <p/>
 * <p/>
 * When setting up an Engine the recommended sequence of operations is:
 * <p/>
 * <ol>
 * <li>
 * Create the Model objects;
 * </li>
 * <li>
 * Create the Engine object;
 * </li>
 * <li>
 * Add the models to the Engine;
 * </li>
 * <li>
 * Start the Engine (<code>Engine.start()</code>);
 * </li>
 * </ol>
 * <p/>
 * Once running, entities can be created and events delivered to them. To stop
 * the engine thread cleanly, call <code>shutdown()</code> then
 * <code>Engine.join()</code> to wait for it to terminate.
 * </p>
 *
 * @author Pete Ford, May 30, 2005 &copy;Pete Ford &amp; CodeXombie.com 2005
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

public final class Engine extends Thread {
    /**
     * <p/>
     * Internal constant used by static initialization.
     * </p>
     */
    private final static String CFG_SYS_CFG_KEY = "jspasm.configuration";

    /**
     * <p/>
     * Internal constant used by static initialization.
     * </p>
     */
    private final static String CFG_EXC_HDR_KEY = "jspasm.exception.handler";

    /**
     * <p/>
     * Internal constant used by static initialization.
     * </p>
     */
    private final static String CFG_SCH_HDR_KEY = "jspasm.statechange.handler";

    /**
     * <p/>
     * Internal constant used by static initialization.
     * </p>
     */
    private final static String CFG_THREAD_SCHEME_KEY = "jspasm.thread.scheme";

    /**
     * <p/>
     * Internal constant used by static initialization.
     * </p>
     */
    private final static String CFG_THREAD_SCHEME_ENGINE = "engine";

    /**
     * <p/>
     * Internal constant used by static initialization.
     * </p>
     */
    private final static String CFG_THREAD_SCHEME_MODEL = "model";

    /**
     * <p/>
     * Internal constant used by static initialization.
     * </p>
     */
    private final static String CFG_THREAD_SCHEME_ENTITY = "entity";

    /**
     * <p/>
     * Preset value for thread-per-engine thread scheme.
     * </p>
     */
    public final static int THREAD_PER_ENGINE = 0;

    /**
     * <p/>
     * Preset value for thread-per-model thread scheme.
     * </p>
     */
    public final static int THREAD_PER_MODEL = 1;

    /**
     * <p/>
     * Preset value for thread-per-entity thread scheme.
     * </p>
     */
    public final static int THREAD_PER_ENTITY = 2;

    /**
     * <p/>
     * Maps thread scheme name strings to values.
     * </p>
     */
    private final static Map threadSchemes;

    /**
     * <p/>
     * Default exception handler. By default one instance of
     * <code>BasicExceptionHandler()</code> is shared by all engine instances.
     * </p>
     */
    static IExceptionHandler defaultExceptionHandler;

    /**
     * <p/>
     * Default state change handler. By default this is <code>null</code>.
     * </p>
     */
    static IStateChangeHandler defaultStateChangeHandler;

    /**
     * <p/>
     * Default thread model setting.
     * </p>
     */
    static int defaultThreadScheme;

    /**
     * <p/>
     * Configuration properties, if any. By default this will be empty (not
     * null). The location of a property file can be provided as a system
     * property on the command line (using the
     * <code>-Djspasm.configuration=<i>location</i></code> setting).
     * </p>
     */
    public static Properties cfgProp;

    /*
     * Initializes static data and attempts to locate and load a properties file
     * specified in the "jspasm.configuration" system property, if one is
     * supplied.
     */
    static {
        threadSchemes = new HashMap();
        threadSchemes.put(null, new Integer(THREAD_PER_ENGINE));
        threadSchemes.put("", new Integer(THREAD_PER_ENGINE));
        threadSchemes.put(CFG_THREAD_SCHEME_ENGINE,
                new Integer(THREAD_PER_ENGINE));
        threadSchemes.put(CFG_THREAD_SCHEME_MODEL, new Integer(THREAD_PER_MODEL));
        threadSchemes.put(CFG_THREAD_SCHEME_ENTITY,
                new Integer(THREAD_PER_ENTITY));
        defaultExceptionHandler = new BasicExceptionHandler();
        defaultStateChangeHandler = null;
        cfgProp = new Properties();
        defaultThreadScheme = THREAD_PER_ENGINE;

        String cfgFileLocation = System.getProperty(CFG_SYS_CFG_KEY);

        if (cfgFileLocation != null) {
            try {
                InputStream cfgInStream = ClassLoader.getSystemResourceAsStream(cfgFileLocation);

                if (cfgInStream == null) {
                    URL cfgFileUrl = new URL(cfgFileLocation);
                    cfgInStream = cfgFileUrl.openStream();
                }

                cfgProp.load(cfgInStream);
                cfgInStream.close();

                String exceptionHandlerClassName = cfgProp.getProperty(CFG_EXC_HDR_KEY);

                if (exceptionHandlerClassName != null) {
                    if (exceptionHandlerClassName.trim().equals("")) {
                        defaultExceptionHandler = null;
                    } else {
                        try {
                            Class exceptionHandlerClass = Class.forName(exceptionHandlerClassName);

                            if (!IExceptionHandler.class.isAssignableFrom(
                                    exceptionHandlerClass)) {
                                throw new ClassCastException("Class <" +
                                        exceptionHandlerClassName +
                                        "> does not implement the IExceptionHandler interface");
                            }

                            defaultExceptionHandler = (IExceptionHandler) exceptionHandlerClass.newInstance();
                        } catch (Exception ex) {
                            System.err.println("Can't load Exception Handler: " +
                                    ex.getMessage());
                        }
                    }
                }

                String stateChangeHandlerClassName = cfgProp.getProperty(CFG_SCH_HDR_KEY);

                if (stateChangeHandlerClassName != null) {
                    if (stateChangeHandlerClassName.trim().equals("")) {
                        defaultStateChangeHandler = null;
                    } else {
                        try {
                            Class stateChangeHandlerClass = Class.forName(stateChangeHandlerClassName);

                            if (!IStateChangeHandler.class.isAssignableFrom(
                                    stateChangeHandlerClass)) {
                                throw new ClassCastException("Class <" +
                                        stateChangeHandlerClassName +
                                        "> does not implement the IStateChangeHandler interface");
                            }

                            defaultStateChangeHandler = (IStateChangeHandler) stateChangeHandlerClass.newInstance();
                        } catch (Exception ex) {
                            System.err.println(
                                    "Can't load State Change Handler: " +
                                            ex.getMessage());
                        }
                    }
                }

                String threadLevelName = cfgProp.getProperty(CFG_THREAD_SCHEME_KEY);

                if (threadLevelName != null) {
                    defaultThreadScheme = threadSchemeFromString(threadLevelName);
                }
            } catch (Exception ex) {
                System.err.println("Can't load Configuration Properties: " +
                        ex.getMessage());
            }
        }
    }

    /**
     * <p/>
     * Name of this engine.
     * </p>
     */
    String id;

    /**
     * <p/>
     * Dispatchers to be joined.
     * </p>
     */
    private LinkedList deadDispatchers;

    /**
     * <p/>
     * All currently active dispatched threads.
     * </p>
     */
    List dispatchers;

    /**
     * <p/>
     * Maps entity names to the corresponding objects.
     * </p>
     */
    Map entityMap;

    /**
     * <p/>
     * Engine event queue.
     * </p>
     */
    EventQueue eventQueue;

    /**
     * <p/>
     * The exception handler that will be called when run-time exceptions
     * occur. This defaults to an instance of
     * <code>BasicExceptionHandler</code> but can be set to a user-supplied
     * handler (i.e. an instance of a class that implements the
     * <code>IExceptionHandler</code> interface) by calling
     * <code>setExceptionHandler</code>. Setting this to <code>null</code>
     * switches exception handling/reporting off.
     * </p>
     */
    IExceptionHandler exceptionHandler;

    /**
     * <p/>
     * Maps Model names to the corresponding Model instances.
     * </p>
     */
    Map modelMap;

    /**
     * <p/>
     * Used for <code>controlledStart()</code>.
     * </p>
     */
    private boolean ready;

    /**
     * <p/>
     * Reflects and controls the run state of the engine.
     * </p>
     */
    private boolean running;

    /**
     * <p/>
     * The state change handler used for reporting state changes - this is
     * primarily used for debugging purposes. By default, this is set to
     * <code>null</code>, so that state changes are not reported. Basic
     * reporting can be enabled by calling
     * <code>setStateChangeHandler()</code> passing an instance of
     * <code>BasicStateChangeHandler</code> (i.e. an instance of a class that
     * implements the <code>IStateChangeHandler</code> interface)as an
     * argument. User-supplied state change handlers can also be created and
     * used.
     * </p>
     */
    IStateChangeHandler stateChangeHandler;

    /**
     * <p/>
     * Thread scheme code for this engine.
     * </p>
     */
    int threadScheme;

    /**
     * <p/>
     * Constructor.
     * </p>
     *
     * @param id The name to give to the engine.
     */
    public Engine(String id) {
        this.id = id;
        deadDispatchers = new LinkedList();
        dispatchers = Collections.synchronizedList(new ArrayList());
        entityMap = Collections.synchronizedMap(new HashMap());
        eventQueue = null;
        exceptionHandler = defaultExceptionHandler;
        modelMap = Collections.synchronizedMap(new HashMap());
        running = false;
        stateChangeHandler = defaultStateChangeHandler;
        threadScheme = defaultThreadScheme;
    }

    /**
     * <p/>
     * Derive the int value of the thread scheme code from a string.
     * </p>
     *
     * @param s The string to check.
     * @return the equivalent int value;
     * @throws Exception if the string is not recognized.
     */
    private static int threadSchemeFromString(String s)
            throws Exception {
        int l;

        if (s != null) {
            s = s.trim().toLowerCase();
        }

        Integer levelInt = (Integer) threadSchemes.get(s);

        if (levelInt != null) {
            l = levelInt.intValue();
        } else {
            throw new Exception("Unrecognized value " + s + " for " +
                    CFG_THREAD_SCHEME_KEY + " key");
        }

        return l;
    }

    /**
     * <p/>
     * Add a model to the Engine's internal data.
     * </p>
     *
     * @param model The model to register.
     * @throws StateModelConfigurationException
     *                               if the supplied model is null
     *                               or already registered.
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void addModel(Model model) throws StateModelConfigurationException {
        if (running) {
            throw new IllegalStateException("Can't add model to running engine");
        }

        if (model == null) {
            throw new StateModelConfigurationException("Null argument");
        }

        if (modelMap.containsKey(model.id)) {
            throw new StateModelConfigurationException("Model id <" + model.id +
                    "> already exists");
        }

        if (modelMap.containsValue(model)) {
            throw new StateModelConfigurationException("Model <" + model.id +
                    "> already registered");
        }

        modelMap.put(model.id, model);
    }

    /**
     * <p/>
     * Creates a new Entity for a specific Model. The Entity class is as
     * defined by the Model.
     * </p>
     *
     * @param modelId  The model to create the entity in.
     * @param entityId The unique id to give to the entity.
     * @param initArgs The initialization arguments (must match the arguments
     *                 for the Model's initial state).
     * @return the new Entity subclass.
     * @throws StateProcessingException if any arguments are null, the model
     *                                  does not exist or the entity id already exists.
     * @throws InstantiationException   if the entity cannot be instantiated.
     * @throws IllegalAccessException   if the state method of the initialization
     *                                  state cannot be executed.
     * @throws IllegalStateException    DOCUMENT ME!
     */
    public AbsEntity createEntity(String modelId, String entityId,
                                  Object[] initArgs)
            throws StateProcessingException, InstantiationException,
            IllegalAccessException {
        if (!running) {
            throw new IllegalStateException(
                    "Can't add entity to non-running engine");
        }

        if ((modelId == null) || (entityId == null) || (initArgs == null)) {
            throw new StateProcessingException("Null argument");
        }

        if (!modelMap.containsKey(modelId)) {
            throw new StateProcessingException("No such model <" + modelId +
                    ">");
        }

        if (entityMap.containsKey(entityId)) {
            throw new StateProcessingException("Entity id <" + entityId +
                    "> already exists");
        }

        Model model = (Model) modelMap.get(modelId);
        EventQueue eQueue = null;
        Dispatcher dispatcher = null;

        switch (threadScheme) {
            case THREAD_PER_ENGINE:
                eQueue = eventQueue;

                break;

            case THREAD_PER_MODEL:
                eQueue = model.eventQueue;

                break;

            case THREAD_PER_ENTITY: {
                eQueue = new EventQueue();
                dispatcher = new Dispatcher(this, eQueue);
            }

            break;
        }

        AbsEntity entity = model.createEntity(entityId, initArgs, this, eQueue);

        if (threadScheme == THREAD_PER_ENTITY) {
            entity.dispatcher = dispatcher;
            dispatchers.add(dispatcher);
            dispatcher.start();
        }

        return entity;
    }

    /**
     * <p/>
     * Remove an entity from the system.
     * </p>
     *
     * @param entity
     */
    void deleteEntity(AbsEntity entity) {
        if (threadScheme == THREAD_PER_ENTITY) {
            entity.dispatcher.shutdown();

            synchronized (deadDispatchers) {
                deadDispatchers.addLast(entity.dispatcher);
                deadDispatchers.notify();
            }
        }

        entityMap.remove(entity.id);
    }

    /**
     * <p/>
     * Generate an event for delivery to a specified entity.
     * </p>
     *
     * @param entityId    The target of the event.
     * @param eventSpecId The event type to be generated.
     * @param args        The event arguments.
     * @throws StateProcessingException if any arguments are null, the event or
     *                                  entity names do not exist, etc.
     */
    public void generateEvent(String entityId, String eventSpecId, Object[] args)
            throws StateProcessingException {
        if ((entityId == null) || (eventSpecId == null) || (args == null)) {
            throw new StateProcessingException("Null argument");
        }

        if (!entityMap.containsKey(entityId)) {
            throw new StateProcessingException("No such entity <" + entityId +
                    ">");
        }

        AbsEntity entity = (AbsEntity) entityMap.get(entityId);

        if (!entity.ownerModel.eventSpecMap.containsKey(eventSpecId)) {
            throw new StateProcessingException("No such event <" + eventSpecId +
                    "> in model <" + entity.ownerModel.id + ">");
        }

        EventSpec eventSpec = (EventSpec) entity.ownerModel.eventSpecMap.get(eventSpecId);
        generateEvent(entity, eventSpec, args, true);
    }

    /**
     * <p/>
     * Generate an event with no arguments for delivery to a specified entity.
     * </p>
     *
     * @param entityId    The target of the event.
     * @param eventSpecId The event type to be generated.
     * @throws StateProcessingException if any arguments are null, the event or
     *                                  entity names do not exist, etc.
     */
    public void generateEvent(String entityId, String eventSpecId)
            throws StateProcessingException {
        generateEvent(entityId, eventSpecId, new Object[0]);
    }

    /**
     * <p/>
     * Generates an event and puts it on the queue.
     * </p>
     *
     * @param entity    The target entity.
     * @param eventSpec The event type.
     * @param args      The event arguments.
     * @param normal    <code>true</code> for normal events, <code>false</code>
     *                  for internal events.
     */
    void generateEvent(AbsEntity entity, EventSpec eventSpec, Object[] args,
                       boolean normal) {
        Event event = new Event(entity, eventSpec, args);

        if (normal) {
            entity.eventQueue.queueNormal(event);
        } else {
            entity.eventQueue.queueInternal(event);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    public void run() {
        // Flag can be cleared to stop the Engine.
        running = true;

        // Initialize
        switch (threadScheme) {
            case THREAD_PER_ENGINE: {
                eventQueue = new EventQueue();

                Dispatcher dispatcher = new Dispatcher(this, eventQueue);
                dispatchers.add(dispatcher);
                dispatcher.start();
            }

            break;

            case THREAD_PER_MODEL: {
                for (Iterator modelMapKeyIterator = modelMap.keySet().iterator();
                     modelMapKeyIterator.hasNext();) {
                    String key = (String) modelMapKeyIterator.next();
                    Model model = (Model) modelMap.get(key);
                    model.eventQueue = new EventQueue();

                    Dispatcher dispatcher = new Dispatcher(this, model.eventQueue);
                    dispatchers.add(dispatcher);
                    dispatcher.start();
                }
            }

            break;

            case THREAD_PER_ENTITY:
                break;
        }

        // Ready to accept calls
        synchronized (this) {
            ready = true;
            notify();
        }

        // Run until shutdown
        while (running) {
            try {
                Dispatcher dispatcher;

                synchronized (deadDispatchers) {
                    while (deadDispatchers.isEmpty()) {
                        deadDispatchers.wait();
                    }

                    dispatcher = (Dispatcher) deadDispatchers.removeFirst();
                }

                dispatcher.join();
            } catch (InterruptedException ex) {
                continue;
            }
        }

        // Clean up
        synchronized (dispatchers) {
            while (!dispatchers.isEmpty()) {
                Dispatcher dispatcher = (Dispatcher) dispatchers.remove(0);
                dispatcher.shutdown();
                deadDispatchers.addLast(dispatcher);
            }
        }

        synchronized (deadDispatchers) {
            while (!deadDispatchers.isEmpty()) {
                Dispatcher dispatcher = (Dispatcher) deadDispatchers.removeFirst();

                try {
                    dispatcher.join();
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Thread#start()
     */
    public synchronized void start() {
        super.start();

        while (!ready) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
    }

    /**
     * <p/>
     * Set the exception handler. Exception handling is disabled by setting the
     * exception handler to <code>null</code>.
     * </p>
     *
     * @param exceptionHandler Exception handler instance, or
     *                         <code>null</code>.
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setExceptionHandler(IExceptionHandler exceptionHandler) {
        if (running) {
            throw new IllegalStateException(
                    "Can't set exception handler in running engine");
        }

        this.exceptionHandler = exceptionHandler;
    }

    /**
     * <p/>
     * Set the state change handler. Change handling is disabled by setting the
     * state change handler to <code>null</code>.
     * </p>
     *
     * @param stateChangeHandler StateChangeHandler instance, or
     *                           <code>null</code>.
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setStateChangeHandler(IStateChangeHandler stateChangeHandler) {
        if (running) {
            throw new IllegalStateException(
                    "Can't set exception handler in running engine");
        }

        this.stateChangeHandler = stateChangeHandler;
    }

    /**
     * <p/>
     * Set the thread scheme.
     * </p>
     *
     * @param threadSchemeValue The thread scheme value.
     * @throws Exception             if the engine is already running or the scheme value
     *                               is invalid.
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setThreadScheme(int threadSchemeValue)
            throws Exception {
        if (running) {
            throw new IllegalStateException(
                    "Can't set thread scheme in running engine");
        }

        if ((threadSchemeValue < THREAD_PER_ENGINE) ||
                (threadSchemeValue > THREAD_PER_ENTITY)) {
            throw new Exception("Thread scheme value out of range");
        }

        threadScheme = threadSchemeValue;
    }

    /**
     * <p/>
     * Set the thread scheme.
     * </p>
     *
     * @param threadSchemeString The thread scheme name.
     * @throws Exception             if the engine is already running or the scheme name is
     *                               not recognized.
     * @throws IllegalStateException DOCUMENT ME!
     */
    public void setThreadScheme(String threadSchemeString)
            throws Exception {
        if (running) {
            throw new IllegalStateException(
                    "Can't set thread scheme in running engine");
        }

        threadScheme = threadSchemeFromString(threadSchemeString);
    }

    /**
     * <p/>
     * Order the Engine thread to terminate.
     * </p>
     */
    public void shutdown() {
        running = false;
        interrupt();
    }
}
