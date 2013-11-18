package com.zeetah.blocks.service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.zeetah.blocks.model.UserCanvas;
import com.zeetah.blocks.model.Block;
import com.zeetah.blocks.protocol.MoveRequest;


@Service
public class BlockService {

	private final Map<String, UserCanvas> canvases = new HashMap<>();

	private SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	public BlockService(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
		
		UserCanvas canvas = new UserCanvas();
		canvas.addBlock(new Block("fab1", 0, 0));
		canvas.addBlock(new Block("fab2", 100, 100));
		this.canvases.put("fabrice", canvas);

		canvas = new UserCanvas();
		canvas.addBlock(new Block("pau1", 0, 0));
		canvas.addBlock(new Block("pau2", 100, 100));
		this.canvases.put("paulson", canvas);
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
	
	public void move(Principal principal, MoveRequest move) {
		UserCanvas canvas = findCanvas(principal.getName());
		Block block = canvas.move(move);
		messagingTemplate.convertAndSend("/topic/user.block",block);
	}

}
