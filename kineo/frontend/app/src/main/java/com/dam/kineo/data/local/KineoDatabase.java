package com.dam.kineo.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dam.kineo.data.local.dao.BadgeDao;
import com.dam.kineo.data.local.dao.ChallengeDao;
import com.dam.kineo.data.local.dao.FriendshipDao;
import com.dam.kineo.data.local.dao.LeaderboardDao;
import com.dam.kineo.data.local.dao.RoutePointDao;
import com.dam.kineo.data.local.dao.StepSessionDao;
import com.dam.kineo.data.local.entity.BadgeEntity;
import com.dam.kineo.data.local.entity.ChallengeEntity;
import com.dam.kineo.data.local.entity.FriendshipEntity;
import com.dam.kineo.data.local.entity.LeaderboardEntryEntity;
import com.dam.kineo.data.local.entity.RoutePointEntity;
import com.dam.kineo.data.local.entity.StepSessionEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {StepSessionEntity.class, RoutePointEntity.class, BadgeEntity.class,
        FriendshipEntity.class, ChallengeEntity.class, LeaderboardEntryEntity.class}, version = 8, exportSchema = false)
public abstract class KineoDatabase extends RoomDatabase {

    public abstract StepSessionDao stepSessionDao();
    public abstract RoutePointDao routePointDao();
    public abstract BadgeDao badgeDao();
    public abstract FriendshipDao friendshipDao();
    public abstract ChallengeDao challengeDao();
    public abstract LeaderboardDao leaderboardDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                "DELETE FROM step_sessions WHERE id NOT IN (" +
                "  SELECT id FROM step_sessions s1" +
                "  WHERE steps = (SELECT MAX(steps) FROM step_sessions s2 WHERE s2.date = s1.date)" +
                "  GROUP BY date" +
                ")"
            );
        }
    };

    private static volatile KineoDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static KineoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (KineoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    KineoDatabase.class, "kineo_database")
                            .addMigrations(MIGRATION_1_2)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
