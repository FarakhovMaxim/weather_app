import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {
    @GET("v2/forecast")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Header("X-Yandex-Weather-Key") apiKey: String
    ): WeatherResponse
}

data class WeatherResponse(
    val fact: Fact
)

data class Fact(
    val temp: Int,
    val condition: String
)

suspend fun getWeather(latitude: Double, longitude: Double, weatherApiKey: String): WeatherResponse? {
    val weatherRetrofit = Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val weatherApi = weatherRetrofit.create(WeatherApi::class.java)
    return try {
        weatherApi.getWeather(
            latitude = latitude,
            longitude = longitude,
            apiKey = weatherApiKey
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
