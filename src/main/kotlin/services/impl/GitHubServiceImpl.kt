package services.impl

import org.springframework.stereotype.Service
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import services.GitHubService
import services.User

@Service
class GitHubServiceImpl: GitHubService{
    override fun getUser(name: String): Call<User> {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val github = retrofit.create(GitHubService::class.java)
        val call = github.getUser(name)
        return call
    }
}
