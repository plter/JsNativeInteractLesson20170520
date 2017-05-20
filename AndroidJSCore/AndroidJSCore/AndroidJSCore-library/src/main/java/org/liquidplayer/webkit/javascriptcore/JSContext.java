//
// JSContext.java
// AndroidJSCore project
//
// https://github.com/ericwlange/AndroidJSCore/
//
// Created by Eric Lange
//
/*
 Copyright (c) 2014-2016 Eric Lange. All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 - Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 - Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.liquidplayer.webkit.javascriptcore;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wraps a JavaScriptCore context 
 */
public class JSContext extends JSObject {

    private final JSWorkerQueue mWorker = new JSWorkerQueue(new Runnable() {
        @Override
        public void run() {
            if (deadReferences.size() > 100) {
                cleanDeadReferences();
            }
        }
    });

    protected void sync(Runnable runnable) {
        mWorker.sync(runnable);
    }
    protected void async(Runnable runnable) {
        mWorker.async(runnable);
    }

    public final Object mMutex = new Object();

    private final List<Long> deadReferences = new ArrayList<>();

    protected void markForUnprotection(Long valueR) {
        synchronized (mMutex) {
            deadReferences.add(valueR);
        }
    }
    private void cleanDeadReferences() {
        synchronized (mMutex) {
            for (Long reference : deadReferences) {
                unprotect(ctxRef(), reference);
            }
            deadReferences.clear();
        }
    }

    /**
     * Object interface for handling JSExceptions.
     * @since 2.1
     */
    public interface IJSExceptionHandler {
        /**
         * Implement this method to catch JSExceptions
         * @param exception caught exception
         * @since 2.1
         */
        void handle(JSException exception);
    }

    protected Long ctx;
    private IJSExceptionHandler exceptionHandler;

    /**
     * Creates a new JavaScript context
     * @since 1.0
     */
    public JSContext() {
        context = this;
        sync(new Runnable() {
            @Override public void run() {
                static_init();
                ctx = create();
                valueRef = getGlobalObject(ctx);
            }
        });
    }
    /**
     * Creates a new JavaScript context in the context group 'inGroup'.
     * @param inGroup  The context group to create the context in
     * @since 1.0
     */
    public JSContext(final JSContextGroup inGroup) {
        context = this;
        sync(new Runnable() {
            @Override public void run() {
                static_init();
                ctx = createInGroup(inGroup.groupRef());
                valueRef = getGlobalObject(ctx);
            }
        });
    }
    /**
     * Creates a JavaScript context, and defines the global object with interface 'iface'.  This
     * object must implement 'iface'.  The methods in 'iface' will be exposed to the JavaScript environment.
     * @param iface  The interface to expose to JavaScript
     * @since 1.0
     */
    public JSContext(final Class<?> iface) {
        context = this;
        sync(new Runnable() {
            @Override public void run() {
                static_init();
                ctx = create();
                valueRef = getGlobalObject(ctx);
                Method[] methods = iface.getDeclaredMethods();
                for (Method m : methods) {
                    JSObject f = new JSFunction(context, m, JSObject.class, context);
                    property(m.getName(),f);
                }
            }
        });
    }
    /**
     * Creates a JavaScript context in context group 'inGroup', and defines the global object
     * with interface 'iface'.  This object must implement 'iface'.  The methods in 'iface' will
     * be exposed to the JavaScript environment.
     * @param inGroup  The context group to create the context in
     * @param iface  The interface to expose to JavaScript
     * @since 1.0
     */
    public JSContext(final JSContextGroup inGroup, final Class<?> iface) {
        context = this;
        sync(new Runnable() {
            @Override public void run() {
                static_init();
                ctx = createInGroup(inGroup.groupRef());
                valueRef = getGlobalObject(ctx);
                Method[] methods = iface.getDeclaredMethods();
                for (Method m : methods) {
                    JSObject f = new JSFunction(context, m, JSObject.class, context);
                    property(m.getName(),f);
                }
            }
        });
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        cleanDeadReferences();
        isDefunct = true;
        release(ctx);
        if (mWorker != null) {
            mWorker.quit();
        }
    }

    /**
     * Sets the JS exception handler for this context.  Any thrown JSException in this
     * context will call the 'handle' method on this object.  The calling function will
     * return with an undefined value.
     * @param handler An object that implements 'IJSExceptionHandler'
     * @since 2.1
     */
    public void setExceptionHandler(IJSExceptionHandler handler) {
        exceptionHandler = handler;
    }

    /**
     * Clears a previously set exception handler.
     * @since 2.1
     */
    public void clearExceptionHandler() {
        exceptionHandler = null;
    }

    /**
     * If an exception handler is set, calls the exception handler, otherwise throws
     * the JSException.
     * @param exception The JSException to be thrown
     * @since 2.1
     */
    public void throwJSException(JSException exception) {
        if (exceptionHandler == null) {
            throw exception;
        } else {
            // Before handling this exception, disable the exception handler.  If a JSException
            // is thrown in the handler, then it would recurse and blow the stack.  This way an
            // actual exception will get thrown.  If successfully handled, then turn it back on.
            IJSExceptionHandler temp = exceptionHandler;
            exceptionHandler = null;
            temp.handle(exception);
            exceptionHandler = temp;
        }
    }

    /**
     * Gets the context group to which this context belongs.
     * @return  The context group to which this context belongs
     */
    public JSContextGroup getGroup() {
        Long g = getGroup(ctx);
        if (g==0) return null;
        return new JSContextGroup(g);
    }

    /**
     * Gets the JavaScriptCore context reference
     * @return  the JavaScriptCore context reference
     */
    public Long ctxRef() {
        return ctx;
    }

    private abstract class JNIReturnClass implements Runnable {
        JNIReturnObject jni;
    }

    /**
     * Executes a the JavaScript code in 'script' in this context
     * @param script  The code to execute
     * @param thiz  The 'this' object
     * @param sourceURL  The URI of the source file, only used for reporting in stack trace (optional)
     * @param startingLineNumber  The beginning line number, only used for reporting in stack trace (optional)
     * @return  The return value returned by 'script'
     * @since 1.0
     */
    public JSValue evaluateScript(final String script, final JSObject thiz,
            final String sourceURL, final int startingLineNumber) {

        JNIReturnClass runnable = new JNIReturnClass() {
            @Override public void run() {
                JSString jsscript = new JSString(script);
                JSString jssourceURL = new JSString(sourceURL);
                jni = evaluateScript(ctx, jsscript.stringRef(),
                        (thiz == null) ? 0L : thiz.valueRef(),
                        jssourceURL.stringRef(),
                        startingLineNumber);
            }
        };
        sync(runnable);

        if (runnable.jni.exception!=0) {
            throwJSException(new JSException(new JSValue(runnable.jni.exception, context)));
            return new JSValue(this);
        }
        return new JSValue(runnable.jni.reference,this);
    }

    /**
     * Executes a the JavaScript code in 'script' in this context
     * @param script  The code to execute
     * @param thiz  The 'this' object
     * @return  The return value returned by 'script'
     * @since 1.0
     */
    public JSValue evaluateScript(String script, JSObject thiz) {
        return evaluateScript(script,thiz,null,0);
    }
    /**
     * Executes a the JavaScript code in 'script' in this context
     * @param script  The code to execute
     * @return  The return value returned by 'script'
     * @since 1.0
     */
    public JSValue evaluateScript(String script) {
        return evaluateScript(script,null,null,0);
    }

    private Map<Long,WeakReference<JSObject>> objects = new HashMap<>();

    /**
     * Keeps a reference to an object in this context.  This is used so that only one
     * Java object instance wrapping a JavaScript object is maintained at any time.  This way,
     * local variables in the Java object will stay wrapped around all returns of the same
     * instance.  This is handled by JSObject, and should not need to be called by clients.
     * @param obj  The object with which to associate with this context
     * @since 1.0
     */
    protected synchronized  void persistObject(JSObject obj) {
        objects.put(obj.valueRef(), new WeakReference<>(obj));
    }
    /**
     * Removes a reference to an object in this context.  Should only be used from the 'finalize'
     * object method.  This is handled by JSObject, and should not need to be called by clients.
     * @param obj the JSObject to dereference
     * @since 1.0
     */
    protected synchronized void finalizeObject(JSObject obj) {
        objects.remove(obj.valueRef());
    }
    /**
     * Reuses a stored reference to a JavaScript object if it exists, otherwise, it creates the
     * reference.
     * @param objRef the JavaScriptCore object reference
     * @param create whether to create the object if it does not exist
     * @since 1.0
     * @return The JSObject representing the reference
     */
    protected synchronized JSObject getObjectFromRef(long objRef,boolean create) {
        if (objRef == valueRef()) return this;
        WeakReference<JSObject> wr = objects.get(objRef);
        JSObject obj = null;
        if (wr != null) {
            obj = wr.get();
            if (obj != null)
                obj.unprotect(ctxRef(),obj.valueRef());
        }
        if (obj==null && create) {
            obj = new JSObject(objRef,this);
            if (isArray(ctxRef(),objRef))
                obj = new JSArray(objRef,this);
            else if (JSTypedArray.isTypedArray(obj))
                obj = JSTypedArray.from(obj);
            else if (isFunction(ctxRef(),objRef))
                obj = new JSFunction(objRef,this);
        }
        return obj;
    }
    protected synchronized JSObject getObjectFromRef(long objRef) {
        return getObjectFromRef(objRef,true);
    }
    /**
     * Forces JavaScript garbage collection on this context
     * @since 1.0
     */
    public void garbageCollect()
    {
        async(new Runnable() {
            @Override
            public void run() {
                garbageCollect(ctx);
            }
        });
    }

    protected static native void staticInit();
    protected native long create();
    protected native long createInGroup(long group);
    protected native long retain(long ctx);
    protected native long release(long ctx);
    protected native long getGroup(long ctx);
    protected native long getGlobalObject(long ctx);
    protected native JNIReturnObject evaluateScript(long ctx, long script, long thisObject, long sourceURL, int startingLineNumber);
    @SuppressWarnings("unused")
    protected native JNIReturnObject checkScriptSyntax(long ctx, long script, long sourceURL, int startingLineNumber);
    protected native void garbageCollect(long ctx);

    static boolean isInit = false;

    private static void static_init() {
        if (!isInit) {
            System.loadLibrary("javascriptcoregtk-4.0");
            System.loadLibrary("android-js-core");
            staticInit();
            isInit = true;
        }
    }
}
