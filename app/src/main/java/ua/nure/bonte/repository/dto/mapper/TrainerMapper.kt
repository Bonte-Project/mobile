package ua.nure.bonte.repository.dto.mapper

import ua.nure.bonte.db.data.entity.ExperienceEntity
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.db.data.entity.TrainerEntity
import ua.nure.bonte.repository.dto.ExperienceDto
import ua.nure.bonte.repository.dto.TrainerDto
import ua.nure.bonte.repository.dto.TrainerResponse
import ua.nure.bonte.ui.trainer.view.Trainer
import ua.nure.bonte.db.data.entity.Trainer as TrainerDb

fun TrainerEntity.toTrainerResponse(experiences: List<ExperienceEntity> = emptyList()): TrainerResponse {
    val expDto = experiences.map { it.toDto() }
    return TrainerResponse(
        message = "Trainer fetched successfully",
        trainer = TrainerDto(
            id = this.trainerId,
            userId = this.userId,
            bio = this.bio,
            certification = this.certification,
            specialization = this.specialization,
            location = this.location,
            isActive = this.isActive,
            experience = expDto,
            createdAt = ""
        )
    )
}

fun ExperienceEntity.toDto(): ExperienceDto {
    return ExperienceDto(
        id = this.experienceId,
        trainerId = this.trainerId,
        title = this.title,
        description = this.description,
        startDate = this.startDate,
        endDate = this.endDate
    )
}


fun TrainerDto.toEntity() = TrainerEntity(
    trainerId = id,
    userId = userId,
    bio = bio,
    certification = certification,
    specialization = specialization,
    location = location,
    isActive = isActive
)

fun ExperienceDto.toEntity() = ExperienceEntity(
    experienceId = id,
    trainerId = trainerId,
    title = title,
    description = description,
    startDate = startDate,
    endDate = endDate
)

fun TrainerResponse.toTrainer(profileEntity: ProfileEntity): TrainerDb {
    return TrainerDb(
        trainerEntity = TrainerEntity(
            trainerId = this.trainer.id,
            userId = profileEntity.id,
            bio = this.trainer.bio,
            certification = this.trainer.certification,
            specialization = this.trainer.specialization,
            location = this.trainer.location,
            isActive = this.trainer.isActive
        ),
        experience = this.trainer.experience?.map { exp ->
            ExperienceEntity(
                experienceId = exp.id,
                trainerId = this.trainer.id,
                title = exp.title,
                description = exp.description,
                startDate = exp.startDate,
                endDate = exp.endDate
            )
        } ?: emptyList(),
        profile = profileEntity
    )
}

