const Container = require("./Container");
const Rect = require("./Rect");
const Text = require("./Text");


class Card extends Container {


    constructor(num) {
        super();

        this.bg = new Rect();
        this.bg.color = "blue";
        this.width = 200;
        this.height = 200;
        this.addChild(this.bg);

        this.txt = new Text(num + "");
        this.txt.textColor = "white";
        this.txt.x = 20;
        this.txt.y = 20;
        this.addChild(this.txt);
    }
}

module.exports = Card;