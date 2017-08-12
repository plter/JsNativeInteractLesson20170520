const DisplayObject = require("./DisplayObject");

class Container extends DisplayObject {


    constructor() {
        super();

        this._children = [];
    }


    /**
     *
     * @param {DisplayObject} child
     */
    addChild(child) {
        this._children.push(child);
    }

    removeChild(child) {
        var index = this._children.indexOf(child);
        if (index >= 0) {
            this._children.splice(index, 1);
        }
    }


    internal_draw(context) {
        this._children.forEach(function (item) {
            item.draw(context);
        });
    }
}

module.exports = Container;