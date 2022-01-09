package org.greenstand.android.TreeTracker.usecases

import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.greenstand.android.TreeTracker.analytics.Analytics
import org.greenstand.android.TreeTracker.database.TreeTrackerDAO
import org.greenstand.android.TreeTracker.database.entity.UserEntity
import org.greenstand.android.TreeTracker.models.LocationUpdateManager

data class CreatePlanterInfoParams(
    val firstName: String,
    val lastName: String,
    val organization: String?,
    val phone: String?,
    val email: String?,
    val identifier: String,
    val photoPath: String,
)

class CreatePlanterInfoUseCase(
    private val locationUpdateManager: LocationUpdateManager,
    private val dao: TreeTrackerDAO,
    private val analytics: Analytics
) : UseCase<CreatePlanterInfoParams, Long>() {

    override suspend fun execute(params: CreatePlanterInfoParams): Long =
        withContext(Dispatchers.IO) {

            val location = locationUpdateManager.currentLocation
            val time = location?.time ?: System.currentTimeMillis()

            val entity = UserEntity(
                wallet = params.identifier,
                firstName = params.firstName,
                lastName = params.lastName,
                organization = params.organization,
                phone = params.phone,
                email = params.email,
                longitude = location?.longitude ?: 0.0,
                latitude = location?.latitude ?: 0.0,
                createdAt = time,
                uploaded = false,
                uuid = UUID.randomUUID().toString(),
                powerUser = false,
                photoPath = params.photoPath,
                photoUrl = null,
            )

            dao.insertUser(entity).also {
                analytics.userInfoCreated(
                    phone = params.phone.orEmpty(),
                    email = params.email.orEmpty()
                )
            }
        }
}
