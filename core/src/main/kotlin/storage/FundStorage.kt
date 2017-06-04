package storage

import domain.Fund
import storage.model.FundDescription


interface FundStorage {

    fun store(fund: Fund): Long

    fun updateName(id: Long, name: String): Fund

    fun updateDescription(id: Long, description: String): Fund

    fun get(id: Long): Fund

    fun linkUsers(fundId: Long, userIds: List<Long>)

    fun unlinkUsers(fundId: Long, userIds: List<Long>)

    fun getSupervisorId(fundId: Long): Long

    fun getLinkedUserIds(fundId: Long): List<Long>

    fun getFundDescriptions(): List<IdEntityHolder<FundDescription>>
}