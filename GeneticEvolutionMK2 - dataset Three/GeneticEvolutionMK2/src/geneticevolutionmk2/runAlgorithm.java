/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticevolutionmk2;
/**
 *
 * @author i2-rickwood
 */
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class runAlgorithm {
    int geneticLength = 160;
    int populationSize = 500;
    int individualFitness = 0;
    int generation = 0;
    boolean solutionFound = false;
    int generations = 1500;
    RuleBase rules = new RuleBase();
    public boolean runNewAlgorithm(){
        //RuleBase rules = new RuleBase();
        Population myPop = new Population(populationSize, true);
        ArrayList<Population> pop = new ArrayList<>();
        
        pop.add(myPop);
        
        for (int k = 0; k < populationSize; k++){
                myPop.setIndividualFitness(rules, k); //checks all individuals against ruleBase to set fitness for initial population
        }
        for (int i = 0; i < generations; i++) {
//            for (int n = 0; n < populationSize; n++){
//                Individual testtest = cloneIndividual(pop.get(i).getIndividual(n));
//                for (int p = 0; p < geneticLength; p++){
//                    System.out.print(testtest.getSingleGene(p));
//                }
//            System.out.println();
//            }
            Population finalPop = clonePopulation(evolutions(pop.get(i)));
            for (int k = 0; k < populationSize; k++){
                finalPop.setIndividualFitness(rules, k); //checks all individuals agains ruleBase to set fitness for all evolved populations
            }
            pop.add(finalPop);
            int mutatedFitness = 0;
            int bestFitness = 0;
            int indv = 0;
            generation++;
            for (int j = 0; j < populationSize; j++){ //finds and stores the best fitness of each generation
                Individual mutatedFitnessTest = cloneIndividual(finalPop.getIndividual(j));
                mutatedFitness = mutatedFitnessTest.returnFitness();
                if (mutatedFitness > bestFitness){
                    indv = j;
                    bestFitness = mutatedFitness;
                }
                if (mutatedFitness >= 2000){ //if solution is found then program ends and individual that found it is printed out
                    for (int p = 0; p < geneticLength; p++){
                        System.out.print(mutatedFitnessTest.getSingleGene(p));
                    }
                    System.out.println();
                    System.out.println("solution found after " + generation + " generations");
                    solutionFound = true;
                }
            }
            System.out.println("The best fitness was " + bestFitness + " for generation " + generation + " from individual " + indv);
            if (solutionFound == true){
                for (int o = 0; o < populationSize; o++){
                    Individual generationTest = cloneIndividual(finalPop.getIndividual(o));
                    for (int p = 0; p < geneticLength; p++){
                        String gene = generationTest.getSingleGene(p);
                        System.out.print(gene);
                    }
                    System.out.println();
                }
            } else if (solutionFound != true) {
                System.out.println("No solution in generation " + generation);
            }
            if (solutionFound == true){
                break;
            }
        }
        
            
        
        return solutionFound;
    }
    
    public Population evolutions(Population newPop){
        
        Population selectedPop = new Population(populationSize, false);
        
        for (int i = 0; i < populationSize; i++){
            boolean same = true;
            int randomOne = 0;
            int randomTwo = 1;
            while (same){
                randomOne = randomNumber(0, populationSize - 1);
                randomTwo = randomNumber(0, populationSize - 1);
                if (randomOne != randomTwo){
                    same = false;
                }
            }
            
            Individual selected = cloneIndividual(tournamentSelection(newPop.getIndividual(randomOne), newPop.getIndividual(randomTwo))); //tournamentSelection is run
            selectedPop.addIndividual(i, selected);
        }
        
        Population crossoverPop = new Population(populationSize, false);
        int positionOne = 0;
        int positionTwo = 1;
        int runThroughs = (int) Math.round(populationSize * 0.5);
        for (int i = 0; i < runThroughs; i++){
            int crossMin = 0;//(int) Math.round(geneticLength * 0);
            int crossMax = geneticLength;//(int) Math.round(geneticLength * 1);
            int crossoverPoint = randomNumber(crossMin, crossMax);
            crossoverPop.addIndividual(i, crossover(selectedPop.getIndividual(i), selectedPop.getIndividual(i+1), crossoverPoint)); //crossover is run
            
            crossoverPop.addIndividual(i, crossover(selectedPop.getIndividual(i+1), selectedPop.getIndividual(i), crossoverPoint));
            positionOne = positionOne + 2;
            positionTwo = positionTwo + 2;
        }
        
        Population mutatedPop = new Population(populationSize, false);
        for (int i = 0; i < populationSize; i++){
            mutatedPop.addIndividual(i, mutate(crossoverPop.getIndividual(i))); //mutation is run
        }
        
        return mutatedPop;
    }
    
    public Individual tournamentSelection (Individual individualOne, Individual individualTwo){
        int fitnessOne = individualOne.returnFitness();
        int fitnessTwo = individualTwo.returnFitness();
        
        if (fitnessOne > fitnessTwo){ //returns fittest individual
            Individual winner = cloneIndividual(individualOne);
            return winner;
        } else if (fitnessOne < fitnessTwo){
            Individual winner = cloneIndividual(individualTwo);
            return winner;
        } else {
            Individual winner = cloneIndividual(individualOne);
            return winner;
        }
    }
    
    public Individual crossover (Individual individualOne, Individual individualTwo, int crossoverPoint){
        for (int i = crossoverPoint; i < geneticLength; i++){
            String gene = individualTwo.getSingleGene(i); //crosses the first individual with the second and returns the new one
            individualOne.changeSingleGene(i, gene);
        }
        return individualOne;
    }
    
    public Individual mutate (Individual individual){
        int mutationRate = 7;
        int bitPosition = 0;
        float randChoice = 0;
        char currentGene = ' ';
        
        for (int i = 0; i < geneticLength; i++){
            String newGene = "";
            String gene = individual.getSingleGene(i);
            //System.out.println(gene);
            int mutateChance = randomNumber(0, 1000);
            if (mutateChance <= mutationRate){
                if (gene.length() != 1){
                    boolean wildcardPresent = false;
                    for (int j = 0; j < 6; j++){ //checks if the data has a wildcard in it
                        currentGene = gene.charAt(j);
                        if (currentGene == '#'){
                            wildcardPresent = true;
                            break;
                        }
                    }
                    if (!wildcardPresent){ //if no wildcards
                        for (int k = 0; k < gene.length(); k++){ //the mutation basically increases or decreases the 'element' in the gene by one or changes it to a wildcard
                            int upOrDown = randomNumber(0,2);
                            if (upOrDown == 0){ //increase value
                                char oldGene = gene.charAt(k);
                                int oldGeneInt = Character.getNumericValue(oldGene);
                                if (oldGeneInt != 9){
                                    oldGeneInt++;
                                } else {
                                    oldGeneInt = 0;
                                }
                                String createdGene = Integer.toString(oldGeneInt);
                                newGene = newGene + createdGene;
                            } else if (upOrDown == 1){ //decrease value
                                char oldGene = gene.charAt(k);
                                int oldGeneInt = Character.getNumericValue(oldGene);
                                if (oldGeneInt != 0){
                                    oldGeneInt--;
                                } else {
                                    oldGeneInt = 9;
                                }
                                String createdGene = Integer.toString(oldGeneInt);
                                newGene = newGene + createdGene;
                            } else if (upOrDown == 2){
                                String createdGene = "#";
                                newGene = newGene + createdGene;
                            }
                        }
                        bitPosition++;
                    } else {
                        for (int x = 0; x < gene.length(); x++){ 
                           char oldGene = gene.charAt(x);
                           if (oldGene == '#'){ //if the gene has a wildcard then if the wildcard needs to be mutated a random new number is assigned to it
                               int newRandomGene = randomNumber(0,9);
                               String createdGene = Integer.toString(newRandomGene);
                               newGene = newGene + createdGene;
                           } else {
                                int upOrDown = randomNumber(0,2);
                                if (upOrDown == 0){ //increase value
                                    int oldGeneInt = Character.getNumericValue(oldGene);
                                    if (oldGeneInt != 9){
                                        oldGeneInt++;
                                    } else {
                                        oldGeneInt = 0;
                                    }
                                    String createdGene = Integer.toString(oldGeneInt);
                                    newGene = newGene + createdGene;
                                } else if (upOrDown == 1){ //decrease value
                                    int oldGeneInt = Character.getNumericValue(oldGene);
                                    if (oldGeneInt != 0){
                                        oldGeneInt--;
                                    } else {
                                        oldGeneInt = 9;
                                    }
                                    String createdGene = Integer.toString(oldGeneInt);
                                    newGene = newGene + createdGene;
                                } else if (upOrDown == 2){
                                    String createdGene = "#";
                                    newGene = newGene + createdGene;
                                }
                           }
                        }
                        bitPosition++;
                    }
                } else { //if it is the result gene it can only be flipped
                    char resultGene = gene.charAt(0);
                    if (resultGene == '0'){
                        newGene = newGene + '1';
                    } else if (resultGene == '1'){
                        newGene = newGene + '0';
                    } else {
                        System.out.println("we have a problem");
                    }
                    bitPosition = 0;
                }
                individual.changeSingleGene(i, newGene);
            } else {
                bitPosition++;
                if (bitPosition > 42){
                    bitPosition = 0;
                }
            }
    }
        Individual mutated = cloneIndividual(individual);
        return mutated;
    }
    
    public int randomNumber(int min, int max){
        
        int chosen = ThreadLocalRandom.current().nextInt(min, max + 1);
        
        return chosen;
    }
    
    public Individual cloneIndividual(Individual individual){
        Individual newIndividual = new Individual();
        
        for (int i  = 0; i < geneticLength; i++){
            String gene = individual.getSingleGene(i);
            newIndividual.changeSingleGene(i, gene);
        }
        newIndividual.changeFitness(rules);
        //System.out.println("changing fitness for new individual");
        
        return newIndividual;
    }
    
    public Population clonePopulation(Population oldPop){
        Population newPop = new Population(populationSize, false);
        
        for(int i = 0; i < populationSize; i++){
            newPop.addIndividual(i, oldPop.getIndividual(i));
        }
        return newPop;
    }
    
    
}
