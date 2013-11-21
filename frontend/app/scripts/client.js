/*global define */
define([
    'jquery',
    'lodash',
    'sockjs',
    'base',
], function ($, _, SockJS, Base) {
    'use strict';

    var Client = Base.extend({
        deferred: $.Deferred(),

        init: function init(opts) {
            this.socket = new SockJS('/websocket-blocks/blocks');
            this.stompClient = Stomp.over(this.socket);

            this.connect();
        },

        connect: function connect() {
            var self = this;
            this.stompClient.connect('', '', function(frame) {
                self.suffix = frame.headers['queue-suffix'];
                self.deferred.resolve(true);
            }, function(error) {
                console.log('STOMP protocol error: ' + error);
            });
        },

        subscribe: function subscribe(path, callback) {
            var self = this;
            $.when(this.deferred.promise()).then(function() {
                self.stompClient.subscribe(path, function(message) {
                    callback(JSON.parse(message.body), message.headers);
                });
            });
        },

        subscribeWithSuffix: function subscribeWithSuffix(path, callback) {
            var self = this;
            $.when(this.deferred.promise()).then(function() {
                self.stompClient.subscribe(path + self.suffix, function(message) {
                    callback(JSON.parse(message.body), message.headers);
                });
            });
        },

        send: function send(path, opts, callback) {
            var self = this;
            $.when(this.deferred.promise()).then(function() {
                self.stompClient.send(path, {}, JSON.stringify(opts));
            });
        }
    });
    
    return Client;
});