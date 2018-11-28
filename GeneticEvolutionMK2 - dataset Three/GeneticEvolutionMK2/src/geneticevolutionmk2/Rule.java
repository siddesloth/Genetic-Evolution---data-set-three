/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticevolutionmk2;

import java.util.ArrayList;

/**
 *
 * @author Isaac
 */
public class Rule {
    ArrayList<String> condition = new ArrayList<>();
    String result;
    
    public Rule(String rule){
        
        String ss[] = rule.split("\\s+");
        for (int i = 0; i < ss.length; i++){
            String partialCondition = "";
            String fullCondition = "";
           if (i < (ss.length-1)){
               fullCondition = ss[i]; //sets to the full '0.xxxxxx.'
               for (int y = 2; y < 8; y++){
                   partialCondition = partialCondition + fullCondition.charAt(y); //sets to only the elements after the '0.' part of the gene
               }
               setCondition(partialCondition); //sets condition to only the numbers as this can compare directly to the created individuals
           } else {
               setResult(ss[i]); //sets result
           }
        }
    }
    public void setCondition(String newCondition){
        condition.add(newCondition);
    }
    
    public void setResult(String newResult){
        //System.out.println("The result is " + newResult);
        result = newResult;
    }
    
    public String getCondition(){
        String useableCondition = "";
        for (int x = 0; x < condition.size(); x++){
            useableCondition = useableCondition + condition.get(x);
        }
        return useableCondition;
    }
    
    public String getResult(){
        return result;
    }
    
}
