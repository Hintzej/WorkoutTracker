package com.jordanhintze.workouttracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Exercise(val workoutId: UUID,
                    @PrimaryKey val id: UUID = UUID.randomUUID(),
                    var name: String = "",
                    var reps: String = ""
)