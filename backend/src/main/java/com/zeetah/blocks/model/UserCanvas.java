package com.zeetah.blocks.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zeetah.blocks.protocol.MoveRequest;


public class UserCanvas {

	private final Map<String,Block> blocks = new HashMap<String,Block>();
	private int id;

	public UserCanvas() {
		this.id = 1;
	}
	
	public int getId() {
		return id;
	}
	
	private void incrementId() {
		this.id++;
	}

	public List<Block> getBlocks() {
		return new ArrayList<Block>(blocks.values());
	}

	public void addBlock(Block block) {
		incrementId();
		blocks.put(block.getName(), block);
	}

	public Block getBlock(String name) {
		return blocks.get(name);
	}

	public Block move(MoveRequest move) {
		Block block = blocks.get(move.getName());
		if ((block == null) || (move.getX() == 0) || (move.getY() == 0)) {
			return null;
		}
		block = new Block(block, move.getX(), move.getY());
		blocks.put(move.getName(), block);
		return block;
	}

	public void remove(String name) {
		Block block = blocks.get(name);
		if (block != null) {
			blocks.remove(name);
		}
	}
}
