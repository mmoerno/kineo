package com.dam.kineo.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dam.kineo.R;
import com.dam.kineo.data.local.entity.RoutePointEntity;
import com.dam.kineo.data.remote.dto.PointDto;
import com.dam.kineo.databinding.FragmentMapBinding;
import com.dam.kineo.service.StepCounterService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapBinding binding;
    private MapViewModel mapViewModel;
    private GoogleMap googleMap;
    private Polyline routePolyline;
    private boolean mapServiceBound;
    private boolean pendingStartAfterBind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        mapViewModel.getStepBinder().observe(getViewLifecycleOwner(), binder -> {
            if (binder != null && pendingStartAfterBind) {
                pendingStartAfterBind = false;
                binder.setCurrentSessionId(mapViewModel.getCurrentSessionId());
                binder.startRouteTracking();
                mapViewModel.startTimer();
                mapViewModel.setIsTracking(true);
                mapServiceBound = true;
            }
        });

        mapViewModel.getIsTracking().observe(getViewLifecycleOwner(), tracking -> {
            if (Boolean.TRUE.equals(tracking)) {
                binding.btnToggleRoute.setText(R.string.btn_stop_route);
            } else {
                binding.btnToggleRoute.setText(R.string.btn_start_route);
            }
        });

        mapViewModel.getRouteDistance().observe(getViewLifecycleOwner(), dist -> {
            if (dist != null) {
                binding.tvRouteDistance.setText(dist);
            }
        });

        mapViewModel.getRouteTime().observe(getViewLifecycleOwner(), time -> {
            if (time != null) {
                binding.tvRouteTime.setText(time);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }
        mapFragment.getMapAsync(this);

        mapViewModel.routePoints.observe(getViewLifecycleOwner(), this::drawRoutePoints);

        binding.btnToggleRoute.setOnClickListener(v -> toggleRoute());
    }

    private void toggleRoute() {
        Context ctx = requireContext();
        Boolean tracking = mapViewModel.getIsTracking().getValue();
        if (Boolean.TRUE.equals(tracking)) {
            StepCounterService.StepServiceBinder binder = mapViewModel.getStepBinder().getValue();
            if (binder != null) {
                List<PointDto> pts = binder.stopRouteTracking();
                mapViewModel.onRoutePointsUpdated(pts);
                mapViewModel.uploadRoutePoints(pts);
            }
            mapViewModel.stopTimer();
            mapViewModel.setIsTracking(false);
            if (mapServiceBound) {
                try {
                    ctx.unbindService(mapViewModel.serviceConnection);
                } catch (Exception ignored) {
                }
                mapServiceBound = false;
            }
            pendingStartAfterBind = false;
        } else {
            mapViewModel.startNewRouteSession();
            pendingStartAfterBind = true;
            Intent intent = new Intent(ctx, StepCounterService.class);
            ctx.bindService(intent, mapViewModel.serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        map.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
    }

    private void drawRoutePoints(List<RoutePointEntity> points) {
        if (googleMap == null) return;
        if (points == null || points.isEmpty()) {
            if (routePolyline != null) {
                routePolyline.remove();
                routePolyline = null;
            }
            return;
        }
        List<LatLng> latLngs = new ArrayList<>();
        for (RoutePointEntity e : points) {
            latLngs.add(new LatLng(e.latitude, e.longitude));
        }
        if (routePolyline != null) {
            routePolyline.remove();
        }
        int color = ContextCompat.getColor(requireContext(), R.color.colorPrimary);
        routePolyline = googleMap.addPolyline(new PolylineOptions()
                .addAll(latLngs)
                .color(color)
                .width(10f));
        LatLng last = latLngs.get(latLngs.size() - 1);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(last, 14f));
    }

    @Override
    public void onDestroyView() {
        if (mapServiceBound) {
            try {
                requireContext().unbindService(mapViewModel.serviceConnection);
            } catch (Exception ignored) {
            }
            mapServiceBound = false;
        }
        googleMap = null;
        routePolyline = null;
        binding = null;
        super.onDestroyView();
    }
}
