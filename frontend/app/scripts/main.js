require.config({
    paths: {
        jquery: '../bower_components/jquery/jquery',
        lodash: '../bower_components/lodash/dist/lodash.min',
        bootstrap: '../bower_components/sass-bootstrap/dist/js/bootstrap.min',
        hammerjs: '../bower_components/hammerjs/dist/jquery.hammer.min',
        sockjs: '../bower_components/sockjs/sockjs'
    },
    shim: {
        bootstrap: {
            deps: ['jquery'],
            exports: 'jquery'
        },
        hammerjs: {
            deps: ['jquery'],
            exports: 'jquery'
        }
    }
});

require([
    'jquery',
    'app',
    'login',
    'bootstrap'
], function ($, App, Login) {
    'use strict';
    
    $.getJSON('websocket-blocks/blocks/info').done(function() {
        new App('#application');
    }).fail(function() {
        new Login('#login', {
            initApplication: function() {
                new App('#application');
            }
        });
    });
});
