package com.seabass.nodes;

import java.util.ArrayList;

public class BigRoom {
	
	protected ArrayList<MapNode> group = new ArrayList<>();
	
	protected int x1 = 999999999, x2 = 0, y1 = 999999999, y2 = 0;
	
	public BigRoom(ArrayList<MapNode> group) {
		this.group = group;
		
		for(MapNode it : group) {
			if(it.x < x1) x1 = it.x;
			if(it.y < y1)y1 = it.y;
			if(it.x > x2) x2 = it.x;
			if(it.y > y2)y2 = it.y;
			it.grouped = true;
		}
	}
	
	public BigRoom(MapNode group[]) {
		for(MapNode it : group) {
			if(it.x < x1) x1 = it.x;
			if(it.y < y1)y1 = it.y;
			if(it.x > x2) x2 = it.x;
			if(it.y > y2)y2 = it.y;
			it.grouped = true;
			this.group.add(it);
		}
	}
	
	
	public MapNode[] getGroup() {
		return group.toArray(new MapNode[group.size()]);
	}

	
	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}
}
