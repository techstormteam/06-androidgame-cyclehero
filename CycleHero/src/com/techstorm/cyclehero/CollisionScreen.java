package com.techstorm.cyclehero;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.android.androidgames.framework.gl.SpatialHashGrid;
import com.android.androidgames.framework.gl.Vertices;
import com.techstorm.androidgames.framework.Game;
import com.techstorm.androidgames.framework.Input.TouchEvent;
import com.techstorm.androidgames.framework.impl.GLGame;
import com.techstorm.androidgames.framework.impl.GLGraphics;
import com.techstorm.androidgames.framework.math.OverlapTester;
import com.techstorm.androidgames.framework.math.Vector2;
import com.techstorm.androidgames.gamedev2d.AcceleratorSprite;
import com.techstorm.androidgames.gamedev2d.Cannon;
import com.techstorm.androidgames.gamedev2d.Sprite;
import com.techstorm.cyclehero.sprite.Target;

public class CollisionScreen extends GameScreen {
	final int NUM_TARGETS = 20;
	final float WIDTH = 5.4f;
	final float HEIGHT = 9.6f;
	
	GLGraphics glGraphics;
	Cannon cannon;
	AcceleratorSprite ball;
	List<Sprite> targets;
	SpatialHashGrid grid;
	
	Vertices cannonVertices;
	Vertices ballVertices;
	Vertices targetVertices;
	
	Vector2 touchPos = new Vector2();
	Vector2 gravity = new Vector2(0, -10);

	public CollisionScreen(Game game) {
		super(game);
		glGraphics = ((GLGame)game).getGLGraphics();
		cannon = new Cannon(0, 0, 1, 1);
		ball = new AcceleratorSprite(0, 0, 0.2f, 0.2f);
		targets = new ArrayList<Sprite>(NUM_TARGETS);
		grid = new SpatialHashGrid(WIDTH, HEIGHT, 2.0f);
		for(int i = 0; i < NUM_TARGETS; i++) {
			Target target = new Target((float)Math.random() * WIDTH, (float)Math.random() * HEIGHT,
					0.5f, 0.5f);
			grid.insertStaticObject(target);
			targets.add(target);
		}
		
		cannonVertices = new Vertices(glGraphics, 3, 0, false, false);
		cannonVertices.setVertices(new float[] {	-1.0f, -1.0f, 
													1.0f, 0.0f,
													-1.0f, 1.0f }, 0, 6);
		
		ballVertices = new Vertices(glGraphics, 4, 6, false, false);
		ballVertices.setVertices(new float[] { -0.1f, -0.1f, 
												0.1f, -0.1f, 
												0.1f, 0.1f,
												-0.1f, 0.1f }, 0, 8);
		ballVertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0 }, 0, 6);
		
		targetVertices = new Vertices(glGraphics, 4, 6, false, false);
		targetVertices.setVertices(new float[] {	-0.25f, -0.25f, 
													0.25f, -0.25f,
													0.25f, 0.25f, 
													-0.25f, 0.25f }, 0, 8);
		targetVertices.setIndices(new short[] {0, 1, 2, 2, 3, 0}, 0, 6);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for(int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			touchPos.x = (event.x / (float) glGraphics.getWidth()) * WIDTH;
			touchPos.y = (1 - event.y / (float) glGraphics.getHeight()) * HEIGHT;
			
			cannon.angle = touchPos.sub(cannon.position).angle();
			
			if(event.type == TouchEvent.TOUCH_UP) {
				float radians = cannon.angle * Vector2.TO_RADIANS;
				float ballSpeed = touchPos.len() * 2;
				ball.position.set(cannon.position);
				ball.velocity.x = FloatMath.cos(radians) * ballSpeed;
				ball.velocity.y = FloatMath.sin(radians) * ballSpeed;
				ball.bounds.lowerLeft.set(ball.position.x - 0.1f, ball.position.y - 0.1f);
			}
		}
		
		ball.velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
		ball.position.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
		ball.bounds.lowerLeft.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
		
		List<Sprite> colliders = grid.getPotentialColliders(ball);
		len = colliders.size();
		for(int i = 0; i < len; i++) {
			Sprite collider = colliders.get(i);
			if(OverlapTester.overlapRectangle(ball.bounds, collider.bounds)) {
				grid.removeObject(collider);
				targets.remove(collider);
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, WIDTH, 0, HEIGHT, 1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glColor4f(0, 1, 0, 1);
		targetVertices.bind();
		int len = targets.size();
		for(int i = 0; i < len; i++) {
			Sprite target = targets.get(i);
			gl.glLoadIdentity();
			gl.glTranslatef(target.position.x, target.position.y, 0);
			targetVertices.draw(GL10.GL_TRIANGLES, 0, 1);
		}
		targetVertices.unbind();
		
		gl.glLoadIdentity();
		gl.glTranslatef(ball.position.x, ball.position.y, 0);
		gl.glColor4f(1, 0, 0, 1);
		ballVertices.bind();
		ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
		ballVertices.unbind();
		
		gl.glLoadIdentity();
		gl.glTranslatef(cannon.position.x, cannon.position.y, 0);
		gl.glRotatef(cannon.angle, 0, 0, 1);
		gl.glColor4f(1, 1, 1, 1);
		cannonVertices.bind();
		cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
		cannonVertices.unbind();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
