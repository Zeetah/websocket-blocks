package com.zeetah.blocks.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeEvent;
import org.springframework.stereotype.Controller;

import com.zeetah.blocks.model.Block;
import com.zeetah.blocks.protocol.AddRequest;
import com.zeetah.blocks.protocol.RemoveRequest;
import com.zeetah.blocks.service.BlockService;


@Controller
public class BlockController {
	private final BlockService blockService;

	@Autowired
	public BlockController(BlockService blockService) {
		this.blockService = blockService;
	}

	@SubscribeEvent("/user")
	public Principal getUser(Principal principal) throws Exception {
		return principal;
	}
	
	@SubscribeEvent("/blocks.my")
	public List<Block> getBlocks(Principal principal) throws Exception {
		return blockService.getBlocks(principal);
	}
	
	@MessageMapping(value = "/block/add")
	public void addBlock(AddRequest add, Principal principal) {
		blockService.add(principal, add);
	}
	
	@MessageMapping(value = "/block/remove")
	public void addBlock(RemoveRequest remove, Principal principal) {
		blockService.remove(principal, remove);
	}

	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public String handleException(Throwable exception) {
		return exception.getMessage();
	}

}
