//index.js
//获取应用实例

var Rect = require("../../shapes/Rect");


var app = getApp();
Page({

    r: new Rect(0, 0),

    data: {
        canvasWidth: 400,
        canvasHeight: 600
    },

    getSysInfo: function () {
        (function (self) {
            wx.getSystemInfo({
                success: function (info) {
                    self.setData({
                        canvasWidth: info.windowWidth,
                        canvasHeight: info.windowHeight
                    });
                }
            });
        })(this);
    },

    draw: function () {
        this.r.x = 100;
        this.r.y = 100;
        this.r.rotation += 0.01;
        this.r.draw(this.context);

        this.context.draw();
    },

    onLoad: function () {
        console.log('onLoad');
        var self = this;

        this.context = wx.createCanvasContext("game");

        this.getSysInfo();
        this.draw();


        // var frames = 0;
        var timerId = setInterval(function () {
            self.draw();
            // frames++;
            // if (frames >= 100) {
            //     clearInterval(timerId);
            // }
        }, 20);
    }
});
