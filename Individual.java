package main;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual {
	
	Rectangle rectangle1 = new Rectangle(), rectangle2 = new Rectangle();
	Random rand = new Random();
	
    ArrayList<Rect> rectangles = new ArrayList<Rect>();

    int maxXPos;
    int minXPos;
    int maxYPos;
    int minYPos;

    private int fitness;
    
    public Individual(ArrayList<Rect> rectangles) {
    	
    	for(Rect rect : rectangles) {
    		this.rectangles.add(new Rect(rect.getWidth(), rect.getHeight()));
    	}
        
        maxXPos = rectangles.get(0).getX() + rectangles.get(0).getWidth();
        minXPos = rectangles.get(0).getX();
        maxYPos = rectangles.get(0).getY() + rectangles.get(0).getHeight();
        minYPos = rectangles.get(0).getY();
        
        generateRandomRectangles();
        fitness = calculateFitness();
    }
    
	public boolean checkCollision(Rect rect1, Rect rect2) {
		
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
    
    public void generateRandomRectangles() {
		for(Rect rect : rectangles) {
			
			int randX, randY;

			
			do {
				randX = rand.nextInt(GeneticAlgorithm.gridSizeX);
				randY = rand.nextInt(GeneticAlgorithm.gridSizeY);
				
				rect.setX(randX);
				rect.setY(randY);
			}
			while(rect.getX() + rect.getWidth() > GeneticAlgorithm.gridSizeX || rect.getY() + rect.getHeight() > GeneticAlgorithm.gridSizeY);
			
			int counter = 0;
			boolean collision;
			
			while(counter < 9) {
				Rect rect2 = rectangles.get(counter);
				collision = false;
				if(rect != rect2) {
					while(checkCollision(rect, rect2)) {
						do {
							randX = rand.nextInt(GeneticAlgorithm.gridSizeX);
							randY = rand.nextInt(GeneticAlgorithm.gridSizeY);
							
							rect.setX(randX);
							rect.setY(randY);
						}
						while(rect.getX() + rect.getWidth() > GeneticAlgorithm.gridSizeX || rect.getY() + rect.getHeight() > GeneticAlgorithm.gridSizeY);
										
						collision = true;
					}

				}
				if(collision) {
					counter = 0;
				}else {
					counter++;
				}
			}
		}
	}
	
	int calculateFitness() {

        int bestArea = calculateBestArea();
        int instantArea = calculateInstantArea();

        int fitness = instantArea - bestArea;
        //System.out.println(instantArea + " " + bestArea + " " + fitness);
        return fitness;
	}

	int calculateInstantArea() {
		int minX = Integer.MAX_VALUE, maxX = 0;
		int minY = Integer.MAX_VALUE, maxY = 0;
		
		for(Rect rectangle : this.rectangles) {
			if(rectangle.getX() < minX) {
				minX = rectangle.getX();
			}
			if(rectangle.getX() + rectangle.getWidth() > maxX) {
				maxX = rectangle.getX() + rectangle.getWidth();
			}
			
			if(rectangle.getY() < minY) {
				minY = rectangle.getY();
			}
			if(rectangle.getY() + rectangle.getHeight() > maxY) {
				maxY = rectangle.getY() + rectangle.getHeight();
			}
		}
		
		minXPos = minX;
		maxXPos = maxX;
		minYPos = minY;
		maxYPos = maxY;
		
		return (maxX - minX) * (maxY - minY);
	}

	int calculateBestArea() {
		int bestArea = 0;
		for(Rect rectangle : rectangles) {
			bestArea += rectangle.getArea();
		}
		return bestArea;
	}

    public int getFitness() {
        return calculateFitness();
    }

    public ArrayList<Rect> getRectangles() {
        //ArrayList<Rect> rectangle = new ArrayList<Rect>(rectangles);
        return rectangles;
    }

    public void setRectangles(ArrayList<Rect> mutatedRectangles) {
    	rectangles.clear();
    	for(int i = 0; i < 9; i++) {
    		this.rectangles.add(new Rect(mutatedRectangles.get(i).getX(), mutatedRectangles.get(i).getY(), mutatedRectangles.get(i).getWidth(), mutatedRectangles.get(i).getHeight()));
    	}
    }
}
