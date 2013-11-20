/*global define */
define([
    'jquery',
    'lodash',
    'base'
], function ($, _, Base) {
    'use strict';

    var Login = Base.extend({
        init: function init(selector, opts) {
            var self = this;
            self.$elem = $(selector);
            self.$elem.removeClass('hidden');
            self.successCallback = opts.initApplication;

            var form = self.$elem.find('form');
            form.on('submit', function(event) {
                self.handleSubmit.call(self, event);
            });
        },
        handleSubmit: function handleSubmit(event, callback) {
            var self = this,
                $elem = $(event.currentTarget);
            event.preventDefault();
            
            $.post('/websocket-blocks/login.html', $elem.serialize()).success(function() {
                self.$elem.remove();
                self.successCallback();
            }).error(function(data) {
                if(data.status === 0) {
                    self.$elem.remove();
                    self.successCallback();
                }
                self.$elem.find('.alert').removeClass('hidden');
            });
        }
    });
    
    return Login;
});