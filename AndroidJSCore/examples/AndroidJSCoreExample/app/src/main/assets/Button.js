this.yideng = this.yideng || {};

(function () {
    class Button {


        constructor() {
            this._nativeId = createButton();
        }

        setText(text) {
            setButtonText(this._nativeId, text);
        }
    }

    yideng.Button = Button;
})();