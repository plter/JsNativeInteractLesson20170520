//index.js
//获取应用实例

var Rect = require("../../game2d/Rect");
const Container = require("../../game2d/Container");
const Card = require("../../game2d/Card");
const Text = require("../../game2d/Text");


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

    draw: function () {
        this.root.draw(this.context);

        this.context.draw();
    },

    startGameTimer: function () {
        setInterval(this.draw.bind(this), 20);
    },

    addCard: function () {
        this.card = new Card(10);
        this.root.addChild(this.card);

        this.card.x = 100;
    },

    onLoad: function () {
        console.log('onLoad');
        var self = this;
        this.context = wx.createCanvasContext("game");

        self.root = new Container();

        this.getSysInfo();
        this.draw();
        this.startGameTimer();

        this.addCard();
    }
});
