package com.solanteq.solar.plugin.util

import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UMethod

fun UMethod.isCallableMethod() = javaPsi.isCallableMethod()

fun PsiMethod.isCallableMethod() =
    (findSuperMethods() + arrayOf(this)).any {
        it.hasAnnotation(CALLABLE_ANNOTATION_FQ_NAME)
    }

fun UClass.isCallableServiceInterface() = javaPsi.isCallableServiceInterface()

fun PsiClass.isCallableServiceInterface() =
    isInterface && hasAnnotation(CALLABLE_SERVICE_ANNOTATION_FQ_NAME)

fun UClass.isCallableServiceClassImpl() = javaPsi.isCallableServiceClassImpl()

fun PsiClass.isCallableServiceClassImpl() = interfaces.any { it.isCallableServiceInterface() }

val UClass.callableMethods: List<PsiMethod>
    get() = javaPsi.callableMethods

val PsiClass.callableMethods: List<PsiMethod>
    get() = if(isCallableServiceClassImpl())
        this.methods.filter { it.isCallableMethod() }
    else
        listOf()

val PsiClass.serviceName: String?
    get() {
        val serviceAnnotation = this.getAnnotation(SERVICE_ANNOTATION_FQ_NAME) ?: return null
        val valueAttribute = serviceAnnotation.findAttributeValue("value") ?: return null
        return JavaPsiFacade.getInstance(project)
            .constantEvaluationHelper.computeConstantExpression(valueAttribute) as? String ?: return null
    }