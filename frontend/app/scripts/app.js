/*global define */
define([
    'jquery',
    'lodash',
    'client',
    'base',
    'hammerjs'
], function ($, _, Client, Base) {
    'use strict';

    var Application = Base.extend({
        init: function init(selector, opts) {
            var self = this;
            self.$elem = $(selector);
            self.$elem.removeClass('hidden');
            self.client = new Client();
            self.blocks = [];

            self.client.subscribe('/app/user', function() {
                self.handleUser.apply(self, arguments);
            });

            self.client.subscribe('/app/blocks.my', function() {
                self.handleBlocks.apply(self, arguments);
            });

            self.client.subscribe('/app/blocks.other', function() {
                self.handleOtherBlocks.apply(self, arguments);
            });

            self.client.subscribe('/topic/user.block', function() {
                self.handleBlock.apply(self, arguments);
            });

            self.client.subscribe('/topic/user.block.add', function() {
                self.handleAddBlock.apply(self, arguments);
            });

            self.client.subscribe('/topic/user.block.remove', function() {
                self.handleRemoveBlock.apply(self, arguments);
            });

            self.setupDomListeners();
        },

        handleUser: function handleUser(user) {
            this.user = user.name;
        },

        handleBlock: function handleBlock(blockData) {
            var self = this,
                $domBlock = _.find(this.blocks, function(block) {
                    return block.attr('data-id') === blockData.name &&
                        (!self.target || self.target !== blockData.name)
                });

            if($domBlock) {
                $domBlock.css({
                    top: blockData.y,
                    left: blockData.x
                });
            }
        },

        handleAddBlock: function handleBlock(block) {
            var classes = 'block';

            if(this.user === block.user) {
                classes += ' my';
            }
            var $blockElem = $('<div><div class="text"></div><div class="remove">X</div></div>')
                .addClass(classes)
                .css({
                    top: block.y,
                    left: block.x
                })
                .attr('data-id', block.name);

            $blockElem.find('.text').html(block.name);

            $()
            this.blocks.push($blockElem);
            this.$elem.append($blockElem);
        },

        handleRemoveBlock: function handleRemoveBlock(remove) {
            var $elem = _.find(this.blocks, function(block) {
                return block.attr('data-id') === remove.name;
            });

            if($elem) {
                $elem.remove();
                this.blocks = _.without(this.blocks, $elem);
            }
        },

        handleOtherBlocks: function handleOtherBlocks(blocks) {
            this.renderBlocks(blocks, false);
        },

        handleBlocks: function handleBlocks(blocks) {
            this.renderBlocks(blocks, true); 
        },

        renderBlocks: function renderBlocks(blocks, isOwn) {
            var self = this,
                classes = 'block';
            if(isOwn) {
                classes += ' my'
            }
            _.each(blocks, function(block) {
                var $blockElem = $('<div><div class="text"></div><div class="remove">X</div></div>')
                    .addClass(classes)
                    .css({
                        top: block.y,
                        left: block.x
                    })
                    .attr('data-id', block.name);
                    
                $blockElem.find('.text').html(block.name);

                self.blocks.push($blockElem);
                self.$elem.append($blockElem);
            });
        },

        setupDomListeners: function setupDomListeners() {
            var self = this;

            /*self.$elem.on('click', '.my.block .text', function(event) {
                var $target = $(event.currentTarget).parent();
                self.client.send('/app/block/move', {
                    name: $target.attr('data-id'),
                    x: Math.floor(Math.random(1) * ($(window).width() - 50)),
                    y: Math.floor(Math.random(1) * ($(window).height() - 140))
                });
            });*/

            self.$elem.on('click', '.my.block .remove', function(event) {
                var $target = $(event.currentTarget).closest('.block');
                self.client.send('/app/block/remove', {
                    name: $target.attr('data-id')
                });
            });

            self.$elem.on('click', '.add', function(event) {
                self.client.send('/app/block/add', {
                    x: Math.floor(Math.random(1) * ($(window).width() - 50)),
                    y: Math.floor(Math.random(1) * ($(window).height() - 140))
                });
            });

            self.$elem.hammer().on('drag', '.my.block', function(event) {
                var touches = event.gesture.touches;
                event.gesture.preventDefault();

                _.each(touches, function(touch, index) {
                    var $target = $(touches[index].target).closest('.block');
                    self.target = $target.attr('data-id');
                    $target.css({
                        left: touches[index].pageX - 40,
                        top: touches[index].pageY - 82
                    });
                    self.client.send('/app/block/move', {
                        name: $target.attr('data-id'),
                        x: $target.position().left,
                        y: $target.position().top
                    });
                });
            });

            self.$elem.hammer().on('dragend', '.my.block', function(event) {
                self.target = undefined;
                event.gesture.preventDefault();
                var $target = $(event.currentTarget).closest('.block');
                self.client.send('/app/block/move', {
                    name: $target.attr('data-id'),
                    x: $target.position().left,
                    y: $target.position().top
                });
            });
        }
    });

    return Application;
});