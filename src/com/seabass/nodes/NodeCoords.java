package com.seabass.nodes;

public class NodeCoords {
		
		public int x,y;
		
		public static int mapScale = 1;
		
		public NodeCoords(int x, int y) {
			
			this.x = (x+1)*mapScale;
			this.y = (y+1)*mapScale;
		}
	
}
