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
package com.zeetah.blocks.controller;

import java.security.Principal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeEvent;
import org.springframework.stereotype.Controller;

import com.zeetah.blocks.model.Block;
import com.zeetah.blocks.protocol.MoveRequest;
import com.zeetah.blocks.service.BlockService;


@Controller
public class BlockController {

	private static final Log logger = LogFactory.getLog(BlockController.class);

	private final BlockService blockService;

	@Autowired
	public BlockController(BlockService blockService) {
		this.blockService = blockService;
	}

	@SubscribeEvent("/blocks")
	public List<Block> getBlocks(Principal principal) throws Exception {
		return blockService.getBlocks(principal);
	}
	
	@MessageMapping(value = "/block/move")
	public void moveBlock(MoveRequest move, Principal principal) {
		blockService.move(principal, move);
	}

	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public String handleException(Throwable exception) {
		return exception.getMessage();
	}

}
