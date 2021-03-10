package com.seabass.main;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.seabass.nodes.BigRoom;
import com.seabass.nodes.EmptyNode;
import com.seabass.nodes.MapNode;
import com.seabass.nodes.NodeCoords;

public class Test {

	BufferedImage mapImage;
	MapNode map[][];
	MapNode start;
	MapNode end;
	BigRoom bigRooms[];
	
	public Test() {
		
		
		System.out.println("Generating Maze");
		long now = System.currentTimeMillis();
		int endDist = 0;
		map = new MapNode[1][1];
		boolean gen = false;
		while(endDist < map.length + map[0].length-1) {
			createMap(map[0].length,map.length);
			if(!gen) {
				now = System.currentTimeMillis();
				gen = true;
			}
			endDist = generateMap();
		}
		now = System.currentTimeMillis()-now;
		System.out.println("Done Generating Maze. Took " + now +" milliseconds");
		drawMap();
	}
	
	void createMap(int wid,int hig) {
		if(wid <3 && hig <3) {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter Width of Map");
		
		 wid = in.nextInt();
		
		while(wid<=3) {
			System.out.println("Please enter a width above 3");
			
			wid = in.nextInt();
		}
		
		System.out.println("Enter Height of Map");
		
		hig = in.nextInt();
		
		while(hig<=3) {
			System.out.println("Please enter a height above 3");
			
			hig = in.nextInt();
		}
		
		System.out.println("How Many Big Rooms?");
		
		bigRooms = new BigRoom[in.nextInt()];
		
		while(bigRooms.length>= (hig/2)*(wid/2)) {
			System.out.println("Not Enough Space, plesae enter a number smaller than " + ((hig/2)*(wid/2)));
			bigRooms = new BigRoom[in.nextInt()];
		}
		
		in.close();
		}
		MapNode[][] map = new MapNode[hig][wid];
		
		for(int i = 0; i < hig; i++) {
			for(int j = 0; j < wid; j++) {
				
				map[i][j] = new EmptyNode(j,i);
			}
		}
		
	}
	
	int generateMap() {
		
	
		Random r = new Random();
		int start = r.nextInt(((map.length > map[0].length)? map.length : map[0].length)-1);
		int end = r.nextInt(((map.length > map[0].length)? map.length : map[0].length)-1);
		
		boolean restrict = ((map.length < map[0].length)? map.length : map[0].length) <= ((start > end)? start : end);
	
		int side = r.nextInt((restrict)? 1:3);
		int sX = 0, sY = 0, eX = 0, eY = 0;
		if(!restrict) {
			
			switch(side) {
				
			case(0):
				sY = start;
				eX = map[0].length-1;
				eY = end;
				break;
			case(1):
				sX = start;
				eX = end;
				eY = map.length-1;
				break;
			case(2):
				sX = map[0].length-1;
				sY = start;
				eY = end;
				break;
			case(3):
				sX = start;
				sY = map[0].length-1;
				eX = end;
				break;
			default:
				eX = map[0].length-1;
				eY = map.length-1;
				break;
			}
		} else if(map.length > map[0].length) {
			sY = start;
			eY = end;
			if(side == 0) {
				eX = map[0].length-1;
			}else {
				sX = map[0].length-1;
				side = 2;
			}
		} else {
			sX = start;
			eX = end;
			if(side == 0) {
				eY = map.length-1;
				side = 1;
			}else {
				sY = map.length-1;
				side = 3;
			}
		}
		
		MapNode node = new MapNode(sX,sY);
		map[sY][sX] = node;
		this.start = node;
		
		ArrayList<MapNode> empties = new ArrayList<>();
		ArrayList<MapNode> loose = new ArrayList<>();
		for(int i = 0; i < map.length; i++) 
			for(int j = 0; j < map[0].length; j++) 
				empties.add(map[i][j]);
		empties.remove(node);
		boolean endFound = false;
		int sinceJump =0;
		while(empties.size() > 0) {
			
			ArrayList<MapNode> near = new ArrayList<>();
			
			if(endFound) {
				if(node == this.end) {
					int count = Math.abs(r.nextInt()) % (node.getGen()-map.length);
					for(int i = 0; i < count && node != node.getParent(); i++)node = node.getParent();
					sinceJump = 0;
				}
			}
			
			for(int i = -1; i <= 1 ; i++ ) 
				for(int j = -1; j <= 1 && (i+node.getY())==clamp((i+node.getY()),0,map.length-1) ; j++ ) 
					if((j+node.getX())==clamp((j+node.getX()),0,map[0].length-1)&& (i==0||j==0) && i!=j) 
						if(map[node.getY()+i][node.getX()+j].empty()) 
							near.add(map[node.getY()+i][node.getX()+j]);
			
			if(near.size() <= 0) { 
				/*
				if(node.getChildren().length==0)for(int i = 0; i < cappedRand(r,node.getGen());i++)node = node.getParent();
				else {
					for(int i = 0; i < cappedRand(r,map.length*map[0].length-empties.size()) && node.getChildren().length > 0; i++) 
						node = node.getChildren()[cappedRand(r,node.getChildren().length)];
				}*/
				loose.remove(node);
				node = loose.get(cappedRand(r,loose.size()));
				sinceJump = 0;
				continue;
			}
			
			if(near.size()>1&&!loose.contains(node)) loose.add(node);
			
			if(endFound) {
				try {
				if(cappedRand(r,1000)/sinceJump<node.sinceIntersect()) {
					int count = cappedRand(r,node.getGen()-map.length);
					for(int i = 0; i < count && node != node.getParent() && loose.contains(node.getParent()); i++)node = node.getParent();
					sinceJump = 0;
					continue;
				}
				}catch(Exception e) {}
				
				MapNode tempNode = near.get(Math.abs(r.nextInt())%near.size());
				empties.remove(tempNode);
			
				node = new MapNode(node,tempNode.getX(),tempNode.getY());
			
				map[node.getY()][node.getX()] = node;
				
				sinceJump++;
				
			} else {
			
				for(MapNode temp: near) {
					if(temp.getX() == node.getParent().getX() || temp.getY() == node.getParent().getY()) {
						MapNode store = near.get(0);
						near.set(0,temp);
						near.set(near.indexOf(temp), store);
					}
				}
				MapNode tempNode = near.get(0);
				for(int i = 1; i < near.size(); i++) {
					int judge = cappedRand(r,100);
					int turnWrong = 30; //chance that the wandering path will actively turn away from the goal
					int turnRight = 60; //chance that the wandering path will turn towards the goal
					if((Math.abs(tempNode.getX()-eX) + Math.abs(tempNode.getY()-eY) > Math.abs(near.get(i).getX()-eX) + Math.abs(near.get(i).getY()-eY) && judge < turnRight)||judge < turnWrong) tempNode = near.get(i); 
				}
				
				empties.remove(tempNode);
				
				node = new MapNode(node,tempNode.getX(),tempNode.getY());
				
				map[node.getY()][node.getX()] = node;
				
				if(node.getX()==eX&&node.getY() == eY) {
					this.end = node;
					int count = cappedRand(r,node.getGen()-map.length);
					for(int i = 0; i < count && node != node.getParent(); i++)node = node.getParent();
					endFound = true;
					if(this.end.getGen() < map.length + map[0].length) break;
				}
			}
			System.out.println(empties.size());
		}
		ArrayList<MapNode> BigRoomNodes = new ArrayList<>();
		
		for( int i = 0; i < bigRooms.length; i++) {
			
			while(true) {
				int x = cappedRand(r,map[0].length-1), y = cappedRand(r,map.length-1);
				
				MapNode[] tempNodes = new MapNode[4];
				
				tempNodes[0] = map[y][x];
				tempNodes[1] = map[y][x+1];
				tempNodes[2] = map[y+1][x];
				tempNodes[3] = map[y+1][x+1];
				
				boolean bad = false;
				for(MapNode node1: tempNodes) if(BigRoomNodes.contains(node1)) bad = true;
				if(bad)continue;
				
				for(MapNode node1: tempNodes) BigRoomNodes.add(node1);
				
				bigRooms[i] = new BigRoom(tempNodes);
				break;
			}
		}
		
		return this.end.getGen();
	}
	
	
	void drawMap() {
		int mapScale = 1000/map.length;
		BufferedImage img = new BufferedImage((map[0].length + 1) * mapScale, (map.length + 1) * mapScale, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
		
        g2d.setColor(Color.black);
        
		g2d.fillRect(0,0, (map[0].length + 1) * mapScale, (map.length + 1) * mapScale);
		
		int dotSize = mapScale/2;
		
		
		NodeCoords.mapScale = mapScale;
		
		for(int i = 0; i < map.length; i ++) {
			for(int j = 0; j < map[i].length; j++) {
				NodeCoords nc = new NodeCoords(j,i);
				if(map[i][j] == start)g2d.setColor(Color.green);
				else if(map[i][j]==end)g2d.setColor(Color.red);
				else g2d.setColor(Color.gray);
				g2d.fillOval(nc.x-dotSize/2, nc.y-dotSize/2, dotSize, dotSize);
				
				for(MapNode child : map[i][j].getChildren()) {
					if(child == end) g2d.setColor(Color.red);
					NodeCoords cc = child.coords();
					int x = (nc.x < cc.x)? nc.x : cc.x, y = (nc.y < cc.y)? nc.y : cc.y;
					g2d.fillRect(x-dotSize/2, y-dotSize/2,Math.abs(nc.x-child.coords().x)+dotSize, Math.abs(nc.y-child.coords().y)+dotSize);

				}
			}
		}
		
		g2d.setColor(Color.blue);
		for(BigRoom room: bigRooms) {
			NodeCoords rc = new NodeCoords(room.getX1(),room.getY1());
			g2d.fillRect(rc.x-dotSize/2, rc.y-dotSize/2, mapScale*(room.getX2()-room.getX1())+dotSize, mapScale*(room.getY2()-room.getY1())+dotSize);
		}
		
		try {
            
            ImageIO.write(img,"jpg", new File("res/test.jpg"));
        }
        catch(Exception exception) {
        	
        }
		
	}
	
	public static void main(String[] args) {
		
		new Test();
	}
	
	
	public int clamp(int x,int min,int max) {
		if(x > max) return max;
		if(x < min) return min;
		return x;
	}
	
	int cappedRand(Random r, int max) {
		if(max <= 0) return 0;
		return Math.abs(r.nextInt())%max;
	}
	
	void println(String oa) {
		System.out.println(oa);
	}
	

}
