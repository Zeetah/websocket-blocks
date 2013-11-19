/*global define */
define([
    'jquery',
    'lodash',
], function ($, _) {
    'use strict';

    var Base = function() {};

    Base.extend = function extend(opts) {
        function Extendable() {
            if(this.init) {
                this.init.apply(this, arguments);
            }
        }
        Extendable.prototype = _.extend(new this(), opts);
        Extendable.prototype.constructor = Extendable;
        Extendable.extend = Base.extend;

        return Extendable;
    }

    return Base;
});