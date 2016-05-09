package com.ttran.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.Random;
import java.util.prefs.Preferences;

import sun.rmi.runtime.Log;


public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	// ShapeRenderer shapeRenderer;
	Texture bird1;
	Texture bird2;
	Circle birdCircle;
	Texture [] birds;
	float birdVert =0;
	Texture bottomtube;
	Texture toptube;
	Texture gameover;
	Texture car;
	int birdState = 0;
	float gap = 600;
	float velocity = 0;
	int gameState = 0;
	float gravity = 0.5f;
	float maxOffset;
	int score = 0;
	int highScore = 0;
	int scoringTube = 0;
	int numOfPraises = 5;
	float carY = 0;
	BitmapFont scoreFont;
	BitmapFont highScoreFont;
	BitmapFont [] praisesFont;
	String [] praises = {"Great", "Wonderful", "Fantastic", "Super", "Magfinicient"};

	float tubeVelocity = 4;

	int numOfTubes = 4;
	float [] tubeX = new float[numOfTubes];
	float [] offset = new float[numOfTubes];
	float distanceBetweenTubes;
	Random randomGenerator;

	Rectangle [] topTubeRectangles;
	Rectangle [] bottomTubeRectangles;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		bird1 = new Texture("birdup.png");
		bird2 = new Texture("birddown.png");
		birds = new Texture[2];
		birds[0] = new Texture("birdup.png");
		birds[1] = new Texture("birddown.png");
		bottomtube = new Texture("bottomtube.png");
		toptube = new Texture("toptube.png");
		maxOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		// shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		topTubeRectangles = new Rectangle[numOfTubes];
		bottomTubeRectangles = new Rectangle[numOfTubes];
		scoreFont = new BitmapFont();
		gameover = new Texture("gameover.jpg");
		car = new Texture("smallcar.png");
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		praisesFont = new BitmapFont[5];
		initialize();
		startGame();
	}

	public void initialize(){
		for (int i=0; i < numOfPraises; i++){
			praisesFont[i] = new BitmapFont();
			praisesFont[i].setColor(Color.YELLOW);
			praisesFont[i].getData().setScale(10);
		}

	}

	public void startGame(){
		birdVert = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;
		carY = Gdx.graphics.getHeight()/4;

		for (int i=0; i < numOfTubes; i++){
			tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2  + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			offset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()/2 - gap/2);
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gameState == 1) {
			if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2){
				score++;
				if (scoringTube < numOfTubes -1){
					scoringTube++;
				}
				else
					scoringTube = 0;

			}
			if (Gdx.input.justTouched()) {
				velocity = -20;

			}


			for (int i=0; i < numOfTubes; i++) {
				if (tubeX[i] < - toptube.getWidth()){
					tubeX[i] += numOfTubes * distanceBetweenTubes;
					offset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()/2 - gap/2);
				}
				tubeX[i] -= tubeVelocity;
				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + offset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + offset[i]);

				topTubeRectangles[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + offset[i],toptube.getWidth(),toptube.getHeight());
				bottomTubeRectangles[i].set(tubeX[i],Gdx.graphics.getHeight()/2 - gap / 2 - bottomtube.getHeight() + offset[i], bottomtube.getWidth(),bottomtube.getHeight());
			}

			if (birdVert > 0 || velocity < 0) {
				velocity = velocity + gravity;
				birdVert -= velocity;
			}

			if (carY > Gdx.graphics.getHeight()/4 || velocity < 0) {
				velocity = velocity + gravity;
				carY -= velocity;
			}
		}
		else if (gameState == 0) {
			if (Gdx.input.justTouched()){
				gameState = 1;
			}
		}
		else if (gameState == 2) {
			batch.draw(gameover,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
			if (Gdx.input.justTouched()){
				gameState = 1;
				score = 0;
				scoringTube = 0;
				velocity = 0;
				startGame();
			}
		}

		if (birdState == 0) {
			birdState = 1;
		} else {
			birdState = 0;
		}

		batch.draw(birds[birdState], Gdx.graphics.getWidth() / 2 - birds[birdState].getWidth() / 2, birdVert);
		batch.draw(car,Gdx.graphics.getWidth()/3,carY);
		birdCircle.set(Gdx.graphics.getWidth() / 2, birdVert + birds[birdState].getHeight() / 2, birds[birdState].getWidth() / 2);
		scoreFont.setColor(Color.YELLOW);
		scoreFont.getData().setScale(5);
		scoreFont.draw(batch,Integer.toString(score),100,100);
		// shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		// shapeRenderer.setColor(Color.RED);
		// shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		for (int i=0; i < numOfTubes; i++) {
			// shapeRenderer.rect(topTubeRectangles[i].x,topTubeRectangles[i].y,toptube.getWidth(),toptube.getHeight());
			// shapeRenderer.rect(bottomTubeRectangles[i].x,bottomTubeRectangles[i].y,bottomtube.getWidth(),bottomtube.getHeight());

			if (Intersector.overlaps(birdCircle,topTubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangles[i])){
				// gameState = 2;

			}
		}
		// shapeRenderer.end();
		batch.end();


	}
}
