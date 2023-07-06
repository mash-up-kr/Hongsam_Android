package com.mashup.twotoo.repository

import com.mashup.twotoo.datasource.remote.view.ViewDataSource
import com.mashup.twotoo.mapper.toDomainModel
import model.challenge.HomeViewDomainModel
import repository.ViewRepository
import javax.inject.Inject

/**
 * @Created by 김현국 2023/07/06
 */
class ViewRepositoryImpl @Inject constructor(
    private val viewDataSource: ViewDataSource,
) : ViewRepository {
    override suspend fun getViewHome(): Result<HomeViewDomainModel> {
        return runCatching { viewDataSource.getViewHome().toDomainModel() }
    }
}
