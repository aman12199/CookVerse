package com.example.cookverse.mvvm

import android.util.Log
import androidx.lifecycle.*
import com.example.cookverse.data.pojo.CategoryResponse
import com.example.cookverse.data.pojo.MealsResponse
import com.example.cookverse.data.pojo.RandomMealResponse
import com.example.cookverse.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG = "MainMVVM"

class MainFragMVVM: ViewModel() {
    private val mutableCategory = MutableLiveData<CategoryResponse>()
    private val mutableRandomMeal = MutableLiveData<RandomMealResponse>()
    private val mutableMealsByCategory = MutableLiveData<MealsResponse>()


    init {
        getRandomMeal()
        getAllCategories()
        getMealsByCategory("beef")
    }


    private fun getAllCategories() {
        RetrofitInstance.foodApi.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                mutableCategory.value = response.body()
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    private fun getRandomMeal() {
        RetrofitInstance.foodApi.getRandomMeal().enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(call: Call<RandomMealResponse>, response: Response<RandomMealResponse>) {
                mutableRandomMeal.value = response.body()
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    private fun getMealsByCategory(category:String) {

        RetrofitInstance.foodApi.getMealsByCategory(category).enqueue(object : Callback<MealsResponse> {
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    mutableMealsByCategory.value = response.body()
                } else {
                    Log.e(TAG, "Empty or null response for category: $category")
                }
            }

            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun observeMealByCategory(): LiveData<MealsResponse> {
        return mutableMealsByCategory
    }

    fun observeRandomMeal(): LiveData<RandomMealResponse> {
        return mutableRandomMeal
    }

    fun observeCategories(): LiveData<CategoryResponse> {
        return mutableCategory
    }

}