this.yideng = this.yideng || {};

(function () {
    class Hello {
        sayHello() {
            trace("Hello World");
        }
    }

    yideng.Hello = Hello;
})();