package ru.geekbrains.javaback.service;
import retrofit2.Call;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.geekbrains.javaback.dto.Category;

public interface CategoryService {
    @GET("categories/{id}")
    Call<Category>getCategory(@Path("id") Integer id);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") int id);

}
