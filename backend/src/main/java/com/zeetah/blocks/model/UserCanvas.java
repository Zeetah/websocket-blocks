/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zeetah.blocks.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zeetah.blocks.protocol.MoveRequest;


public class UserCanvas {

	private final Map<String,Block> blocks = new HashMap<String,Block>();

	public List<Block> getBlocks() {
		return new ArrayList<Block>(blocks.values());
	}

	public void addBlock(Block position) {
		blocks.put(position.getName(), position);
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
