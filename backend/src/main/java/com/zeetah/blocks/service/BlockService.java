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
import com.zeetah.blocks.protocol.RemoveRequest;

@Service
public class BlockService {

	private final Map<String, UserCanvas> canvases = new HashMap<>();

	private SimpMessageSendingOperations messagingTemplate;

	@Autowired
	public BlockService(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;

		UserCanvas canvas = new UserCanvas();
		canvas.addBlock(new Block("blo1", 20, 40));
		canvas.addBlock(new Block("blo2", 200, 100));
		this.canvases.put("block", canvas);

		canvas = new UserCanvas();
		canvas.addBlock(new Block("soc1", 30, 150));
		canvas.addBlock(new Block("soc2", 180, 250));
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

	public void add(Principal principal, AddRequest add) {
		UserCanvas canvas = findCanvas(principal.getName());

		Block newBlock = new Block(
				getName(principal.getName(), canvas.getId()), add.getX(),
				add.getY());
		canvas.addBlock(newBlock);
		newBlock.setUser(principal.getName());
		this.canvases.put(principal.getName(), canvas);
		messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/user.block.add", newBlock);
	}

	public void remove(Principal principal, RemoveRequest remove) {
		UserCanvas canvas = findCanvas(principal.getName());
		canvas.remove(remove.getName());
		this.canvases.put(principal.getName(), canvas);
		messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/user.block.remove", remove);
	}

	private String getName(String name, int id) {
		return name.substring(0, 3) + id;
	}

}
