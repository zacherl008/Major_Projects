/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecosystem;

/**
 *
 * @author kzacherl
 */

public class World {
    private Acre[][] acreArray = new Acre[3][3];
    
    public World(){
        initAcres();
    }
    
    public Acre[][] getAcreArray(){
        return acreArray;
    }
    
    private void printInfo(){
        for(int i = 0; i < acreArray.length; i++){
            for(int j = 0; j < acreArray[i].length; j++){
                h.pl("Grass: " + acreArray[i][j].numGrass());
                h.pl("Sheep: " + acreArray[i][j].numSheep());
                h.pl("Wolves: " + acreArray[i][j].numWolves());
            }
        }
    }
    private void initAcres(){
        //adding an acre to each spot in the 2d array
        for(int i = 0; i < acreArray.length; i++){
            for(int j = 0; j < acreArray[i].length; j++){
                acreArray[i][j] = new Acre();
            }
        }
        for(int i = 0; i < acreArray.length; i++){
            for(int j = 0; j < acreArray[i].length; j++){
                //add north neighbor
                if(i == 0){
                    acreArray[i][j].setNorth(null);
                }
                else{
                    acreArray[i][j].setNorth(acreArray[i-1][j]);
                }    
                //add east neighbor
                if(j >= acreArray[i].length - 1){
                    acreArray[i][j].setNorth(null);
                }    
                else{
                    acreArray[i][j].setEast(acreArray[i][j+1]);
                }
                //add south neighbor
                if(i == acreArray.length - 1){
                    acreArray[i][j].setSouth(null);
                }
                else{
                    acreArray[i][j].setSouth(acreArray[i+1][j]);
                }   
                //add west neighbor   
                if(j == 0){
                    acreArray[i][j].setWest(null);
                }
                else{
                    acreArray[i][j].setWest(acreArray[i][j-1]);
                }
            }
        }
    }
    
    public void cycle(){
            loopAcres();
    }
    public void loopAcres(){
        for(int i = 0; i < acreArray.length; i++){
            for(int j = 0; j < acreArray[i].length; j++){
                acreArray[i][j].growGrass();
                h.pl("Acre " + i + "," + j);
                h.pl("Wolves: " + acreArray[i][j].numWolves());
                h.pl("Sheep: " + acreArray[i][j].numSheep());
                h.pl("Grass: " + acreArray[i][j].numGrass());
                acreArray[i][j].loopAnimals();
                h.pl("");
            }
        }
        //reset reproduced variable at the end of the day
        for(int i = 0; i < acreArray.length; i++){
            for(int j = 0; j < acreArray[i].length; j++){
                acreArray[i][j].resetRepro();
            }
        }
    }
 
}
