package com.flexnet.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flexnet.data.room.RoomTypeConverter
import com.flexnet.data.room.dao.HttpInspectorDao
import com.flexnet.data.room.dao.NetworkRuleDao
import com.flexnet.data.room.entity.httpinspector.HttpInspectorEntity
import com.flexnet.data.room.entity.rule.NetworkRuleEntity

@Database(entities = [NetworkRuleEntity::class, HttpInspectorEntity::class], version = 2)
@TypeConverters(RoomTypeConverter::class)
internal abstract class FlexNetDatabase : RoomDatabase() {
    abstract fun networkRuleDao(): NetworkRuleDao
    abstract fun httpInspectorDao(): HttpInspectorDao
}
