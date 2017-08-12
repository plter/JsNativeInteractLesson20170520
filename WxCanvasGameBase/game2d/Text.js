const DisplayObject = require("./DisplayObject");

class Text extends DisplayObject {


    constructor(string) {
        super();
        this.string = string;
        this.fontSize = 30;
        this.textColor = "black";
    }


    internal_draw(context) {
        context.setFontSize(this.fontSize);
        context.setFillStyle(this.textColor);
        context.fillText(this.string, 0, this.fontSize);
    }
}

module.exports = Text;