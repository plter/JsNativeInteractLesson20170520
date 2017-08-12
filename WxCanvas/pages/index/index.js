//index.js
//获取应用实例
var app = getApp()
Page({

    data: {
        canvasWidth: 100,
        canvasHeight: 100
    },


    getSysInfo: function () {
        var self = this;

        wx.getSystemInfo({
            success: function (info) {
                console.log(info);

                self.setData({
                    canvasWidth: info.windowWidth,
                    canvasHeight: info.windowHeight
                });
            }
        });
    },

    drawRect: function () {
        this.ctx.setFillStyle("red");
        this.ctx.setStrokeStyle("#ffff00");
        this.ctx.fillRect(10, 10, 100, 100);
        this.ctx.strokeRect(10, 10, 100, 100);
        this.ctx.draw();
    },

    drawLines: function () {
        this.ctx.setStrokeStyle("green");
        // this.ctx.setLineWidth(10);
        this.ctx.moveTo(100, 100);
        this.ctx.lineTo(200, 200);
        this.ctx.lineTo(200, 100);
        this.ctx.lineTo(100, 100);
        this.ctx.stroke();
        this.ctx.draw();
    },

    drawText: function () {
        this.ctx.setFontSize(50);
        this.ctx.setFillStyle("#00ffff");
        this.ctx.fillText("Hello World", 0, 50);
        this.ctx.draw();
    },

    drawPhoto: function () {
        var self = this;

        wx.chooseImage({
            success: function (res) {
                console.log(res);
                self.ctx.drawImage(res.tempFilePaths[0], 10, 10, 200, 200);
                self.ctx.draw();
            }
        });
    },

    onLoad: function () {
        this.getSysInfo();

        this.ctx = wx.createCanvasContext("game");
        // this.drawRect();
        // this.drawLines();
        // this.drawText();
        this.drawPhoto();
    }
});
