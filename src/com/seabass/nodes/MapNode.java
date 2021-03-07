package com.seabass.nodes;

import java.util.ArrayList;

public class MapNode extends GridNode{

	protected MapNode parent;
	
	protected ArrayList<MapNode> children = new ArrayList<MapNode>();
	
	protected boolean end,bonus;
	
	protected ArrayList<MapNode> decendants = new ArrayList<MapNode>();
	
	public boolean empty() {return false;}
	
	public MapNode(MapNode parent, int x, int y) {
		super(x,y);
		this.parent = parent;
		this.bonus = parent.end||parent.bonus;
		parent.addChild(this);
		
	}
	
	public MapNode(MapNode parent, int x, int y,boolean end) {
		super(x,y);
		this.parent = parent;
		this.end = end;
		this.bonus = (parent.end||parent.bonus)&&!end;
		parent.addChild(this);
	}
	
	public MapNode(int x, int y) {
		super(x,y);
		System.out.println(x+y);
		parent = this;
	}
	
	public MapNode(int x, int y, int e) {
		super(x,y);
	}
	public int getGen() {
		
		int i = 0;
		
		MapNode temp = this;
		
		while(temp != temp.parent) {
			temp = temp.parent;
			i++;
		}
		
		return i;
	}
	
	public int sinceIntersect() {
		
		int i = 0;
		
		MapNode temp = this;
		
		while(temp != temp.parent && temp.getChildren().length <= 1) {
		
			temp = temp.parent;
			i++;
		}
		
		return i;
	}

	public MapNode getParent() {
		return parent;
	}

	public void setParent(MapNode parent) {
		if(!decendants.contains(parent)) this.parent = parent;
	}

	public MapNode[] getChildren() {
		return children.toArray(new MapNode[children.size()]);
	}
	
	public ArrayList<MapNode> getDecendants() {
		
		return decendants;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public NodeCoords coords() {return new NodeCoords(getX(),getY());}
	
	public void addChild(MapNode child) {
		
		addDecendant(child);
		children.add(child);
	}
	
	public void addDecendant(MapNode child) {
		
		decendants.add(child);
		if(parent!=this)parent.addDecendant(child);
	}
}
