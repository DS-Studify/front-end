package com.ds.studify.core.data.repository

import com.ds.studify.core.domain.entity.CameraEntity
import kotlinx.serialization.json.JsonElement

interface StudyRepository {
    suspend fun postRecord(entity: CameraEntity): Result<JsonElement?>
}