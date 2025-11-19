package ua.nure.bonte.db

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.AppDb

interface DbRepository {
    val dbFlow: Flow<AppDb>
    val db: AppDb
}