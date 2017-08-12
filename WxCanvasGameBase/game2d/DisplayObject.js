class DisplayObject {


    constructor() {
        this.x = 0;
        this.y = 0;
        this.scaleX = 1;
        this.scaleY = 1;

        /**
         * 以弧度为单位
         * @type {number}
         */
        this.rotation = 0;
    }

    /**
     * 该函数由子类重写实现具体绘制操作，永远不应该被外部直接调用
     * @param context
     * @protected
     * @abstract
     */
    internal_draw(context) {
    }

    draw(context) {
        context.save();
        context.translate(this.x, this.y);
        context.rotate(this.rotation);
        this.internal_draw(context);
        context.restore();
    }
}

module.exports = DisplayObject;