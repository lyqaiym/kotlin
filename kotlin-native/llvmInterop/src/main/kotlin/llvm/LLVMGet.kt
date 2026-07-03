/*
 * Copyright 2010-2026 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package llvm

import kotlinx.cinterop.*
import kotlinx.cinterop.toKString
import kotlinx.cinterop.Arena
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.rawValue
import kotlinx.cinterop.value

@ExperimentalForeignApi
fun LLVMGetTargetFromTriple2(Triple: String?, T: CValuesRef<LLVMTargetRefVar>?, ErrorMessage: CValuesRef<CPointerVar<ByteVar>>?): LLVMBool {
    memScoped {
        println("Triple=${Triple}")
        val arena = Arena()
        val errMsgPtr = arena.alloc<CPointerVar<ByteVar>>()
        val res = kniBridge762(Triple?.cstr?.getPointer(memScope).rawValue, T?.getPointer(memScope).rawValue, errMsgPtr.ptr.getPointer(memScope).rawValue)
        println("Triple:res=${res}")
        if (res != 0) {
            // 取出 LLVM 分配的 char*
            val errorCStr = errMsgPtr.value
            if (errorCStr != null) {
                val cErrStr = errorCStr.toKString()
                println("LLVM错误信息：$cErrStr")
            }else{
                println("LLVM错误信息：errorCStr = null")
            }
            // 释放LLVM内部创建的错误字符串
//            cErrStr?.let { LLVMDisposeMessage(it.ptr) }
        }
        return res
    }
}