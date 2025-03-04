package com.flexnet.presentation.feature.add

import com.flexnet.domain.model.NetworkRule

internal sealed interface AddRuleArgs {
    data class Detail(val networkRule: NetworkRule) : AddRuleArgs
    data class Add(val networkRule: NetworkRule?) : AddRuleArgs
}
