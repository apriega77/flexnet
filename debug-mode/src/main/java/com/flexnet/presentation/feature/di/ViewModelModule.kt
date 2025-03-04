package com.flexnet.presentation.feature.di

import androidx.lifecycle.ViewModel
import com.flexnet.presentation.base.ViewModelKey
import com.flexnet.presentation.feature.FlexNetViewModel
import com.flexnet.presentation.feature.add.AddRuleViewModel
import com.flexnet.presentation.feature.httpinspector.HttpInspectorViewModel
import com.flexnet.presentation.feature.rules.NetworkRulesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FlexNetViewModel::class)
    abstract fun bindFlexNetViewModel(viewModel: FlexNetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NetworkRulesViewModel::class)
    abstract fun bindNetworkRulesViewModel(viewModel: NetworkRulesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddRuleViewModel::class)
    abstract fun bindAddRuleViewModel(viewModel: AddRuleViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HttpInspectorViewModel::class)
    abstract fun bindHttpInspectorViewModel(viewModel: HttpInspectorViewModel): ViewModel
}
