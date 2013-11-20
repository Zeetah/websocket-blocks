package com.zeetah.blocks.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.zeetah.blocks.model.UserCanvas;
import com.zeetah.blocks.model.Block;
import com.zeetah.blocks.protocol.AddRequest;
import com.zeetah.blocks.protocol.MoveRequest;
import com.zeetah.blocks.protocol.RemoveRequest;

@Service
public class BlockService {

	private final Map<String, UserCanvas> canvases = new HashMap<>();

	private SimpMessageSendingOperations messagingTemplate;

	@Autowired
	public BlockService(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;

		UserCanvas canvas = new UserCanvas();
		canvas.addBlock(new Block("blo1", 0, 0));
		canvas.addBlock(new Block("blo2", 100, 100));
		this.canvases.put("block", canvas);

		canvas = new UserCanvas();
		canvas.addBlock(new Block("soc1", 0, 50));
		canvas.addBlock(new Block("soc2", 50, 100));
		this.canvases.put("socket", canvas);
	}

	private UserCanvas findCanvas(String username) {
		UserCanvas canvas = this.canvases.get(username);
		if (canvas == null) {
			throw new IllegalArgumentException(username);
		}
		return canvas;
	}

	public List<Block> getBlocks(Principal principal) {
		UserCanvas canvas = findCanvas(principal.getName());
		return canvas.getBlocks();
	}

	public List<Block> getOtherBlocks(Principal principal) {
		List<Block> blocks = new ArrayList<Block>();
		for (Map.Entry<String, UserCanvas> entry : this.canvases.entrySet()) {
			if (!entry.getKey().equals(principal.getName())) {
				UserCanvas canvas = (UserCanvas) entry.getValue();
				blocks.addAll(canvas.getBlocks());
			}
		}
		return blocks;
	}

	public void move(Principal principal, MoveRequest move) {
		UserCanvas canvas = findCanvas(principal.getName());
		Block block = canvas.move(move);
		messagingTemplate.convertAndSend("/topic/user.block", block);
	}

	public void add(Principal principal, AddRequest add) {
		UserCanvas canvas = findCanvas(principal.getName());

		Block newBlock = new Block(
				getName(principal.getName(), canvas.getId()), add.getX(),
				add.getY());
		canvas.addBlock(newBlock);
		newBlock.setUser(principal.getName());
		this.canvases.put(principal.getName(), canvas);
		messagingTemplate.convertAndSend("/topic/user.block.add", newBlock);
	}

	public void remove(Principal principal, RemoveRequest remove) {
		UserCanvas canvas = findCanvas(principal.getName());
		canvas.remove(remove.getName());
		this.canvases.put(principal.getName(), canvas);
		messagingTemplate.convertAndSend("/topic/user.block.remove", remove);
	}

	private String getName(String name, int id) {
		return name.substring(0, 3) + id;
	}

}
