package com.seabass.nodes;

public class EmptyNode extends MapNode{

	
	public boolean empty() {return true;}
	
	
	public EmptyNode(int x, int y) {
		super(x, y,1);
	}

	public int getGen() {
		
		System.out.println("Warning: trying to get generation of empty node" );
		
		int test[] = {1};
		test[3] = 4;
		return 0;
	}
	
	public int sinceIntersect() {
		
		System.out.println("Warning: trying to get last intersection");
		
		int test[] = {1};
		test[3] = 4;
		
		return 0;
	}
}
