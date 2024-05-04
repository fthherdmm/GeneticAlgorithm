package main;

import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JFrame;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class GeneticAlgorithm extends JComponent{
	
	int rectangleNum;
	static int gridSizeX, gridSizeY;
	static ArrayList<Rect> rectangles;
	
	final int populationSize = 100;

	Random rand = new Random();
	
	Rectangle rectangle1 = new Rectangle(), rectangle2 = new Rectangle();
	
	ArrayList<Individual> population = new ArrayList<Individual>();
	Individual bestIndividual;	
	
	public GeneticAlgorithm() {
		readFile();
		
		for(int i = 0; i < populationSize; i++) {
			Individual individual = new Individual(rectangles);
			population.add(individual);
		}
		
		//population.sort((population1, population2) -> Integer.compare(population1.getFitness(), population2.getFitness()));
		
		for(int i = 0; i < 5000; i++) {
			crossover();
			System.out.println(population.get(bestIndividual()).getFitness());
		}
		
		bestIndividual = population.get(bestIndividual());
		
		writeFile(bestIndividual);
	}
	
	public boolean checkCollisionRect(Rect rect1, Rect rect2) {
		
		rectangle1.x = rect1.getX();
		rectangle1.y = rect1.getY();
		rectangle1.width = rect1.getWidth() + 0;
		rectangle1.height = rect1.getHeight() + 0;
		
		rectangle2.x = rect2.getX();
		rectangle2.y = rect2.getY();
		rectangle2.width = rect2.getWidth() + 0;
		rectangle2.height = rect2.getHeight() + 0;
		
		
		if (rectangle1.intersects(rectangle2)) {
			return true;
		}
		
		return false;
	}
	
	public boolean checkCollision(Individual individual) {
		for(Rect rect : individual.getRectangles()) {
			
			int counter = 0;
			
			if(rect.getX() + rect.getWidth() > gridSizeX || rect.getY() + rect.getHeight() > gridSizeY) {
				return true;
			}
			
			while(counter < rectangleNum) {
				Rect rect2 = individual.getRectangles().get(counter);
				if(rect != rect2) {
					if(checkCollisionRect(rect, rect2)) {
						return true;
					}
				}
				counter++;
			}
		}
		
		return false;
	}
	
	public void crossover() {
		
		Individual childIndividual = new Individual(rectangles);
		
		do {
			Individual parent1 = selectParent();
			Individual parent2 = selectParent();
			
			ArrayList<Rect> newRectangles = new ArrayList<Rect>();
			
			int pivot = rand.nextInt(9);
			
			for(int i = 0; i < pivot; i++) {
				newRectangles.add(new Rect(parent1.rectangles.get(i).getX(), parent1.rectangles.get(i).getY(), parent1.rectangles.get(i).getWidth(), parent1.rectangles.get(i).getHeight()));
			}
			
			for(int i = pivot; i < 9; i++) {
				newRectangles.add(new Rect(parent2.rectangles.get(i).getX(), parent2.rectangles.get(i).getY(), parent2.rectangles.get(i).getWidth(), parent2.rectangles.get(i).getHeight()));
			}
			
			childIndividual.setRectangles(newRectangles);
			
			if(rand.nextInt(100) < 15) {
				//childIndividual = mutation(childIndividual);
			}
			
		}while(checkCollision(childIndividual) || childIndividual.getFitness() > population.get(bestIndividual()).getFitness());
		
		System.out.println(population.get(bestIndividual()).getFitness() + " " + childIndividual.getFitness() + " " + population.get(worstIndividual()).getFitness());

		if(population.get(bestIndividual()).getFitness() > childIndividual.getFitness()) {
			population.set(worstIndividual(), childIndividual);
		}
		
	}
	
	public Individual mutation(Individual ind) {
		int randRect = rand.nextInt(rectangleNum);
		Rect rect = ind.rectangles.get(randRect);
		
		do {
			int tempWidth = rect.getWidth();
			rect.setWidth(rect.getHeight());
			rect.setHeight(tempWidth);
		}
		while(rect.getX() + rect.getWidth() > GeneticAlgorithm.gridSizeX || rect.getY() + rect.getHeight() > GeneticAlgorithm.gridSizeY);
		
		int counter = 0;
		boolean collision;
		
		while(counter < 9) {
			Rect rect2 = ind.rectangles.get(counter);
			collision = false;
			if(rect != rect2) {
				if(checkCollisionRect(rect, rect2)) {
					int tempWidth = rect.getWidth();
					rect.setWidth(rect.getHeight());
					rect.setHeight(tempWidth);
				}
			}
			if(collision) {
				counter = 0;
			}else {
				counter++;
			}
		}
		
		ind.rectangles.set(randRect, rect);
			
		return ind;
	}
	
	public int bestIndividual() {
		int minFitness = Integer.MAX_VALUE;
		int bestIndex = -1;
		
		for(int i = 0; i < populationSize; i++) {
			if(population.get(i).getFitness() < minFitness) {
				bestIndex = i;
				minFitness = population.get(i).getFitness();
			}
		}
		
		return bestIndex;
	}
	
	public int worstIndividual() {
		int maxFitness = 0;
		int worstIndex = -1;
		
		for(int i = 0; i < populationSize; i++) {
			if(population.get(i).getFitness() > maxFitness) {
				worstIndex = i;
				maxFitness = population.get(i).getFitness();
			}
		}
		
		return worstIndex;
	}
	
	public Individual selectParent() {
		
		int[] randoms = new int[3];
		
		do {
			
			randoms[0] = rand.nextInt(populationSize);
			randoms[1] = rand.nextInt(populationSize);
			randoms[2] = rand.nextInt(populationSize);
			
		}while(randoms[0] == randoms[1] || randoms[0] == randoms[2] || randoms[1] == randoms[2]);

		int minFitness = Integer.MAX_VALUE;
		Individual bestIndividual = null;
		
		
		for(int i = 0; i < 3; i++) {
			if(population.get(randoms[i]).getFitness() < minFitness) {
				bestIndividual = population.get(randoms[i]);
				minFitness = population.get(randoms[i]).getFitness();
			}
		}
		
		
		return bestIndividual;
	}


	public void readFile() {
		 try {
		      File input = new File("input.txt");
		      Scanner myReader = new Scanner(input);
		      
		      rectangleNum = Integer.parseInt(myReader.nextLine());
		      	  
		      String[] screenSize = myReader.nextLine().split(" ");
		      gridSizeX = Integer.parseInt(screenSize[0]);
		      gridSizeY = Integer.parseInt(screenSize[1]);
		      
		      rectangles = new ArrayList<Rect>();
		      
		      for(int i = 0; i < rectangleNum; i++) { 
			      String[] recSize = myReader.nextLine().split(" ");
			      int recWidth = Integer.parseInt(recSize[0]);
			      int recHeight = Integer.parseInt(recSize[1]);
			      rectangles.add(new Rect(recWidth, recHeight)); 
		      }
		      
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	  public void writeFile(Individual individual) {
		    try {
		        FileWriter output = new FileWriter("output.txt");
		        for(int i = 0; i < gridSizeY; i++) {
			        for(int j = 0; j < gridSizeX; j++) {
			        	boolean checkRect = false;
			        	int rectNum = 0;
			        	int counter = 1;
			        	for(Rect rect : individual.getRectangles()) {
			        		rectNum++;
			        		if(rect.getX() + rect.getWidth() >= j && rect.getX() <= j &&
			        				rect.getY() + rect.getHeight() >= i && rect.getY() <= i) {
			        			checkRect = true;
			        			counter = rectNum;
			        		}
			        	}
			        	if(checkRect) {
			        		output.write("1"); //String.valueOf(counter)
			        	}else {
			        		output.write("0");
			        	}
			        	
			        }
			        output.write("\n");
		        }
		        output.write("\n");output.write("\n");output.write("\n");output.write("\n");
		        output.close();
		      } catch (IOException e) {
		        System.out.println("An error occurred.");
		        e.printStackTrace();
		      }
		  }
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int offsetX = 10;
        int offsetY = 10;
        
        Color[] colors = {Color.BLUE, Color.GREEN, Color.YELLOW, Color.PINK, Color.BLACK, Color.GRAY, Color.CYAN, Color.ORANGE, Color.MAGENTA};
        int counter = 0;
        
        System.out.println("Best Individual Fitness:" + bestIndividual.getFitness());
        
        g.drawRect(offsetX, offsetY, gridSizeX, gridSizeY);
        rectangles = bestIndividual.getRectangles();
        for (Rect rect : rectangles) {
        	g.setColor(colors[counter]);
            g.fillRect(rect.getX() + offsetX, rect.getY() + offsetX, rect.getWidth(), rect.getHeight());
            counter++;
        }
    }

	public static void main(String[] args) {
		
		GeneticAlgorithm ga = new GeneticAlgorithm();
		
        JFrame frame = new JFrame("Rectangles");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(ga.gridSizeX + 20, ga.gridSizeY + 50);
        frame.getContentPane().add(ga);

        frame.setVisible(true);

	}

}