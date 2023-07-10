package com.mashup.twotoo.repository

import com.mashup.twotoo.datasource.remote.challenge.ChallengeDataSource
import repository.ChallengeRepository
import javax.inject.Inject


class ChallengeRepositoryImpl @Inject constructor(
    private val challengeDataSource: ChallengeDataSource,
) : ChallengeRepository {
    override suspend fun createChallenge() {
        TODO("Not yet implemented")
    }

    override suspend fun approveChallenge() {
        TODO("Not yet implemented")
    }

    override suspend fun pushSting() {
        TODO("Not yet implemented")
    }
}