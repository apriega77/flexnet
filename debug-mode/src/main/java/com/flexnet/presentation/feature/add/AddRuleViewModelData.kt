package com.flexnet.presentation.feature.add

import android.content.ClipboardManager
import com.flexnet.domain.model.Method

internal sealed interface AddRuleState {
    val idState: Int
    val titleState: String
    val isActiveState: Boolean
    val urlState: String
    val methodState: Method
    val httpCodeState: String
    val responseBodyState: String

    fun copyValue(
        titleState: String = this.titleState,
        isActiveState: Boolean = this.isActiveState,
        urlState: String = this.urlState,
        methodState: Method = this.methodState,
        httpCodeState: String = this.httpCodeState,
        responseBodyState: String = this.responseBodyState,
    ): AddRuleState

    data class Add(
        override val titleState: String = "",
        override val isActiveState: Boolean = false,
        override val urlState: String = "",
        override val methodState: Method = Method.GET,
        override val httpCodeState: String = "",
        override val responseBodyState: String = "",
    ) : AddRuleState {
        override val idState: Int
            get() = 0

        override fun copyValue(
            titleState: String,
            isActiveState: Boolean,
            urlState: String,
            methodState: Method,
            httpCodeState: String,
            responseBodyState: String,
        ): AddRuleState {
            return this.copy(
                titleState = titleState,
                isActiveState = isActiveState,
                urlState = urlState,
                methodState = methodState,
                httpCodeState = httpCodeState,
                responseBodyState = responseBodyState,
            )
        }
    }

    data class Detail(
        override val titleState: String,
        override val isActiveState: Boolean,
        override val urlState: String,
        override val methodState: Method,
        override val httpCodeState: String,
        override val responseBodyState: String,
        override val idState: Int,
    ) : AddRuleState {
        override fun copyValue(
            titleState: String,
            isActiveState: Boolean,
            urlState: String,
            methodState: Method,
            httpCodeState: String,
            responseBodyState: String,
        ): AddRuleState {
            return this.copy(
                titleState = titleState,
                isActiveState = isActiveState,
                urlState = urlState,
                methodState = methodState,
                httpCodeState = httpCodeState,
                responseBodyState = responseBodyState,
            )
        }
    }
}

internal sealed interface AddRuleEvent {
    data class SetArgs(val addRuleArgs: AddRuleArgs) :
        AddRuleEvent

    data class OnTitleChange(val text: String) : AddRuleEvent
    data class OnToggleChange(val isActive: Boolean) : AddRuleEvent
    data class OnUrlChange(val text: String) : AddRuleEvent
    data class OnHttpCodeChange(val text: String) : AddRuleEvent
    data class OnResponseBodyChange(val text: String) : AddRuleEvent
    data class OnMethodItemChange(val method: Method) : AddRuleEvent

    object SaveRule : AddRuleEvent
    object DeleteRule : AddRuleEvent
    data class PasteClipboard(val clipboard: ClipboardManager) : AddRuleEvent
}

internal sealed interface AddRuleEffect {
    data object PopBackToNetworkRulesScreen : AddRuleEffect
    data class ShowSnackBar(val text: String) : AddRuleEffect
}
