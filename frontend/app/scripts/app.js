/*global define */
define([
    'jquery',
    'lodash',
    'client',
    'base',
    'hammerjs'
], function ($, _, Client, Base, hammerjs) {
    'use strict';

    var Application = Base.extend({
        init: function init(selector, opts) {
            var self = this;
            this.client = new Client();
            
            this.client.subscribe('/app/blocks', function() {
                self.handleBlocks.apply(self, arguments);
            });

            this.client.subscribe('/topic/user.block', function() {
                self.handleBlock.apply(self, arguments);
            });
        },

        handleBlock: function handleBlock(message) {
            console.log(message);
        },

        handleBlocks: function handleBlocks(message) {
            console.log(message);

            this.client.send('/app/block/move', {
                name: 'fab1',
                x: Math.floor(Math.random(1)*500),
                y: Math.floor(Math.random(1)*500)
            });
        }


    });

    return Application;
});