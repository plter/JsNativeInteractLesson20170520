const DisplayObject = require("./DisplayObject");

class Rect extends DisplayObject {


    constructor() {
        super();
        this.color = "red";
        this.width = 100;
        this.height = 100;
    }


    internal_draw(context) {
        context.setFillStyle(this.color);
        context.fillRect(0, 0, this.width, this.height);
    }
}

module.exports = Rect;