import org.jscience.computing.geneticprogramming.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class SimpleGp1 implements FitnessFunction {
    /**
     * DOCUMENT ME!
     */
    private static final int MaxNumberOfGeneration = 50;

    /**
     * DOCUMENT ME!
     */
    private static final int PopulationSize = 100;

    /**
     * DOCUMENT ME!
     */
    private GeneticProgram geneticProgram = null;

    /**
     * DOCUMENT ME!
     */
    boolean[][] fitnessCases = {
            { false, false, false, false },
            { false, false, true, false },
            { false, true, false, true },
            { true, false, false, false },
            { true, true, false, false },
            { true, false, true, true },
            { false, true, true, true },
            { true, true, true, true }
        };

    /**
     * Creates a new SimpleGp1 object.
     *
     * @param populationSize DOCUMENT ME!
     * @param terminalSet DOCUMENT ME!
     * @param functionSet DOCUMENT ME!
     * @param generationMethod DOCUMENT ME!
     * @param selectionMethod DOCUMENT ME!
     */
    public SimpleGp1(int populationSize, Terminal[] terminalSet,
        Function[] functionSet, int generationMethod, int selectionMethod) {
        geneticProgram = new GeneticProgram(populationSize, this, terminalSet,
                functionSet, generationMethod, selectionMethod);
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        geneticProgram.createPopulation();

        while (!terminationPredicate()) {
            geneticProgram.evolve();
            System.out.println("Generation: " +
                geneticProgram.getGenerationNo());
            System.out.println("Best Individual: " +
                geneticProgram.getBestIndividual().getProgram().toText());
            System.out.println("Best Fitness: " +
                geneticProgram.getBestIndividual().getStandardizedFitness());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean terminationPredicate() {
        return (geneticProgram.getGenerationNo() >= MaxNumberOfGeneration);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ind DOCUMENT ME!
     * @param individualNo DOCUMENT ME!
     */
    public void evaluate(Individual ind, int individualNo) {
        boolean[] b1 = new boolean[3];
        double hits = 0;

        for (int i = 0; i < 8; i++) {
            b1[0] = fitnessCases[i][0];
            b1[1] = fitnessCases[i][1];
            b1[2] = fitnessCases[i][2];

            boolean result = ((Boolean) ind.getProgram().eval(b1)).booleanValue();

            if (result == fitnessCases[i][3]) {
                hits++;
            }
        }

        ind.setStandardizedFitness(((double) (8 - hits)) / 8.0);

        Individual bestIndividual = geneticProgram.getBestIndividual();

        if ((bestIndividual == null) ||
                (bestIndividual.getStandardizedFitness() > (((double) (8 -
                hits)) / 8.0))) {
            geneticProgram.setBestIndividual(individualNo);
            geneticProgram.setGenerationOfBestIndividual(geneticProgram.getGenerationNo());
        }

        Individual bestIndividualInCurrentGeneration = geneticProgram.getBestIndividualInCurrentGeneration();

        if ((bestIndividualInCurrentGeneration == null) ||
                (bestIndividualInCurrentGeneration.getStandardizedFitness() > (((double) (8 -
                hits)) / 8.0))) {
            geneticProgram.setBestIndividualInCurrentGeneration(individualNo);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        Terminal[] terminalSet = new Terminal[3];
        Function[] functionSet = new Function[3];

        terminalSet[0] = new A0Terminal();
        terminalSet[1] = new D0Terminal();
        terminalSet[2] = new D1Terminal();

        functionSet[0] = new AndFunction();
        functionSet[1] = new OrFunction();
        functionSet[2] = new NotFunction();

        SimpleGp1 gpdemo = new SimpleGp1(PopulationSize, terminalSet,
                functionSet, GeneticProgram.GENERATION_GROW,
                GeneticProgram.SELECTION_TOURNAMENT);

        gpdemo.start();
    }
}
