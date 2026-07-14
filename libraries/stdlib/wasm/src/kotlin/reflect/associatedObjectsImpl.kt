/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package kotlin.wasm.internal

import kotlin.internal.UsedFromCompilerGeneratedCode
import kotlin.reflect.KClass

//private fun KClass<*>.getTypeId(): Long? = when (this) {
//    is KClassImpl<*> -> getTypeId(rtti)
//    is KClassInterfaceImpl<*> -> typeData.typeId
//    else -> return null
//}

@PublishedApi
internal fun findAssociatedObject(klass: KClass<*>, key: Int): Any? {
    val klassId = (klass as? KClassImpl<*>)?.typeData?.typeId ?: return null
    return tryGetAssociatedObject(klassId, key)
}

//@PublishedApi
//internal fun findAssociatedObject(klass: KClass<*>, key: KClass<*>): Any? {
//    val klassId = (klass as? KClassImpl<*>)?.typeData?.typeId ?: return null
//    val keyId = (klass as? KClassImpl<*>)?.typeData?.typeId ?: return null
//    return tryGetAssociatedObject(klassId, keyId)
//}

//@UsedFromCompilerGeneratedCode
//internal fun tryGetAssociatedObject(klassId: Long, keyId: Long): Any? {
//    return moduleDescriptors.firstNotNullOfOrNull { moduleDescriptor ->
//        callAssociatedObjectGetter(klassId, keyId, moduleDescriptor.associatedObjectGetter)
//    }
//}

internal fun tryGetAssociatedObject(
    @Suppress("UNUSED_PARAMETER") klassId: Int,
    @Suppress("UNUSED_PARAMETER") keyId: Int,
): Any? {
    // Init implicitly with AssociatedObjectsLowering and WasmCompiledModuleFragment::createTryGetAssociatedObjectFunction:
    // if (C1.klassId == klassId) if (Key1.klassId == keyId) return OBJ1
    // if (C2.klassId == klassId) if (Key2.klassId == keyId) return OBJ2
    // ...
    return null
}
