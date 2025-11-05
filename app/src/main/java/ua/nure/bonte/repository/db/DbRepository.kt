package ua.nure.bonte.repository.db

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.repository.db.data.AppDb

interface DbRepository {
    val dbFlow: Flow<AppDb>
    val db: AppDb
}