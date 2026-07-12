package com.dam.kineo.ui.badges;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dam.kineo.data.local.KineoDatabase;
import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.domain.model.Badge;
import com.dam.kineo.domain.repository.BadgeRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BadgesViewModel extends ViewModel {

    public final LiveData<List<Badge>> allBadges;

    @Inject
    public BadgesViewModel(BadgeRepository badgeRepository, AuthPreferences authPreferences) {
        // Inicializamos los logros por defecto si no existen
        KineoDatabase.databaseWriteExecutor.execute(badgeRepository::initDefaultBadges);
        
        // Obtenemos la fuente de datos local (Room)
        allBadges = badgeRepository.getAllBadges();

        // Forzamos la sincronización con el servidor nada más entrar
        String token = authPreferences.getToken();
        if (token != null) {
            badgeRepository.syncBadges(token);
        }
    }
}
