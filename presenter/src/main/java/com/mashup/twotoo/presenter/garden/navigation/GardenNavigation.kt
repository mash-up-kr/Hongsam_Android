package com.mashup.twotoo.presenter.garden.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mashup.twotoo.presenter.garden.GardenRoute

const val gardenNavigationRoute = "garden_route"

fun NavController.navigateToGarden(navOptions: NavOptions? = null) {
    this.navigate(route = gardenNavigationRoute, navOptions = navOptions)
}

fun NavGraphBuilder.gardenGraph() {
    composable(route = gardenNavigationRoute) {
        GardenRoute()
    }
}