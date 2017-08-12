class Rect {


    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.rotation = 0;
    }

    draw(context) {
        context.save();
        context.translate(this.x, this.y);
        context.rotate(this.rotation);
        context.fillRect(-50, -50, 100, 100);
        context.restore();
    }
}

module.exports = Rect;