//package com.dam.kineo.data.remote;
//
//import android.util.Log;
//
//import java.io.IOException;
//
//import okhttp3.Interceptor;
//import okhttp3.MediaType;
//import okhttp3.Protocol;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//
//public class MockInterceptor implements Interceptor {
//
//    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        String path = request.url().encodedPath();
//        String method = request.method();
//
//        Log.d("MockInterceptor", method + " " + path);
//
//        String body = getMockBody(path, method);
//
//        if (body != null) {
//            return new Response.Builder()
//                    .code(200)
//                    .message("OK (mock)")
//                    .request(request)
//                    .protocol(Protocol.HTTP_1_1)
//                    .body(ResponseBody.create(body, JSON))
//                    .addHeader("content-type", "application/json")
//                    .build();
//        }
//
//        // Si no hay mock para este endpoint, intenta la red real
//        try {
//            return chain.proceed(request);
//        } catch (Exception e) {
//            // Si tampoco hay red, devuelve error controlado
//            return new Response.Builder()
//                    .code(503)
//                    .message("Sin conexión y sin mock")
//                    .request(request)
//                    .protocol(Protocol.HTTP_1_1)
//                    .body(ResponseBody.create("{\"error\":\"sin conexión\"}", JSON))
//                    .build();
//        }
//    }
//
//    private String getMockBody(String path, String method) {
//
//        // AUTH
//        if (path.contains("auth/login") || path.contains("auth/register")) {
//            return "{" +
//                    "\"access_token\":\"mock_token_dev\"," +
//                    "\"refresh_token\":\"mock_refresh\"," +
//                    "\"user_id\":\"user_mock_123\"," +
//                    "\"username\":\"UsuarioPrueba\"" +
//                    "}";
//        }
//
//        // PASOS DE HOY
//        if (path.contains("steps/today")) {
//            return "{" +
//                    "\"id\":\"sess_001\"," +
//                    "\"date\":\"2026-05-13\"," +
//                    "\"steps\":6240," +
//                    "\"calories\":249.6," +
//                    "\"distance_meters\":4368.0," +
//                    "\"active_minutes\":62" +
//                    "}";
//        }
//
//        // BADGES
//        if (path.contains("badges")) {
//            return "[" +
//                    "{\"id\":\"first_1000\",\"name\":\"Primeros Pasos\",\"description\":\"Acumula 1.000 pasos\",\"icon_url\":\"\",\"unlocked_at\":1715000000000,\"is_unlocked\":true}," +
//                    "{\"id\":\"five_k\",\"name\":\"5K Runner\",\"description\":\"5.000 pasos en un día\",\"icon_url\":\"\",\"unlocked_at\":1715100000000,\"is_unlocked\":true}," +
//                    "{\"id\":\"ten_k\",\"name\":\"10K Club\",\"description\":\"10.000 pasos en un día\",\"icon_url\":\"\",\"unlocked_at\":null,\"is_unlocked\":false}," +
//                    "{\"id\":\"week_streak\",\"name\":\"Semana Activa\",\"description\":\"7 días con más de 5.000 pasos\",\"icon_url\":\"\",\"unlocked_at\":null,\"is_unlocked\":false}," +
//                    "{\"id\":\"early_bird\",\"name\":\"Madrugador\",\"description\":\"Primera sesión antes de las 8:00\",\"icon_url\":\"\",\"unlocked_at\":null,\"is_unlocked\":false}," +
//                    "{\"id\":\"marathon\",\"name\":\"Maratoniano\",\"description\":\"42.195 metros acumulados\",\"icon_url\":\"\",\"unlocked_at\":null,\"is_unlocked\":false}" +
//                    "]";
//        }
//
//        // AMIGOS
//        if (path.contains("friends/requests")) {
//            return "[" +
//                    "{\"id\":\"u_003\",\"name\":\"Laura Gómez\",\"email\":\"laura@test.com\",\"avatar_url\":\"\",\"status\":\"pending\",\"today_steps\":0}" +
//                    "]";
//        }
//        if (path.contains("friends")) {
//            return "[" +
//                    "{\"id\":\"u_001\",\"name\":\"Carlos Ruiz\",\"email\":\"carlos@test.com\",\"avatar_url\":\"\",\"status\":\"accepted\",\"today_steps\":7850}," +
//                    "{\"id\":\"u_002\",\"name\":\"María López\",\"email\":\"maria@test.com\",\"avatar_url\":\"\",\"status\":\"accepted\",\"today_steps\":12300}" +
//                    "]";
//        }
//
//        // RETOS
//        if (path.contains("challenges")) {
//            return "[" +
//                    "{\"id\":\"ch_001\",\"creator_id\":\"u_001\",\"challenger_id\":\"user_mock_123\"," +
//                    "\"creator_username\":\"CarlosR\",\"challenger_username\":\"UsuarioPrueba\"," +
//                    "\"metric\":\"steps\",\"period\":\"daily\"," +
//                    "\"starts_at\":1715000000000,\"ends_at\":1715086400000," +
//                    "\"status\":\"active\",\"winner_id\":null}," +
//                    "{\"id\":\"ch_002\",\"creator_id\":\"user_mock_123\",\"challenger_id\":\"u_002\"," +
//                    "\"creator_username\":\"UsuarioPrueba\",\"challenger_username\":\"MariaL\"," +
//                    "\"metric\":\"distance_meters\",\"period\":\"weekly\"," +
//                    "\"starts_at\":1714400000000,\"ends_at\":1715000000000," +
//                    "\"status\":\"finished\",\"winner_id\":\"u_002\"}" +
//                    "]";
//        }
//
//        // LEADERBOARD
//        if (path.contains("leaderboard")) {
//            return "[" +
//                    "{\"user_id\":\"u_002\",\"name\":\"María López\",\"total_steps\":12300,\"rank\":1}," +
//                    "{\"user_id\":\"u_001\",\"name\":\"Carlos Ruiz\",\"total_steps\":7850,\"rank\":2}," +
//                    "{\"user_id\":\"user_mock_123\",\"name\":\"UsuarioPrueba\",\"total_steps\":6240,\"rank\":3}" +
//                    "]";
//        }
//
//        return null; // sin mock para este endpoint
//    }
//}