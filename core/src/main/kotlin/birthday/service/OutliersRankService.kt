package birthday.service

import birthday.model.UserRank
import core.model.Balance
import core.model.User


class OutliersRankService : RankService {

    override fun calculateRanks(balances: List<Balance>): Map<User, UserRank> {
        val sortedBalances = balances.map { it.value }.sorted()
        val quartiles = quartiles(sortedBalances)
        val iqr = quartiles.q3 - quartiles.q1

        val lowerInnerFence = quartiles.q1 - 1.5 * iqr
        val upperInnerFence = quartiles.q3 + 1.5 * iqr

        val lowerOuterFence = quartiles.q1 - 3 * iqr
        val upperOuterFence = quartiles.q3 + 3 * iqr

        return balances.map {
            val rank = when {
                it.value < lowerOuterFence -> UserRank.EXTREMELY_LOW
                it.value < lowerInnerFence -> UserRank.GREATLY_LOW
                it.value > upperInnerFence -> UserRank.GREATLY_HIGH
                it.value > upperOuterFence -> UserRank.EXTREMELY_HIGH
                else -> when {
                    it.value < 0 -> UserRank.SLIGHTLY_LOW
                    it.value > 0 -> UserRank.SLIGHTLY_HIGH
                    else -> UserRank.ZEN
                }
            }
            Pair(it.user, rank)
        }.toMap()
    }

    private fun quartiles(values: List<Long>): Quartiles {
        val n = values.size / 4
        if (values.size % 4 == 0) {
            return Quartiles(
                    q1 = (values[n - 1] + values[n]) / 2.0,
                    q2 = (values[n * 2 - 1] + values[n * 2]) / 2.0,
                    q3 = (values[n * 3 - 1] + values[n * 3]) / 2.0
            )
        } else if (values.size % 4 == 1) {
            return Quartiles(
                    q1 = (values[n - 1] * 0.25 + values[n] * 0.75),
                    q2 = values[n * 2].toDouble(),
                    q3 = (values[n * 3] * 0.75 + values[n * 3 + 1] * 0.25)
            )
        } else if (values.size % 4 == 2) {
            return Quartiles(
                    q1 = values[n].toDouble(),
                    q2 = (values[n * 2] + values[n * 2 + 1]) / 2.0,
                    q3 = values[n * 3 + 1].toDouble()
            )
        } else {
            return Quartiles(
                    q1 = (values[n] * 0.75 + values[n + 1] * 0.25),
                    q2 = values[n * 2 + 1].toDouble(),
                    q3 = (values[n * 3 + 1] * 0.25 + values[n * 3 + 2] * 0.75)
            )
        }
    }
}

private data class Quartiles(val q1: Double, val q2: Double, val q3: Double)