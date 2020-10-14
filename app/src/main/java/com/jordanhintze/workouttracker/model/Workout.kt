package com.jordanhintze.workouttracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Workout (@PrimaryKey val id: UUID = UUID.randomUUID(),
                    var title: String = "",
                    var date: Date = Date())