// load("hello.js");

// var h = new yideng.Hello();
// h.sayHello();

load("Button.js");

trace("will create button");
var btn = new yideng.Button();

trace("set button text");
btn.setText("Click me");

trace("add button");
addButton(btn._nativeId);
trace("button added");