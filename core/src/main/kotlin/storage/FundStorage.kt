package storage

import domain.Fund
import storage.model.FundDescription


interface FundStorage {

    fun store(fund: Fund): Long

    fun updateName(id: Long, name: String): Fund

    fun updateDescription(id: Long, description: String): Fund

    fun get(id: Long): Fund

    fun linkUser(fundId: Long, userId: Long): Boolean

    fun unlinkUser(fundId: Long, userId: Long): Boolean

    fun getSupervisorId(fundId: Long): Long

    fun getLinkedUserIds(fundId: Long): List<Long>

    fun getFundDescriptions(): List<IdEntityHolder<FundDescription>>
}