//
//  ViewController.swift
//  RunJavaScriptOniOS
//
//  Created by plter on 5/20/17.
//  Copyright © 2017 yidengxuetang. All rights reserved.
//

import UIKit
import JavaScriptCore

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        let context = JSContext()
        context?.evaluateScript("var a = 10;var b = 5;var c = a + b;")
        let value = context?.evaluateScript("c")
        
        if let v = value?.toInt32() {
            print(v)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

