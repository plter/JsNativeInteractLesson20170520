//index.js
//获取应用实例
var app = getApp();
Page({
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

    drawRects: function () {
        this.context.save();
        this.context.setFillStyle("red");
        // this.context.scale(2, 1);
        this.context.translate(100, 100);
        this.context.rotate(Math.PI / 4);
        this.context.fillRect(-50, -50, 100, 100);
        this.context.restore();

        this.context.save();
        this.context.setFillStyle("green");
        this.context.fillRect(0, 0, 100, 100);
        this.context.draw();
        this.context.restore();
    },

    onLoad: function () {
        console.log('onLoad');

        this.context = wx.createCanvasContext("game");

        this.getSysInfo();
        this.drawRects();
    }
});
