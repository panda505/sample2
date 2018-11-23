package services


import org.springframework.stereotype.Service
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


class User(val name: String)

@Service
interface GitHubService {
        @GET("/users/{name}")
        fun getUser( @Path("name") name: String): Call<User>
}