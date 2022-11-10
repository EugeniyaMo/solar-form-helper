package com.solanteq.solar.plugin.reference.request

import com.intellij.json.psi.JsonStringLiteral
import com.intellij.openapi.util.TextRange
import org.jetbrains.uast.UClass

class ServiceNameReference(
    element: JsonStringLiteral,
    range: TextRange,
    requestData: RequestData
) : AbstractServiceReference(element, range, requestData) {

    override fun getVariantsInService(serviceClass: UClass): Array<Any> = emptyArray()

    override fun resolveReferenceInService(serviceClass: UClass) = serviceClass.sourcePsi

}