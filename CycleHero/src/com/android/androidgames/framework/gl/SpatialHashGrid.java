package com.android.androidgames.framework.gl;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.util.FloatMath;

import com.android.androidgames.gamedev2d.GameObject;

@SuppressLint("FloatMath")
public class SpatialHashGrid {
	List<GameObject>[] dynamicCells;
	List<GameObject>[] staticCells;
	int cellsPerRow;
	int cellsPerCol;
	float cellSize;
	int[] cellIds = new int[4];
	List<GameObject> foundObjects;
	
	@SuppressLint("FloatMath")
	@SuppressWarnings("unchecked")
	public SpatialHashGrid(float worldWidth, float worldHeight, float cellSize) {
		this.cellSize = cellSize;
		this.cellsPerRow = (int)FloatMath.ceil(worldWidth/cellSize);
		this.cellsPerCol = (int)FloatMath.ceil(worldHeight/cellSize);
		int numCells = cellsPerRow * cellsPerCol;
		dynamicCells = new List[numCells];
		staticCells =  new List[numCells];
		for(int i = 0; i < numCells; i++) {
			dynamicCells[i] = new ArrayList<GameObject>(10);
			staticCells[i] = new ArrayList<GameObject>(10);
		}
		foundObjects = new ArrayList<GameObject>(10);
	}
	
	public void insertStaticObject(GameObject object) {
		int[] cellIds = getCellIds(object);
		int i = 0;
		int cellId = -1 ;
		while(i < 4 && (cellId = cellIds[i++]) != -1) {
			staticCells[cellId].add(object);
		}
	}
	
	public void insertDynamicObject(GameObject objectbj) {
		int[] cellIds = getCellIds(objectbj);
		int i = 0;
		int cellId = -1 ;
		while(i < 4 && (cellId = cellIds[i++]) != -1) {
			dynamicCells[cellId].add(objectbj);
		}
	}
	
	public void removeObject(GameObject objectj) {
		int[] cellIds = getCellIds(objectj);
		int i = 0;
		int cellId = -1;
		while(i < 4 && (cellId = cellIds[i++]) != -1) {
			staticCells[cellId].remove(objectj);
			dynamicCells[cellId].remove(objectj);
		}
	}
	
	public void clearDynamicCell(GameObject object) {
		int len = dynamicCells.length;
		for (int i = 0; i < len; i++) {
			dynamicCells[i].clear();
		}
	}
	
	public List<GameObject> getPotentialColliders(GameObject object) {
		foundObjects.clear();
		int[] cellIds = getCellIds(object);
		int i = 0;
		int cellId = -1;
		while (i < 4 && (cellId = cellIds[i++]) != -1) {
			int len = dynamicCells[cellId].size();
			for(int j = 0; j < len; j++) {
				GameObject colliders = dynamicCells[cellId].get(j);
				if(!foundObjects.contains(colliders))
					foundObjects.add(colliders);
			}
			
			len = staticCells[cellId].size();
			for(int j = 0; j < len; j++) {
				GameObject colliders = staticCells[cellId].get(j);
				if(!foundObjects.contains(colliders))
					foundObjects.add(colliders);
			}
		}
		return foundObjects;
	}
	
	public int[] getCellIds(GameObject objectj) {
		int x1 = (int) FloatMath.floor(objectj.bounds.lowerLeft.x / cellSize);
		int y1 = (int) FloatMath.floor(objectj.bounds.lowerLeft.y / cellSize);
		int x2 = (int) FloatMath.floor((objectj.bounds.lowerLeft.x + objectj.bounds.width ) / cellSize);
		int y2 = (int) FloatMath.floor((objectj.bounds.lowerLeft.y + objectj.bounds.height) / cellSize);
		
		if(x1==x2 && y1 == y2) {
			if(x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
				cellIds[0] = x1 + y1 * cellsPerRow;
			else
				cellIds[0] = -1;
			cellIds[1] = -1;
			cellIds[2] = -1;
			cellIds[3] = -1;
		}
		else if( x1 == x2 ) {
			int i = 0;
			if(x1 >= 0 && x1 < cellsPerRow) {
				if(y1 >= 0 && y1 < cellsPerCol)
					cellIds[i++] = x1 + y1 * cellsPerRow;
				if(y2 >= 0 && y2 < cellsPerCol)
					cellIds[i++] = x1 + y2 * cellsPerRow;
			}
			while(i < 4)
				cellIds[i++] = -1;
		}
		else if (y1 == y2) {
			int i = 0;
			if(y1 >= 0 && y1 < cellsPerCol) {
				if(x1 >= 0 && x1 < cellsPerRow)
					cellIds[i++] = x1 + y1 * cellsPerRow;
				if(x2 >= 0 && x2 < cellsPerRow)
					cellIds[i++] = x2 + y1 * cellsPerRow;
			}
			while(i < 4)
				cellIds[i++] = -1;
		}
		else {
			int i = 0;
			int y1CellPerRow = y1 * cellsPerRow;
			int y2CellPerRow = y2 * cellsPerRow;
			if( x1 >= 0 && x1 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
				cellIds[i++] = x1 + y1CellPerRow;
			if( x2 >= 0 && x2 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol)
				cellIds[i++] = x2 + y2CellPerRow;
			if( x2 >= 0 && x2 < cellsPerRow && y1 >= 0 && y1 < cellsPerCol)
				cellIds[i++] = x2 + y1CellPerRow;
			if( x1 >= 0 && x1 < cellsPerRow && y2 >= 0 && y2 < cellsPerCol)
				cellIds[i++] = x1 + y2CellPerRow;
			while(i < 4)
				cellIds[i++] = -1;
		}
		return cellIds;
	}
}
