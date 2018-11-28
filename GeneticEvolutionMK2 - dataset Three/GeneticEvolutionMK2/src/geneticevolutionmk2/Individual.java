/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticevolutionmk2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author i2-rickwood
 */
public class Individual {
    int geneticLength = 160;
    int gene = 0;
    ArrayList<String> genes = new ArrayList<>();
    //char[] genes = new char[geneticLength];
    public int fitness = 0;
    
    public Individual(){
        //int position = 0;
        for (int i = 0; i < geneticLength; i++){
            String fullGene = "";
            for (int y = 0; y < 7; y++){ //runs 7 times to create genes before a result
                fullGene = "";
                for (int x = 0; x < 6; x++){ //creates the 6 numbers in each 'real data' element
                    gene = ThreadLocalRandom.current().nextInt(0, 10 + 1);
                    if (gene == 10){ //sets as a wildcard
                        String sGene = "#";
                        fullGene = fullGene + sGene;
                    } else {
                        String sGene = Integer.toString(gene);
                        fullGene = fullGene + sGene;
                        //position++;
                    }
                }
                genes.add(fullGene); //adds genes to list
            }
            fullGene = "";
            gene = ThreadLocalRandom.current().nextInt(0, 1 + 1); //every 43rd 'bit' will be only 0 or 1 as this is the result bit
            String sGene = Integer.toString(gene);
            fullGene = fullGene + sGene;
            genes.add(fullGene);
        }
    }
    
    public void setFitness(RuleBase ruleBase){
        ArrayList<String> geneRules = new ArrayList<>();
        int newFitness = 0;
        String Gene = "";
        int stringMax = 0;
        int setsAdded = 0;
        
        for (int i = 0; i < geneticLength; i++){ //for all genes of the individual
            String Sgene = getSingleGene(i); //set single gene to string
            Gene = Gene + Sgene; //add gene to existing string
            stringMax++;
            if (stringMax == (ruleBase.getRule(0).getCondition().length() + 1)){ //if string length is now equal to condition length + result
                geneRules.add(setsAdded, Gene); //add string to arrayList
                setsAdded++;
                stringMax = 0; //reset string count
                Gene = ""; //reset string of genes
            }
        }
        for (int j = 0; j < ruleBase.getListSize(); j++){ //for each data set rule
            //System.out.println("Testing for ruleBase rule " + j);
            String Con = ruleBase.getRule(j).getCondition();
            String Res = ruleBase.getRule(j).getResult();
            boolean flag = false;
            for (int k = 0; !flag && k < geneRules.size(); k++){ //compares to each local rule
                boolean conMatch = false;
                boolean resMatch = false;
                String localRule = geneRules.get(k);
                //System.out.println("The condition is " + Con + " the result is " + Res + " the local rule is " + localRule);
                conMatch = matchCon(Con, localRule, ruleBase.getRule(k).getCondition().length());
                if (conMatch){ //if conditions match returns true
                    resMatch = matchRes(Res, localRule, ruleBase.getRule(k).getCondition().length());
                    if (resMatch){
                        newFitness++; //if condition matches and result does then increase fitness by one
                        //System.out.println("increasing fitness");
                        flag = true;
                    } else {
                        //System.out.println("result didn't match");
                        flag = true; //if only condition matches then break anyway
                    }
                }
            }
        }
        fitness = newFitness;
    }
    
    public boolean matchCon(String Con, String localRule, int conLength){
        boolean matched = false;
        int matches = 0;
        boolean subFlag = false;
        for (int x = 0; !subFlag && x < conLength; x++){
            char localChar = localRule.charAt(x);
            char conChar = Con.charAt(x);
            if (localChar == conChar){
                matches++;
                //System.out.println("Sucessful match");
            } else if (localChar == '#'){
                matches++;
                //System.out.println("Sucessful match");
            } else {
                matches = 0;
                //System.out.println("Failed match");
                subFlag = true;
            }
        }
        if (matches == conLength){
            System.out.println("Condition matched");
            matched = true;
        }
        return matched;
    }
    
    public boolean matchRes(String Res, String localRule, int conLength){
        boolean matched = false;
        char result = Res.charAt(0);
        char localResult = localRule.charAt(conLength);
        if (result == localResult){
            matched = true;
        }
        return matched;
    }
    
    public int returnFitness(){
        return fitness;
    }
    
    public void changeFitness(RuleBase ruleBase){
        setFitness(ruleBase);
    }
    
    
    public String getSingleGene(int gene){
        String returnedGene = genes.get(gene);
        return returnedGene;
    }
    
    public void changeSingleGene(int gene, String newgene){
        genes.set(gene, newgene);
    }
}
